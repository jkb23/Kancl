package online.kancl.page.login;

import online.kancl.db.TransactionJobRunner;
import online.kancl.page.hello.HelloController;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

public class LoginController extends Controller {

    private final PebbleTemplateRenderer pebbleTemplateRenderer;
    private final TransactionJobRunner transactionJobRunner;
    private LoginInfo loginInfo;

    private final String InvalidCredentials = "Invalid credentials, try again";
    private final String BlockUser = "You was blocked, try again later";

    public LoginController(PebbleTemplateRenderer pebbleTemplateRenderer, TransactionJobRunner transactionJobRunner) {
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
        this.transactionJobRunner = transactionJobRunner;
    }

    @Override
    public String get(Request request, Response response) {
        loginInfo = new LoginInfo("");

        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, loginInfo);
    }

    @Override
    public String post(Request request, Response response) {
        return transactionJobRunner.runInTransaction((dbRunner) -> {
            var user = new Login(
                    request.queryParams("username"),
                    request.queryParams("password"));

            assert !isNotNull(user);

            //TODO here authenticate user
            loginInfo = new LoginInfo(InvalidCredentials);
            loginInfo = new LoginInfo(BlockUser);
            return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, loginInfo);

            //return pebbleTemplateRenderer.renderDefaultControllerTemplate(new HelloController(pebbleTemplateRenderer), new Object());
        });
    }

    private boolean isNotNull(Login user){
        return !user.password().isEmpty() && !user.username().isEmpty();
    }
}
