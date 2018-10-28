package models;

import controllers.constants.Validation;
import io.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class School extends Model {

    @Id
    @GeneratedValue
    private Long schoolId;
    @Constraints.Required
    @Constraints.Pattern(Validation.app_generic_alpha_numeric_pattern)
    private String schoolName;
    @Constraints.Required
    @Constraints.Pattern(Validation.app_generic_alpha_numeric_pattern)
    private String description;
    @Constraints.Required
    @Constraints.Pattern(Validation.app_generic_alpha_numeric_pattern)
    private String contactPersonName;
    @Constraints.Required
    @Constraints.Email
    private String contactPersonEmail;
    @Constraints.Pattern(Validation.app_phone_pattern)
    private String contactPersonPhone;
    private String logo;

    public School() {
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public void setContactPersonEmail(String contactPersonEmail) {
        this.contactPersonEmail = contactPersonEmail;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}
