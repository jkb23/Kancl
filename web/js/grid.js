const grid = [];
const container = document.getElementById("container");

const array = [];
obstacles = [][2];
let me = null;
let lastData = null;

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
 element.setAttribute("name", "user")
 container.appendChild(element);

  //TODO: add varible with h4(username) and p(state)
  const userState = document.createElement("div");
  userState.classList.add("state");
  const userHeading = document.createElement("h4");
  userState.appendChild(userHeading);
  userHeading.textContent += user.username;
  userState.innerHTML += `<p>${user.status}</p>`;

  element.appendChild(userState);
}

function removeUser(user, container) {
 const element = document.createElement("div");
 element.classList.add("user");
 element.id = "user";
 container.removeChild(element);
}

function addZoom(zoom, container) {
 container.classList.add("zoom"); const zoomItem = document.querySelector(".zoom")
 const zoomEl = document.createElement("div");
 zoomEl.classList.add("zoom_state");
 zoomEl.textContent += "Zoom meeting"
 zoomItem.appendChild(zoomEl)

 const zoomLink = document.createElement("a");
 zoomLink.textContent += "Připojit se na meeting"
 zoomLink.setAttribute("href", zoom.link)
 zoomLink.setAttribute("target", "_blank")
 zoomEl.appendChild(zoomLink);
}

function addWall(wall, container) {
 container.classList.add("wall");
}

document.addEventListener("keyup", handleKey);

function handleKey(e) {
    console.log(e);
    let updatedUser = false;
    if (e.key === "ArrowUp" || e.key === "w") {
        if (me.y > 0) {
            me.y--;
        }
        updatedUser = true;
    } else if (e.key === "ArrowRight" || e.key === "d") {
        if (me.x < 25) {
            me.x++;
        }
        updatedUser = true;
    } else if (e.key === "ArrowLeft" || e.key === "a") {
        if (me.x > 0) {
            me.x--;
        }
        updatedUser = true;
    } else if (e.key === "ArrowDown" || e.key === "s") {
        if (me.y < 17) {
            me.y++;
        }
        updatedUser = true;
    }

    if (updatedUser) {
        e.preventDefault();
        console.log(me);
        if (lastData)
            refreshOfficeState(lastData);
        sendRequest(me.x, me.y)
    }
}

window.addEventListener("load", () => {
 let fetchInterval = 1000;
 fetchOfficeState();
 setInterval(fetchOfficeState, fetchInterval);
});

function fetchOfficeState() {
data = {};
 fetch("/api/office")
   .then(function (response) {
     return response.json();
   })
   .then(function (data) {
     lastData = data;
     refreshOfficeState(data);
   })
   .catch(function (err) {
     console.log("error: " + err);
   });
}

function refreshOfficeState(data) {
 let userElements = document.getElementsByName("user");
 for (const userElement of userElements) {
    userElement.remove();
 }

 let haveUser = false;

 for (const object of data.objects) {
   const x = object.x;
   const y = object.y;
   const coordinates = grid[x][y];

   if (object.type === "wall") {
     addWall(object, coordinates);
   } else if (object.type === "user") {
     addUser(object, coordinates);
     if (!haveUser) {
        me = object;
        haveUser = true;
     }
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