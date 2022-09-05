package online.kancl.page.helloworld;

import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

public class HelloWorldController extends Controller {

    private final PebbleTemplateRenderer pebbleTemplateRenderer;

    private final HelloWorld helloWorld;

    public HelloWorldController(PebbleTemplateRenderer pebbleTemplateRenderer, HelloWorld helloWorld){
        this.helloWorld = helloWorld;
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
    }

    @Override
    public String get(Request request, Response response) {
        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, helloWorld);
    }
}
