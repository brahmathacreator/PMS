package controllers.user;


import controllers.Interface.GenericOps;
import controllers.constants.Constants;
import controllers.constants.Validation;
import controllers.dto.PageFilter;
import controllers.handler.MultipartFormDataHandler;
import controllers.routes;
import controllers.security.SessionMgmt;
import controllers.utils.Utils;
import io.ebean.PagedList;
import models.User;
import play.Logger;
import play.api.Play;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.mvc.*;
import services.Interface.UserMeta;
import views.html.error.error;
import views.html.user.adminregistration;
import views.html.user.user;

import javax.inject.Inject;
import java.io.File;
import java.util.UUID;

public class UserOps extends Controller implements GenericOps {

    @Inject
    private UserMeta userMeta;
    @Inject
    private FormFactory factory;

    public Result adminRegisterPage() {
        Logger.debug("Inside [UserOps][register]");
        Integer count = 0;
        Form<User> userForm = null;
        User user = null;
        try {
            count = userMeta.count();
            if (count != null && count <= 0) {
                user = new User();
                user.setRoleType(Constants.ROLE_TYPE_ADMIN);
                userForm = factory.form(User.class).fill(user);
                return ok(adminregistration.render(ctx().messages().at("app.admin.registration.header"), userForm));
            }
            return badRequest(error.render(ctx().messages().at("app.details"), ctx().messages().at("app.error.txt.1")));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            count = null;
            user = null;
            userForm = null;
        }
    }

    @AddCSRFToken
    @BodyParser.Of(MultipartFormDataHandler.class)
    public Result saveAdmin() {
        Logger.debug("Inside [UserOps][saveAdmin]");
        Form<User> userForm = null;
        User user = null;
        Http.MultipartFormData<File> body = null;
        Http.MultipartFormData.FilePart<File> logo = null;
        File source = null, dest = null;
        String path = null, imgName = null;
        try {
            userForm = factory.form(User.class).bindFromRequest();
            if (userForm.hasErrors()) {
                return badRequest(adminregistration.render(ctx().messages().at("app.admin.registration.header"), userForm));
            }
            body = request().body().asMultipartFormData();
            logo = body.getFile("avatar");
            if (body == null || logo == null) {
                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.2"));
                return badRequest(adminregistration.render(ctx().messages().at("app.admin.registration.header"), userForm));
            }
            user = userForm.get();
            user.setPasswordReference(UUID.randomUUID() + "");
            if (logo.getFilename() == null || logo.getFilename().trim().isEmpty()) {
                user.setLogo(Constants.DEFAULT_USER_LOGO);
            } else {
                try {
                    Logger.info("[UserOps][saveAdmin] File details: [" + logo.getFilename() + "][" + logo.getContentType() + "]");
                    if ((logo.getFilename().endsWith(".png") || logo.getFilename().endsWith(".jpg") || logo.getFilename().endsWith(".jpeg"))) {
                        source = logo.getFile();
                        Logger.info("[UserOps][saveAdmin] File size: [" + source.length() + "]");
                        if (Utils.checkFileSize(source, Validation.max_user_logo_size_mb)) {
                            imgName = user.getPasswordReference() + logo.getFilename().substring(logo.getFilename().lastIndexOf("."), logo.getFilename().length());
                            path = Play.current().path().getAbsolutePath() + Constants.CUST_USER_LOGO_LOC + imgName;
                            dest = new File(path);
                            Logger.info("[UserOps][saveAdmin] File loc: [" + path + "]");
                            user.setLogo(imgName);
                            source.renameTo(dest);
                        } else {
                            flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.3"));
                            return badRequest(adminregistration.render(ctx().messages().at("app.admin.registration.header"), userForm));
                        }
                    } else {
                        flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.3"));
                        return badRequest(adminregistration.render(ctx().messages().at("app.admin.registration.header"), userForm));
                    }
                } catch (Exception ex) {
                    user.setLogo(Constants.DEFAULT_USER_LOGO);
                    Logger.error("[UserOps][saveAdmin] File upload failed. Default user logo updated. Error : " + ex);
                }
            }
            Logger.info("[UserOps][saveAdmin] Admin logo file name: " + logo.getFilename());
            if (userMeta.saveOrEdit(user, Constants.CURD_SAVE, ctx())) {
                return redirect(routes.Main.index());
            } else {
                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.0"));
                return badRequest(adminregistration.render(ctx().messages().at("app.admin.registration.header"), userForm));
            }
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            userForm = null;
            user = null;
            body = null;
            logo = null;
            source = null;
            dest = null;
            path = null;
            imgName = null;
        }
    }


