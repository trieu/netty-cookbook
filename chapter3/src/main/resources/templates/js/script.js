var fun1 = function(name) {
    print('Hi there from Javascript, ' + name);
    return "greetings from javascript";
};

var router = function (req, resp) {
    //print("req: " + Object.prototype.toString.call(req));
    resp.data = "hello from JS Nashorn Engine";
    //resp.time = new Date().getTime();
    return resp;
};

router(theReq, theResp)

/*
load('http://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.6.0/underscore-min.js');

var odds = _.filter([1, 2, 3, 4, 5, 6], function (num) {
    return num % 2 == 1;
});

print(odds);  // 1, 3, 5
*/