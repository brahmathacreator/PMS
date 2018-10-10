package services.Implementation;

import controllers.constants.Constants;
import controllers.constants.Validation;
import controllers.dto.Navigation;
import controllers.dto.NotificationDetails;
import controllers.dto.PageFilter;
import controllers.handler.AppException;
import io.ebean.*;
import models.*;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.api.Play;
import play.db.ebean.EbeanConfig;
import play.db.ebean.Transactional;
import play.mvc.Http;
import services.Interface.UserMeta;
import services.utils.MailSender;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;


@Singleton
public class UserImpl implements UserMeta {

    @Inject
    private MailSender mailSender;
    private final EbeanServer ebeanServer;

    @Inject
    public UserImpl(EbeanConfig ebeanConfig) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
    }


    @Override
    public Integer count() throws Exception {
        Logger.debug("Inside [UserImpl][count]");
        Integer count = 0;
        try {
            count = ebeanServer.find(User.class).where().eq("roleType", Constants.ROLE_TYPE_ADMIN).findCount();
            Logger.info("Inside [UserImpl][count] Count: " + count);
            return count;
        } finally {
            count = null;
        }
    }

    @Override
    public PagedList<User> getAllData(PageFilter filter) throws Exception {
        ExpressionList<User> exList = null;
        try {
            exList = ebeanServer.find(User.class).where();
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
    public Optional<User> getDataByKey(Long id, Integer curdOpt) throws Exception {
        Logger.debug("Inside [UserImpl][getDataByKey]");
        Optional<User> optional;
        try {
            optional = Optional.ofNullable((ebeanServer.find(User.class).where().eq("userKey", id).findOne()));
            return optional;
        } finally {
            optional = null;
        }
    }

    @Override
    public PagedList<User> getAllDataAndExclude(PageFilter filter) throws Exception {
        ExpressionList<User> exList = null;
        try {
            exList = ebeanServer.find(User.class).where();
            exList.add(Expr.ne("userKey", Http.Context.current().session().get(Constants.SESSION_USER_KEY)));
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
    public Optional<User> getDataByEmail(String emailId, Integer curdOpt) throws Exception {
        Logger.debug("Inside [UserImpl][getDataByEmail]");
        Optional<User> optional;
        try {
            optional = Optional.ofNullable(ebeanServer.find(User.class).where().and(
                    Expr.eq("email", emailId.toLowerCase()),
                    Expr.or(Expr.isNull("passwordGenerationStatus"), Expr.ne("passwordGenerationStatus", 0))
            ).findOne());
            return optional;
        } finally {
            optional = null;
        }
    }

    @Override
    public boolean authenticateUser(Navigation nav, Integer curdOpt) throws Exception {
        Logger.debug("Inside [UserImpl][authenticateUser]");
        User user = null;
        boolean status = false;
        try {
            user = ebeanServer.find(User.class).where().eq("userId", nav.getUserId().toUpperCase()).findOne();
            status = BCrypt.checkpw(nav.getPassword(), user.getPwdInfo().getPassword());
            if (status) {
                Http.Context.current().session().put(Constants.SESSION_USER_NAME, user.getUsername());
                Http.Context.current().session().put(Constants.SESSION_USER_KEY, user.getUserKey() + "");
                Http.Context.current().session().put(Constants.SESSION_ROLE_TYPE, user.getUserRole().getRoleType() + "");
                Http.Context.current().session().put(Constants.SESSION_USER_LOGO, user.getLogo());
                Http.Context.current().session().put(Constants.SESSION_SCHOOL_ID, user.getSchoolId() + "");
                Http.Context.current().session().put(Constants.SESSION_BATCH_ID, user.getBatchId() + "");
                Http.Context.current().session().put(Constants.SESSION_SECTION_ID, user.getSectionId() + "");
            }

        } finally {
            user = null;
        }
        return status;
    }

    @Override
    public Map<Long, String> getAllSchools() throws Exception {
        Map<Long, String> map = null;
        List<School> list = null;
        try {
            list = ebeanServer.find(School.class).findList();
            if (list != null) {
                map = new HashMap<Long, String>();
                for (School obj : list) {
                    map.put(obj.getSchoolId(), obj.getSchoolName());
                }
            }
            return map;
        } finally {
            map = null;
            list = null;
        }
    }

    @Override
    public Map<Long, String> getAllBatchs() throws Exception {
        Map<Long, String> map = null;
        List<Batch> list = null;
        try {
            list = ebeanServer.find(Batch.class).findList();
            if (list != null) {
                map = new HashMap<Long, String>();
                for (Batch obj : list) {
                    map.put(obj.getBatchId(), obj.getBatchName());
                }
            }
            return map;
        } finally {
            map = null;
            list = null;
        }
    }

    @Override
    public Map<Long, String> getAllSections() throws Exception {
        Map<Long, String> map = null;
        List<Section> list = null;
        try {
            list = ebeanServer.find(Section.class).findList();
            if (list != null) {
                map = new HashMap<Long, String>();
                for (Section obj : list) {
                    map.put(obj.getSectionId(), obj.getSectionName());
                }
            }
            return map;
        } finally {
            map = null;
            list = null;
        }
    }

    @Override
    public Optional<User> getDataByKeyAndExcludeKey(Long id, Long eId) throws Exception {
        return Optional.empty();
    }

    @Override
    @Transactional
    public boolean saveOrEdit(User object, Integer curdOpt, Http.Context ctx) throws Exception {
        Logger.debug("Inside [UserImpl][saveOrEdit]");
        Notification notification = null;
        NotificationDetails details = null;
        Date d = null;
        UserRole userRole = null;
        boolean status = false;
        try {

            if (curdOpt == Constants.CURD_SAVE || curdOpt == Constants.CURD_UPDATE) {
                userRole = new UserRole();
                if (object.getRoleType() == Constants.ROLE_TYPE_ADMIN) {
                    userRole.setRoleName(Constants.ADMIN_ROLE);
                    userRole.setRoleDescription(Constants.ADMIN_ROLE_DESC);
                } else if (object.getRoleType() == Constants.ROLE_TYPE_TUTOR) {
                    userRole.setRoleName(Constants.TUTOR_ROLE);
                    userRole.setRoleDescription(Constants.TUTOR_ROLE_DESC);
                } else if (object.getRoleType() == Constants.ROLE_TYPE_STUDENT) {
                    userRole.setRoleName(Constants.STUDENT_ROLE);
                    userRole.setRoleDescription(Constants.STUDENT_ROLE_DESC);
                } else {
                    throw new AppException(ctx.messages().at("app.error.txt.6"));
                }

                userRole.setRoleType(object.getRoleType());
                object.setUserRole(userRole);
            }

            if (curdOpt == Constants.CURD_SAVE) {
                notification = new Notification();
                notification.setNotificationType(Constants.NOTIFICATION_EMAIL);
                notification.setTitle(ctx.messages().at("app.mail.password.reset.header"));
                notification.setMessage(ctx.messages().at("app.mail.password.reset.message"));
                notification.setToAddress(object.getEmail());
                d = new Date();
                notification.setNotificationInitiationDT(d);
                details = new NotificationDetails();
                details.setNotification(notification);
                details.getSupportData().add(object);
                if (mailSender.sendPasswordMail(details)) {
                    notification.save();
                    Logger.info("[UserImpl][saveOrEdit] Notification saved.");

                }
            }
            object.setUserId(object.getUserId().toUpperCase());
            object.setEmail(object.getEmail().toLowerCase());
            if (!object.getLogo().contains(Constants.ASSETS_USER_LOGO_LOC))
                object.setLogo(Constants.ASSETS_USER_LOGO_LOC + object.getLogo());
            object.getUserRole().setUser(object);
            if (curdOpt == Constants.CURD_SAVE) {
                object.save();
                object.getUserRole().save();
            } else if (curdOpt == Constants.CURD_UPDATE) {
                object.update();
                userRole = ebeanServer.find(UserRole.class).where().eq("userKey", object.getUserKey()).findOne();
                if (userRole != null)
                    userRole.delete();
                object.getUserRole().save();
            }
            Logger.info("[UserImpl][saveOrEdit] User saved.");
            status = true;
        } finally {
            notification = null;
            details = null;
            d = null;
            userRole = null;

        }
        return status;
    }

    @Override
    @Transactional
    public boolean delete(Long rcdKey) throws Exception {
        boolean stat = false;
        UserRole userRole = null;
        Password password = null;
        User user = null;
        try {
            user = ebeanServer.find(User.class).where().eq("userKey", rcdKey).findOne();
            if (user.getPwdInfo() != null)
                user.getPwdInfo().delete();
            if (user.getUserRole() != null)
                user.getUserRole().delete();
            user.delete();
            stat = true;
        } finally {
            userRole = null;
            password = null;
            user = null;
        }
        return stat;

    }
}