    @Override
    @Security.Authenticated(SessionMgmt.class)
    public Result index(Integer curdOpt, String activeMenu) {
        Logger.debug("Inside [UserOps][index]");
        PagedList<User> page = null;
        PageFilter filter = null;
        try {
            filter = new PageFilter();
            filter.setCurrentPage(Constants.INT_VALUE_0);
            filter.setSearchColumn("userId");
            filter.setSearchValue("");
            filter.setOrderByColumn("userId");
            filter.setOrderByValue("asc");
            page = userMeta.getAllDataAndExclude(filter);
            return ok(user.render(curdOpt, activeMenu, filter.getOrderByColumn(), filter.getOrderByValue(), filter.getSearchColumn(), filter.getSearchValue(), page, null, null));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            page = null;
            filter = null;
        }
    }

    @Override
    @Security.Authenticated(SessionMgmt.class)
    public Result search(Integer curdOpt, String activeMenu, Integer currentPage, String searchBy, String searchValue, String currentSortBy, String currentOrder) {
        Logger.debug("Inside [UserOps][search]");
        PageFilter filter = null;
        PagedList<User> page = null;
        try {
            filter = new PageFilter();
            filter.setCurrentPage(currentPage);
            filter.setSearchColumn(searchBy);
            filter.setSearchValue(searchValue);
            filter.setOrderByColumn(currentSortBy);
            filter.setOrderByValue(currentOrder);
            page = userMeta.getAllDataAndExclude(filter);
            return ok(user.render(curdOpt, activeMenu, filter.getOrderByColumn(), filter.getOrderByValue(), filter.getSearchColumn(), filter.getSearchValue(), page, null, null));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            filter = null;
            page = null;
        }
    }

    @Override
    @Security.Authenticated(SessionMgmt.class)
    public Result selectRecord(Integer curdOpt, String activeMenu, Long id) {
        Logger.debug("Inside [UserOps][selectRecord]");
        User object = null;
        Form<User> form = null;
        PageFilter filter = null;
        try {
            if (curdOpt == Constants.CURD_SAVE) {
                object = new User();
            } else if (curdOpt == Constants.CURD_UPDATE || curdOpt == Constants.CURD_VIEW || curdOpt == Constants.CURD_DELETE) {
                object = userMeta.getDataByKey(id, curdOpt).orElse(null);
            } else {
                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.7"));
                return redirect(controllers.user.routes.UserOps.index(Constants.CURD_VIEW_ALL, activeMenu));
            }
            if (object == null) {
                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.11"));
                return redirect(controllers.user.routes.UserOps.index(Constants.CURD_VIEW_ALL, activeMenu));
            }
            filter = new PageFilter();
            filter.setSchools(userMeta.getAllSchools());
            filter.setBatches(userMeta.getAllBatchs());
            filter.setSections(userMeta.getAllSections());
            form = factory.form(User.class).fill(object);
            return ok(user.render(curdOpt, activeMenu, null, null, null, null, null, form, filter));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            object = null;
            form = null;
            filter = null;
        }
    }

