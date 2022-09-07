package online.kancl.page.userpage;

import online.kancl.db.TransactionJobRunner;
import online.kancl.page.PageContext;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

public class UserPageController extends Controller {

    private final PebbleTemplateRenderer pebbleTemplateRenderer;
    //private final TransactionJobRunner transactionJobRunner;


    public UserPageController(PebbleTemplateRenderer pebbleTemplateRenderer, TransactionJobRunner transactionJobRunner) {
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
        //this.transactionJobRunner = transactionJobRunner;
    }

    @Override
    public String get(Request request, Response response) {
        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, new PageContext(request));
    }


}