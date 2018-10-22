package models;

import controllers.constants.Validation;
import io.ebean.Model;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Comments extends Model {

    @Id
    @GeneratedValue
    private Long commentId;
    @Constraints.Required
    private String subject;
    @Constraints.Required
    private String commentByName;
    @Constraints.Required
    @Lob
    @Column(name="description", length=2000)
    private String description;
    private String attachment;
    @Formats.DateTime(pattern = "MM/dd/yyyy")
    @Constraints.Required
    private Date startDate;
    @Formats.DateTime(pattern = "MM/dd/yyyy")
    @Constraints.Required
    private Date endDate;
    @Formats.DateTime(pattern = "MM/dd/yyyy")
    private Date actualEndDate;


    @Transient
    @Constraints.Required
    private Long projectId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId", nullable = false)
    private Project project;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment")
    private List<SubComments> subComments = new ArrayList<SubComments>(1);


    public Comments() {
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCommentByName() {
        return commentByName;
    }

    public void setCommentByName(String commentByName) {
        this.commentByName = commentByName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public List<SubComments> getSubComments() {
        return subComments;
    }

    public void setSubComments(List<SubComments> subComments) {
        this.subComments = subComments;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }
}
