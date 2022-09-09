const grid = [];
const container = document.getElementById("container");

const array = [];
obstacles = [][2];

//TODO: check the walls
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

  //TODO: add varible with h4(username) and p(state)
  const userState = document.createElement("div");
  userState.classList.add("state");
  const userHeading = document.createElement("h4");
  userState.appendChild(userHeading);
  userHeading.textContent += "Username"
  userState.innerHTML += "<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Etiam quis quam. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Fusce aliquam vestibulum ipsum. Etiam dui sem, fermentum vit</p>";

  element.appendChild(userState);
}

function removeUser(user, container) {
 const element = document.createElement("div");
 element.classList.add("user");
 element.id = "user";
 container.removeChild(element);
}

function addZoom(zoom, container) {
 container.classList.add("zoom");

 const zoomItem = document.querySelector(".zoom")
 const zoomEl = document.createElement("div");
 zoomEl.classList.add("zoom_state");
 zoomEl.textContent += "Zoom meeting"
 zoomItem.appendChild(zoomEl)

 //TODO: add varible with link
 const zoomLink = document.createElement("a");
 zoomLink.textContent += "Připojit se na meeting"
 zoomLink.setAttribute("href", "https://www.google.com/")
 zoomLink.setAttribute("target", "_blank")
 zoomEl.appendChild(zoomLink);

}

function addWall(wall, container) {
 container.classList.add("wall");
}

function getUserCoordinates(user, coordinates) {
 document.addEventListener("keydown", handleKey);
 let userEl = document.getElementById("user");

 function handleKey(e) {
   if (e.key === "ArrowUp" || e.key === "w") {
     if (user.y > 0) {
       user.y--;
       userEl.remove();
     }
     sendRequest(user.x, user.y)

   } else if (e.key === "ArrowRight" || e.key === "d") {
     if (user.x < 25) {
       user.x++;
       userEl.remove();
     }
     sendRequest(user.x, user.y)

   } else if (e.key === "ArrowLeft" || e.key === "a") {
     if (user.x > 0) {
       user.x--;
       userEl.remove();
     }
     sendRequest(user.x, user.y)

   } else if (e.key === "ArrowDown" || e.key === "s") {
     if (user.y < 17) {
       user.y++;
       userEl.remove();
     }
     sendRequest(user.x, user.y)
   }
 }
}

window.addEventListener("load", () => {
 let fetchInterval = 1000;
 fetchOfficeState();
 setInterval(fetchOfficeState, fetchInterval);
});

function fetchOfficeState() {
 fetch("/api/office")
   .then(function (response) {
     return response.json();
   })
   .then(function (data) {
     refreshOfficeState(data);
   })
   .catch(function (err) {
     console.log("error: " + err);
   });
}

function refreshOfficeState(data) {
 for (const object of data.objects) {
   const x = object.x;
   const y = object.y;
   const coordinates = grid[x][y];

   if (object.type === "wall") {
     addWall(object, coordinates);
   } else if (object.type === "user") {
     addUser(object, coordinates);
     getUserCoordinates(object, coordinates);
   } else if (object.type === "zoom") {
     addZoom(object, coordinates);
   }
 }
}

function sendRequest(xCoordinates, yCoordinates) {
 let xmlhttp = new XMLHttpRequest();
 let url = "/api/office";
 xmlhttp.open("POST", url);
 xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
 xmlhttp.send(
   JSON.stringify({
     objectType: "user",
     username: "correct",
     action: "move",
     x: xCoordinates,
     y: yCoordinates
   })
 );
}