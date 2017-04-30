package com.cine.service.model;

/**
 * Created by DELL on 01-05-2017.
 */

public class EventsModel {

    private String event_date;

    private String event_description;

    private String event_title;

    private String event_video;

    private String event_image;

    public String getEvent_date ()
    {
        return event_date;
    }

    public void setEvent_date (String event_date)
    {
        this.event_date = event_date;
    }

    public String getEvent_description ()
    {
        return event_description;
    }

    public void setEvent_description (String event_description)
    {
        this.event_description = event_description;
    }

    public String getEvent_title ()
    {
        return event_title;
    }

    public void setEvent_title (String event_title)
    {
        this.event_title = event_title;
    }

    public String getEvent_video ()
    {
        return event_video;
    }

    public void setEvent_video (String event_video)
    {
        this.event_video = event_video;
    }

    public String getEvent_image ()
    {
        return event_image;
    }

    public void setEvent_image (String event_image)
    {
        this.event_image = event_image;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [event_date = "+event_date+", event_description = "+event_description+", event_title = "+event_title+", event_video = "+event_video+", event_image = "+event_image+"]";
    }
}
