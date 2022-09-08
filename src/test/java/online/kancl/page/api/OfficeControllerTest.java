package online.kancl.page.api;

import online.kancl.objects.GridData;
import online.kancl.objects.User;
import online.kancl.objects.Wall;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

        OfficeController officeController = new OfficeController(gridData);
        System.out.println(officeController.get(null, null));
        assertThat(officeController.get(null, null))
                .isEqualTo("{\"objects\":[{\"type\":\"user\",\"username\":\"Jozko\",\"x\":0,\"y\":0}," +
                        "{\"type\":\"user\",\"username\":\"Ferko\",\"x\":0,\"y\":0}," +
                        "{\"type\":\"wall\",\"x\":1,\"y\":1}]}\n");
    }
}
