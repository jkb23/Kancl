package online.kancl.auth;

import online.kancl.page.PageContext;
import spark.Request;
import spark.Response;


public class AuthGuard {


    public static String checkIfLogged(Request request, Response response) {
        PageContext pageContext = new PageContext(request);
        if ("".equals(pageContext.getUsername())) {
            response.redirect("/login");
            return "";
        } else {
            return request, response;
        }
    }
}