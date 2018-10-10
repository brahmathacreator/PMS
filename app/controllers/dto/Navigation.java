package controllers.dto;

import play.data.validation.Constraints;

public class Navigation {


    @Constraints.Required
    private String userId;
    @Constraints.Required
    private String password;
    @Constraints.Required
    private Integer adminUserFlg;


    public Navigation() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAdminUserFlg() {
        return adminUserFlg;
    }

    public void setAdminUserFlg(Integer adminUserFlg) {
        this.adminUserFlg = adminUserFlg;
    }


}
