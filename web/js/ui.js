const webSocketProtocolAndHost = window.document.URL
	.replace("http://", "ws://")
	.replace("https://", "wss://");

const socket = new WebSocket(webSocketProtocolAndHost + "websocket");
socket.onopen = () => socket.send("get");
socket.onmessage = updateMeetings;

function updateMeetings(message) {
	const meetings = JSON.parse(message.data);
	const prettyJson = JSON.stringify(meetings, null, 4);

	window.document.getElementsByTagName("pre")[0].textContent = prettyJson;
}
