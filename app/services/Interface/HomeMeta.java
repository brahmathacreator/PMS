package services.Interface;

import com.google.inject.ImplementedBy;
import controllers.dto.Home;
import services.Implementation.HomeImpl;

@ImplementedBy(HomeImpl.class)
public interface HomeMeta {

    Home getAdminHome() throws Exception;

    Home getTutorHome() throws Exception;
}
