export function iterateOverGrid(fn) {
    for (let y = 0; y < 18; y++) {
        for (let x = 0; x < 26; x++) {
            fn(x, y);
        }
    }
}

export function addWall(wall, container) {
    container.classList.add("wall");
}

export function addCoffeeMachine(coffeeMachine, container) {
    container.classList.add("coffeeMachine");
}

export function meetingSetUp(meeting, container) {
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

    return meetingLink
}

export function sendLogoutRequestOnClose() {
    const httpRequest = new XMLHttpRequest();
    httpRequest.open("GET", "/logout");
    httpRequest.send();
}

export function createSquare(x, y) {
    const square = document.createElement("div");
    square.classList.add("item");
    let squareId = x + "-" + y;
    square.setAttribute("id", squareId);
    return square
}

export function refreshGridSquares() {
    for (let y = 0; y < 18; y++) {
        for (let x = 0; x < 26; x++) {
            let square = document.getElementById(x + "-" + y);
            square.removeAttribute("class");
            square.classList.add("item");
        }
    }
}

export function sendRequestWithUpdatedObject(xCoordinates, yCoordinates, type, action, link, url, username) {
    const httpRequest = new XMLHttpRequest();
    httpRequest.open("POST", url);
    httpRequest.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    httpRequest.send(
        JSON.stringify({
            objectType: type,
            action: action,
            username: username,
            x: xCoordinates,
            y: yCoordinates,
            link: link
        })
    );
}