const grid = [];
const container = document.getElementById('container');
let user = document.getElementById("user");
const array = [];
obstacles = [][2];

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

function addUserToDefaultCoordinates() {
    const defaultCoordinates =  grid[12][17]
    const user = document.createElement('div');
    user.classList.add("user"); 
    user.id = "user";
    defaultCoordinates.appendChild(user);  
}

console.log(grid);

function addWalls(wall) {
    console.log(wall.length)
    for(let i = 0; i < wall.length; i++) {
        console.log(wall[i]);
    }
}

window.addEventListener('load', () => {
    addUserToDefaultCoordinates()
    addWalls([[4,0][4,1]])
})