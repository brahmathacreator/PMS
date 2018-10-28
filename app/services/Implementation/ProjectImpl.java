package services.Implementation;

import controllers.constants.Constants;
import controllers.constants.Validation;
import controllers.dto.PageFilter;
import io.ebean.*;
import models.*;
import models.Project;
import play.Logger;
import play.db.ebean.EbeanConfig;
import play.db.ebean.Transactional;
import play.mvc.Http;
import services.Interface.ProjectMeta;
import services.Interface.ProjectMeta;
import services.utils.Zipper;

import javax.inject.Inject;
import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
            exList.add(Expr.or(Expr.eq("createdBy", Http.Context.current().session().get(Constants.SESSION_USER_KEY)), Expr.eq("studentId", Http.Context.current().session().get(Constants.SESSION_USER_KEY))));
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
        List<User> users = null;
        try {
            exList = ebeanServer.find(Project.class).where();
            exList.add(Expr.eq("schoolId", Http.Context.current().session().get(Constants.SESSION_SCHOOL_ID)));
            exList.add(Expr.or(Expr.eq("createdBy", Http.Context.current().session().get(Constants.SESSION_USER_KEY)), Expr.eq("studentId", Http.Context.current().session().get(Constants.SESSION_USER_KEY))));
            exList.add(Expr.eq("projectId", id));
            optional = Optional.ofNullable(exList.findOne());
            if (optional.isPresent()) {
                //users = ebeanServer.find(User.class).where();
            }
            return optional;
        } finally {
            optional = null;
        }
    }

    @Override
    public Project completeProjectByKey(Long id, Integer curdOpt) throws Exception {
        Logger.debug("Inside [ProjectImpl][completeProjectByKey]");
        Project project;
        ExpressionList<Project> projExList = null;
        ExpressionList<Comments> commentsExList = null;
        ExpressionList<SubComments> subCommentsExList = null;
        List<Comments> comments = null;
        List<SubComments> subComments = null;
        List<String> inputFileNames = null;
        String fileName = null;
        try {
            projExList = ebeanServer.find(Project.class).where();
            projExList.add(Expr.eq("schoolId", Http.Context.current().session().get(Constants.SESSION_SCHOOL_ID)));
            projExList.add(Expr.or(Expr.eq("createdBy", Http.Context.current().session().get(Constants.SESSION_USER_KEY)), Expr.eq("studentId", Http.Context.current().session().get(Constants.SESSION_USER_KEY))));
            projExList.add(Expr.eq("projectId", id));
            project = projExList.findOne();
            if (project != null) {
                inputFileNames = new ArrayList<String>(1);
                commentsExList = ebeanServer.find(Comments.class).where();
                commentsExList.add(Expr.isNotNull("attachment"));
                commentsExList.add(Expr.eq("markedFlag", Constants.INT_VALUE_1));
                commentsExList.add(Expr.eq("project.projectId", id));
                comments = commentsExList.findList();
                if (comments != null && comments.size() > 0) {
                    for (Comments c : comments) {
                        subCommentsExList = ebeanServer.find(SubComments.class).where();
                        subCommentsExList.add(Expr.isNotNull("attachment"));
                        subCommentsExList.add(Expr.ne("attachment", Constants.NA));
                        subCommentsExList.add(Expr.eq("markedFlag", Constants.INT_VALUE_1));
                        subCommentsExList.add(Expr.eq("comment.commentId", c.getCommentId()));
                        subComments = subCommentsExList.findList();
                        for (SubComments s : subComments) {
                            inputFileNames.add(s.getAttachment().replace(Constants.LOC_ASSETS, Constants.LOC_PUPLIC));
                        }
                    }
                }
            }
            if (inputFileNames != null && inputFileNames.size() > 0) {
                fileName = Constants.COMPLETED_FILE_LOC + (Http.Context.current().session().get(Constants.SESSION_SCHOOL_ID) + "_" + Http.Context.current().session().get(Constants.SESSION_BATCH_ID) + "_" + Http.Context.current().session().get(Constants.SESSION_SECTION_ID) + "_" + Http.Context.current().session().get(Constants.SESSION_USER_NAME) + "_" + project.getProjectTitle()) + Constants.COMPLETED_FILE_EXT;
                if (Zipper.zipMultipleFiles(inputFileNames, Constants.LOC_PUPLIC + fileName)) {
                    Logger.info("Zipped Successfully [" + fileName + "].");
                    project.setZippedComments(Constants.LOC_ASSETS + fileName);
                }
            }
            return project;
        } finally {
            project = null;
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
                object.setCreatedBy(Long.parseLong(Http.Context.current().session().get(Constants.SESSION_USER_KEY)));
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
                comment.setMarkedFlag(object.getMarkedFlag());
                comment.setProject(ebeanServer.find(Project.class).where().eq("projectId", object.getProjectId()).findOne());
                comment.save();
            } else if (curdOpt == Constants.CURD_SAVE) {
                object.setCommentByName(Http.Context.current().session().get(Constants.SESSION_USER_NAME) + "_" + Http.Context.current().session().get(Constants.SESSION_USER_KEY));
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
                object.setCommentByName(Http.Context.current().session().get(Constants.SESSION_USER_NAME) + "_" + Http.Context.current().session().get(Constants.SESSION_USER_KEY));
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
