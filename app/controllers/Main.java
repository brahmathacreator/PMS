package controllers;


import com.typesafe.config.Config;
import controllers.constants.Constants;
import controllers.dto.Navigation;
import controllers.security.SessionMgmt;
import controllers.utils.Utils;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.Interface.UserMeta;
import views.html.index;
import views.html.error.error;
import views.html.dashboard.board;

import javax.inject.Inject;
import java.util.Date;

public class Main extends Controller {


    @Inject
    private UserMeta userMeta;
    @Inject
    private FormFactory factory;
    @Inject
    private Config config;

    public Main() {
    }


    public Result index() {
        Logger.debug("Inside [Main][index]");
        Integer count = 0;
        Form<Navigation> navForm = null;
        Navigation nav = null;
        try {
            count = userMeta.count();
            nav = new Navigation();
            if (count != null && count <= 0) {
                nav.setAdminUserFlg(Constants.INT_VALUE_1);
            } else {
                nav.setAdminUserFlg(Constants.INT_VALUE_0);
            }
            navForm = factory.form(Navigation.class).fill(nav);
            return ok(index.render(navForm));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            nav = null;
            count = null;
            navForm = null;
        }
    }

    @AddCSRFToken
    public Result login() {
        Logger.debug("Inside [Main][login]");
        Form<Navigation> navForm = null;
        Navigation nav = null;
        try {
            navForm = factory.form(Navigation.class).bindFromRequest();
            if (navForm.hasErrors()) {
                return badRequest(index.render(navForm));
            }
            nav = navForm.get();
            if (userMeta.authenticateUser(nav, Constants.CURD_VIEW)) {
                return redirect(controllers.dashboard.routes.DashboardOps.index(Constants.CURD_DASHBOARD, ctx().messages().at("app.menu.dashboard")));
            }
            flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.0"));
            return badRequest(index.render(navForm));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            nav = null;
            navForm = null;
        }
    }

    public Result logout() {
        try {
            session().clear();
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        }
        return redirect(routes.Main.index());
    }
}
