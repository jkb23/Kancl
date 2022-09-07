const grid = [];
const container = document.getElementById('container');
let user = document.getElementById("user");
const array = [];
obstacles = [][2];
let gridData;

function check_obstacles(x,y)
{
    for(i = 0; i != obstacles.length(); i++)
    {
        if(obstacles[i][0] == x)
        {
            if(obstacles[i][1] == y)
            {
                return false;
            }
        }
    }
    return true;
}

function iterateOverGrid(fn) {
    for (let y = 0; y < 18; y++) {
        for (let x = 0; x < 26; x++) {
            fn(x, y);
        }
    }
}

function createGrid() {
    iterateOverGrid((x, y) => {
        const square = document.createElement('div');
        square.classList.add("item");       
        container.appendChild(square);  

        if (y == 0)
            grid.push([]);
        
        grid[x].push(square);
    });
}

createGrid();

function addUser(user, container) {    
    const element = document.createElement('div');
    element.classList.add("user"); 
    element.id = "user";
    container.appendChild(element);  
}



function addWall(wall, container) {  
    container.classList.add("wall");   
}

window.addEventListener('load', () => {
    addUserToDefaultCoordinates()
    addWalls([[4,0][4,1]])
    var fetchInterval = 1000;
    setInterval(fetchOfficeState, fetchInterval);

})

function fetchOfficeState() {
  fetch('https://jsonplaceholder.typicode.com/todos/1')
    .then(function (response) {
      return response.json();
    })
    .then(function (data) {
      refreshOfficeState(data);
    })
    .catch(function (err) {
      console.log('error: ' + err);
    });
}

function refreshOfficeState(data) {
    console.log(data);
    gridData = JSON.parse(data);
}