package com.cine.service.model;

/**
 * Created by DELL on 07-05-2017.
 */

public class GetReqStatusModel {

    private Emailrequests emailrequests;

    private Addressrequests addressrequests;

    private Mobilerequests mobilerequests;

    public Emailrequests getEmailrequests ()
    {
        return emailrequests;
    }

    public void setEmailrequests (Emailrequests emailrequests)
    {
        this.emailrequests = emailrequests;
    }

    public Addressrequests getAddressrequests ()
    {
        return addressrequests;
    }

    public void setAddressrequests (Addressrequests addressrequests)
    {
        this.addressrequests = addressrequests;
    }

    public Mobilerequests getMobilerequests ()
    {
        return mobilerequests;
    }

    public void setMobilerequests (Mobilerequests mobilerequests)
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
        private String cg_msg;

        private String address_status;

        public String getCg_msg ()
        {
            return cg_msg;
        }

        public void setCg_msg (String cg_msg)
        {
            this.cg_msg = cg_msg;
        }

        public String getAddress_status ()
        {
            return address_status;
        }

        public void setAddress_status (String address_status)
        {
            this.address_status = address_status;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [cg_msg = "+cg_msg+", address_status = "+address_status+"]";
        }
    }

    public class Mobilerequests
    {
        private String cg_msg;

        private String mobile_status;

        public String getCg_msg ()
        {
            return cg_msg;
        }

        public void setCg_msg (String cg_msg)
        {
            this.cg_msg = cg_msg;
        }

        public String getMobile_status ()
        {
            return mobile_status;
        }

        public void setMobile_status (String mobile_status)
        {
            this.mobile_status = mobile_status;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [cg_msg = "+cg_msg+", mobile_status = "+mobile_status+"]";
        }
    }

    public class Emailrequests
    {
        private String email_status;

        private String cg_msg;

        public String getEmail_status ()
        {
            return email_status;
        }

        public void setEmail_status (String email_status)
        {
            this.email_status = email_status;
        }

        public String getCg_msg ()
        {
            return cg_msg;
        }

        public void setCg_msg (String cg_msg)
        {
            this.cg_msg = cg_msg;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [email_status = "+email_status+", cg_msg = "+cg_msg+"]";
        }
    }

}
