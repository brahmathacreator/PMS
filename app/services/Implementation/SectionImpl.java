package services.Implementation;

import controllers.constants.Constants;
import controllers.constants.Validation;
import controllers.dto.PageFilter;
import io.ebean.*;
import models.Section;
import play.Logger;
import play.db.ebean.EbeanConfig;
import play.mvc.Http;
import services.Interface.SectionMeta;

import javax.inject.Inject;
import java.util.Optional;

public class SectionImpl implements SectionMeta {


    private final EbeanServer ebeanServer;

    @Inject
    public SectionImpl(EbeanConfig ebeanConfig) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
    }

    @Override
    public Integer count() throws Exception {
        return null;
    }

    @Override
    public PagedList<Section> getAllData(PageFilter filter) throws Exception {
        Logger.debug("Inside [SectionImpl][getDataByKey]");
        ExpressionList<Section> exList = null;
        try {
            exList = ebeanServer.find(Section.class).where();
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
    public Optional<Section> getDataByKey(Long id, Integer curdOpt) throws Exception {
        Logger.debug("Inside [SectionImpl][getDataByKey]");
        Optional<Section> optional;
        try {
            optional = Optional.ofNullable((ebeanServer.find(Section.class).where().eq("sectionId", id).findOne()));
            return optional;
        } finally {
            optional = null;
        }
    }

    @Override
    public Optional<Section> getDataByKeyAndExcludeKey(Long id, Long eId) throws Exception {
        return Optional.empty();
    }

    @Override
    public boolean saveOrEdit(Section object, Integer curdOpt, Http.Context ctx) throws Exception {
        Logger.debug("Inside [SectionImpl][saveOrEdit]");
        boolean status = false;
        try {
            object.setSectionInchargeEmail(object.getSectionInchargeEmail().toLowerCase());
            if (curdOpt == Constants.CURD_SAVE) {
                object.save();
            } else if (curdOpt == Constants.CURD_UPDATE) {
                object.update();
            }
            Logger.info("[SectionImpl][saveOrEdit] Record saved.");
            status = true;
        } finally {
        }
        return status;
    }

    @Override
    public boolean delete(Long rcdKey) throws Exception {
        boolean stat = false;
        Section object = null;
        try {
            object = ebeanServer.find(Section.class).where().eq("sectionId", rcdKey).findOne();
            if (object != null)
                object.delete();
            stat = true;
        } finally {
            object = null;
        }
        return stat;
    }
}
