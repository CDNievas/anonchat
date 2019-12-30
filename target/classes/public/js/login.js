// small helper function for selecting element by id
let id = id => document.getElementById(id);
var room = id("chatroom");

id("login").addEventListener("click", () => redirect(room.value));

function redirect(room) {
    var url = "http://" + location.hostname + ":" + location.port + "/chat/" + room;
    window.location.href = url;
}
