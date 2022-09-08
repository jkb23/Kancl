const grid = [];
const container = document.getElementById("container");
let user = document.getElementById("user");
const array = [];
obstacles = [][2];
let gridData;

function check_obstacles(x, y) {
  for (i = 0; i != obstacles.length(); i++) {
    if (obstacles[i][0] == x) {
      if (obstacles[i][1] == y) {
        return false;
      }
    }
  }
  return true;
}

function iterateOverGrid(fn) {
  for (let y = 0; y < 18; y++) {
    for (let x = 0; x < 26; x++) {
      fn(x, y);
    }
  }
}

function createGrid() {
  iterateOverGrid((x, y) => {
    const square = document.createElement("div");
    square.classList.add("item");
    container.appendChild(square);

    if (y == 0) grid.push([]);

    grid[x].push(square);
  });
}

createGrid();

function addUser(user, container) {
  const element = document.createElement("div");
  element.classList.add("user");
  element.id = "user";
  container.appendChild(element);
}

function addZoom(zoom, container) {
  container.classList.add("zoom");
}

var gridData = {
  objects: [
    { type: "user", x: 12, y: 17 },
    { type: "wall", x: 0, y: 4 },
    { type: "wall", x: 1, y: 4 },
    { type: "wall", x: 2, y: 4 },
    { type: "wall", x: 3, y: 4 },
    { type: "wall", x: 4, y: 4 },
    { type: "wall", x: 5, y: 4 },
    { type: "wall", x: 6, y: 4 },
    { type: "wall", x: 6, y: 0 },
    { type: "wall", x: 6, y: 1 },
    { type: "wall", x: 6, y: 2 },
    { type: "wall", x: 19, y: 0 },
    { type: "wall", x: 19, y: 1 },
    { type: "wall", x: 19, y: 2 },
    { type: "wall", x: 19, y: 4 },
    { type: "wall", x: 20, y: 4 },
    { type: "wall", x: 21, y: 4 },
    { type: "wall", x: 22, y: 4 },
    { type: "wall", x: 23, y: 4 },
    { type: "wall", x: 24, y: 4 },
    { type: "wall", x: 25, y: 4 },
    { type: "zoom", x: 25, y: 0 },
  ],
};

function addWall(wall, container) {
  container.classList.add("wall");
}

for (const object of gridData.objects) {
  const container = grid[object.x][object.y];

  if (object.type === "wall") {
    addWall(object, container);
  } else if (object.type === "user") {
    addUser(object, container);
  } else if (object.type === "zoom") {
    addZoom(object, container);

    console.log(object.x, object.y);
  }
}

document.addEventListener("keydown", handleKey);

for (const object of gridData.objects) {
  function handleKey(e) {
    let containerCoordinates = container.getBoundingClientRect();



    if (e.key === "ArrowUp") {
      console.log("Go up");
      console.log(gridData.objects[0].type[user]);
    } else if (e.key === "ArrowRight") {
      console.log("Go right");
    } else if (e.key === "ArrowLeft") {
      console.log("Go left");
    } else {
      console.log("Go down");
    }
  }
window.addEventListener('load', () => {
    addUserToDefaultCoordinates()
    addWalls([[4,0][4,1]])
    var fetchInterval = 1000;
    setInterval(fetchOfficeState, fetchInterval);

})

function fetchOfficeState() {
  fetch('https://jsonplaceholder.typicode.com/todos/1')
    .then(function (response) {
      return response.json();
    })
    .then(function (data) {
      refreshOfficeState(data);
    })
    .catch(function (err) {
      console.log('error: ' + err);
    });
}

function refreshOfficeState(data) {
    console.log(data);
    gridData = JSON.parse(data);
}