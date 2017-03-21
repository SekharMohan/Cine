package com.cine.service.model;

/**
 * Created by sekhar on 21/03/17.
 */

public class FeedModel {private Languages[] languages;

    private String[] commonwall_posts;

    private Admin_event[] admin_event;

    private Categories[] Categories;

    public Languages[] getLanguages ()
    {
        return languages;
    }

    public void setLanguages (Languages[] languages)
    {
        this.languages = languages;
    }

    public String[] getCommonwall_posts ()
    {
        return commonwall_posts;
    }

    public void setCommonwall_posts (String[] commonwall_posts)
    {
        this.commonwall_posts = commonwall_posts;
    }

    public Admin_event[] getAdmin_event ()
    {
        return admin_event;
    }

    public void setAdmin_event (Admin_event[] admin_event)
    {
        this.admin_event = admin_event;
    }

    public Categories[] getCategories ()
    {
        return Categories;
    }

    public void setCategories (Categories[] Categories)
    {
        this.Categories = Categories;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [languages = "+languages+", commonwall_posts = "+commonwall_posts+", admin_event = "+admin_event+", Categories = "+Categories+"]";
    }


    public class Admin_event
    {
        private String post_video;

        private String post_images;

        private String post_id;

        public String getPost_video ()
        {
            return post_video;
        }

        public void setPost_video (String post_video)
        {
            this.post_video = post_video;
        }

        public String getPost_images ()
        {
            return post_images;
        }

        public void setPost_images (String post_images)
        {
            this.post_images = post_images;
        }

        public String getPost_id ()
        {
            return post_id;
        }

        public void setPost_id (String post_id)
        {
            this.post_id = post_id;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [post_video = "+post_video+", post_images = "+post_images+", post_id = "+post_id+"]";
        }
    }

    public class Languages
    {
        private String language_id;

        private String language_name;

        public String getLanguage_id ()
        {
            return language_id;
        }

        public void setLanguage_id (String language_id)
        {
            this.language_id = language_id;
        }

        public String getLanguage_name ()
        {
            return language_name;
        }

        public void setLanguage_name (String language_name)
        {
            this.language_name = language_name;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [language_id = "+language_id+", language_name = "+language_name+"]";
        }
    }

    public class Categories
    {
        private String maincategory_name;

        private String category_id;

        private String subcategory_names;

        public String getMaincategory_name ()
        {
            return maincategory_name;
        }

        public void setMaincategory_name (String maincategory_name)
        {
            this.maincategory_name = maincategory_name;
        }

        public String getCategory_id ()
        {
            return category_id;
        }

        public void setCategory_id (String category_id)
        {
            this.category_id = category_id;
        }

        public String getSubcategory_names ()
        {
            return subcategory_names;
        }

        public void setSubcategory_names (String subcategory_names)
        {
            this.subcategory_names = subcategory_names;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [maincategory_name = "+maincategory_name+", category_id = "+category_id+", subcategory_names = "+subcategory_names+"]";
        }
    }
}