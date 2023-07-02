package online.kancl;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import online.kancl.db.*;
import online.kancl.objects.CoffeeMachine;
import online.kancl.objects.GridData;
import online.kancl.objects.MeetingObject;
import online.kancl.objects.Wall;
import online.kancl.page.login.LoginController;
import online.kancl.page.login.LoginInfo;
import online.kancl.page.logout.LogoutController;
import online.kancl.page.main.MainPageController;
import online.kancl.page.office.OfficeController;
import online.kancl.page.recreatedb.RecreateDbController;
import online.kancl.page.registration.RegistrationController;
import online.kancl.page.registration.RegistrationInfo;
import online.kancl.page.userpage.UserPageController;
import online.kancl.server.ExceptionHandler;
import online.kancl.server.WebServer;
import online.kancl.server.template.PebbleExtension;
import online.kancl.server.template.PebbleTemplateRenderer;
import online.kancl.util.DirectoryHashCalculator;
import online.kancl.util.JsonObjectParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final Path TEMPLATE_DIRECTORY = Paths.get("src", "main", "resources");
    public static final Path SQL_SCRATCH_DIRECTORY = Paths.get("src", "main", "resources", "sql");
    private static final Path DB_DIRECTORY = Paths.get("db");
    private static final String DB_NAME = "data";

    public static void main(String[] args) {
        PebbleTemplateRenderer pebbleTemplateRenderer = createPebbleTemplateRenderer(TEMPLATE_DIRECTORY);
        ConnectionProvider connectionProvider = ConnectionProvider.forDatabaseInFile(DB_DIRECTORY, DB_NAME);
        DirectoryHashCalculator directoryHashCalculator = new DirectoryHashCalculator();
        TransactionJobRunner transactionJobRunner = new TransactionJobRunner(connectionProvider);
        SchemaCreator schemaCreator = new SchemaCreator(directoryHashCalculator, connectionProvider, SQL_SCRATCH_DIRECTORY);
        schemaCreator.recreateSchemaIfNeeded();
        GridData gridData = new GridData();
        JsonObjectParser jsonObjectParser = new JsonObjectParser(new UserStorage(new DatabaseRunner(connectionProvider.getConnection())));
        if (!jsonObjectParser.createGridDataFromJson(gridData)) {
            createInitialGridTemplate(gridData);
        }

        WebServer webServer = new WebServer(8081, new ExceptionHandler(), transactionJobRunner, "/login");
        webServer.addRoute("/", () -> new MainPageController(pebbleTemplateRenderer));
        webServer.addRoute("/recreateDb", () -> new RecreateDbController(schemaCreator));
        webServer.addRoute("/user", (dbRunner) -> new UserPageController(pebbleTemplateRenderer, new UserStorage(dbRunner), gridData, transactionJobRunner));
        webServer.addRoute("/register", (dbRunner) -> new RegistrationController(pebbleTemplateRenderer, transactionJobRunner, new RegistrationInfo(), new UserStorage(dbRunner), gridData));
        webServer.addRoute("/login", (dbRunner) -> new LoginController(pebbleTemplateRenderer, transactionJobRunner, new LoginInfo(), gridData, new UserStorage(dbRunner)));
        webServer.addRoute("/logout", () -> new LogoutController(gridData));
        webServer.addRoute("/api/office", () -> new OfficeController(gridData));
        webServer.addPublicPaths("/login", "/register", "/recreateDb");
        webServer.start();

        System.out.println("Server running");
    }

    public static PebbleTemplateRenderer createPebbleTemplateRenderer(Path templateDirectory) {
        FileLoader pebbleTemplateLoader = new FileLoader();
        pebbleTemplateLoader.setPrefix(templateDirectory.toAbsolutePath().toString());
        PebbleEngine pebbleEngine = new PebbleEngine.Builder()
                .loader(pebbleTemplateLoader)
                .extension(new PebbleExtension())
                .cacheActive(false)
                .build();

        return new PebbleTemplateRenderer(pebbleEngine);
    }

    private static void createInitialGridTemplate(GridData gridData) {
        List<String> meetingsLinks = addMeetingLinksIfExistent();
        gridData.addWallsList(getWallList());
        gridData.addMeeting(new MeetingObject(25, 0, meetingsLinks.get(0)));
        gridData.addMeeting(new MeetingObject(0, 0, meetingsLinks.get(1)));
        gridData.addMeeting( new MeetingObject(0, 17, meetingsLinks.get(2)));
        gridData.addMeeting(new MeetingObject(25, 17, meetingsLinks.get(3)));
        gridData.addCoffeeMachine(new CoffeeMachine(12, 0));
        gridData.addCoffeeMachine(new CoffeeMachine(13, 0));
    }

    private static List<Wall> getWallList() {
        return Arrays.asList(
                new Wall(0, 4),
                new Wall(1, 4),
                new Wall(2, 4),
                new Wall(3, 4),
                new Wall(6, 3),
                new Wall(6, 4),
                new Wall(6, 0),
                new Wall(6, 1),
                new Wall(6, 2),
                new Wall(7, 4),
                new Wall(8, 4),
                new Wall(9, 4),
                new Wall(10, 4),
                new Wall(19, 0),
                new Wall(19, 1),
                new Wall(19, 2),
                new Wall(19, 4),
                new Wall(19, 3),
                new Wall(22, 4),
                new Wall(23, 4),
                new Wall(24, 4),
                new Wall(25, 4),
                new Wall(18, 4),
                new Wall(17, 4),
                new Wall(16, 4),
                new Wall(15, 4),
                new Wall(0, 13),
                new Wall(1, 13),
                new Wall(2, 13),
                new Wall(3, 13),
                new Wall(6, 14),
                new Wall(6, 13),
                new Wall(6, 15),
                new Wall(6, 16),
                new Wall(6, 17),
                new Wall(19, 14),
                new Wall(19, 15),
                new Wall(19, 16),
                new Wall(19, 17),
                new Wall(19, 13),
                new Wall(22, 13),
                new Wall(23, 13),
                new Wall(24, 13),
                new Wall(25, 13)
        );
    }

    private static List<String> addMeetingLinksIfExistent() {
        List<String> meetingsLinks = new ArrayList<>();
        try {
            File linksFile = new File("meetinglinks.txt");
            Scanner scanner = new Scanner(linksFile);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                meetingsLinks.add(data);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            meetingsLinks.add("https://www.google.com/");
            meetingsLinks.add("https://www.google.com/");
            meetingsLinks.add("https://www.google.com/");
            meetingsLinks.add("https://www.google.com/");
        }

        return meetingsLinks;
    }
}
