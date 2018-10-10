package services.Implementation;

import controllers.constants.Constants;
import controllers.dto.Home;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.Batch;
import models.School;
import models.Section;
import models.User;
import play.db.ebean.EbeanConfig;
import services.Interface.HomeMeta;

import javax.inject.Inject;

public class HomeImpl implements HomeMeta {

    private final EbeanServer ebeanServer;

    @Inject
    public HomeImpl(EbeanConfig ebeanConfig) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
    }


    @Override
    public Home getAdminHome() throws Exception {
        Home obj = null;
        try {
            obj = new Home();
            obj.setSchools(ebeanServer.find(School.class).findCount());
            obj.setBatches(ebeanServer.find(Batch.class).findCount());
            obj.setSections(ebeanServer.find(Section.class).findCount());
            obj.setUsers(ebeanServer.find(User.class).findCount());
            obj.setAdmins(ebeanServer.find(User.class).where().eq("roleType", Constants.ROLE_TYPE_ADMIN).findCount());
            obj.setTutors(ebeanServer.find(User.class).where().eq("roleType", Constants.ROLE_TYPE_TUTOR).findCount());
            obj.setStudents(ebeanServer.find(User.class).where().eq("roleType", Constants.ROLE_TYPE_STUDENT).findCount());
            return obj;
        } finally {
            obj = null;
        }
    }

    @Override
    public Home getTutorHome() throws Exception {
        Home obj = null;
        try {
            obj = new Home();
            obj.setTutors(ebeanServer.find(User.class).where().eq("roleType", Constants.ROLE_TYPE_TUTOR).findCount());
            obj.setStudents(ebeanServer.find(User.class).where().eq("roleType", Constants.ROLE_TYPE_STUDENT).findCount());
            return obj;
        } finally {
            obj = null;
        }
    }
}
