const registerButton = document.getElementById("submit");
const elements = document.querySelectorAll("input");
const passwordInput = document.getElementById("password");
const passwordCheck = document.getElementById("passwordCheck");

for (const element of elements) {
    element.oninput = () => {
        if (elements[0].value !== "" && elements[1].value !== "" && elements[2].value !== "" && elements[3].value !== "") {
            registerButton.removeAttribute("disabled")
        } else {
            registerButton.setAttribute("disabled", "")
        }
    }
}

const isEqual = function () {
    if (passwordInput.value === passwordCheck.value) {
        submit.removeAttribute("disabled");
        passwordCheck.style.color = "black";
        passwordCheck.style.borderColor = "#080891";

    } else {
        submit.setAttribute("disabled", "");
        passwordCheck.style.color = "red";
        passwordCheck.style.outlineColor = "red";
    }
};

const isLongEnough = function () {
    if (passwordInput.value.length < 5) {
        passwordInput.style.color = "#DC343B";
    } else {
        passwordInput.style.color = "#f7f7f7";
    }
};
