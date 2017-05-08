package com.cine.service.model;

/**
 * Created by DELL on 07-05-2017.
 */

public class GetCurrecntRequestsModel {

    private Emailrequests[] emailrequests;

    private Addressrequests[] addressrequests;

    private Mobilerequests[] mobilerequests;

    public Emailrequests[] getEmailrequests ()
    {
        return emailrequests;
    }

    public void setEmailrequests (Emailrequests[] emailrequests)
    {
        this.emailrequests = emailrequests;
    }

    public Addressrequests[] getAddressrequests ()
    {
        return addressrequests;
    }

    public void setAddressrequests (Addressrequests[] addressrequests)
    {
        this.addressrequests = addressrequests;
    }

    public Mobilerequests[] getMobilerequests ()
    {
        return mobilerequests;
    }

    public void setMobilerequests (Mobilerequests[] mobilerequests)
    {
        this.mobilerequests = mobilerequests;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [emailrequests = "+emailrequests+", addressrequests = "+addressrequests+", mobilerequests = "+mobilerequests+"]";
    }

    public class Addressrequests
    {
        private String requested_username;

        private String user_fullname;

        public String getRequested_username ()
        {
            return requested_username;
        }

        public void setRequested_username (String requested_username)
        {
            this.requested_username = requested_username;
        }

        public String getUser_fullname ()
        {
            return user_fullname;
        }

        public void setUser_fullname (String user_fullname)
        {
            this.user_fullname = user_fullname;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [requested_username = "+requested_username+", user_fullname = "+user_fullname+"]";
        }
    }

    public class Mobilerequests
    {
        private String requested_username;

        private String user_fullname;

        public String getRequested_username ()
        {
            return requested_username;
        }

        public void setRequested_username (String requested_username)
        {
            this.requested_username = requested_username;
        }

        public String getUser_fullname ()
        {
            return user_fullname;
        }

        public void setUser_fullname (String user_fullname)
        {
            this.user_fullname = user_fullname;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [requested_username = "+requested_username+", user_fullname = "+user_fullname+"]";
        }
    }
    public class Emailrequests
    {
        private String requested_username;

        private String user_fullname;

        public String getRequested_username ()
        {
            return requested_username;
        }

        public void setRequested_username (String requested_username)
        {
            this.requested_username = requested_username;
        }

        public String getUser_fullname ()
        {
            return user_fullname;
        }

        public void setUser_fullname (String user_fullname)
        {
            this.user_fullname = user_fullname;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [requested_username = "+requested_username+", user_fullname = "+user_fullname+"]";
        }
    }


}
