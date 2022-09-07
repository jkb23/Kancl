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

function addUser(user, container) {    
    const element = document.createElement('div');
    element.classList.add("user"); 
    element.id = "user";
    container.appendChild(element);  
}

var gridData = {
    objects: [
        {type: "user", x: 12, y: 17},
        {type: "wall", x: 0, y: 4},
        {type: "wall", x: 1, y: 4},
        {type: "wall", x: 2, y: 4},
        {type: "wall", x: 3, y: 4},
        {type: "wall", x: 4, y: 4},
        {type: "wall", x: 5, y: 4},
        {type: "wall", x: 6, y: 4},
        {type: "wall", x: 7, y: 4}
    ]
};

function addWall(wall, container) {  
    container.classList.add("wall");   
}

for (const object of gridData.objects) {
    const container =  grid[object.x][object.y];
   
    if(object.type === "wall") {
        addWall(object, container);
    } else if(object.type === "user") {
        addUser(object, container);
    }
}