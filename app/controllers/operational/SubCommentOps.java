package controllers.operational;

import controllers.Interface.GenericOps;
import controllers.constants.Constants;
import controllers.constants.Validation;
import controllers.handler.MultipartFormDataHandler;
import controllers.security.SessionController;
import controllers.utils.Utils;
import models.Comments;
import models.Project;
import models.SubComments;
import play.Logger;
import play.api.Play;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import services.Interface.ProjectMeta;
import views.html.error.error;
import views.html.operations.subcomments;

import javax.inject.Inject;
import java.io.File;
import java.util.UUID;

public class SubCommentOps extends SessionController implements GenericOps {


    @Inject
    private FormFactory factory;

    @Inject
    private ProjectMeta meta;

    @Override
    public Result index(Integer curdOpt, String activeMenu) {
        return null;
    }

    @Override
    public Result search(Integer curdOpt, String activeMenu, Integer currentPage, String searchBy, String searchValue, String currentSortBy, String currentOrder) {
        return null;
    }

    @Override
    public Result selectRecord(Integer curdOpt, String activeMenu, Long id) {
        Logger.debug("Inside [SubCommentOps][index]");
        Comments comment = null;
        Form<SubComments> form = null;
        try {
            comment = meta.getCommentDataByKey(id, curdOpt).orElse(null);
            if (comment == null) {
                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.9"));
                return redirect(routes.ProjectOps.index(Constants.CURD_VIEW_ALL, activeMenu));
            }
            form = factory.form(SubComments.class);
            return ok(subcomments.render(curdOpt, activeMenu, comment.getProject().getProjectId(), comment, form));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            comment = null;
            form = null;
        }
    }

    @Override
    @AddCSRFToken
    @BodyParser.Of(MultipartFormDataHandler.class)
    public Result curdOperations(Integer curdOpt, String activeMenu) {
        Logger.debug("Inside [SubCommentOps][curdOperations]");
        Form<SubComments> form = null;
        SubComments object = null;
        Http.MultipartFormData<File> body = null;
        Http.MultipartFormData.FilePart<File> logo = null;
        File source = null, dest = null;
        String path = null, imgName = null;
        String projectId = null;
        try {
            form = factory.form(SubComments.class).bindFromRequest();
            if (form.hasErrors() && curdOpt != Constants.CURD_DELETE) {
                return badRequest(subcomments.render(curdOpt, activeMenu, object.getProjectId(), object.getComment(), form));
            }
            if (curdOpt == Constants.CURD_SAVE || curdOpt == Constants.CURD_UPDATE) {
                body = request().body().asMultipartFormData();
                logo = body.getFile("avatar");
                if (body == null || logo == null) {
                    flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.2"));
                    return badRequest(subcomments.render(curdOpt, activeMenu, object.getProjectId(), object.getComment(), form));
                }
                object = form.get();
                if (logo.getFilename() == null || logo.getFilename().trim().isEmpty()) {
                    if (object.getAttachment() == null)
                        object.setAttachment(Constants.NA);
                } else {
                    try {
                        Logger.info("[SubCommentOps][curdOperations] File details: [" + logo.getFilename() + "][" + logo.getContentType() + "]");
                        if (logo.getFilename().endsWith(".zip")) {
                            source = logo.getFile();
                            Logger.info("[SubCommentOps][curdOperations] File size: [" + source.length() + "]");
                            if (Utils.checkFileSize(source, Validation.max_user_attachment_size_mb)) {
                                imgName = UUID.randomUUID() + logo.getFilename().substring(logo.getFilename().lastIndexOf("."), logo.getFilename().length());
                                path = Play.current().path().getAbsolutePath() + Constants.CUST_USER_ATTACHMENT_LOC + imgName;
                                dest = new File(path);
                                Logger.info("[SubCommentOps][curdOperations] File loc: [" + path + "]");
                                object.setAttachment(imgName);
                                source.renameTo(dest);
                            } else {
                                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.3"));
                                return badRequest(subcomments.render(curdOpt, activeMenu, object.getProjectId(), object.getComment(), form));
                            }
                        } else {
                            flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.3"));
                            return badRequest(subcomments.render(curdOpt, activeMenu, object.getProjectId(), object.getComment(), form));
                        }
                    } catch (Exception ex) {
                        object.setAttachment(Constants.NA);
                        Logger.error("[SubCommentOps][curdOperations] File upload failed. NA updated. Error : " + ex);
                    }
                }
                Logger.info("[SubCommentOps][curdOperations] Admin logo file name: " + logo.getFilename());
            }
            if (curdOpt == Constants.CURD_SAVE || curdOpt == Constants.CURD_UPDATE) {
                if (meta.saveSubComments(object, curdOpt, ctx())) {
                    if (curdOpt == Constants.CURD_SAVE) {
                        flash(Constants.ALERT_SUCCESS, ctx().messages().at("app.record.created.successfully"));
                    } else if (curdOpt == Constants.CURD_UPDATE) {
                        flash(Constants.ALERT_SUCCESS, ctx().messages().at("app.record.updated.successfully"));
                    }
                    return redirect(routes.SubCommentOps.selectRecord(Constants.CURD_VIEW, activeMenu, object.getComment().getCommentId()));
                }
            } else {
                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.7"));
                return badRequest(subcomments.render(curdOpt, activeMenu, object.getProjectId(), object.getComment(), form));
            }
            flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.0"));
            return badRequest(subcomments.render(curdOpt, activeMenu, object.getProjectId(), object.getComment(), form));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            form = null;
        }
    }


}
