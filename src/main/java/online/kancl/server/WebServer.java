package online.kancl.server;

import spark.Spark;

import java.util.function.Supplier;

public class WebServer {

    public WebServer(int port, ExceptionHandler exceptionHandler) {
        Spark.port(port);
        Spark.staticFiles.externalLocation("web");
        Spark.exception(Exception.class, exceptionHandler::handleException);
    }

    public void addRoute(String path, Supplier<Controller> controllerSupplier) {
        Spark.get(path, (request, response) -> controllerSupplier.get().get(request, response));
        Spark.post(path, (request, response) -> controllerSupplier.get().post(request, response));
    }

    public void start() {
        Spark.init();
    }
}
