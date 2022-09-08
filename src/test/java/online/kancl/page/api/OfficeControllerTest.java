package online.kancl.page.api;

import online.kancl.db.DatabaseRunner;
import online.kancl.objects.GridData;
import online.kancl.objects.User;
import online.kancl.objects.Wall;
import online.kancl.objects.ZoomObject;
import online.kancl.test.ProductionDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ProductionDatabase.class)
class OfficeControllerTest {

    private final DatabaseRunner dbRunner;

    OfficeControllerTest(DatabaseRunner dbRunner) {
        this.dbRunner = dbRunner;
    }

    @Test
    void correctJsonEncapsulation(){
        var gridData = new GridData();

        User user1 = new User("correct", dbRunner);
        Wall wall = new Wall(1,1);
        ZoomObject zoomObject = new ZoomObject(5, 5, "zoom.com");

        user1.moveRight();
        gridData.addUser(user1);
        gridData.addWall(wall);
        gridData.addZoom(zoomObject);

        OfficeController officeController = new OfficeController(gridData);
        assertThat(officeController.get(null, null))
                .isEqualTo("{\"objects\":[{\"type\":\"user\",\"username\":\"correct\",\"status\":\"Mam se dobre!\"" +
                        ",\"x\":0,\"y\":0},{\"type\":\"wall\",\"x\":1,\"y\":1}" +
                        ",{\"type\":\"zoom\",\"link\":\"zoom.com\",\"x\":5,\"y\":5}]}");
    }
}
