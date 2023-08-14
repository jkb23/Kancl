import {
    addCoffeeMachine,
    addWall,
    createSquare,
    iterateOverGrid,
    meetingSetUp,
    refreshGridSquares,
    sendLogoutRequestOnClose,
    sendRequestWithUpdatedObject
} from "./common.js";

const grid = [];
const container = document.getElementById("container");

let lastData = [];
let canAddWalls = false;
let canAddMeetings = false;

window.addEventListener("load", () => {
    const fetchInterval = 1000;
    fetchOfficeState();
    setInterval(fetchOfficeState, fetchInterval);
});

window.addEventListener("unload", function (e) {
    e.preventDefault();
    sendLogoutRequestOnClose();
});

createGrid()

function createGrid() {
    iterateOverGrid((x, y) => {
        let square = createSquare(x, y)
        square.addEventListener("click", () => handleEdit(x, y));
        container.appendChild(square);

        if (y === 0) grid.push([]);

        grid[x].push(square);
    });

    fetchOfficeState();
}

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

    refreshGridSquares()

    for (const object of data.objects) {
        const x = object.x;
        const y = object.y;
        const coordinates = grid[x][y];

        if (object.type === "wall") {
            addWall(object, coordinates);
        } else if (object.type === "meeting") {
            meetingSetUp(object, coordinates);
        } else if (object.type === "coffeeMachine") {
            addCoffeeMachine(object, coordinates);
        }
    }
}

function handleEdit(x, y) {
    if (canAddWalls && !canAddMeetings) {
        sendRequestWithUpdatedObject(x, y, "wall", "add", "", "/api/edit", "");

    }
    if (canAddMeetings && !canAddWalls) {
        let linkElement = document.getElementById("editMeetingLink");
        let linkValue = linkElement.value;
        sendRequestWithUpdatedObject(x, y, "meeting", "add", linkValue, "/api/edit", "");
    }

}

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
    sendRequestWithUpdatedObject(0, 0, "none", "rewrite", "", "/api/edit", "")
    window.location.href = "/";
}

const saveButton = document.getElementById("saveButton");
saveButton.addEventListener("click", handleSaveButton);
