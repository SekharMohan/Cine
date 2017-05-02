package com.cine.service.model;

/**
 * Created by DELL on 01-05-2017.
 */

public class UserWallModel {

    private String username;

    private String usermaincat;

    private String usersubcat;

    private String userprofilepic;

    private String userfullname;

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getUsermaincat ()
    {
        return usermaincat;
    }

    public void setUsermaincat (String usermaincat)
    {
        this.usermaincat = usermaincat;
    }

    public String getUsersubcat ()
    {
        return usersubcat;
    }

    public void setUsersubcat (String usersubcat)
    {
        this.usersubcat = usersubcat;
    }

    public String getUserprofilepic ()
    {
        return userprofilepic;
    }

    public void setUserprofilepic (String userprofilepic)
    {
        this.userprofilepic = userprofilepic;
    }

    public String getUserfullname ()
    {
        return userfullname;
    }

    public void setUserfullname (String userfullname)
    {
        this.userfullname = userfullname;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [username = "+username+", usermaincat = "+usermaincat+", usersubcat = "+usersubcat+", userprofilepic = "+userprofilepic+", userfullname = "+userfullname+"]";
    }

}
