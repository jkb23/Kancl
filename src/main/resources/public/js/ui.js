let grid;
let squareSize = 20;
let height = 40;
let width = 40;

let desiredValue = null;
let socket;

const setMessageRegex = /^set (-?[0-9]+) (-?[0-9]+) = ([01])$/;

function setup() {
	createCanvas(squareSize * width, squareSize * height);
	grid = createGrid();

	const webSocketProtocolAndHost = window.document.URL
		.replace("http://", "ws://")
		.replace("https://", "wss://");

	socket = new WebSocket(webSocketProtocolAndHost + "websocket");
	socket.onopen = () => socket.send("get");
	socket.onmessage = updateGrid;
}

function updateGrid(message) {
	for (const line of message.data.split("\n")) {
		const match = line.match(setMessageRegex);
		if (match) {
			const x = parseInt(match[1], 10);
			const y = parseInt(match[2], 10);
			const value = parseInt(match[3], 10);

			grid[x][y] = value !== 0;
		}
	}
}

function draw() {

	if (mouseIsPressed) {
		const x = Math.floor(mouseX / squareSize);
		const y = Math.floor(mouseY / squareSize);
		if (desiredValue === null) {
			desiredValue = !grid[x][y];
		}

		if (grid[x][y] !== desiredValue) {
			grid[x][y] = desiredValue;
			socket.send("set " + x + " " + y + " = " + (desiredValue ? 1 : 0));
		}
	}
	else {
		desiredValue = null;
	}

	iterateOverGrid(grid, function (cell, x, y) {
		if (cell)
			fill(0); // black = alive
		else
			fill(255); // white = dead

		square(x * squareSize, y * squareSize, squareSize);
	});
}

function iterateOverGrid(grid, iterator) {
	for (let x = 0; x < width; ++x) {
		for (let y = 0; y < height; ++y) {
			iterator(grid[x][y], x, y);
		}
	}
}

function createGrid() {
	const rows = [];

	for (let x = 0; x < width; ++x) {
		const column = [];
		for (let y = 0; y < height; ++y) {
			column.push(false);
		}
		rows.push(column);
	}

	return rows;
}
