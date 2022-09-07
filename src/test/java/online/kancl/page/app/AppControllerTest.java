package online.kancl.page.app;

import online.kancl.objects.GridData;
import online.kancl.objects.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppControllerTest {

    @Test
    void correctJsonEncapsulation(){
        var gridData = new GridData();

        User user1 = new User("Jozko");
        User user2 = new User("Ferko");
        user2.moveDown();
        user1.moveRight();
        gridData.addUser(user1);
        gridData.addUser(user2);

        AppController appController = new AppController(gridData);
        assertThat(appController.get(null, null))
                .isEqualTo("{\"users\":[{\"username\":\"Jozko\",\"x\":1,\"y\":0}" +
                        ",{\"username\":\"Ferko\",\"x\":0,\"y\":1}]}");
    }
}