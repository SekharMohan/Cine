package com.cine.service.model.category;

/**
 * Created by sekhar on 01/04/17.
 */
public class Mincategories
{
    private String maincat_name;

    private String maincat_id;

    public String getMaincat_name ()
    {
        return maincat_name;
    }

    public void setMaincat_name (String maincat_name)
    {
        this.maincat_name = maincat_name;
    }

    public String getMaincat_id ()
    {
        return maincat_id;
    }

    public void setMaincat_id (String maincat_id)
    {
        this.maincat_id = maincat_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [maincat_name = "+maincat_name+", maincat_id = "+maincat_id+"]";
    }
}


