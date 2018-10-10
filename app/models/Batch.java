package models;

import controllers.constants.Validation;
import io.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Batch extends Model {

    @Id
    @GeneratedValue
    private Long batchId;
    @Constraints.Required
    @Constraints.Pattern(Validation.app_generic_alpha_numeric_pattern)
    private String batchName;
    @Constraints.Required
    @Constraints.Pattern(Validation.app_generic_alpha_numeric_pattern)
    private String description;
    @Constraints.Required
    @Constraints.Pattern(Validation.app_generic_alpha_numeric_pattern)
    private String batchInchargeName;
    @Constraints.Required
    @Constraints.Email
    private String batchInchargeEmail;
    @Constraints.Required
    @Constraints.Pattern(Validation.app_phone_pattern)
    private String batchInchargePhone;
    private String logo;

    public Batch() {
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBatchInchargeName() {
        return batchInchargeName;
    }

    public void setBatchInchargeName(String batchInchargeName) {
        this.batchInchargeName = batchInchargeName;
    }

    public String getBatchInchargeEmail() {
        return batchInchargeEmail;
    }

    public void setBatchInchargeEmail(String batchInchargeEmail) {
        this.batchInchargeEmail = batchInchargeEmail;
    }

    public String getBatchInchargePhone() {
        return batchInchargePhone;
    }

    public void setBatchInchargePhone(String batchInchargePhone) {
        this.batchInchargePhone = batchInchargePhone;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}
