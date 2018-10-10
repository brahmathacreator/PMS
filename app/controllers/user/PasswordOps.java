package controllers.user;

import controllers.constants.Constants;
import controllers.routes;
import models.Password;
import models.User;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.mvc.Controller;
import play.mvc.Result;
import services.Interface.PasswordMeta;
import services.Interface.UserMeta;
import views.html.error.error;
import views.html.user.changePassword;

import javax.inject.Inject;

public class PasswordOps extends Controller {

    @Inject
    private UserMeta userMeta;
    @Inject
    private PasswordMeta passwordMeta;
    @Inject
    private FormFactory factory;

    public PasswordOps() {
    }

    public Result resetPasswordPage() {
        Logger.debug("Inside [PasswordOps][changePasswordPage]");
        Form<Password> passwordForm = null;
        try {
            passwordForm = factory.form(Password.class);
            return ok(changePassword.render(ctx().messages().at("app.pwd.module.title"), passwordForm));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            passwordForm = null;
        }
    }

    @AddCSRFToken
    public Result resetPassword() {
        Logger.debug("Inside [PasswordOps][resetPassword]");
        Form<Password> passwordForm = null;
        Password password = null;
        User user = null;
        try {
            passwordForm = factory.form(Password.class).bindFromRequest();
            if (passwordForm.hasErrors()) {
                return badRequest(changePassword.render(ctx().messages().at("app.admin.registration.header"), passwordForm));
            }
            password = passwordForm.get();
            if (!password.getPassword().equals(password.getConfirmPassword())) {
                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.5"));
                return redirect(routes.Main.index());
            }
            user = userMeta.getDataByEmail(password.getEmail(), Constants.CURD_VIEW).orElse(null);
            if (user != null) {
                password.setUser(user);
                if (passwordMeta.saveOrEdit(password, Constants.CURD_SAVE, ctx())) {
                    user.setPasswordGenerationStatus(Constants.INT_VALUE_1);
                    if (userMeta.saveOrEdit(user, Constants.CURD_UPDATE, ctx())) {
                        flash(Constants.ALERT_SUCCESS, ctx().messages().at("app.pwd.txt1"));
                        return redirect(routes.Main.index());
                    }
                }
            }
            return badRequest(error.render(ctx().messages().at("app.details"), ctx().messages().at("app.error.txt.0")));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            passwordForm = null;
            password = null;
            user = null;
        }
    }
}
