package services.Interface;

import com.google.inject.ImplementedBy;
import controllers.dto.Navigation;
import controllers.dto.PageFilter;
import io.ebean.PagedList;
import models.School;
import models.User;
import services.Implementation.SchoolImpl;
import services.Implementation.UserImpl;

import java.util.Optional;

@ImplementedBy(SchoolImpl.class)
public interface SchoolMeta extends Generic<School> {

}
