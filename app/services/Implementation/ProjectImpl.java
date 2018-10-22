package services.Implementation;

import controllers.constants.Constants;
import controllers.constants.Validation;
import controllers.dto.PageFilter;
import io.ebean.*;
import models.Comments;
import models.Project;
import models.Project;
import models.SubComments;
import play.Logger;
import play.db.ebean.EbeanConfig;
import play.db.ebean.Transactional;
import play.mvc.Http;
import services.Interface.ProjectMeta;
import services.Interface.ProjectMeta;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;

public class ProjectImpl implements ProjectMeta {


    private final EbeanServer ebeanServer;

    @Inject
    public ProjectImpl(EbeanConfig ebeanConfig) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
    }

    @Override
    public Integer count() throws Exception {
        return null;
    }

    @Override
    public PagedList<Project> getAllData(PageFilter filter) throws Exception {
        Logger.debug("Inside [ProjectImpl][getDataByKey]");
        ExpressionList<Project> exList = null;
        try {
            exList = ebeanServer.find(Project.class).where();
            if (filter.getSearchColumn() != null && !filter.getSearchColumn().isEmpty() && filter.getSearchValue() != null && !filter.getSearchValue().isEmpty())
                exList.add(Expr.ilike(filter.getSearchColumn(), "%" + filter.getSearchValue() + "%"));
            if (filter.getOrderByColumn() != null && !filter.getOrderByColumn().isEmpty() && filter.getOrderByValue() != null && !filter.getOrderByValue().isEmpty())
                exList.orderBy(filter.getOrderByColumn() + " " + filter.getOrderByValue());
            exList.add(Expr.eq("schoolId", Http.Context.current().session().get(Constants.SESSION_SCHOOL_ID)));
            exList.add(Expr.eq("batchId", Http.Context.current().session().get(Constants.SESSION_BATCH_ID)));
            exList.add(Expr.eq("sectionId", Http.Context.current().session().get(Constants.SESSION_SECTION_ID)));
            exList.setFirstRow(filter.getCurrentPage() * Validation.DEFAULT_PAGE_SIZE);
            exList.setMaxRows(Validation.DEFAULT_PAGE_SIZE);
            return exList.findPagedList();
        } finally {
            exList = null;
        }
    }

    @Override
    public Optional<Comments> getCommentDataByKey(Long id, Integer curdOpt) throws Exception {
        Logger.debug("Inside [ProjectImpl][getDataByKey]");
        Optional<Comments> optional;
        ExpressionList<Comments> exList = null;
        try {
            exList = ebeanServer.find(Comments.class).where();
            exList.add(Expr.eq("commentId", id));
            optional = Optional.ofNullable(exList.findOne());
            return optional;
        } finally {
            optional = null;
        }
    }

    @Override
    public Optional<Project> getDataByKey(Long id, Integer curdOpt) throws Exception {
        Logger.debug("Inside [ProjectImpl][getDataByKey]");
        Optional<Project> optional;
        ExpressionList<Project> exList = null;
        try {
            exList = ebeanServer.find(Project.class).where();
            exList.add(Expr.eq("schoolId", Http.Context.current().session().get(Constants.SESSION_SCHOOL_ID)));
            exList.add(Expr.eq("batchId", Http.Context.current().session().get(Constants.SESSION_BATCH_ID)));
            exList.add(Expr.eq("sectionId", Http.Context.current().session().get(Constants.SESSION_SECTION_ID)));
            exList.add(Expr.eq("schoolId", id));
            optional = Optional.ofNullable(exList.findOne());
            return optional;
        } finally {
            optional = null;
        }
    }

    @Override
    public Optional<Project> getDataByKeyAndExcludeKey(Long id, Long eId) throws Exception {
        return Optional.empty();
    }

    @Override
    @Transactional
    public boolean saveOrEdit(Project object, Integer curdOpt, Http.Context ctx) throws Exception {
        Logger.debug("Inside [ProjectImpl][saveOrEdit]");
        boolean status = false;
        Date d = null;
        try {
            if (curdOpt == Constants.CURD_SAVE) {
                object.setSchoolId(Long.parseLong(Http.Context.current().session().get(Constants.SESSION_SCHOOL_ID)));
                object.setBatchId(Long.parseLong(Http.Context.current().session().get(Constants.SESSION_BATCH_ID)));
                object.setSectionId(Long.parseLong(Http.Context.current().session().get(Constants.SESSION_SECTION_ID)));
                d = new Date();
                object.setProjectCreationDT(d);
            }
            if (!object.getLogo().contains(Constants.ASSETS_USER_LOGO_LOC))
                object.setLogo(Constants.ASSETS_USER_LOGO_LOC + object.getLogo());
            if (curdOpt == Constants.CURD_SAVE) {
                object.save();
            } else if (curdOpt == Constants.CURD_UPDATE) {
                object.update();
            }
            Logger.info("[ProjectImpl][saveOrEdit] Record saved.");
            status = true;
        } finally {
            d = null;
        }
        return status;
    }

    @Override
    @Transactional
    public boolean delete(Long rcdKey) throws Exception {
        boolean stat = false;
        Project object = null;
        try {
            object = ebeanServer.find(Project.class).where().eq("projectId", rcdKey).findOne();
            if (!object.getComments().isEmpty()) {
                ebeanServer.find(Comments.class).where().eq("projectId", rcdKey).delete();
            }
            if (object != null)
                object.delete();
            stat = true;
        } finally {
            object = null;
        }
        return stat;
    }

    @Override
    public boolean saveComments(Comments object, Integer curdOpt, Http.Context ctx) throws Exception {
        Logger.debug("Inside [ProjectImpl][saveOrEdit]");
        boolean status = false;
        Date d = null;
        Comments comment = null;
        try {
            if (curdOpt == Constants.CURD_UPDATE) {
                comment = ebeanServer.find(Comments.class).where().eq("commentId", object.getCommentId()).findOne();
                comment.setActualEndDate(object.getActualEndDate());
                comment.setProject(ebeanServer.find(Project.class).where().eq("projectId", object.getProjectId()).findOne());
                comment.save();
            } else if (curdOpt == Constants.CURD_SAVE) {
                if (!object.getCommentByName().contains(Http.Context.current().session().get(Constants.SESSION_USER_NAME) + "_" + Http.Context.current().session().get(Constants.SESSION_USER_KEY)))
                    object.setCommentByName(object.getCommentByName() + " [" + Http.Context.current().session().get(Constants.SESSION_USER_NAME) + "_" + Http.Context.current().session().get(Constants.SESSION_USER_KEY) + "]");
                object.setProject(ebeanServer.find(Project.class).where().eq("projectId", object.getProjectId()).findOne());
                if (!Constants.NA.contains(object.getAttachment()) && !object.getAttachment().contains(Constants.ASSETS_USER_ATTACHMENT_LOC))
                    object.setAttachment(Constants.ASSETS_USER_ATTACHMENT_LOC + object.getAttachment());
                object.save();
            }
            Logger.info("[ProjectImpl][saveOrEdit] Record saved.");
            status = true;
        } finally {
            d = null;
            comment = null;
        }
        return status;
    }

    @Override
    public boolean saveSubComments(SubComments object, Integer curdOpt, Http.Context ctx) throws Exception {
        Logger.debug("Inside [ProjectImpl][saveOrEdit]");
        boolean status = false;
        Comments comment = null;
        SubComments subComment = null;
        Date d = null;
        try {
            if (curdOpt == Constants.CURD_SAVE) {
                if (!object.getCommentByName().contains(Http.Context.current().session().get(Constants.SESSION_USER_NAME) + "_" + Http.Context.current().session().get(Constants.SESSION_USER_KEY)))
                    object.setCommentByName(object.getCommentByName() + " [" + Http.Context.current().session().get(Constants.SESSION_USER_NAME) + "_" + Http.Context.current().session().get(Constants.SESSION_USER_KEY) + "]");
                comment = ebeanServer.find(Comments.class).where().eq("commentId", object.getCommentId()).findOne();
                comment.setProject(ebeanServer.find(Project.class).where().eq("projectId", object.getProjectId()).findOne());
                object.setComment(comment);
                if (!Constants.NA.contains(object.getAttachment()) && !object.getAttachment().contains(Constants.ASSETS_USER_ATTACHMENT_LOC))
                    object.setAttachment(Constants.ASSETS_USER_ATTACHMENT_LOC + object.getAttachment());
                object.save();
            } else if (curdOpt == Constants.CURD_UPDATE) {
                subComment = ebeanServer.find(SubComments.class).where().eq("subCommentId", object.getSubCommentId()).findOne();
                subComment.setMarkedFlag(object.getMarkedFlag());
                comment = ebeanServer.find(Comments.class).where().eq("commentId", object.getCommentId()).findOne();
                comment.setProject(ebeanServer.find(Project.class).where().eq("projectId", object.getProjectId()).findOne());
                subComment.setComment(comment);
                subComment.save();
            }
            Logger.info("[ProjectImpl][saveOrEdit] Record saved.");
            status = true;
        } finally {
            subComment = null;
            comment = null;
            d = null;
        }
        return status;
    }
}
