const loginButton = document.getElementById("submit");

const elements = document.querySelectorAll("input")

for (const element of elements) {
    element.oninput = () => {
        if (elements[0].value !== "" && elements[1].value !== "") {
            loginButton.removeAttribute("disabled")
        } else {
            loginButton.setAttribute("disabled", "")
        }
    }
}
