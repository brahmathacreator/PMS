package models;

import io.ebean.Model;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.*;

@Entity
public class Project extends Model {

    @Id
    @GeneratedValue
    private Long projectId;
    @Constraints.Required(message = "page.validation.txt.Mandatory.yes")
    private String projectTitle;
    @Constraints.Required(message = "page.validation.txt.Mandatory.yes")
    private String projectTitleDesc;
    private String logo;
    @Constraints.Required(message = "page.validation.txt.Mandatory.yes")
    @Lob
    @Column(name = "description", length = 2000)
    private String description;
    @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date projectCreationDT;
    @Constraints.Required(message = "page.validation.txt.Mandatory.yes")
    private Integer duration;
    @Column(nullable = false)
    private Long schoolId;
    @Column(nullable = false)
    private Long createdBy;
    @Constraints.Required(message = "page.validation.txt.Mandatory.yes")
    @Column(nullable = false)
    private Long studentId;
    private String finalDescription;
    private String zippedComments;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private List<Comments> comments = new ArrayList<Comments>(1);


    public Project() {
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectTitleDesc() {
        return projectTitleDesc;
    }

    public void setProjectTitleDesc(String projectTitleDesc) {
        this.projectTitleDesc = projectTitleDesc;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getProjectCreationDT() {
        return projectCreationDT;
    }

    public void setProjectCreationDT(Date projectCreationDT) {
        this.projectCreationDT = projectCreationDT;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getFinalDescription() {
        return finalDescription;
    }

    public void setFinalDescription(String finalDescription) {
        this.finalDescription = finalDescription;
    }

    public String getZippedComments() {
        return zippedComments;
    }

    public void setZippedComments(String zippedComments) {
        this.zippedComments = zippedComments;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
