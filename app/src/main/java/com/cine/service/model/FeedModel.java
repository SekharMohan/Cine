package com.cine.service.model;

/**
 * Created by sekhar on 21/03/17.
 */

public class FeedModel {


    private Commonwall_posts[] commonwall_posts;

    private Admin_event[] admin_event;

    private Categories[] Categories;

    private User_datas[] user_datas;

    public Commonwall_posts[] getCommonwall_posts ()
    {
        return commonwall_posts;
    }

    public void setCommonwall_posts (Commonwall_posts[] commonwall_posts)
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

    public User_datas[] getUser_datas() {
        return user_datas;
    }

    public void setUser_datas(User_datas[] user_datas) {
        this.user_datas = user_datas;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [commonwall_posts = "+commonwall_posts+", admin_event = "+admin_event+", Categories = "+Categories+", User_datas = "+user_datas+"]";
    }



    public class User_datas{

        private String user_profile;

        private String first_name;

        private String email;

        private String subcategory;

        private String userlang_id;

        private String last_name;

        private String maincategory;

        private String full_name;

        public String getUser_profile ()
        {
            return user_profile;
        }

        public void setUser_profile (String user_profile)
        {
            this.user_profile = user_profile;
        }

        public String getFirst_name ()
        {
            return first_name;
        }

        public void setFirst_name (String first_name)
        {
            this.first_name = first_name;
        }

        public String getEmail ()
        {
            return email;
        }

        public void setEmail (String email)
        {
            this.email = email;
        }

        public String getSubcategory ()
        {
            return subcategory;
        }

        public void setSubcategory (String subcategory)
        {
            this.subcategory = subcategory;
        }

        public String getUserlang_id ()
        {
            return userlang_id;
        }

        public void setUserlang_id (String userlang_id)
        {
            this.userlang_id = userlang_id;
        }

        public String getLast_name ()
        {
            return last_name;
        }

        public void setLast_name (String last_name)
        {
            this.last_name = last_name;
        }

        public String getMaincategory ()
        {
            return maincategory;
        }

        public void setMaincategory (String maincategory)
        {
            this.maincategory = maincategory;
        }

        public String getFull_name ()
        {
            return full_name;
        }

        public void setFull_name (String full_name)
        {
            this.full_name = full_name;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [user_profile = "+user_profile+", first_name = "+first_name+", email = "+email+", subcategory = "+subcategory+", userlang_id = "+userlang_id+", last_name = "+last_name+", maincategory = "+maincategory+", full_name = "+full_name+"]";
        }
    }

    public class Commonwall_posts
    {
        private String post_user_fullname;

        private String post_user_image;

        private String post_uscat;

        private String post_comments;

        private String post_photos;

        private String post_text;

        private String post_video_url;

        private String post_likes;

        private String post_comment_replies;

        private String post_ucat;

        private String post_id;

        private String post_username;

        private String post_date;

        private String post_langid;

        public String getPost_uscat ()
        {
            return post_uscat;
        }

        public void setPost_uscat (String post_uscat)
        {
            this.post_uscat = post_uscat;
        }

        public String getPost_comments ()
        {
            return post_comments;
        }

        public void setPost_comments (String post_comments)
        {
            this.post_comments = post_comments;
        }

        public String getPost_photos ()
    {
        return post_photos;
    }

        public void setPost_photos (String post_photos)
        {
            this.post_photos = post_photos;
        }

        public String getPost_text ()
        {
            return post_text;
        }

        public void setPost_text (String post_text)
        {
            this.post_text = post_text;
        }

        public String getPost_video_url ()
        {
            return post_video_url;
        }

        public void setPost_video_url (String post_video_url)
        {
            this.post_video_url = post_video_url;
        }

        public String getPost_likes ()
        {
            return post_likes;
        }

        public void setPost_likes (String post_likes)
        {
            this.post_likes = post_likes;
        }

        public String getPost_comment_replies ()
        {
            return post_comment_replies;
        }

        public void setPost_comment_replies (String post_comment_replies)
        {
            this.post_comment_replies = post_comment_replies;
        }

        public String getPost_ucat ()
        {
            return post_ucat;
        }

        public void setPost_ucat (String post_ucat)
        {
            this.post_ucat = post_ucat;
        }

        public String getPost_id ()
        {
            return post_id;
        }

        public void setPost_id (String post_id)
        {
            this.post_id = post_id;
        }

        public String getPost_username ()
        {
            return post_username;
        }

        public void setPost_username (String post_username)
        {
            this.post_username = post_username;
        }

        public String getPost_date ()
        {
            return post_date;
        }

        public void setPost_date (String post_date)
        {
            this.post_date = post_date;
        }

        public String getPost_langid ()
        {
            return post_langid;
        }

        public void setPost_langid (String post_langid)
        {
            this.post_langid = post_langid;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [post_user_fullname = "+post_user_fullname+", post_user_image = "+post_user_image+", post_uscat = "+post_uscat+", post_comments = "+post_comments+", post_photos = "+post_photos+", post_text = "+post_text+", post_video_url = "+post_video_url+", post_likes = "+post_likes+", post_comment_replies = "+post_comment_replies+", post_ucat = "+post_ucat+", post_id = "+post_id+", post_username = "+post_username+", post_date = "+post_date+", post_langid = "+post_langid+"]";
        }

        public String getPost_user_fullname() {
            return post_user_fullname;
        }

        public void setPost_user_fullname(String post_user_fullname) {
            this.post_user_fullname = post_user_fullname;
        }

        public String getPost_user_image() {
            return post_user_image;
        }

        public void setPost_user_image(String post_user_image) {
            this.post_user_image = post_user_image;
        }
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
