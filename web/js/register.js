const loginButton = document.getElementById("submit");
const elements = document.querySelectorAll("input");
const passwordInput = document.getElementById("password");
const passwordCheck = document.getElementById("passwordCheck");
const message = document.getElementById("message");

for (let i = 0; i < elements.length; i++) {
    elements[i].oninput = () => {
        if (!(elements[0].value === "") && !(elements[1].value === "") && !(elements[2].value === "") && !(elements[3].value === "")) {
            loginButton.removeAttribute("disabled")
        } else {
            loginButton.setAttribute("disabled", "")
        }
    }
}
