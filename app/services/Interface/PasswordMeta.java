package services.Interface;

import com.google.inject.ImplementedBy;
import models.Password;
import models.User;
import services.Implementation.PasswordImpl;
import services.Implementation.UserImpl;

@ImplementedBy(PasswordImpl.class)
public interface PasswordMeta extends Generic<Password>{

}
