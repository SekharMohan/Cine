package com.cine.service.model.subcategory.fans;

/**
 * Created by sekhar on 01/04/17.
 */

public class Subcategories
{
    private String scat_name;

    public String getScat_name ()
    {
        return scat_name;
    }

    public void setScat_name (String scat_name)
    {
        this.scat_name = scat_name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [scat_name = "+scat_name+"]";
    }
}