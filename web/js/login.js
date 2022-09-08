const loginButton = document.getElementById("submit");
//const elements = [document.getElementById("username"), document.getElementById("password")];

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


//TODO https://stackoverflow.com/questions/7115022/how-do-i-enumerate-all-of-the-html-ids-in-a-document-with-javascript

