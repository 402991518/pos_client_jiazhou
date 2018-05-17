package com.uxun.pos.domain.bo.submit;

import java.util.List;

//提交的数据
public class Submiter {

    public SaleH h;

    public List<SaleD> ds;

    public List<SaleP> ps;

    public List<SaleR> rs;

    public List<SaleS> ss;

    public Submiter(SaleH h, List<SaleD> ds, List<SaleP> ps, List<SaleR> rs, List<SaleS> ss) {
        this.h = h;
        this.ds = ds;
        this.ps = ps;
        this.rs = rs;
        this.ss = ss;
    }
}
