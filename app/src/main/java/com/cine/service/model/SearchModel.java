package com.cine.service.model;

/**
 * Created by DELL on 07-05-2017.
 */

public class SearchModel {

    private String search_userfullname;

    private String search_usersubcat;

    private String search_usermaincat;

    private String search_userprofilepic;

    private String search_username;

    public String getSearch_userfullname ()
    {
        return search_userfullname;
    }

    public void setSearch_userfullname (String search_userfullname)
    {
        this.search_userfullname = search_userfullname;
    }

    public String getSearch_usersubcat ()
    {
        return search_usersubcat;
    }

    public void setSearch_usersubcat (String search_usersubcat)
    {
        this.search_usersubcat = search_usersubcat;
    }

    public String getSearch_usermaincat ()
    {
        return search_usermaincat;
    }

    public void setSearch_usermaincat (String search_usermaincat)
    {
        this.search_usermaincat = search_usermaincat;
    }

    public String getSearch_userprofilepic ()
    {
        return search_userprofilepic;
    }

    public void setSearch_userprofilepic (String search_userprofilepic)
    {
        this.search_userprofilepic = search_userprofilepic;
    }

    public String getSearch_username ()
    {
        return search_username;
    }

    public void setSearch_username (String search_username)
    {
        this.search_username = search_username;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [search_userfullname = "+search_userfullname+", search_usersubcat = "+search_usersubcat+", search_usermaincat = "+search_usermaincat+", search_userprofilepic = "+search_userprofilepic+", search_username = "+search_username+"]";
    }
}
