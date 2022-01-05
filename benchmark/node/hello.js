// Load the http module to create an http server.
var http = require('http');

// Configure our HTTP server to respond with Hello World to all requests.
var server = http.createServer(function (request, response) {
  response.writeHead(200, {"Content-Type": "text/html; charset=UTF-8"});
  response.end("<html><body><h1>Great BBL !</h1></body></html>\n");
});

// Listen on port 8000, IP defaults to 127.0.0.1
server.listen(8222);

// Put a friendly message on the terminal
console.log("Server running at http://127.0.0.1:8222/");
