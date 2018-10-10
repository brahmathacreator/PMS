package controllers.dashboard;

import controllers.constants.Constants;
import controllers.dto.Home;
import controllers.security.SessionController;
import play.Logger;
import play.data.FormFactory;
import play.mvc.Http;
import play.mvc.Result;
import services.Interface.HomeMeta;
import views.html.error.error;
import views.html.dashboard.board;

import javax.inject.Inject;

public class DashboardOps extends SessionController {


    @Inject
    private FormFactory factory;

    @Inject
    private HomeMeta meta;

    public Result index(Integer curdOpt, String activeMenu) {
        Logger.debug("Inside [DashboardOps][index]");
        Home obj = null;
        String userType = null;
        try {
            userType = Http.Context.current().session().get(Constants.SESSION_ROLE_TYPE);
            if (userType != null && Constants.ROLE_TYPE_ADMIN.equals(Integer.parseInt(userType)))
                obj = meta.getAdminHome();
            else if (userType != null && Constants.ROLE_TYPE_TUTOR.equals(Integer.parseInt(userType)))
                obj = meta.getTutorHome();
            else
                Logger.info("Student Login");
            return ok(board.render(curdOpt, activeMenu, obj));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            obj = null;
        }
    }


}
