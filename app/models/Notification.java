package models;

import io.ebean.Finder;
import io.ebean.Model;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Notification extends Model {

    @Id
    @GeneratedValue
    private Long notificationId;
    @Constraints.Required
    private Integer notificationType;
    @Constraints.Required
    private Integer notificationContentType;
    @Constraints.Required
    private String title;
    @Constraints.Required
    private String message;
    @Constraints.Required
    private String toAddress;
    @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date notificationInitiationDT;
    @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date notificationSentDT;

    public static final Finder<Long, Notification> finder = new Finder<>(Notification.class);

    public Notification() {
    }

    public Notification(Long notificationId, @Constraints.Required Integer notificationType, @Constraints.Required Integer notificationContentType, @Constraints.Required String title, @Constraints.Required String message, @Constraints.Required String toAddress, Date notificationInitiationDT, Date notificationSentDT) {
        this.notificationId = notificationId;
        this.notificationType = notificationType;
        this.notificationContentType = notificationContentType;
        this.title = title;
        this.message = message;
        this.toAddress = toAddress;
        this.notificationInitiationDT = notificationInitiationDT;
        this.notificationSentDT = notificationSentDT;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(Integer notificationType) {
        this.notificationType = notificationType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getNotificationInitiationDT() {
        return notificationInitiationDT;
    }

    public void setNotificationInitiationDT(Date notificationInitiationDT) {
        this.notificationInitiationDT = notificationInitiationDT;
    }

    public Date getNotificationSentDT() {
        return notificationSentDT;
    }

    public void setNotificationSentDT(Date notificationSentDT) {
        this.notificationSentDT = notificationSentDT;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public Integer getNotificationContentType() {
        return notificationContentType;
    }

    public void setNotificationContentType(Integer notificationContentType) {
        this.notificationContentType = notificationContentType;
    }
}
