"use strict"

function commonFactory(constructor, evaluate, toString, diff, prefix = toString, postfix = toString) {
    constructor.prototype = {
        evaluate: evaluate,
        toString: toString,
        diff: diff,
        prefix: prefix,
        postfix: postfix,
        constructor: constructor
    }
    return constructor
}

const Const = commonFactory(
    function (x) {
        this.x = x
    },
    function () {
        return this.x;
    },
    function () {
        return "" + this.x;
    },
    function () {
        return new Const(0)
    }
)

Const.zero = new Const(0)
Const.one = new Const(1)
Const.two = new Const(2)

const vars = { "x": 0, "y": 1, "z": 2 };

const Variable = commonFactory(
    function (x) {
        this.x = x
    },
    function (...args) {
        return args[vars[this.x]];
    },
    function () {
        return this.x;
    },
    function (dif) {
        return this.x === dif ? Const.one: Const.zero;
    },
)

const Operation = commonFactory(
    function (...args) {
        this.args = args
    },
    function (...args) {
        return this.operation(...this.args.map(func => func.evaluate(...args)));
    },
    function () {
        return this.args.map(func => func.toString()).join(" ") + " " + this.getType();
    },
    function (dif) {
        return this.curDiff(...this.args.concat(...this.args.map(v => v.diff(dif))));
    },
    function () {
        return "(" + this.getType() + " " + this.args.map(f => f.prefix()).join(" ") + ")"
    },
    function () {
        return "(" + this.args.map(func => func.postfix()).join(" ") + " " + this.getType() + ")";
    }
)

const opers = new Map()

function curOperationFactory(operation, curDiff, type, constructor) {
    this.operation = operation
    this.curDiff = curDiff
    this.getType = function() {
        return type;
    }
    this.constructor = constructor
}

function operationFactory(operation, curDiff, type) {
    const op = function (...args) {
        Operation.apply(this, args);
    }
    op.prototype = Object.create(Operation.prototype);
    curOperationFactory.call(op.prototype, operation, curDiff, type, op);
    opers.set(type, op)
    return op;
}

const Add = operationFactory(
    function (x, y) {
        return x + y
    },
    function (x, y, dx, dy) {
        return new Add(dx, dy)
    },
    "+",
)

const Subtract = operationFactory(
    function (x, y) {
        return x - y
    },
    function (x, y, dx, dy) {
        return new Subtract(dx, dy)
    },
    "-"
)

const Negate = operationFactory(
    function (x) {
        return -x
    },
    function (x, dx) {
        return new Negate(dx)
    },
    "negate"
)

const Multiply = operationFactory(
    function (x, y) {
        return x * y
    },
    function (x, y, dx, dy) {
        return new Add(new Multiply(dx, y), new Multiply(dy, x))
    },
    "*"
)

const Divide = operationFactory(
    function (x, y) {
        return x / y
    },
    function (x, y, dx, dy) {
        return new Divide(new Subtract(new Multiply(dx, y), new Multiply(dy, x)),
            new Multiply(y, y))
    },
    "/"
)

const Hypot = operationFactory(
    function (x, y) {
        return x * x + y * y
    },
    function (x, y, dx, dy) {
        return new Add(new Add(new Multiply(dx, x), new Multiply(dx, x)), new Add(new Multiply(dy, y), new Multiply(dy, y)))
    },
    "hypot"
)

const HMean = operationFactory(
    function (x, y) {
        return 2  / (1 / x + 1 / y)
    },
    function (x, y, dx, dy) {
        return new Divide(
            new Multiply(Const.two, new Subtract(
                    new Multiply(new Add(new Multiply(dx, y), new Multiply(dy, x)), new Add(x, y)),
                    new Multiply(new Multiply(x, y), new Add(dx, dy)))),
            new Multiply(new Add(x, y), new Add(x, y)))
    },
    "hmean"
)

function foldLeft(f, zero) {
    return (...args) => {
        let result = zero;
        for (const arg of args) {
            result = f(result, arg);
        }
        return result;
    }
}

const sum = foldLeft((x, y) => x + y, 0)
const mul = foldLeft((a, b) => a * b, 1)
const add = foldLeft((x, y) => new Add(x, y), Const.zero)
const multiply = foldLeft((a, b) => new Multiply(a, b), Const.one)

const ArithMean = operationFactory(
    function (...args) {
        return sum(...args) / args.length
    },
    function (...args) {
        const len = args.length / 2
        return new Divide(add(...args.slice(-len)), new Const(len))
    },
    "arith-mean"
)

const fillWith = function (x, y) {
    let arr = [];
    for (let i = 0; i < y; i++) {
        arr[i] = x;
    }
    return arr
}

const Pow = operationFactory(
    function (x, y) {
        return mul(...fillWith(x, y))
    },
    function (x, y) {
        return new Multiply(multiply(...fillWith(x, y - 1)), new Const(+y))
    }
)

const Sign = operationFactory(
    function (x) {
        return x > 0 ? 1 : -1
    },
)

function foldLeftHarmGeom(cond) {
    return (...args) => {
        const len = args.length / 2
        let result, index, indexDiff = 0, res = Const.zero
        while (indexDiff !== len) {
            index = 0
            result = Const.one
            while (index !== len) {
                if (index !== indexDiff || cond)
                    result = new Multiply(result, args[index === indexDiff ? len + indexDiff : index])
                index++
            }
            res = new Add(res, result)
            indexDiff++
        }
        return res;
    }
}

const foldLeftHarm = foldLeftHarmGeom(false)
const foldLeftGeom = foldLeftHarmGeom(true)

