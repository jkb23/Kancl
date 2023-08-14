package online.kancl.server;

import online.kancl.db.DatabaseRunner;
import online.kancl.db.TransactionJobRunner;
import spark.Filter;
import spark.Request;
import spark.Route;
import spark.Spark;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class WebServer {

    private final TransactionJobRunner transactionJobRunner;
    private final String loginPage;
    private final Set<String> publicPaths;

    public WebServer(int port, ExceptionHandler exceptionHandler, TransactionJobRunner transactionJobRunner, String loginPage) {
        this.transactionJobRunner = transactionJobRunner;
        this.loginPage = loginPage;
        this.publicPaths = new HashSet<>();

        Spark.port(port);
        Spark.staticFiles.externalLocation("web");
        Spark.exception(Exception.class, exceptionHandler::handleException);

        Spark.before(protectPrivatePaths());
    }

    public void addPublicPaths(String... publicPaths) {
        this.publicPaths.addAll(List.of(publicPaths));
    }

    public void addRoute(String path, Supplier<Controller> controllerSupplier) {
        Spark.get(path, (request, response) -> controllerSupplier.get().get(request, response));
        Spark.post(path, (request, response) -> controllerSupplier.get().post(request, response));
    }

    public void addRoute(String path, Function<DatabaseRunner, Controller> controllerSupplier) {
        Spark.get(path, processGetWithTransaction(controllerSupplier));
        Spark.post(path, processPostWithTransaction(controllerSupplier));
    }

    private Filter protectPrivatePaths() {
        return (request, response) -> {
            String requestPath = request.pathInfo();
            if (!isUserLoggedIn(request) && !publicPaths.contains(requestPath)) {
                response.redirect(loginPage);
                Spark.halt();
            }
        };
    }

    private boolean isUserLoggedIn(Request request) {
        return request.session().attribute("user") != null;
    }

    private Route processGetWithTransaction(Function<DatabaseRunner, Controller> controllerSupplier) {
        return (request, response) -> transactionJobRunner.runInTransaction(dbRunner -> controllerSupplier.apply(dbRunner)
                .get(request, response));
    }

    private Route processPostWithTransaction(Function<DatabaseRunner, Controller> controllerSupplier) {
        return (request, response) -> transactionJobRunner.runInTransaction(dbRunner -> controllerSupplier.apply(dbRunner)
                .post(request, response));
    }

    public void start() {
        Spark.init();
    }
}
