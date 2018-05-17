package com.uxun.pos.view.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.domain.bo.Ware;
import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.domain.dto.Round;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.utils.DecimalUtils;
import com.uxun.pos.view.widget.Order;

import java.math.BigDecimal;
import java.util.Date;

//商品列表适配器
public class WareAdapter extends Adapter<Ware> {

    public WareAdapter(ListView listView) {
        super(listView);
    }

    public Date getBeginData() {
        if (this.getCount() > 0) {
            Ware ware = (Ware) this.getItem(0);
            return ware.getCreateDate();
        }
        return null;
    }

    public BigDecimal getTotalReal() {
        int count = this.getCount();
        BigDecimal sumReal = new BigDecimal(0);
        for (int i = 0; i < count; i++) {
            Ware ware = (Ware) this.getItem(i);
            sumReal = sumReal.add(ware.getSubRealPrice());
        }
        Integer scale = null;
        if (ConstantLogin.loginData.device.TradeRound != null) {
            scale = ConstantLogin.loginData.device.TradeRound.intValue();
        }
        Integer mode = null;
        if (ConstantLogin.loginData.device.TradeWay != null) {
            mode = ConstantLogin.loginData.device.TradeWay.intValue();
        }
        return DecimalUtils.round(sumReal, new Round(scale, mode));
    }

    public BigDecimal getTotalSale() {
        int count = this.getCount();
        BigDecimal sumSale = new BigDecimal(0);
        for (int i = 0; i < count; i++) {
            Ware ware = (Ware) this.getItem(i);
            sumSale = sumSale.add(ware.getSubSalePrice());
        }
        Integer scale = null;
        if (ConstantLogin.loginData.device.TradeRound != null) {
            scale = ConstantLogin.loginData.device.TradeRound.intValue();
        }
        Integer mode = null;
        if (ConstantLogin.loginData.device.TradeWay != null) {
            mode = ConstantLogin.loginData.device.TradeWay.intValue();
        }
        return DecimalUtils.round(sumSale, new Round(scale, mode));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1.填充图形
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_ware, null);
            Cache cache = new Cache();
            cache.rowno = convertView.findViewById(R.id.rowno);
            cache.itemname = convertView.findViewById(R.id.itemname);
            cache.realPrice = convertView.findViewById(R.id.realPrice);
            cache.quantity = convertView.findViewById(R.id.quantity);
            cache.subRealPrice = convertView.findViewById(R.id.subRealPrice);
            convertView.setTag(cache);
        }
        //2.设置文本
        Cache cache = (Cache) convertView.getTag();
        Ware ware = (Ware) getItem(position);
        cache.rowno.setText((position + 1) + "");
        String itemname = "";
        try {
            itemname = ware.getGoods().ITEMNAME;
        } catch (Exception e) {
        }
        cache.itemname.setText(itemname);

        String realPrice = "";
        try {
            realPrice = "¥" + ware.getRealPrice().toString();
        } catch (Exception e) {
        }
        cache.realPrice.setText(realPrice);
        String quantity = "";
        try {
            quantity = ware.getQuantity().toString();
        } catch (Exception e) {
        }
        cache.quantity.setText(quantity);
        String subRealPrice = "";
        try {
            subRealPrice = "¥" + ware.getSubRealPrice().toString();
        } catch (Exception e) {
        }
        cache.subRealPrice.setText(subRealPrice);
        //3.选中与非选中设置
        Order order = (Order) cache.rowno;
        if (getCursor() == position) {
            convertView.setBackgroundColor(SELECT_COLOR);
            order.setColor(UN_SELECT_COLOR, SELECT_COLOR);
            cache.rowno.setTextColor(UN_SELECT_COLOR);
            cache.itemname.setTextColor(UN_SELECT_COLOR);
            cache.realPrice.setTextColor(UN_SELECT_COLOR);
            cache.quantity.setTextColor(UN_SELECT_COLOR);
            cache.subRealPrice.setTextColor(UN_SELECT_COLOR);
        } else {
            convertView.setBackgroundColor(UN_SELECT_COLOR);
            order.setColor(Color.BLACK, UN_SELECT_COLOR);
            cache.rowno.setTextColor(Color.BLACK);
            cache.itemname.setTextColor(Color.BLACK);
            cache.realPrice.setTextColor(Color.BLACK);
            cache.quantity.setTextColor(Color.BLACK);
            cache.subRealPrice.setTextColor(Color.BLACK);
        }
        cache.rowno.setTextSize(SystemConfig.font_size);
        cache.itemname.setTextSize(SystemConfig.font_size);
        cache.realPrice.setTextSize(SystemConfig.font_size);
        cache.quantity.setTextSize(SystemConfig.font_size);
        cache.subRealPrice.setTextSize(SystemConfig.font_size);
        return convertView;
    }

    private static class Cache {
        TextView rowno;
        TextView itemname;
        TextView realPrice;
        TextView quantity;
        TextView subRealPrice;
    }
}
