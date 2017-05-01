package com.cine.service.model.userinfo;

/**
 * Created by Sekhar Madhiyazhagan on 5/1/2017.
 */

public class User {
    private String cg_log_status;

    private Cg_info cg_info;

    public String getCg_log_status ()
    {
        return cg_log_status;
    }

    public void setCg_log_status (String cg_log_status)
    {
        this.cg_log_status = cg_log_status;
    }

    public Cg_info getCg_info ()
    {
        return cg_info;
    }

    public void setCg_info (Cg_info cg_info)
    {
        this.cg_info = cg_info;
    }

}