const GeomMean = operationFactory(
    function (...args) {
        return Math.pow(Math.abs(mul(...args)), 1 / args.length)
    },
    function (...args) {
        const len = args.length / 2
        return new Divide(
            new Multiply(foldLeftGeom(...args), new Sign(multiply(...args.slice(0, len)))),
            new Multiply(
                new Pow(
                    new GeomMean(...args.slice(0, len)),
                    new Const(len - 1)
                ),
                new Const(len)
            ))
    },
    "geom-mean"
)

function foldLeftHarmDenominator(...args) {
    const len = args.length / 2
    let result, index, indexDiff, indexAbsence = 0, res = Const.zero
    while (indexAbsence !== len) {
        indexDiff = 0
        while (indexDiff !== len) {
            if (indexDiff !== indexAbsence) {
                index = 0
                result = Const.one
                while (index !== len) {
                    if (index !== indexAbsence)
                        result = new Multiply(result, args[index === indexDiff ? len + indexDiff : index])
                    index++
                }
                res = new Add(res, result)
            }
            indexDiff++
        }
        indexAbsence++
    }
    return res;
}

const HarmMean = operationFactory(
    function (...args) {
        return args.length / (foldLeft((x, y) => x + 1 / y, 0)(...args))
    },
    function (...args) {
        const len = args.length / 2
        const harm = foldLeftHarm(...args)
        return new Divide(
            new Multiply(new Subtract(
                new Multiply(harm, foldLeftGeom(...args)),
                new Multiply(foldLeftHarmDenominator(...args), multiply(...args.slice(0, len)))
            ), new Const(len)),
            new Pow(harm, Const.two)
        )
    },
    "harm-mean"
)

const errors = (function(){
    function errorFactory(name, message) {
        const error = function(...args) {
            this.message = message(...args);
        }
        error.prototype = Object.create(Error.prototype)
        error.prototype.name = name
        error.prototype.constructor = error
        return error
    }

    const MissingParenthesisError = errorFactory(
        "ParenthesisError",
        function (position, token) {
            return "Expected ), found " + token + " at " + position + " position";
        }
    )

    const InvalidTokenError = errorFactory(
        "InvalidTokenError",
        function (position, token) {
            return "Can not resolve '" + token + "' at " + position + " position";
        }
    )

    const InvalidOperationError = errorFactory(
        "InvalidOperationError",
        function (position, token) {
            return "Invalid operation '" + token + "' at " + position + " position";
        }
    )

    const ArityMismatchError = errorFactory(
        "ArityMismatchError",
        function (number, op, position) {
            return "Number of operands (" + number + ") mismatching arity of operation " + op + " at " + position + " position";
        }
    )

    const UnexpectedTokenError = errorFactory(
        "UnexpectedTokenError",
        function (position, token) {
            return "Unexpected token '" + token + "' at " + position + " position";
        }
    )

    const InvalidInputError = errorFactory(
        "InvalidInputError",
        function (message) {
            return message
        }
    )

    return {
        MissingParenthesisError: MissingParenthesisError,
        InvalidTokenError: InvalidTokenError,
        InvalidOperationError: InvalidOperationError,
        ArityMismatchError: ArityMismatchError,
        UnexpectedTokenError: UnexpectedTokenError,
        InvalidInputError: InvalidInputError
    }
})();

const AbstractParser = (expr, mode) => {
    let pos = 0
    const test = (ch) => {
        if (hasNext() && expr[pos].toString() === ch) {
            pos++
            return true;
        }
        return false;
    };
    const hasNext = () => pos < expr.length;
    const fetchNextToken = () => hasNext() ? expr[pos++].toString() : "nothing"
    const parseToken = () => {
        if (test("(")) {
            const res = parseExpression()
            if (!test(")")) {
                throw new errors.MissingParenthesisError(pos, fetchNextToken())
            }
            return res
        }
        const token = fetchNextToken()
        if (!isNaN(+token))
            return new Const(+token)
        if (token in vars)
            return new Variable(token)
        throw new errors.InvalidTokenError(pos, token)
    }
    const parseOperation = () => {
        const token = fetchNextToken()
        if (!opers.has(token))
            throw new errors.InvalidOperationError(pos, token)
        return opers.get(token)
    }
    const parseOperands = () => {
        let res = []
        while (hasNext() && expr[pos].toString() !== ")" && !opers.has(expr[pos].toString()))
            res.push(parseToken())
        return res
    }
    const unlimitedOperations = {"arith-mean": 0, "geom-mean": 1, "harm-mean": 2}
    const parseExpression = () => {
        let operands, operation
        if (mode) {
            operation = parseOperation()
            operands = parseOperands()
        } else {
            operands = parseOperands()
            operation = parseOperation()
        }
        if (!(operation.prototype.getType() in unlimitedOperations) && operation.prototype.operation.length !== operands.length)
            throw new errors.ArityMismatchError(operands.length, operation.prototype.getType(), pos)
        return new operation(...operands)
    }
    const parse = () => {
        if (!hasNext())
            throw new errors.InvalidInputError("Nothing found to parse")
        const res = parseToken()
        if (hasNext())
            throw new errors.UnexpectedTokenError(pos, fetchNextToken())
        return res
    }
    return parse()
}

const matcher = cond => {
    return str => {
        return AbstractParser(Array.from(str.matchAll(/\(|\)|[^\s()]+/g)), cond)
    }
}
const parsePrefix = matcher( true)
const parsePostfix = matcher(false)

const parse = str => {
    let stack = [];
    for (const token of str.split(" ").filter(x => x.length > 0)) {
        if (opers.has(token)) {
            stack.push(new (opers.get(token))(...stack.splice(-opers.get(token).prototype.operation.length)));
        } else if (token in vars) {
            stack.push(new Variable(token));
        } else {
            stack.push(new Const(+token));
        }
    }
    return stack.pop()
}
