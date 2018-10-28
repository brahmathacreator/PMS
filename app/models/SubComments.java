package models;

import io.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;

@Entity
public class SubComments extends Model {

    @Id
    @GeneratedValue
    private Long subCommentId;
    private String subject;
    private String commentByName;
    @Constraints.Required
    @Lob
    @Column(name = "description", length = 2000)
    private String description;
    private String attachment;
    private Date startDate;
    private Date endDate;
    private Date actualEndDate;
    private int markedFlag;


    @Transient
    @Constraints.Required
    private Long commentId;

    @Transient
    @Constraints.Required
    private Long projectId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "commentId", nullable = false)
    private Comments comment;


    public SubComments() {
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

    public Long getSubCommentId() {
        return subCommentId;
    }

    public void setSubCommentId(Long subCommentId) {
        this.subCommentId = subCommentId;
    }

    public Comments getComment() {
        return comment;
    }

    public void setComment(Comments comment) {
        this.comment = comment;
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

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public int getMarkedFlag() {
        return markedFlag;
    }

    public void setMarkedFlag(int markedFlag) {
        this.markedFlag = markedFlag;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
