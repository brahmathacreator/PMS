package controllers.security;

import com.typesafe.config.Config;
import controllers.constants.Constants;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import java.util.Date;

import views.html.error.error;

public class SessionMgmt extends Security.Authenticator {

    private final Config config;

    @Inject
    public SessionMgmt(Config config) {
        this.config = config;
    }

    @Override
    public String getUsername(Http.Context ctx) {
        try {
            // see if user is logged in
            if (ctx.session().get(Constants.SESSION_USER_NAME) == null)
                return null;

            // see if the session is expired
            String previousTick = ctx.session().get(Constants.SESSION_ACTIVITY_TIME);
            if (previousTick != null && !previousTick.equals("")) {
                long previousT = Long.valueOf(previousTick);
                long currentT = new Date().getTime();
                long timeout = Long.valueOf(config.getString("sessionTimeout")) * 1000 * 60;
                if ((currentT - previousT) > timeout) {
                    ctx.session().clear();
                    return null;
                }
            }
            // update time in session
            String tickString = Long.toString(new Date().getTime());
            ctx.session().put(Constants.SESSION_ACTIVITY_TIME, tickString);
            return ctx.session().get(Constants.SESSION_USER_NAME);
        } catch (Exception ex) {
            Logger.error("Error in [Main][index]: " + ex);
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        ctx.session().clear();
        return ok(error.render(Http.Context.current().messages().at("app.details"), (Http.Context.current().messages().at("app.status") + " 500 - " + Http.Context.current().messages().at("app.session.timeout"))));
    }

}
