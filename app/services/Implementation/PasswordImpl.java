package services.Implementation;

import controllers.dto.PageFilter;
import io.ebean.PagedList;
import models.Password;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.mvc.Http;
import services.Interface.PasswordMeta;

import java.util.Optional;

public class PasswordImpl implements PasswordMeta {

    @Override
    public Integer count() throws Exception {
        return null;
    }

    @Override
    public PagedList<Password> getAllData(PageFilter filter) throws Exception {
        return null;
    }

    @Override
    public Optional<Password> getDataByKey(Long id, Integer curdOpt) throws Exception {
        return Optional.empty();
    }


    @Override
    public Optional<Password> getDataByKeyAndExcludeKey(Long id, Long eId) throws Exception {
        return Optional.empty();
    }

    @Override
    public boolean saveOrEdit(Password object, Integer curdOpt, Http.Context ctx) throws Exception {
        Logger.debug("Inside [UserImpl][saveOrEdit]");
        boolean status = false;
        try {
            object.setPassword(BCrypt.hashpw(object.getPassword(), BCrypt.gensalt()));
            object.setEmail(object.getEmail().toLowerCase());
            object.save();
            status = true;
        } finally {

        }
        return status;
    }

    @Override
    public boolean delete(Long rcdKey) throws Exception {
        return false;
    }
}
