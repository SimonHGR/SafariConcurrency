console.log("fib.js loading")

function fib(x) {
    if (x < 3) return 1
    else return fib(x - 1) + fib(x - 2)
}

onmessage = e => {
    // console.log("worker received message " + e.data)
    let number = Number.parseInt(e.data)
    if (number !== NaN) {
        let res = fib(number)
        // console.log("Calculated Fib " + number + " = " + res)
        postMessage(res)
    } else {
        postMessage("Please enter a number")
    }
}
