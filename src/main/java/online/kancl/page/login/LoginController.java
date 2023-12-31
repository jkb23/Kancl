package online.kancl.page.login;

import online.kancl.auth.AuthReturnCode;
import online.kancl.auth.Authenticator;
import online.kancl.db.TransactionJobRunner;
import online.kancl.db.UserStorage;
import online.kancl.objects.GridData;
import online.kancl.objects.User;
import online.kancl.page.PageContext;
import online.kancl.server.Controller;
import online.kancl.server.template.PebbleTemplateRenderer;
import spark.Request;
import spark.Response;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static online.kancl.auth.AuthReturnCode.CORRECT;
import static online.kancl.page.ZoomConstants.CLIENT_ID;
import static online.kancl.page.ZoomConstants.ZOOM_CALLBACK_URL;

public class LoginController extends Controller {

    private final PebbleTemplateRenderer pebbleTemplateRenderer;
    private final TransactionJobRunner transactionJobRunner;
    private final GridData gridData;
    private final UserStorage userStorage;
    private final Authenticator authenticator;

    private LoginInfo loginInfo;

    public LoginController(PebbleTemplateRenderer pebbleTemplateRenderer, TransactionJobRunner transactionJobRunner,
                           LoginInfo loginInfo, GridData gridData, UserStorage userStorage) {
        this.pebbleTemplateRenderer = pebbleTemplateRenderer;
        this.transactionJobRunner = transactionJobRunner;
        this.loginInfo = loginInfo;
        this.gridData = gridData;
        this.userStorage = userStorage;
        this.authenticator = new Authenticator(userStorage);
    }

    @Override
    public String get(Request request, Response response) {
        PageContext pageContext = new PageContext(request, userStorage);
        if ("".equals(pageContext.getUsername())) {
            loginInfo = new LoginInfo();

            return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, loginInfo);
        } else {
            response.redirect("/");

            return "";
        }
    }

    @Override
    public String post(Request request, Response response) {
        return transactionJobRunner.runInTransaction(dbRunner -> {
            Login user = new Login(
                    request.queryParams("username"),
                    request.queryParams("password"));

            return authenticate(request, response, authenticator, user);
        });
    }

    String authenticate(Request request, Response response, Authenticator authenticator, Login user) {
        AuthReturnCode returnCode = authenticator.checkCredentialsWithBruteForcePrevention(user.username(), user.password());
        if (returnCode.equals(CORRECT)) {
            User userObject = new User(user.username(), userStorage);
            gridData.addUser(userObject);
            request.session(true);
            request.session().attribute("user", user.username());
            response.redirect(getZoomAuthUrl());

            return "";
        }
        loginInfo.setErrorMessage(returnCode.message);

        return pebbleTemplateRenderer.renderDefaultControllerTemplate(this, loginInfo);
    }

    public String getZoomAuthUrl() {
        return "https://zoom.us/oauth/authorize?response_type=code&client_id="
                + CLIENT_ID
                + "&redirect_uri="
                + URLEncoder.encode(ZOOM_CALLBACK_URL, StandardCharsets.UTF_8);
    }
}
