package services.Implementation;

import controllers.constants.Constants;
import controllers.constants.Validation;
import controllers.dto.PageFilter;
import io.ebean.*;
import models.Batch;
import play.Logger;
import play.db.ebean.EbeanConfig;
import play.mvc.Http;
import services.Interface.BatchMeta;
import javax.inject.Inject;
import java.util.Optional;

public class BatchImpl implements BatchMeta {


    private final EbeanServer ebeanServer;

    @Inject
    public BatchImpl(EbeanConfig ebeanConfig) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
    }

    @Override
    public Integer count() throws Exception {
        return null;
    }

    @Override
    public PagedList<Batch> getAllData(PageFilter filter) throws Exception {
        Logger.debug("Inside [BatchImpl][getDataByKey]");
        ExpressionList<Batch> exList = null;
        try {
            exList = ebeanServer.find(Batch.class).where();
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
    public Optional<Batch> getDataByKey(Long id, Integer curdOpt) throws Exception {
        Logger.debug("Inside [BatchImpl][getDataByKey]");
        Optional<Batch> optional;
        try {
            optional = Optional.ofNullable((ebeanServer.find(Batch.class).where().eq("batchId", id).findOne()));
            return optional;
        } finally {
            optional = null;
        }
    }

    @Override
    public Optional<Batch> getDataByKeyAndExcludeKey(Long id, Long eId) throws Exception {
        return Optional.empty();
    }

    @Override
    public boolean saveOrEdit(Batch object, Integer curdOpt, Http.Context ctx) throws Exception {
        Logger.debug("Inside [BatchImpl][saveOrEdit]");
        boolean status = false;
        try {
            object.setBatchInchargeEmail(object.getBatchInchargeEmail().toLowerCase());
            if (!object.getLogo().contains(Constants.ASSETS_USER_LOGO_LOC))
                object.setLogo(Constants.ASSETS_USER_LOGO_LOC + object.getLogo());
            if (curdOpt == Constants.CURD_SAVE) {
                object.save();
            } else if (curdOpt == Constants.CURD_UPDATE) {
                object.update();
            }
            Logger.info("[BatchImpl][saveOrEdit] Record saved.");
            status = true;
        } finally {
        }
        return status;
    }

    @Override
    public boolean delete(Long rcdKey) throws Exception {
        boolean stat = false;
        Batch object = null;
        try {
            object = ebeanServer.find(Batch.class).where().eq("batchId", rcdKey).findOne();
            if (object != null)
                object.delete();
            stat = true;
        } finally {
            object = null;
        }
        return stat;
    }
}
