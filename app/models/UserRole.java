package models;

import io.ebean.Finder;
import io.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;

@Entity
public class UserRole extends Model {

    @Id
    @GeneratedValue
    private Long roleId;
    @Constraints.Required
    private String roleName;
    @Constraints.Required
    private Integer roleType;
    @Constraints.Required
    private String roleDescription;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userKey")
    private User user;


    public static final Finder<Long, UserRole> finder = new Finder<>(UserRole.class);


    public UserRole() {
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }
}
