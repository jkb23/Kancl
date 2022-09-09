const loginButton = document.getElementById("submit");

const elements = document.querySelectorAll("input")

for (let i = 0; i < elements.length; i++) {
    elements[i].oninput = () => {
        if (!(elements[0].value == "") && !(elements[1].value == "")) {
            loginButton.removeAttribute("disabled")
        } else {
            loginButton.setAttribute("disabled", "")
        }
    }
}
