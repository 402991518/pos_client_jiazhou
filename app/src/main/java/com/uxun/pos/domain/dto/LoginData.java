package com.uxun.pos.domain.dto;

import java.util.Date;
import java.util.List;

//登录数据
public class LoginData {

    public Device device;
    public User user;
    public List<Key> keys;
    public Peripheral peripheral;
    public List<PayType> payTypes;
    public List<PayKind> payKinds;
    public Date date;
    public String saleno;
    public List<GrdGoods> grdGoodss;
    public TicketConfig ticketConfig;
    public Zhaokeyi zhaokeyi;

}
