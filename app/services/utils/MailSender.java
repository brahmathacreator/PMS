package services.utils;

import controllers.dto.NotificationDetails;
import models.Notification;
import models.User;
import play.Logger;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import play.mvc.Http;
import views.html.notification.MT1;

import javax.inject.Inject;
import java.util.Date;


public class MailSender {

    @Inject
    private MailerClient mailerClient;

    public MailSender() {
    }

    public boolean sendPasswordMail(NotificationDetails details) throws Exception {
        Logger.debug("Inside [MailSender][sendMailNotification]");
        Email mail = null;
        boolean status = false;
        String url = null;
        Date d = null;
        try {
            mail = new Email();
            mail.setFrom("ram.apptesting.01@gmail.com");
            mail.setSubject(details.getNotification().getTitle());
            mail.addTo(details.getNotification().getToAddress());
            if (Http.Context.current().request().secure()) {
                url = "https://" + Http.Context.current().request().host() + "/resetPasswordPage?key=" + ((User) details.getSupportData().get(0)).getPasswordReference();
            } else {
                url = "http://" + Http.Context.current().request().host() + "/resetPasswordPage?key=" + ((User) details.getSupportData().get(0)).getPasswordReference();
            }
            mail.setBodyHtml(MT1.render(details.getNotification().getTitle(), details.getNotification().getMessage(),url).body());
            mailerClient.send(mail);
            d= new Date();
            details.getNotification().setNotificationSentDT(d);
            status = true;
        } finally {
            mail = null;
            d = null;
        }
        return status;
    }
}
