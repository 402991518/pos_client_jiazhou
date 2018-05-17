package com.uxun.pos.domain.bo;

import com.uxun.pos.domain.dto.Goods;
import com.uxun.pos.utils.DecimalUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;


public class Ware {

    private Goods goods;// 商品
    private BigDecimal quantity;// 数量
    private BigDecimal realPrice;// 商品实价

    private BigDecimal scorescale = new BigDecimal(0);// 积分因子
    private BigDecimal scoretimes = new BigDecimal(1);// 积分倍数

    private List<Rate> rates;// 变价信息
    private Date createDate = new Date();// 创建时间

    public Ware(Goods goods, BigDecimal quantity) {
        this.goods = goods;
        this.quantity = quantity;
        this.realPrice = this.goods.SALEPRICE;
    }

    //小计实价
    public BigDecimal getSubRealPrice() {
        BigDecimal totalReal = this.quantity.multiply(this.realPrice);
        return DecimalUtils.round(totalReal, goods.round);
    }

    //小计售价
    public BigDecimal getSubSalePrice() {
        BigDecimal totalSale = this.quantity.multiply(this.goods.SALEPRICE);
        return DecimalUtils.round(totalSale, goods.round);
    }

    //获取商品实价
    public BigDecimal getRealPrice() {
        return this.realPrice.divide(new BigDecimal(1), 2, RoundingMode.HALF_UP);
    }

    //获取商品售价
    public BigDecimal getSalePrice() {
        return goods.SALEPRICE.divide(new BigDecimal(1), 2, RoundingMode.HALF_UP);
    }

    //获取商品数量
    public BigDecimal getQuantity() {
        return quantity;
    }

    //商品折扣
    public BigDecimal getRebate() {
        if (this.goods.SALEPRICE.compareTo(new BigDecimal(0)) == 0) {
            return new BigDecimal(0);
        }
        return this.realPrice.divide(this.goods.SALEPRICE, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
    }

    //设置商品实价
    public void setRealPrice(BigDecimal realPrice) {
        this.realPrice = realPrice;
    }

    //设置商品数量
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    //设置商品折扣,85表示85折
    public void setRebate(BigDecimal rebate) {
        this.realPrice = rebate.multiply(this.goods.SALEPRICE).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
    }

    //获取商品
    public Goods getGoods() {
        return goods;
    }

    //获取商品添加时间
    public Date getCreateDate() {
        return createDate;
    }

    //获取积分因子
    public BigDecimal getScorescale() {
        return scorescale;
    }

    //设置积分因子
    public void setScorescale(BigDecimal scorescale) {
        if (scorescale != null) {
            this.scorescale = scorescale;
        }
    }

    //获取积分倍数
    public BigDecimal getScoretimes() {
        return scoretimes;
    }

    //设置积分倍数
    public void setScoretimes(BigDecimal scoretimes) {
        if (scoretimes != null) {
            this.scoretimes = scoretimes;
        }
    }

    //设置变价信息
    public List<Rate> getRates() {
        return rates;
    }

    //获取变价信息
    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }
}
