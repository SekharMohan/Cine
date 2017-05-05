package com.cine.service.model;

/**
 * Created by DELL on 05-05-2017.
 */

public class Alerts {

    private String alert_picture;

    private String alert_description;

    private String alert_tyoe;

    private String alert_title;

    public String getAlert_picture ()
    {
        return alert_picture;
    }

    public void setAlert_picture (String alert_picture)
    {
        this.alert_picture = alert_picture;
    }

    public String getAlert_description ()
    {
        return alert_description;
    }

    public void setAlert_description (String alert_description)
    {
        this.alert_description = alert_description;
    }

    public String getAlert_tyoe ()
    {
        return alert_tyoe;
    }

    public void setAlert_tyoe (String alert_tyoe)
    {
        this.alert_tyoe = alert_tyoe;
    }

    public String getAlert_title ()
    {
        return alert_title;
    }

    public void setAlert_title (String alert_title)
    {
        this.alert_title = alert_title;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [alert_picture = "+alert_picture+", alert_description = "+alert_description+", alert_tyoe = "+alert_tyoe+", alert_title = "+alert_title+"]";
    }
}
