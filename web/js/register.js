const loginButton = document.getElementById("submit");
const elements = document.querySelectorAll("input");
const passwordInput = document.getElementById("password");
const passwordCheck = document.getElementById("passwordCheck");
const message = document.getElementById("message");

for (let i = 0; i < elements.length; i++) {
    elements[i].oninput = () => {
        if (!(elements[0].value == "") && !(elements[1].value == "") && !(elements[2].value == "") && !(elements[3].value == "")) {
            loginButton.removeAttribute("disabled")
        } else {
            loginButton.setAttribute("disabled", "")
        }
    }
}

let isEqual = function() {
  if (passwordInput.value == passwordCheck.value) {
    message.setAttribute("hidden", "");
    submit.removeAttribute("disabled");

  } else {
    message.removeAttribute("hidden");
    message.style.color = '#DC343B';
    message.innerHTML = 'Not matching';
    submit.setAttribute("disabled", "");
  }
}

let isLongEnough = function() {
    if (passwordInput.value.length < 5) {
       passwordInput.style.color="#DC343B";
    } else {
        passwordInput.style.color="#f7f7f7";
    }
}





