package com.cine.service.model.subcategory.fans;

/**
 * Created by sekhar on 01/04/17.
 */

public class FansSubCategory {
    private Subcategories[] subcategories;

    public Subcategories[] getSubcategories ()
    {
        return subcategories;
    }

    public void setSubcategories (Subcategories[] subcategories)
    {
        this.subcategories = subcategories;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Subcategories = "+subcategories+"]";
    }

}
