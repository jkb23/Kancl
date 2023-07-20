const grid = [];
const container = document.getElementById("container");

let me = null;
let lastData = [];
let canAddWalls = false;
let canAddMeetings = false;

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

    fetchOfficeState();
}

function handleEdit(x, y) {
    let square = document.getElementById(x + "-" + y);

    if (canAddWalls && !canAddMeetings) {
        square.removeAttribute("class");
        square.classList.add("item");
        sendRequestWithUpdatedObject(x, y, "wall", "add");
    }

    if (canAddMeetings && !canAddWalls) {
        square.removeAttribute("class");
        square.classList.add("item");
        sendRequestWithUpdatedObject(x, y, "meeting", "add");
    }
}

createGrid();

function addMeeting(meeting, container) {
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
}

function addWall(wall, container) {
    container.classList.add("wall");
}

function addCoffeeMachine(coffeeMachine, container) {
    container.classList.add("coffeeMachine");
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

function handleEnableAddWalls() {
    canAddWalls = !canAddWalls;
    if (canAddMeetings) {
        canAddMeetings = false;
    } else {
        container.classList.toggle("inEditMode");
        document.getElementById("editLabel").classList.toggle("show");
    }
}

const editWallsButton = document.getElementById("editWallsButton");
editWallsButton.addEventListener("click", handleEnableAddWalls);

function handleEnableAddMeetings() {
    canAddMeetings = !canAddMeetings;
    if (canAddWalls) {
        canAddWalls = false;
    } else {
        container.classList.toggle("inEditMode");
        document.getElementById("editLabel").classList.toggle("show");
    }
}

const editMeetingsButton = document.getElementById("editMeetingsButton");
editMeetingsButton.addEventListener("click", handleEnableAddMeetings);

function handleSaveButton() {
    sendRequestWithUpdatedObject(0, 0, "none", "rewrite")
    window.location.href = "/";
}

const saveButton = document.getElementById("saveButton");
saveButton.addEventListener("click", handleSaveButton);

function fetchOfficeState() {
    fetch("/api/edit")
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

    for (const object of data.objects) {
        const x = object.x;
        const y = object.y;
        const coordinates = grid[x][y];

        if (object.type === "wall") {
            addWall(object, coordinates);
        } else if (object.type === "user") {
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

function sendRequestWithUpdatedObject(xCoordinates, yCoordinates, type, action, update) {
    const httpRequest = new XMLHttpRequest();
    const url = "/api/edit";
    httpRequest.open("POST", url);
    httpRequest.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    httpRequest.send(
        JSON.stringify({
            objectType: type,
            action: action,
            x: xCoordinates,
            y: yCoordinates,
        })
    );
}

function sendLogoutRequestOnClose() {
    const httpRequest = new XMLHttpRequest();
    httpRequest.open("GET", "/logout");
    httpRequest.send();
}
