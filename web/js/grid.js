let user = document.getElementById("user");
const array = [];

let container = document.getElementById('container');
for (let i = 0; i < 468; i++) {
    var square = {};
    square.x = i%26;
    square.y = i%18;
    array.push(square);
    let element = document.createElement('div');
    element.classList.add("item");       
    container.appendChild(element);
  
};


console.log(array)