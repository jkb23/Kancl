package online.kancl.server;

import spark.Spark;

public class WebServer {

    public WebServer(int port, ExceptionHandler exceptionHandler) {
        Spark.port(port);
        Spark.staticFiles.externalLocation("web");
        Spark.exception(Exception.class, exceptionHandler::handleException);
    }

    public void addRoute(String path, Controller controller) {
        Spark.get(path, controller::get);
        Spark.post(path, controller::post);
    }

    public void start() {
        Spark.init();
    }
}
