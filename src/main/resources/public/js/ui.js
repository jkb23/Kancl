let grid;
let squareSize = 20;
let height = 40;
let width = 40;

let desiredValue = null;

function setup() {
	createCanvas(squareSize * width, squareSize * height);
	grid = createGrid();
}

function draw() {

	if (mouseIsPressed) {
		const x = Math.floor(mouseX / squareSize);
		const y = Math.floor(mouseY / squareSize);
		if (desiredValue === null) {
			desiredValue = !grid[x][y];
		}

		grid[x][y] = desiredValue;
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
