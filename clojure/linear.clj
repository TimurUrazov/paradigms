(defn common-check [type count & args] (and (every? vector? args)
                                            (every? (partial every? type) args)
                                            (apply = (mapv count args))))

(defn vector-operation [func] (fn [& vectors] {:pre [(apply common-check number? (partial count) vectors)]} (apply mapv func vectors)))
(def v+ (vector-operation +))
(def v- (vector-operation -))
(def v* (vector-operation *))
(def vd (vector-operation /))

(defn v*s [vector & num]
  {:pre [(every? number? num)
         (vector? vector)
         (every? number? vector)]} (mapv (partial * (reduce * num)) vector))

(defn scalar [& vectors] (reduce + (apply v* vectors)))
(defn matrix-operation [func] (fn [& matrix]
                                {:pre [(apply common-check vector? (partial mapv (partial count)) matrix)
                                       (every? (partial every? (partial every? number?)) matrix)]} (apply mapv func matrix)))
(def m+ (matrix-operation v+))
(def m- (matrix-operation v-))
(def m* (matrix-operation v*))
(def md (matrix-operation vd))

(defn transpose [matrix] {:pre [(apply = (mapv (partial count) matrix))]} (apply mapv vector matrix))

(defn matrix-checker [matrix & args] (and (every? number? args)
                                          (every? vector? matrix)
                                          (every? vector? matrix)
                                          (every? (partial every? number?) matrix)))

(defn m*s [matrix & num] {:pre [(apply matrix-checker matrix num)]} (mapv (fn [vector] (v*s vector (reduce * num))) matrix))
(defn m*v [matrix vector] {:pre [(apply matrix-checker matrix vector)]} (mapv (partial scalar vector) matrix))

(defn m*m-double [first-matrix second-matrix] (transpose (mapv (partial m*v first-matrix) (transpose second-matrix))))
(defn m*m [& matrix]
  {:pre [(every? (partial every? vector?) matrix)
         (every? (partial every? (partial every? number?)) matrix)]} (reduce m*m-double matrix))

(defn rem-vec-ind [index vector] (vec (concat (subvec vector 0 index) (subvec vector (inc index)))))
(defn rem-matrix-ind [matrix index] (mapv (partial rem-vec-ind index) matrix))
(defn sign [index] (if (even? index) 1 (- 0 1)))

(defn det [matrix] (letfn [(matrix-minor [matrix index] (rem-matrix-ind (rest matrix) index))]
                     (if (== (count matrix) 1)
                       (nth (nth matrix 0) 0)
                       (loop [n (count matrix) a 0 res 0] (if (== 0 n)
                                                            res
                                                            (recur (- n 1)
                                                                   (+ a 1)
                                                                   (+ res (* (sign a) (* (nth (nth matrix 0) a) (det (matrix-minor matrix a)))))))))))

(defn vect-double [x y] (letfn [(matrix-minor-second [matrix index] (rem-matrix-ind matrix index))]
                          (loop [n (count x) a 0 res []]
                            (if (== 0 n)
                              res
                              (recur (- n 1) (+ a 1) (assoc res a (* (sign a) (det (matrix-minor-second (vec [x y]) a)))))))))

(defn vect [& vectors]
  {:pre [(every? vector? vectors)
         (every? true? (mapv (partial every? number?) vectors))
         (apply = (mapv (partial count) vectors))]} (reduce vect-double (first vectors) (rest vectors)))

(defn type-of-tensor [tensor] ((fn [ten res] (if (not (vector? ten))
                                res
                                (recur (first ten) (conj res (count ten))))) tensor []))

(defn check-vectors-prefix [& vectors] (apply = (map (fn [vector] (subvec vector 0 (count (first vectors)))) vectors)))
(defn check-tensors-prefix [& tensors] (apply check-vectors-prefix (map type-of-tensor tensors)))

(defn find-max-in-vector [vector] (.indexOf vector (apply max vector)))
(defn size-of-tensor [tensor] (count (type-of-tensor tensor)))
(defn find-max-tensor [& tensors] (nth tensors (find-max-in-vector (mapv size-of-tensor tensors))))

(defn expand [res tensor] (if (number? tensor)
                            (if (empty? (rest res))
                              (vec (repeat (first res) tensor))
                              (expand (rest res) (vec (repeat (first res) tensor))))
                            (mapv (partial expand res) tensor)))

(defn broadcast-double [x y]
  {:pre [(check-tensors-prefix y x)]}
  (if (not (empty? (subvec (type-of-tensor x) (count (type-of-tensor y)))))
    (expand (subvec (type-of-tensor x) (count (type-of-tensor y))) y) y))

(defn broadcast [& args] (let [zero (apply find-max-tensor args)] (mapv (partial broadcast-double zero) args)))

(defn tensor-functions [func] (letfn [(tb [& tensors]
                                        (if (number? (first tensors))
                                          (apply func tensors)
                                          (apply mapv tb tensors)))] (fn [& tensors] (let [tensors (apply broadcast tensors)] (apply tb tensors)))))

(def tb+ (tensor-functions +))
(def tb- (tensor-functions -))
(def tb* (tensor-functions *))
(def tbd (tensor-functions /))