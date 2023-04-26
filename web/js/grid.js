const grid = [];
const container = document.getElementById("container");

obstacles = [][2];
let me = null;
let lastData = null;

function canMoveRight() {
    for (let object of lastData.objects) {
        if (object.type === "wall") {
            if ((me.x + 1) === object.x && me.y === object.y) {
                return false;
            }
        }
    }

    return true;
}

function canMoveLeft() {
    for (let object of lastData.objects) {
        if (object.type === "wall") {
            if ((me.x - 1) === object.x && me.y === object.y) {
                return false;
            }
        }
    }

    return true;
}

function canMoveUp() {
    for (let object of lastData.objects) {
        if (object.type === "wall") {
            if ((me.y - 1) === object.y && me.x === object.x) {
                return false;
            }
        }
    }

    return true;
}

function canMoveDown() {
    for (let object of lastData.objects) {
        if (object.type === "wall") {
            if ((me.y + 1) === object.y && me.x === object.x) {
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

        if (y === 0) grid.push([]);

        grid[x].push(square);
    });
}

createGrid();

function addUser(user, container) {
    const element = document.createElement("div");
    element.classList.add("user");
    element.setAttribute("name", "user",)
    element.style.background = user.avatarBackgroundColor;
    container.appendChild(element);

    const userState = document.createElement("div");
    userState.classList.add("state");

    const userHeading = document.createElement("h4");
    userState.appendChild(userHeading);
    userHeading.textContent += user.username;
    userState.innerHTML += `<p>${user.status}</p>`;

    const userProfilePicture = document.createElement("p");
    userProfilePicture.textContent += user.username.charAt(0).toUpperCase();

    element.appendChild(userState);
    element.appendChild(userProfilePicture);
}

function addZoom(zoom, container) {
    container.classList.add("zoom");
    const zoomItem = document.querySelector(".zoom")
    const zoomEl = document.createElement("div");
    zoomEl.classList.add("zoom_state");
    zoomEl.textContent += "Zoom meeting"
    zoomItem.appendChild(zoomEl)

    const zoomLink = document.createElement("a");
    zoomLink.textContent += "Join a meeting"
    zoomLink.setAttribute("href", zoom.link)
    zoomLink.setAttribute("target", "_blank")
    zoomEl.appendChild(zoomLink);
}

function addWall(wall, container) {
    container.classList.add("wall");
}

function addCoffeeMachine(coffeeMachine, container) {
    container.classList.add("coffeeMachine");
}

document.addEventListener("keydown", handleKey);

function handleKey(e) {
    let updatedUser = false;
    if (e.key === "ArrowUp" || e.key === "w") {
        if (canMoveUp()) {
            if (me.y > 0) {
                me.y--;
            }
            updatedUser = true;
        }
    } else if (e.key === "ArrowRight" || e.key === "d") {
        if (canMoveRight()) {
            if (me.x < 25) {
                me.x++;
            }
            updatedUser = true;
        }
    } else if (e.key === "ArrowLeft" || e.key === "a") {
        if (canMoveLeft()) {
            if (me.x > 0) {
                me.x--;
            }
            updatedUser = true;
        }
    } else if (e.key === "ArrowDown" || e.key === "s") {
        if (canMoveDown()) {
            if (me.y < 17) {
                me.y++;
            }
            updatedUser = true;
        }
    } else if (e.key === "r") {
        fetchOfficeState();
    }

    if (updatedUser) {
        e.preventDefault();
        console.log('move', me);
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
    console.log('removing', userElements);
    for (const userElement of userElements) {
        userElement.remove();
    }

    for (const object of data.objects) {
        const x = object.x;
        const y = object.y;
        const coordinates = grid[x][y];

        if (object.type === "wall") {
            addWall(object, coordinates);
        } else if (object.type === "user") {
            console.log(object);
            addUser(object, coordinates);
            if (object.username === data.me) {
                me = object;
            }
        } else if (object.type === "zoom") {
            addZoom(object, coordinates);
        } else if (object.type === "coffeeMachine") {
            addCoffeeMachine(object, coordinates);
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
            username: me.username,
            action: "move",
            x: xCoordinates,
            y: yCoordinates
        })
    );
}