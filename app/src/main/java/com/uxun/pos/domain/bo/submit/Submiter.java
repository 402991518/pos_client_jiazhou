package com.uxun.pos.domain.bo.submit;

import java.util.List;

//提交的数据
public class Submiter {

    public SaleH h;

    public List<SaleD> ds;

    public List<SaleP> ps;

    public List<SaleR> rs;

    public List<SaleS> ss;


    public YuelangScore yuelangScore;// 粤浪积分（销售产生）

    public YuelangReback yuelangReback;// 粤浪退积分(退货产生)

    public List<YuelangPrize> yuelangPrizes;// 粤浪卡券信息


    public Submiter(SaleH h, List<SaleD> ds, List<SaleP> ps, List<SaleR> rs, List<SaleS> ss) {
        this.h = h;
        this.ds = ds;
        this.ps = ps;
        this.rs = rs;
        this.ss = ss;
    }
}
