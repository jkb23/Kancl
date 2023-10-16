package online.kancl;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import online.kancl.db.ConnectionProvider;
import online.kancl.db.DatabaseRunner;
import online.kancl.db.SchemaCreator;
import online.kancl.db.TransactionJobRunner;
import online.kancl.db.UserStorage;
import online.kancl.objects.GridData;
import online.kancl.objects.OfficeObjectsCreator;
import online.kancl.page.edit.EditController;
import online.kancl.page.edit.EditOfficeController;
import online.kancl.page.login.LoginController;
import online.kancl.page.login.LoginInfo;
import online.kancl.page.logout.LogoutController;
import online.kancl.page.main.MainPageController;
import online.kancl.page.office.OfficeController;
import online.kancl.page.recreatedb.RecreateDbController;
import online.kancl.page.registration.RegistrationController;
import online.kancl.page.registration.RegistrationInfo;
import online.kancl.page.userpage.UserPageController;
import online.kancl.page.zoom.callback.ZoomCallbackController;
import online.kancl.page.zoom.meetings.ZoomController;
import online.kancl.page.zoom.meetings.ZoomMeetingController;
import online.kancl.server.ExceptionHandler;
import online.kancl.server.WebServer;
import online.kancl.server.template.PebbleExtension;
import online.kancl.server.template.PebbleTemplateRenderer;
import online.kancl.util.DirectoryHashCalculator;

import java.nio.file.Path;
import java.nio.file.Paths;

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
        OfficeObjectsCreator officeObjectsCreator = new OfficeObjectsCreator(new UserStorage(new DatabaseRunner(connectionProvider.getConnection())));
        officeObjectsCreator.create(gridData);

        WebServer webServer = new WebServer(8081, new ExceptionHandler(), transactionJobRunner, "/login");
        webServer.addRoute("/", () -> new MainPageController(pebbleTemplateRenderer));
        webServer.addRoute("/recreateDb", () -> new RecreateDbController(schemaCreator));
        webServer.addRoute("/user", dbRunner -> new UserPageController(pebbleTemplateRenderer, new UserStorage(dbRunner), gridData, transactionJobRunner));
        webServer.addRoute("/register", dbRunner -> new RegistrationController(pebbleTemplateRenderer, transactionJobRunner, new RegistrationInfo(), new UserStorage(dbRunner), gridData));
        webServer.addRoute("/login", dbRunner -> new LoginController(pebbleTemplateRenderer, transactionJobRunner, new LoginInfo(), gridData, new UserStorage(dbRunner)));
        webServer.addRoute("/logout", () -> new LogoutController(gridData));
        webServer.addRoute("/api/office", () -> new OfficeController(gridData));
        webServer.addRoute("/api/edit", () -> new EditOfficeController(gridData));
        webServer.addRoute("/edit", () -> new EditController(pebbleTemplateRenderer));
        webServer.addRoute("/zoom-callback", ZoomCallbackController::new);
        webServer.addRoute("/zoom-meeting", () -> new ZoomController(pebbleTemplateRenderer));
        webServer.addRoute("/api/zoom-meeting", () -> new ZoomMeetingController(gridData));
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
}
