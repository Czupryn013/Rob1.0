var client = null;

function showMessage(value, username) {
    var newResponse = document.createElement("p");
    var time = new Date();
    newResponse.appendChild(document.createTextNode(((time.getHours() < 10) ? "0" + time.getHours() : time.getHours()) + ":"
     + ((time.getMinutes() < 10) ? "0" + time.getMinutes() : time.getMinutes()) +
    "-"+ username + ": " +value));
    var response = document.getElementById("response");
    response.appendChild(newResponse);
}


function connect() {
//    client = Stomp.client("ws://192.168.1.26:8080/chat");
    client = Stomp.client("ws://localhost:8080/chat");
    client.connect({}, function(frame) {
        client.subscribe("/topic/messages", function(message) {
            showMessage(JSON.parse(message.body).value, JSON.parse(message.body).username)
        });
    })
}

function sendMessage() {
    var messageToSend = document.getElementById("messageToSend").value;
    var username = document.getElementById("username").value;

     client.send("/app/chat", {}, JSON.stringify({'value' : messageToSend, 'username' : username}));
     document.getElementById("messageToSend").value = "";
}