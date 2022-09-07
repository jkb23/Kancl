package online.kancl.page.app;

import online.kancl.objects.GridData;
import online.kancl.objects.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppControllerTest {

    @Test
    void jsonTest(){
        var gridData = new GridData();

        User user1 = new User("Jozko");
        User user2 = new User("Ferko");
        user2.moveDown();
        user1.moveLeft();
        gridData.addUser(user1);
        gridData.addUser(user2);

        AppController appController = new AppController(gridData);
        System.out.println(appController.get(null, null));
    }

}