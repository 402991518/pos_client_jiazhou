package com.uxun.pos.domain.dto;

/**
 * Created by Administrator on 2018/4/19.
 */

public class Zhaokeyi {
    public String crm_url;
    public String macid;
    public String ucardno;
    public String upass;

    @Override
    public String toString() {
        return "Zhaokeyi{" +
                "crm_url='" + crm_url + '\'' +
                ", macid='" + macid + '\'' +
                ", ucardno='" + ucardno + '\'' +
                ", upass='" + upass + '\'' +
                '}';
    }
}
