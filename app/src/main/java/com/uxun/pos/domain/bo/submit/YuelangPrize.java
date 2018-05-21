package com.uxun.pos.domain.bo.submit;


import java.util.Date;

public class YuelangPrize {

    public String orgcode;
    public String posno;
    public String saleno;
    public Integer rowno; // 券行号（积分可送N张券，券编号）

    public String name;
    public String quantity;
    public String price;

    public String Create_By;
    public Date Time_Create;
}