    @Override
    @Security.Authenticated(SessionMgmt.class)
    @AddCSRFToken
    @BodyParser.Of(MultipartFormDataHandler.class)
    public Result curdOperations(Integer curdOpt, String activeMenu) {
        Logger.debug("Inside [UserOps][curdOperations]");
        Form<User> form = null;
        User object = null;
        Http.MultipartFormData<File> body = null;
        Http.MultipartFormData.FilePart<File> logo = null;
        File source = null, dest = null;
        String path = null, imgName = null;
        PageFilter filter = null;
        try {
            filter = new PageFilter();
            filter.setSchools(userMeta.getAllSchools());
            filter.setBatches(userMeta.getAllBatchs());
            filter.setSections(userMeta.getAllSections());
            form = factory.form(User.class).bindFromRequest();
            if (form.hasErrors() && curdOpt != Constants.CURD_DELETE) {
                return badRequest(user.render(curdOpt, activeMenu, null, null, null, null, null, form, filter));
            }
            if (curdOpt == Constants.CURD_SAVE || curdOpt == Constants.CURD_UPDATE) {
                body = request().body().asMultipartFormData();
                logo = body.getFile("avatar");
                if (body == null || logo == null) {
                    flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.2"));
                    return badRequest(user.render(curdOpt, activeMenu, null, null, null, null, null, form, filter));
                }
                object = form.get();
                if (object.getRoleType() != Constants.ROLE_TYPE_ADMIN) {
                    if (object.getSchoolId() == null || object.getSchoolId() == 0) {
                        flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.12"));
                        return badRequest(user.render(curdOpt, activeMenu, null, null, null, null, null, form, filter));
                    }
                    if (object.getBatchId() == null || object.getBatchId() == 0) {
                        flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.13"));
                        return badRequest(user.render(curdOpt, activeMenu, null, null, null, null, null, form, filter));
                    }
                    if (object.getSectionId() == null || object.getSectionId() == 0) {
                        flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.14"));
                        return badRequest(user.render(curdOpt, activeMenu, null, null, null, null, null, form, filter));
                    }
                    Logger.info("[UserOps][curdOperations]:[School / Batch / Section]:[" + object.getSchoolId() + "/" + object.getBatchId() + "/" + object.getSectionId() + "]");
                } else {
                    Logger.info("[UserOps][curdOperations]:[Admin User]");
                }
                if (curdOpt == Constants.CURD_SAVE && userMeta.getDataByEmail(object.getEmail(), curdOpt).isPresent()) {
                    flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.15"));
                    return badRequest(user.render(curdOpt, activeMenu, null, null, null, null, null, form, filter));
                }
                if (object.getPasswordReference() == null || object.getPasswordReference().trim().isEmpty())
                    object.setPasswordReference(UUID.randomUUID() + "");
                if (logo.getFilename() == null || logo.getFilename().trim().isEmpty()) {
                    if (object.getLogo() == null)
                        object.setLogo(Constants.DEFAULT_USER_LOGO);
                } else {
                    try {
                        Logger.info("[UserOps][curdOperations] File details: [" + logo.getFilename() + "][" + logo.getContentType() + "]");
                        if ((logo.getFilename().endsWith(".png") || logo.getFilename().endsWith(".jpg") || logo.getFilename().endsWith(".jpeg"))) {
                            source = logo.getFile();
                            Logger.info("[UserOps][curdOperations] File size: [" + source.length() + "]");
                            if (Utils.checkFileSize(source, Validation.max_user_logo_size_mb)) {
                                imgName = object.getPasswordReference() + logo.getFilename().substring(logo.getFilename().lastIndexOf("."), logo.getFilename().length());
                                path = Play.current().path().getAbsolutePath() + Constants.CUST_USER_LOGO_LOC + imgName;
                                dest = new File(path);
                                Logger.info("[UserOps][curdOperations] File loc: [" + path + "]");
                                object.setLogo(imgName);
                                source.renameTo(dest);
                            } else {
                                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.3"));
                                return badRequest(user.render(curdOpt, activeMenu, null, null, null, null, null, form, filter));
                            }
                        } else {
                            flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.3"));
                            return badRequest(user.render(curdOpt, activeMenu, null, null, null, null, null, form, filter));
                        }
                    } catch (Exception ex) {
                        object.setLogo(Constants.DEFAULT_USER_LOGO);
                        Logger.error("[UserOps][curdOperations] File upload failed. Default user logo updated. Error : " + ex);
                    }
                }
                Logger.info("[UserOps][curdOperations] Admin logo file name: " + logo.getFilename());
            }
            if (curdOpt == Constants.CURD_SAVE || curdOpt == Constants.CURD_UPDATE) {
                if (userMeta.saveOrEdit(object, curdOpt, ctx())) {
                    if (curdOpt == Constants.CURD_SAVE) {
                        flash(Constants.ALERT_SUCCESS, ctx().messages().at("app.record.created.successfully"));
                    } else if (curdOpt == Constants.CURD_UPDATE) {
                        flash(Constants.ALERT_SUCCESS, ctx().messages().at("app.record.updated.successfully"));
                    }
                    return redirect(controllers.user.routes.UserOps.index(Constants.CURD_VIEW_ALL, activeMenu));
                }
            } else if (curdOpt == Constants.CURD_DELETE) {
                String rcdKey = form.field("userKey").getValue().orElse(null);
                if (rcdKey != null && userMeta.delete(Long.parseLong(rcdKey))) {
                    flash(Constants.ALERT_SUCCESS, ctx().messages().at("app.record.deleted.successfully"));
                    return redirect(controllers.user.routes.UserOps.index(Constants.CURD_VIEW_ALL, activeMenu));
                } else {
                    flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.7"));
                    return redirect(controllers.user.routes.UserOps.index(Constants.CURD_VIEW_ALL, activeMenu));
                }
            } else {
                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.7"));
                return badRequest(user.render(curdOpt, activeMenu, null, null, null, null, null, form, filter));
            }
            flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.0"));
            return badRequest(user.render(curdOpt, activeMenu, null, null, null, null, null, form, filter));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            filter = null;
            form = null;
        }
    }
}
