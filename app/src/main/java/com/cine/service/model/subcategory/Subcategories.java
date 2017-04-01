package com.cine.service.model.subcategory;

/**
 * Created by sekhar on 01/04/17.
 */

public class Subcategories
{
    private String subcat_name;

    public String getSubcat_name ()
    {
        return subcat_name;
    }

    public void setSubcat_name (String subcat_name)
    {
        this.subcat_name = subcat_name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [subcat_name = "+subcat_name+"]";
    }
}


