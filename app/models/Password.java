package models;

import controllers.constants.Validation;
import io.ebean.Model;
import io.ebean.annotation.History;
import play.data.validation.Constraints;


import javax.persistence.*;


@Entity
public class Password extends Model {

    @Id
    @GeneratedValue
    private long pwd_id;
    @Constraints.Required(message = "page.validation.txt.Mandatory.yes")
    private String password;
    @Constraints.Required(message = "page.validation.txt.Mandatory.yes")
    @Transient
    private String confirmPassword;
    @Constraints.Required(message = "page.validation.txt.Mandatory.yes")
    @Constraints.Email(message = "app.generic.invalid.email")
    private String email;

    @OneToOne
    @JoinColumn(name = "userKey", nullable = false)
    private User user;

    public Password() {
    }

    public Password(long pwd_id, @Constraints.Required @Constraints.Pattern(Validation.app_password_pattern) String password, @Constraints.Required @Constraints.Pattern(Validation.app_password_pattern) String confirmPassword, @Constraints.Required @Constraints.Email String email, User user) {
        this.pwd_id = pwd_id;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getPwd_id() {
        return pwd_id;
    }

    public void setPwd_id(long pwd_id) {
        this.pwd_id = pwd_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
