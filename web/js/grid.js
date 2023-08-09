const grid = [];
const container = document.getElementById("container");

let me = null;
let lastData = [];

function canMoveRight() {
    for (const object of lastData.objects) {
        if (object.type === "wall") {
            if ((me.x + 1) === object.x && me.y === object.y) {
                return false;
            }
        }
    }

    return true;
}

function canMoveLeft() {
    for (const object of lastData.objects) {
        if (object.type === "wall") {
            if ((me.x - 1) === object.x && me.y === object.y) {
                return false;
            }
        }
    }

    return true;
}

function canMoveUp() {
    for (const object of lastData.objects) {
        if (object.type === "wall") {
            if ((me.y - 1) === object.y && me.x === object.x) {
                return false;
            }
        }
    }

    return true;
}

function canMoveDown() {
    for (const object of lastData.objects) {
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
        square.addEventListener("click", () => handleEdit(x, y));
        let squareId = x + "-" + y;
        square.setAttribute("id", squareId);
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

function addMeeting(meeting, container) {
    const xCoordinate = meeting.x;
    const yCoordinate = meeting.y;

    container.classList.add("meeting");

    const meetingElement = document.createElement("div");
    meetingElement.classList.add("meeting-state");
    meetingElement.textContent = "Meeting";
    container.appendChild(meetingElement);

    const meetingLink = document.createElement("a");
    meetingLink.textContent = "Join a meeting";
    meetingLink.setAttribute("href", meeting.link);
    meetingLink.setAttribute("target", "_blank");
    meetingElement.appendChild(meetingLink);

    meetingLink.addEventListener("click", function () {
        sendRequestWithUpdatedObject(xCoordinate, yCoordinate, "user", "move");
    });
}

function addWall(wall, container) {
    container.classList.add("wall");
}

function addCoffeeMachine(coffeeMachine, container) {
    container.classList.add("coffeeMachine");
}

document.addEventListener("keydown", handleUserMove);

function handleUserMove(e) {
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
        if (lastData) {
            refreshOfficeState(lastData);
        }
        sendRequestWithUpdatedObject(me.x, me.y, "user", "move")
    }
}

window.addEventListener("load", () => {
    const fetchInterval = 1000;
    fetchOfficeState();
    setInterval(fetchOfficeState, fetchInterval);
});

window.addEventListener("unload", function (e) {
    e.preventDefault();
    sendLogoutRequestOnClose();
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
    const userElements = document.getElementsByName("user");
    for (const userElement of userElements) {
        userElement.remove();
    }

    refreshGridSquares()

    for (const object of data.objects) {
        const x = object.x;
        const y = object.y;
        const coordinates = grid[x][y];

        if (object.type === "wall") {
            addWall(object, coordinates);
        } else if (object.type === "user") {
            addUser(object, coordinates);
            if (object.username === data.me) {
                me = object;
            }
        } else if (object.type === "meeting") {
            addMeeting(object, coordinates);
        } else if (object.type === "coffeeMachine") {
            addCoffeeMachine(object, coordinates);
        }
    }
}

function refreshGridSquares() {
    for (let y = 0; y < 18; y++) {
        for (let x = 0; x < 26; x++) {
            let square = document.getElementById(x + "-" + y);
            square.removeAttribute("class");
            square.classList.add("item");
        }
    }
}

function sendRequestWithUpdatedObject(xCoordinates, yCoordinates, type, action) {
    const httpRequest = new XMLHttpRequest();
    const url = "/api/office";
    httpRequest.open("POST", url);
    httpRequest.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    httpRequest.send(
        JSON.stringify({
            objectType: type,
            username: me.username,
            action: action,
            x: xCoordinates,
            y: yCoordinates
        })
    );
}

function sendLogoutRequestOnClose() {
    const httpRequest = new XMLHttpRequest();
    httpRequest.open("GET", "/logout");
    httpRequest.send();
}
