package services.Implementation;

import controllers.constants.Constants;
import controllers.constants.Validation;
import controllers.dto.PageFilter;
import io.ebean.*;
import models.School;
import play.Logger;
import play.db.ebean.EbeanConfig;
import play.mvc.Http;
import services.Interface.SchoolMeta;

import javax.inject.Inject;
import java.util.Optional;

public class SchoolImpl implements SchoolMeta {


    private final EbeanServer ebeanServer;

    @Inject
    public SchoolImpl(EbeanConfig ebeanConfig) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
    }

    @Override
    public Integer count() throws Exception {
        return null;
    }

    @Override
    public PagedList<School> getAllData(PageFilter filter) throws Exception {
        Logger.debug("Inside [SchoolImpl][getDataByKey]");
        ExpressionList<School> exList = null;
        try {
            exList = ebeanServer.find(School.class).where();
            if (filter.getSearchColumn() != null && !filter.getSearchColumn().isEmpty() && filter.getSearchValue() != null && !filter.getSearchValue().isEmpty())
                exList.add(Expr.ilike(filter.getSearchColumn(), "%" + filter.getSearchValue() + "%"));
            if (filter.getOrderByColumn() != null && !filter.getOrderByColumn().isEmpty() && filter.getOrderByValue() != null && !filter.getOrderByValue().isEmpty())
                exList.orderBy(filter.getOrderByColumn() + " " + filter.getOrderByValue());
            exList.setFirstRow(filter.getCurrentPage() * Validation.DEFAULT_PAGE_SIZE);
            exList.setMaxRows(Validation.DEFAULT_PAGE_SIZE);
            return exList.findPagedList();
        } finally {
            exList = null;
        }
    }

    @Override
    public Optional<School> getDataByKey(Long id, Integer curdOpt) throws Exception {
        Logger.debug("Inside [SchoolImpl][getDataByKey]");
        Optional<School> optional;
        try {
            optional = Optional.ofNullable((ebeanServer.find(School.class).where().eq("schoolId", id).findOne()));
            return optional;
        } finally {
            optional = null;
        }
    }

    @Override
    public Optional<School> getDataByKeyAndExcludeKey(Long id, Long eId) throws Exception {
        return Optional.empty();
    }

    @Override
    public boolean saveOrEdit(School object, Integer curdOpt, Http.Context ctx) throws Exception {
        Logger.debug("Inside [SchoolImpl][saveOrEdit]");
        boolean status = false;
        try {
            object.setContactPersonEmail(object.getContactPersonEmail().toLowerCase());
            if (!object.getLogo().contains(Constants.ASSETS_USER_LOGO_LOC))
                object.setLogo(Constants.ASSETS_USER_LOGO_LOC + object.getLogo());
            if (curdOpt == Constants.CURD_SAVE) {
                object.save();
            } else if (curdOpt == Constants.CURD_UPDATE) {
                object.update();
            }
            Logger.info("[SchoolImpl][saveOrEdit] Record saved.");
            status = true;
        } finally {
        }
        return status;
    }

    @Override
    public boolean delete(Long rcdKey) throws Exception {
        boolean stat = false;
        School object = null;
        try {
            object = ebeanServer.find(School.class).where().eq("schoolId", rcdKey).findOne();
            if (object != null)
                object.delete();
            stat = true;
        } finally {
            object = null;
        }
        return stat;
    }
}
