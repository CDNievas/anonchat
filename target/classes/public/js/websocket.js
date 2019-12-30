// small helper function for selecting element by id
let id = id => document.getElementById(id);

var room = location.pathname.split("/")[2];

//Establish the WebSocket connection and set up event handlers
let ws = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chatsocket/" + room);
ws.onmessage = msg => recvMsg(msg);
ws.onclose = () => alert("WebSocket connection closed");

// Add event listeners to button and input field
id("send").addEventListener("click", () => sendAndClear(id("message").value));
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) { // Send message if enter is pressed in input field
        sendAndClear(e.target.value);
    }
});

function sendAndClear(message) {
    if (message !== "") {
        ws.send(message);
        id("message").value = "";
    }
}

function recvMsg(msg) { // Update chat-panel and list of connected users
    let data = JSON.parse(msg.data);
    switch(data.msgType){

        case 1:
            id("userlist").innerHTML = data.userList.map(user => "<li>" + user + "</li>").join("");
            var element = id("userlist");
            break;

        case 2: 
            var html = "<div class=\"message\"><p><b>" + data.userSender + " says:</b></p><p>" + data.userMessage + "</p></div>";
            id("messages").insertAdjacentHTML("beforeend", html);
            var element = id("messages");
            
            break;

        default:
            break;
    }
	
	var element = id("messages");
	element.scrollTop = element.scrollHeight;
	
}

