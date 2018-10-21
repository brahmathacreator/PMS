package models;

import controllers.constants.Validation;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.History;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Entity
public class User extends Model {

    @Id
    @GeneratedValue
    private Long userKey;
    @Constraints.Required
    @Constraints.Pattern(Validation.app_generic_alpha_numeric_pattern)
    @Column(unique = true)
    private String userId;
    @Constraints.Required
    @Constraints.Pattern(Validation.app_generic_alpha_numeric_pattern)
    private String username;
    @Constraints.Required
    @Constraints.Email
    @Column(unique = true)
    private String email;
    @Constraints.Required
    @Constraints.Pattern(Validation.app_phone_pattern)
    private String phone;
    private String logo;
    @Constraints.Required
    private Integer roleType;
    @Column(columnDefinition = "integer default 0")
    private Integer passwordGenerationStatus;
    private String passwordReference;
    @Column(columnDefinition = "integer default 0")
    private Long schoolId;
    @Column(columnDefinition = "integer default 0")
    private Long batchId;
    @Column(columnDefinition = "integer default 0")
    private Long sectionId;


    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private Password pwdInfo;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private UserRole userRole;


    public User() {
    }


    public Long getUserKey() {
        return userKey;
    }

    public void setUserKey(Long userKey) {
        this.userKey = userKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordReference() {
        return passwordReference;
    }

    public void setPasswordReference(String passwordReference) {
        this.passwordReference = passwordReference;
    }

    public Password getPwdInfo() {
        return pwdInfo;
    }

    public void setPwdInfo(Password pwdInfo) {
        this.pwdInfo = pwdInfo;
    }

    public Integer getPasswordGenerationStatus() {
        return passwordGenerationStatus;
    }

    public void setPasswordGenerationStatus(Integer passwordGenerationStatus) {
        this.passwordGenerationStatus = passwordGenerationStatus;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

}
