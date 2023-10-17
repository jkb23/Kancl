import {
    addCoffeeMachine,
    addWall,
    createSquare,
    iterateOverGrid,
    refreshGridSquares,
    sendLogoutRequestOnClose,
    sendRequestWithUpdatedObject
} from "./common.js";

const EDIT_URL = "/api/edit";
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
    fetch(EDIT_URL)
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

function meetingSetUp(meeting, container) {
    container.classList.add("meeting");

    let meetingLink = meeting.link;
    let meetingName = document.getElementById("editMeetingName").value;
    let square = document.getElementById(meeting.x + "-" + meeting.y);

    square.setAttribute('data-meeting-id', meeting.id);
    square.setAttribute('data-meeting-link', meetingLink);
    square.setAttribute('data-meeting-name', meetingName);
}

function meetingExists(square) {
    return square.className === "item meeting";
}

async function handleEdit(x, y) {
    if (canAddWalls && !canAddMeetings) {
        sendRequestWithUpdatedObject(x, y, "wall", "add", "", EDIT_URL, "", "", "");
    }

    if (canAddMeetings && !canAddWalls) {
        let square = document.getElementById(x + "-" + y);

        if (meetingExists(square)) {
            let meetingId = square.getAttribute('data-meeting-id');
            let meetingLink = square.getAttribute('data-meeting-link');
            let meetingName = square.getAttribute('data-meeting-name');

            if (meetingId) {
                await callZoomMeetingEndpoint(x, y, 'delete', meetingName, meetingId, meetingLink);
            }
        } else {
            let meetingName = document.getElementById("editMeetingName").value;
            await callZoomMeetingEndpoint(x, y,'create', meetingName, '', '');
        }
    }
}

async function callZoomMeetingEndpoint(x, y, action, meetingName, meetingId, meetingLink) {
    const httpRequest = new XMLHttpRequest();
    httpRequest.open("POST", '/zoom-meeting');
    httpRequest.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    httpRequest.send(
        JSON.stringify({
            xCoordinate: x,
            yCoordinate: y,
            action: action,
            meetingName: meetingName,
            meetingId: meetingId,
            meetingLink: meetingLink
        })
    );
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
    sendRequestWithUpdatedObject(0, 0, "none", "rewrite", "", EDIT_URL, "", "", "")
    window.location.href = "/";
}

const saveButton = document.getElementById("saveButton");
saveButton.addEventListener("click", handleSaveButton);
