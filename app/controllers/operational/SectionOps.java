package controllers.operational;

import controllers.Interface.GenericOps;
import controllers.constants.Constants;
import controllers.constants.Validation;
import controllers.dto.PageFilter;
import controllers.handler.MultipartFormDataHandler;
import controllers.security.SessionController;
import controllers.utils.Utils;
import io.ebean.PagedList;
import models.Section;
import play.Logger;
import play.api.Play;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import services.Interface.SectionMeta;
import views.html.error.error;
import views.html.operations.section;

import javax.inject.Inject;
import java.io.File;
import java.util.UUID;

public class SectionOps extends SessionController implements GenericOps {


    @Inject
    private FormFactory factory;

    @Inject
    private SectionMeta meta;

    @Override
    public Result index(Integer curdOpt, String activeMenu) {
        Logger.debug("Inside [SectionOps][index]");
        PagedList<Section> page = null;
        PageFilter filter = null;
        try {
            filter = new PageFilter();
            filter.setCurrentPage(Constants.INT_VALUE_0);
            filter.setSearchColumn("sectionName");
            filter.setSearchValue("");
            filter.setOrderByColumn("sectionName");
            filter.setOrderByValue("asc");
            page = meta.getAllData(filter);
            return ok(section.render(curdOpt, activeMenu, filter.getOrderByColumn(), filter.getOrderByValue(), filter.getSearchColumn(), filter.getSearchValue(), page, null));
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
        Logger.debug("Inside [SectionOps][search]");
        PageFilter filter = null;
        PagedList<Section> page = null;
        try {
            filter = new PageFilter();
            filter.setCurrentPage(currentPage);
            filter.setSearchColumn(searchBy);
            filter.setSearchValue(searchValue);
            filter.setOrderByColumn(currentSortBy);
            filter.setOrderByValue(currentOrder);
            page = meta.getAllData(filter);
            return ok(section.render(curdOpt, activeMenu, filter.getOrderByColumn(), filter.getOrderByValue(), filter.getSearchColumn(), filter.getSearchValue(), page, null));
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
        Logger.debug("Inside [SectionOps][selectRecord]");
        Section object = null;
        Form<Section> form = null;
        try {
            if (curdOpt == Constants.CURD_SAVE) {
                object = new Section();
            } else if (curdOpt == Constants.CURD_UPDATE || curdOpt == Constants.CURD_VIEW || curdOpt == Constants.CURD_DELETE) {
                object = meta.getDataByKey(id, curdOpt).orElse(null);
            } else {
                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.7"));
                return redirect(routes.SectionOps.index(Constants.CURD_VIEW_ALL, activeMenu));
            }
            if (object == null) {
                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.11"));
                return redirect(routes.SectionOps.index(Constants.CURD_VIEW_ALL, activeMenu));
            }
            form = factory.form(Section.class).fill(object);
            return ok(section.render(curdOpt, activeMenu, null, null, null, null, null, form));
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
    public Result curdOperations(Integer curdOpt, String activeMenu) {
        Logger.debug("Inside [SectionOps][curdOperations]");
        Form<Section> form = null;
        Section object = null;
        try {
            form = factory.form(Section.class).bindFromRequest();
            if (form.hasErrors() && curdOpt != Constants.CURD_DELETE) {
                return badRequest(section.render(curdOpt, activeMenu, null, null, null, null, null, form));
            }
            object = form.get();
            if (curdOpt == Constants.CURD_SAVE || curdOpt == Constants.CURD_UPDATE) {
                if (meta.saveOrEdit(object, curdOpt, ctx())) {
                    if (curdOpt == Constants.CURD_SAVE) {
                        flash(Constants.ALERT_SUCCESS, ctx().messages().at("app.record.created.successfully"));
                    } else if (curdOpt == Constants.CURD_UPDATE) {
                        flash(Constants.ALERT_SUCCESS, ctx().messages().at("app.record.updated.successfully"));
                    }
                    return redirect(routes.SectionOps.index(Constants.CURD_VIEW_ALL, activeMenu));
                }
            } else if (curdOpt == Constants.CURD_DELETE) {
                String rcdKey = form.field("sectionId").getValue().orElse(null);
                if (rcdKey != null && meta.delete(Long.parseLong(rcdKey))) {
                    flash(Constants.ALERT_SUCCESS, ctx().messages().at("app.record.deleted.successfully"));
                    return redirect(routes.SectionOps.index(Constants.CURD_VIEW_ALL, activeMenu));
                } else {
                    flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.7"));
                    return redirect(routes.SectionOps.index(Constants.CURD_VIEW_ALL, activeMenu));
                }
            } else {
                flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.7"));
                return badRequest(section.render(curdOpt, activeMenu, null, null, null, null, null, form));
            }
            flash(Constants.ALERT_FAILURE, ctx().messages().at("app.error.txt.0"));
            return badRequest(section.render(curdOpt, activeMenu, null, null, null, null, null, form));
        } catch (Exception ex) {
            Logger.error("Error : " + ex);
            return badRequest(error.render(ctx().messages().at("app.details"), ex));
        } finally {
            form = null;
        }

    }


}
