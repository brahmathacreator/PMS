package controllers.operational;

import controllers.Interface.GenericOps;
import controllers.constants.Constants;
import controllers.constants.Validation;
import controllers.handler.MultipartFormDataHandler;
import controllers.security.SessionController;
import controllers.utils.Utils;
import models.Comments;
import models.Project;
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
import views.html.operations.comments;

import javax.inject.Inject;
import java.io.File;
import java.util.UUID;

public class CommentOps extends SessionController implements GenericOps {


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
        Logger.debug("Inside [CommentOps][index]");
        Project project = null;
        Form<Comments> form = null;
        try {
            project = meta.getDataByKey(id, curdOpt).orElse(null);
            if (project == null) {
                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.9"));
                return redirect(routes.ProjectOps.index(Constants.CURD_VIEW_ALL, activeMenu));
            }
            form = factory.form(Comments.class);
            if (project.getLogo() == null || project.getLogo().trim().isEmpty())
                project.setLogo(Constants.ASSETS_BLOG_LOGO);
            return ok(comments.render(curdOpt, activeMenu, project, form));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            project = null;
            form = null;
        }
    }

    @Override
    @AddCSRFToken
    @BodyParser.Of(MultipartFormDataHandler.class)
    public Result curdOperations(Integer curdOpt, String activeMenu) {
        Logger.debug("Inside [CommentOps][curdOperations]");
        Form<Comments> form = null;
        Comments object = null;
        Http.MultipartFormData<File> body = null;
        Http.MultipartFormData.FilePart<File> logo = null;
        File source = null, dest = null;
        String path = null, imgName = null;
        String projectId = null;
        try {
            form = factory.form(Comments.class).bindFromRequest();
            if (form.hasErrors() && curdOpt != Constants.CURD_DELETE) {
                return badRequest(comments.render(curdOpt, activeMenu, object.getProject(), form));
            }
            if (curdOpt == Constants.CURD_SAVE || curdOpt == Constants.CURD_UPDATE) {
                body = request().body().asMultipartFormData();
                logo = body.getFile("avatar");
                if (body == null || logo == null) {
                    flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.2"));
                    return badRequest(comments.render(curdOpt, activeMenu, object.getProject(), form));
                }
                object = form.get();
                if (logo.getFilename() == null || logo.getFilename().trim().isEmpty()) {
                    if (object.getAttachment() == null)
                        object.setAttachment(Constants.NA);
                } else {
                    try {
                        Logger.info("[CommentOps][curdOperations] File details: [" + logo.getFilename() + "][" + logo.getContentType() + "]");
                        if (logo.getFilename().endsWith(".zip")) {
                            source = logo.getFile();
                            Logger.info("[CommentOps][curdOperations] File size: [" + source.length() + "]");
                            if (Utils.checkFileSize(source, Validation.max_user_attachment_size_mb)) {
                                imgName = UUID.randomUUID() + logo.getFilename().substring(logo.getFilename().lastIndexOf("."), logo.getFilename().length());
                                path = Play.current().path().getAbsolutePath() + Constants.CUST_USER_ATTACHMENT_LOC + imgName;
                                dest = new File(path);
                                Logger.info("[CommentOps][curdOperations] File loc: [" + path + "]");
                                object.setAttachment(imgName);
                                source.renameTo(dest);
                            } else {
                                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.3"));
                                return badRequest(comments.render(curdOpt, activeMenu, object.getProject(), form));
                            }
                        } else {
                            flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.3"));
                            return badRequest(comments.render(curdOpt, activeMenu, object.getProject(), form));
                        }
                    } catch (Exception ex) {
                        object.setAttachment(Constants.NA);
                        Logger.error("[CommentOps][curdOperations] File upload failed. NA updated. Error : " + ex);
                    }
                }
                Logger.info("[CommentOps][curdOperations] Admin logo file name: " + logo.getFilename());
            }
            if (curdOpt == Constants.CURD_SAVE || curdOpt == Constants.CURD_UPDATE) {
                if (meta.saveComments(object, curdOpt, ctx())) {
                    if (curdOpt == Constants.CURD_SAVE) {
                        flash(Constants.ALERT_SUCCESS, ctx().messages().at("app.record.created.successfully"));
                    } else if (curdOpt == Constants.CURD_UPDATE) {
                        flash(Constants.ALERT_SUCCESS, ctx().messages().at("app.record.updated.successfully"));
                    }
                    return redirect(routes.CommentOps.selectRecord(Constants.CURD_VIEW, activeMenu, object.getProject().getProjectId()));
                }
            } else {
                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.7"));
                return badRequest(comments.render(curdOpt, activeMenu, object.getProject(), form));
            }
            flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.0"));
            return badRequest(comments.render(curdOpt, activeMenu, object.getProject(), form));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            form = null;
        }
    }


}
