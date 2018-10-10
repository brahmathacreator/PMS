package controllers.dto;

import models.Notification;

import java.util.ArrayList;

public class NotificationDetails {

    private Notification notification;

    private ArrayList<Object> supportData = new ArrayList<Object>();

    public NotificationDetails() {
    }

    public ArrayList<Object> getSupportData() {
        return supportData;
    }

    public void setSupportData(ArrayList<Object> supportData) {
        this.supportData = supportData;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
