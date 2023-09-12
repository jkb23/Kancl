package online.kancl.page.office;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import online.kancl.db.DatabaseRunner;
import online.kancl.db.UserStorage;
import online.kancl.objects.GridData;
import online.kancl.objects.User;
import online.kancl.objects.Wall;
import online.kancl.objects.MeetingObject;
import online.kancl.test.ProductionDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ProductionDatabase.class)
class OfficeControllerTest {

    private final DatabaseRunner dbRunner;

    OfficeControllerTest(DatabaseRunner dbRunner) {
        this.dbRunner = dbRunner;
    }

    @Test
    void correctJsonEncapsulation(@Injectable Response response, @Mocked Request request, @Mocked Session session) {
        new Expectations() {{
            request.session();
            result = session;

            session.attribute("user");
            result = "John";
        }};

        GridData gridData = new GridData();

        User user1 = new User("correct", new UserStorage(dbRunner));
        Wall wall = new Wall(1,1);
        MeetingObject meetingObject = new MeetingObject(5, 5, "zoom.com", "name", "id");

        gridData.addUser(user1);
        gridData.addWall(wall);
        gridData.addMeeting(meetingObject);

        OfficeController officeController = new OfficeController(gridData);
        assertThat(officeController.get(request, response))
                .isEqualTo("{\"objects\":[{\"type\":\"user\",\"username\":\"correct\",\"status\":\"Mam se dobre!\"" +
                        ",\"x\":13,\"y\":8},{\"type\":\"wall\",\"x\":1,\"y\":1}" +
                        ",{\"type\":\"zoom\",\"link\":\"zoom.com\",\"x\":5,\"y\":5}],\"me\":\"John\"}");

    }
}
