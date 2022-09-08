package online.kancl.server;

import online.kancl.db.DatabaseRunner;
import online.kancl.db.TransactionJobRunner;
import spark.Route;
import spark.Spark;

import java.util.function.Function;
import java.util.function.Supplier;

public class WebServer {

    private final TransactionJobRunner transactionJobRunner;

    public WebServer(int port, ExceptionHandler exceptionHandler, TransactionJobRunner transactionJobRunner) {
        this.transactionJobRunner = transactionJobRunner;
        Spark.port(port);
        Spark.staticFiles.externalLocation("web");
        Spark.exception(Exception.class, exceptionHandler::handleException);

        Spark.before((request, response) -> {
            if (request.session().attribute("user") == null && !request.pathInfo().equals("/login")) {
                response.redirect("/login");
                Spark.halt();
            }
        });
    }

    public void addRoute(String path, Supplier<Controller> controllerSupplier) {
        Spark.get(path, (request, response) -> controllerSupplier.get().get(request, response));
        Spark.post(path, (request, response) -> controllerSupplier.get().post(request, response));
    }

    public void addRoute(String path, Function<DatabaseRunner, Controller> controllerSupplier) {
        Spark.get(path, processGet(controllerSupplier));
        Spark.post(path, processPost(controllerSupplier));
    }

    private Route processGet(Function<DatabaseRunner, Controller> controllerSupplier) {
        return (request, response) -> {
            return transactionJobRunner.runInTransaction((dbRunner) -> {
                return controllerSupplier.apply(dbRunner)
                        .get(request, response);
            });
        };
    }

    private Route processPost(Function<DatabaseRunner, Controller> controllerSupplier) {
        return (request, response) -> {
            return transactionJobRunner.runInTransaction((dbRunner) -> {
                return controllerSupplier.apply(dbRunner)
                        .post(request, response);
            });
        };
    }

    public void start() {
        Spark.init();
    }
}
