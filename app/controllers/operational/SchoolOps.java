package controllers.operational;

import controllers.Interface.GenericOps;
import controllers.constants.Constants;
import controllers.constants.Validation;
import controllers.dto.PageFilter;
import controllers.handler.MultipartFormDataHandler;
import controllers.security.SessionController;
import controllers.utils.Utils;
import io.ebean.PagedList;
import models.School;
import play.Logger;
import play.api.Play;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import services.Interface.SchoolMeta;
import views.html.error.error;
import views.html.operations.school;

import javax.inject.Inject;
import java.io.File;
import java.util.UUID;

public class SchoolOps extends SessionController implements GenericOps {


    @Inject
    private FormFactory factory;

    @Inject
    private SchoolMeta meta;

    @Override
    public Result index(Integer curdOpt, String activeMenu) {
        Logger.debug("Inside [UserOps][index]");
        PagedList<School> page = null;
        PageFilter filter = null;
        try {
            filter = new PageFilter();
            filter.setCurrentPage(Constants.INT_VALUE_0);
            filter.setSearchColumn("schoolName");
            filter.setSearchValue("");
            filter.setOrderByColumn("schoolName");
            filter.setOrderByValue("asc");
            page = meta.getAllData(filter);
            return ok(school.render(curdOpt, activeMenu, filter.getOrderByColumn(), filter.getOrderByValue(), filter.getSearchColumn(), filter.getSearchValue(), page, null));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            page = null;
            filter = null;
        }
    }

    @Override
    public Result search(Integer curdOpt, String activeMenu, Integer currentPage, String searchBy, String searchValue, String currentSortBy, String currentOrder) {
        Logger.debug("Inside [UserOps][search]");
        PageFilter filter = null;
        PagedList<School> page = null;
        try {
            filter = new PageFilter();
            filter.setCurrentPage(currentPage);
            filter.setSearchColumn(searchBy);
            filter.setSearchValue(searchValue);
            filter.setOrderByColumn(currentSortBy);
            filter.setOrderByValue(currentOrder);
            page = meta.getAllData(filter);
            return ok(school.render(curdOpt, activeMenu, filter.getOrderByColumn(), filter.getOrderByValue(), filter.getSearchColumn(), filter.getSearchValue(), page, null));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            filter = null;
            page = null;
        }
    }

    @Override
    public Result selectRecord(Integer curdOpt, String activeMenu, Long id) {
        Logger.debug("Inside [UserOps][selectRecord]");
        School object = null;
        Form<School> form = null;
        try {
            if (curdOpt == Constants.CURD_SAVE) {
                object = new School();
            } else if (curdOpt == Constants.CURD_UPDATE || curdOpt == Constants.CURD_VIEW || curdOpt == Constants.CURD_DELETE) {
                object = meta.getDataByKey(id, curdOpt).orElse(null);
            } else {
                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.7"));
                return redirect(controllers.operational.routes.SchoolOps.index(Constants.CURD_VIEW_ALL, activeMenu));
            }
            if (object == null) {
                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.11"));
                return redirect(controllers.operational.routes.SchoolOps.index(Constants.CURD_VIEW_ALL, activeMenu));
            }
            form = factory.form(School.class).fill(object);
            return ok(school.render(curdOpt, activeMenu, null, null, null, null, null, form));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            object = null;
            form = null;
        }
    }

    @Override
    @AddCSRFToken
    @BodyParser.Of(MultipartFormDataHandler.class)
    public Result curdOperations(Integer curdOpt, String activeMenu) {

        Logger.debug("Inside [UserOps][curdOperations]");
        Form<School> form = null;
        School object = null;
        Http.MultipartFormData<File> body = null;
        Http.MultipartFormData.FilePart<File> logo = null;
        File source = null, dest = null;
        String path = null, imgName = null;
        try {
            form = factory.form(School.class).bindFromRequest();
            if (form.hasErrors() && curdOpt != Constants.CURD_DELETE) {
                return badRequest(school.render(curdOpt, activeMenu, null, null, null, null, null, form));
            }
            if (curdOpt == Constants.CURD_SAVE || curdOpt == Constants.CURD_UPDATE) {
                body = request().body().asMultipartFormData();
                logo = body.getFile("avatar");
                if (body == null || logo == null) {
                    flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.2"));
                    return badRequest(school.render(curdOpt, activeMenu, null, null, null, null, null, form));
                }
                object = form.get();
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
                                imgName = UUID.randomUUID() + logo.getFilename().substring(logo.getFilename().lastIndexOf("."), logo.getFilename().length());
                                path = Play.current().path().getAbsolutePath() + Constants.CUST_USER_LOGO_LOC + imgName;
                                dest = new File(path);
                                Logger.info("[UserOps][curdOperations] File loc: [" + path + "]");
                                object.setLogo(imgName);
                                source.renameTo(dest);
                            } else {
                                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.3"));
                                return badRequest(school.render(curdOpt, activeMenu, null, null, null, null, null, form));
                            }
                        } else {
                            flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.3"));
                            return badRequest(school.render(curdOpt, activeMenu, null, null, null, null, null, form));
                        }
                    } catch (Exception ex) {
                        object.setLogo(Constants.DEFAULT_USER_LOGO);
                        Logger.error("[UserOps][curdOperations] File upload failed. Default user logo updated. Error : " + ex);
                    }
                }
                Logger.info("[UserOps][curdOperations] Admin logo file name: " + logo.getFilename());
            }
            if (curdOpt == Constants.CURD_SAVE || curdOpt == Constants.CURD_UPDATE) {
                if (meta.saveOrEdit(object, curdOpt, ctx())) {
                    if (curdOpt == Constants.CURD_SAVE) {
                        flash(Constants.ALERT_SUCCESS, ctx().messages().at("app.record.created.successfully"));
                    } else if (curdOpt == Constants.CURD_UPDATE) {
                        flash(Constants.ALERT_SUCCESS, ctx().messages().at("app.record.updated.successfully"));
                    }
                    return redirect(controllers.operational.routes.SchoolOps.index(Constants.CURD_VIEW_ALL, activeMenu));
                }
            } else if (curdOpt == Constants.CURD_DELETE) {
                String rcdKey = form.field("schoolId").getValue().orElse(null);
                if (rcdKey != null && meta.delete(Long.parseLong(rcdKey))) {
                    flash(Constants.ALERT_SUCCESS, ctx().messages().at("app.record.deleted.successfully"));
                    return redirect(controllers.operational.routes.SchoolOps.index(Constants.CURD_VIEW_ALL, activeMenu));
                } else {
                    flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.7"));
                    return redirect(controllers.operational.routes.SchoolOps.index(Constants.CURD_VIEW_ALL, activeMenu));
                }
            } else {
                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.7"));
                return badRequest(school.render(curdOpt, activeMenu, null, null, null, null, null, form));
            }
            flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.0"));
            return badRequest(school.render(curdOpt, activeMenu, null, null, null, null, null, form));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            form = null;
        }

    }


}
