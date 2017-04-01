package com.cine.service.model.subcategory;

/**
 * Created by sekhar on 01/04/17.
 */

public class ProfSubCategory {
    private Subcategories[] Subcategories;

    public Subcategories[] getSubcategories ()
    {
        return Subcategories;
    }

    public void setSubcategories (Subcategories[] Subcategories)
    {
        this.Subcategories = Subcategories;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Subcategories = "+ Subcategories +"]";
    }
}
