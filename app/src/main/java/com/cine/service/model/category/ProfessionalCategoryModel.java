package com.cine.service.model.category;

/**
 * Created by sekhar on 01/04/17.
 */

public class ProfessionalCategoryModel
{
    private Mincategories[] mincategories;

    public Mincategories[] getMincategories ()
    {
        return mincategories;
    }

    public void setMincategories (Mincategories[] mincategories)
    {
        this.mincategories = mincategories;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [mincategories = "+mincategories+"]";
    }
}
