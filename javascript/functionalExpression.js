"use strict"

const cnst = value => () => value;
const one = cnst(1);
const two = cnst(2);
const vars = { "x": 0, "y": 1, "z": 2 };
const variable = variableName => (...args) => args[vars[variableName]];
const operation = op => (...func) => (...args) => op(...func.map(f => f(...args)));
const add = operation((x, y) => x + y);
const subtract = operation((x, y) => x - y);
const multiply = operation((x, y) => x * y);
const madd = operation((x, y, z) => x * y + z);
const divide = operation((x, y) => x / y);
const negate = operation(x => -x);
const floor = operation(Math.floor);
const ceil = operation(Math.ceil);
const opers = { "+": add, "-": subtract, "*": multiply, "/": divide, "negate": negate, "*+": madd, "_": floor, "^": ceil, "madd": madd, "floor": floor, "ceil": ceil };
const numOfArgs = { "+": 2, "-": 2, "*": 2, "/": 2, "negate": 1, "*+": 3, "_": 1, "^": 1, "madd": 3, "floor": 1, "ceil": 1 };
const cnsts = { "one": one, "two": two };

const parse = expression => {
    const stack = []
    for (const token of expression.split(" ").filter(x => x.length > 0)) {
        if (token in opers) {
            stack.push(opers[token](...stack.splice(-numOfArgs[token])))
        } else if (token in vars) {
            stack.push(variable(token))
        } else if (token in cnsts) {
            stack.push(cnsts[token])
        } else {
            stack.push(cnst(+token))
        }
    }
    return stack.pop()
}
