(load-file "proto.clj")
(load-file "parser.clj")

(def constant constantly)
(defn variable [var]
  (fn [values] (values var)))

(defn operation-factory [func]
  (fn [& functions]
    (fn [values]
      (apply func (mapv #(% values) functions)))))

(def add (operation-factory +))
(def subtract (operation-factory -))
(def multiply (operation-factory *))
(defn divide'
  ([x] (divide' 1 x))
  ([fst & rst] (/ (double fst) (double (apply * rst)))))

(def negate subtract)
(def divide (operation-factory divide'))
(defn mean' [& args] (/ (apply + args) (count args)))
(def mean (operation-factory mean'))
(def square #(* % %))
(def varn (operation-factory (fn [& args] (- (apply mean' (mapv square args)) (square (apply mean' args))))))

(def variables
  {'x (variable "x")
   'y (variable "y")
   'z (variable "z")})

(def operations
  {'+ add
   '- subtract
   '* multiply
   '/ divide
   'negate negate
   'mean mean
   'varn varn})

(defn parser [vars opers cnst]
  (letfn [(parse [expr]
            (cond
              (number? expr) (cnst expr)
              (contains? vars expr) (vars expr)
              (list? expr) (apply (opers (first expr)) (mapv parse (rest expr)))))] (fn [expr] (parse (read-string expr)))))

(def parseFunction (parser variables operations constant))

(def evaluate (method :evaluate))
(def toString (method :toString))
(def diff (method :diff))
(def toStringInfix (method :toStringInfix))

(defn common-factory [ctor evaluate toString diff toStringInfix]
  (let [proto {:evaluate evaluate
               :toString toString
               :diff diff
               :toStringInfix toStringInfix}]
    (constructor ctor proto)))

(defn primitive-obj [ctor evaluate diff toStringInfix]
  (let [_x (field :x)]
    (common-factory
      ctor
      (evaluate _x)
      (fn [this] (str (_x this)))
      diff
      toStringInfix)))

(declare zero)
(def Constant
  (primitive-obj
    (fn [this x] (assoc this :x x))
    (fn [x] (fn [this _] (x this)))
    (fn [_ _] zero)
    toString))

(def zero (Constant 0.0))
(def one (Constant 1.0))
(def two (Constant 2.0))

(def Variable
  (let [_x (field :x)
        _str (field :str)]
    (primitive-obj
      (fn [this x] (assoc this :x (str (Character/toLowerCase (first x))) :str x))
      (fn [x] (fn [this values] (values (x this))))
      (fn [this dif]
        (if (= (_x this) dif)
          one zero))
      (fn [this] (str (_str this))))))

(println (instance? String (str (first "xx"))))

(def Operation
  (let [_args (field :args)
        _type (field :type)
        _operation (field :operation)
        cur-diff (field :curDiff)]
    (common-factory
      (fn [this & args] (assoc this :args args))
      (fn [this values] (apply (_operation this) (mapv #(evaluate % values) (_args this))))
      (fn [this] (str "(" (_type this) " " (clojure.string/join " " (mapv toString (_args this))) ")"))
      (fn [this dif] ((cur-diff this) (_args this) (mapv #(diff % dif) (_args this))))
      (fn [this]
        (if (= (count (_args this)) 1)
          (str (_type this) "(" (toStringInfix (first (_args this))) ")")
          (str "(" (clojure.string/join (str " " (_type this) " ") (mapv toStringInfix (_args this))) ")"))))))



(defn oper-factory [type operation cur-diff]
  (let [oper {:type type
              :operation operation
              :curDiff cur-diff}]
    (fn [& args] (assoc oper :prototype (apply Operation args)))))

(defn common-dif [op] (fn [_ dx] (apply op dx)))
(def Add
  (oper-factory
    "+" +
    (common-dif (fn [& args] (apply Add args)))))

(def Subtract
  (oper-factory
    "-" -
    (common-dif (fn [& args] (apply Subtract args)))))

(declare Multiply)
(defn mul-dif [x dx]
  (second (reduce (fn [[a da] [b db]] [(Multiply a b) (Add (Multiply da b) (Multiply a db))]) (mapv vector x dx))))

(def Multiply
  (oper-factory
    "*" *
    mul-dif))

(def pow'
  (fn [x y]
    (apply * (repeat y x))))

(def pow-dif
  (fn [x _]
    (let [sec (second x)]
      (apply Multiply sec (repeat (- sec 1) (first x))))))

(def Pow
  (oper-factory
    "pow" pow'
    pow-dif))

(declare Divide)
(defn div-dif [x dx]
  (let [fx (first x) fdx (first dx) rx (rest x)]
    (if (= 1 (count x))
      (Multiply (Subtract (Divide one (Pow fx two))) fdx)
      (Divide
        (Subtract (Multiply fdx (apply Multiply rx)) (Multiply fx (mul-dif rx (rest dx))))
        (Pow (apply Multiply rx) two)))))

(def Divide
  (oper-factory
    "/" divide'
    div-dif))

(def Negate
  (oper-factory
    "negate" -
    (common-dif (fn [& args] (apply Negate args)))))

(defn arith-mean' [& args] (divide' (apply + args) (count args)))

(def ArithMean
  (oper-factory
    "arith-mean" arith-mean'
    (fn [_ dx]
      (Divide (apply Add dx) (Constant (count dx))))))

(def Sign
  (oper-factory
    "sign" (fn [x] (if (> x 0) 1 -1))
    ()))

(defn geom-mean' [& args] (Math/pow (Math/abs^double (apply * args)) (divide' 1 (count args))))

(declare GeomMean)
(def geom-dif
  (fn [x dx]
    (let [len (count x)]
      (Divide
        (Multiply (Sign (apply Multiply x)) (mul-dif x dx))
        (Multiply (Pow (apply GeomMean x) (Constant (- len 1))) (Constant len))))))

(defn harm-mean' [& args] (divide' (count args) (apply + (mapv #(divide' %) args))))

(def harm-dif
  (fn [x dx]
    (let [len (count x)
          sum (apply Add (map #(Divide %) x))
          sum-diff (apply Add (map (fn [[x dx]] (div-dif [one x] [zero dx])) (mapv vector x dx)))]
      (Multiply (div-dif [one sum] [zero sum-diff]) (Constant len)))))

(def GeomMean
  (oper-factory
    "geom-mean" geom-mean'
    geom-dif))

(def HarmMean
  (oper-factory
    "harm-mean" harm-mean'
    harm-dif))

(def And
  (oper-factory
    "&&"
    (fn [a b]
      (if (and (pos? a) (pos? b)) 1 0))
    ()))

(def Or
  (oper-factory
    "||"
    (fn [a b]
      (if (or (pos? a) (pos? b)) 1 0))
    ()))

(def Xor
  (oper-factory
    "^^"
    (fn [a b]
      (if (or (and (not (pos? a)) (pos? b)) (and (pos? a) (not (pos? b)))) 1 0))
    ()))

(def Impl
  (oper-factory
    "->"
    (fn [a b]
      (if (or (not (pos? a)) (pos? b)) 1 0))
    ()))

(def Iff
  (oper-factory
    "<->"
    (fn [a b]
      (if (or (and (pos? a) (pos? b)) (and (not (pos? a)) (not (pos? b)))) 1 0))
    ()))

(def Operations
  {'+ Add
   '- Subtract
   '* Multiply
   '/ Divide
   'negate Negate
   'arith-mean ArithMean
   'geom-mean GeomMean
   'harm-mean HarmMean
   '&& And
   '|| Or
   (symbol "^^") Xor
   '-> Impl
   '<-> Iff})

(def Variables
  {'x (Variable "x")
   'y (Variable "y")
   'z (Variable "z")})

(def parseObject (parser Variables Operations Constant))

(def *digit (+char "0123456789"))
(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))

(def *number
  (+map
    read-string
    (+seqf
      (fn [sign int delimiter fraction]
        (apply str sign (apply str int) delimiter (apply str fraction)))
      (+opt (+char "+-"))
      (+plus *digit)
      (+opt (+char "."))
      (+opt (+plus *digit)))))

(def *var
  (+map
    read-string
    (+seqf
      (partial apply str)
      *ws
      (+plus (+char "xyzXYZ")))))

(def *constant
  (+map
    Constant
    *number))

(def *variable
  (+map
    #(Variable (str %))
    *var))

(defn get-op [op]
  (+map
    (constantly (Operations (symbol op)))
    (apply +seq (map (comp +char str) op))))

(defn current-opers [& opers] (apply +or (map get-op opers)))

(declare *parse-low)
(def *negation
  (+map
    (partial apply Negate)
    (+seq
      *ws (+ignore (get-op "negate")) *ws
      (delay *parse-low))))

(declare *parse-expr)
(def *brackets
  (+seqn
    0
    *ws (+ignore (+char "("))
    *ws (delay *parse-expr) *ws
    (+ignore (+char ")")) *ws))

(def *parse-low (+or *negation *constant *variable *brackets))

(def base-parser
  #(reduce %1 (first %2) (partition 2 2 (rest %2))))

(defn flat [args oper]
  (+map
    flatten
    (+seq
      *ws args *ws
      (+star (+seq oper *ws args)))))

(defn common-assoc [fold]
  #(+map fold (flat %1 %2)))

(defn order [value-order parsing-order]
  (common-assoc
    #(base-parser value-order (parsing-order %))))

(def *left-assoc-parser
  (order #((first %2) %1 (second %2)) identity))

(def *right-assoc-parser 
  (order #((first %2) (second %2) %1) reverse))

(def *parsing-levels
  (list
    (list *left-assoc-parser "*" "/")
    (list *left-assoc-parser "+" "-")
    (list *left-assoc-parser "&&")
    (list *left-assoc-parser "||")
    (list *left-assoc-parser "^^")
    (list *right-assoc-parser "->")
    (list *left-assoc-parser "<->")))

(def *parse-expr
  (reduce
    (fn [*next-lvl [cur-parser & ops]]
      (cur-parser
        *next-lvl
        (apply current-opers ops)))
    *parse-low
    *parsing-levels))

(def parseObjectInfix (+parser (+seqn 0 *ws *parse-expr *ws)))
