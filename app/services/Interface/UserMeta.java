package services.Interface;

import com.google.inject.ImplementedBy;
import controllers.dto.Navigation;
import controllers.dto.PageFilter;
import io.ebean.PagedList;
import models.User;
import services.Implementation.UserImpl;

import java.util.Map;
import java.util.Optional;

@ImplementedBy(UserImpl.class)
public interface UserMeta extends Generic<User> {

    PagedList<User> getAllDataAndExclude(PageFilter filter) throws Exception;

    Optional<User> getDataByEmail(String emailId, Integer curdOpt) throws Exception;

    boolean authenticateUser(Navigation nav, Integer curdOpt) throws Exception;

    Map<Long, String> getAllSchools() throws Exception;

    Map<Long, String> getAllBatchs() throws Exception;

    Map<Long, String> getAllSections() throws Exception;

    Map<Long, String> getAllStudents() throws Exception;


}
