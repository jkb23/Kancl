package online.kancl.page.login;

import online.kancl.db.TransactionJobRunner;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

public class LoginController extends Controller {

    private final PebbleTemplateRenderer pebbleTemplateRenderer;
    private final TransactionJobRunner transactionJobRunner;

    public LoginController(PebbleTemplateRenderer pebbleTemplateRenderer, TransactionJobRunner transactionJobRunner) {
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
        this.transactionJobRunner = transactionJobRunner;
    }

    @Override
    public String get(Request request, Response response) {
        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, new Object());
    }

    @Override
    public String post(Request request, Response response) {
        return transactionJobRunner.runInTransaction((dbRunner) -> {
            var user = new Login(
                    request.queryParams("username"),
                    request.queryParams("password"));

            if (!isNotNull(user))
            {
                //TODO show error massage
            }
            //TODO here authenticate user
            return "";
        });
    }

    private boolean isNotNull(Login user){
        return !user.password().isEmpty() && !user.username().isEmpty();
    }
}
