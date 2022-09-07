package online.kancl;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import online.kancl.auth.Auth;
import online.kancl.objects.GridData;
import online.kancl.page.app.AppController;
import online.kancl.page.comments.CommentsController;
import online.kancl.page.hello.HelloController;
import online.kancl.page.login.LoginController;
import online.kancl.page.login.LogoutController;
import online.kancl.page.main.MainPageController;
import online.kancl.page.recreatedb.RecreateDbController;
import online.kancl.page.userpage.UserPageController;
import online.kancl.page.zoomhook.ZoomHookController;
import online.kancl.db.ConnectionProvider;
import online.kancl.db.SchemaCreator;
import online.kancl.db.TransactionJobRunner;
import online.kancl.page.main.Meetings;
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
        var directoryHashCalculator = new DirectoryHashCalculator();
        var schemaCreator = new SchemaCreator(directoryHashCalculator, connectionProvider, SQL_SCRATCH_DIRECTORY);
        schemaCreator.recreateSchemaIfNeeded();

        var transactionJobRunner = new TransactionJobRunner(connectionProvider);

        var meetings = new Meetings();

        var webServer = new WebServer(8081, new ExceptionHandler());
        webServer.addRoute("/", new MainPageController(pebbleTemplateRenderer, meetings));
        webServer.addRoute("/comments", new CommentsController(pebbleTemplateRenderer, transactionJobRunner));
        webServer.addRoute("/zoomhook", new ZoomHookController(meetings));
        webServer.addRoute("/recreateDb", new RecreateDbController(schemaCreator));
        webServer.addRoute("/hello", new HelloController(pebbleTemplateRenderer));
        webServer.addRoute("/login", new LoginController(pebbleTemplateRenderer, transactionJobRunner));
        webServer.addRoute("/user", new UserPageController(pebbleTemplateRenderer, transactionJobRunner));
        webServer.addRoute("/logout", new LogoutController());

        webServer.addRoute("/app", new AppController(gridData));
        webServer.start();

        System.out.println("Server running");
    }

    public static PebbleTemplateRenderer createPebbleTemplateRenderer(Path templateDirectory) {
        var pebbleTemplateLoader = new FileLoader();
        pebbleTemplateLoader.setPrefix(templateDirectory.toAbsolutePath().toString());
        var pebbleEngine = new PebbleEngine.Builder()
                .loader(pebbleTemplateLoader)
                .extension(new PebbleExtension())
                .cacheActive(false)
                .build();

        return new PebbleTemplateRenderer(pebbleEngine);
    }
}
