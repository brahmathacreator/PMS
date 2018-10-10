package models;

import controllers.constants.Validation;
import io.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Section extends Model {

    @Id
    @GeneratedValue
    private Long sectionId;
    @Constraints.Required
    @Constraints.Pattern(Validation.app_generic_alpha_numeric_pattern)
    private String sectionName;
    @Constraints.Required
    @Constraints.Pattern(Validation.app_generic_alpha_numeric_pattern)
    private String description;
    @Constraints.Required
    @Constraints.Pattern(Validation.app_generic_alpha_numeric_pattern)
    private String sectionInchargeName;
    @Constraints.Required
    @Constraints.Email
    private String sectionInchargeEmail;
    @Constraints.Required
    @Constraints.Pattern(Validation.app_phone_pattern)
    private String sectionInchargePhone;

    public Section() {
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSectionInchargeName() {
        return sectionInchargeName;
    }

    public void setSectionInchargeName(String sectionInchargeName) {
        this.sectionInchargeName = sectionInchargeName;
    }

    public String getSectionInchargeEmail() {
        return sectionInchargeEmail;
    }

    public void setSectionInchargeEmail(String sectionInchargeEmail) {
        this.sectionInchargeEmail = sectionInchargeEmail;
    }

    public String getSectionInchargePhone() {
        return sectionInchargePhone;
    }

    public void setSectionInchargePhone(String sectionInchargePhone) {
        this.sectionInchargePhone = sectionInchargePhone;
    }
}
