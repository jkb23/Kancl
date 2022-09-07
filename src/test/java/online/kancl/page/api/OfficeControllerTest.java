package online.kancl.page.api;

import online.kancl.objects.GridData;
import online.kancl.objects.User;
import online.kancl.objects.Wall;
import online.kancl.page.app.AppController;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OfficeControllerTest {
    @Test
    void correctJsonEncapsulation(){
        var gridData = new GridData();

        User user1 = new User("Jozko");
        User user2 = new User("Ferko");
        Wall wall = new Wall(1,1);

        user2.moveDown();
        user1.moveRight();
        gridData.addUser(user1);
        gridData.addWall(wall);
        gridData.addUser(user2);

        AppController appController = new AppController(gridData);
        System.out.println(appController.get(null, null));
        assertThat(appController.get(null, null))
                .isEqualTo("{\"users\":[{\"username\":\"Jozko\",\"x\":1,\"y\":0}" +
                        ",{\"username\":\"Ferko\",\"x\":0,\"y\":1}]}");
    }
}
