//const username = document.getElementById("username");
//const password = document.getElementById("password");
const loginButton = document.getElementById("submit");
//
//username.addEventListener('input', updateUsername);
//password.addEventListener('input', updatePassword);
//
//function updateUsername(e){
//    updateValue(e);
//}
//
//function updatePassword(e){
//    updateValue(e);
//}
//
//function updateValue(e) {
// if(e.target.value > "") {
//    console.log("má hodnotu")
//    loginButton.disabled = false;
//
// } else {
//    console.log("je prázdný")
//     loginButton.disabled = true;
// }
//}

const elements = document.querySelectorAll("input");


for (let i = 0; i < elements.length; i++) {
    elements[i].onchange = () => {
        if (!(elements[0].value == "") && !(elements[1].value == "")) {
            loginButton.removeAttribute("disabled")
        }
    }
}




