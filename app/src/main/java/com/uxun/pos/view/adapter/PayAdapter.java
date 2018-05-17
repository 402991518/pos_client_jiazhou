package com.uxun.pos.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.domain.bo.pay.PayBean;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.view.widget.Order;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/3/20.
 */

public class PayAdapter extends Adapter<PayBean> {

    private WareAdapter wareAdapter;

    public PayAdapter(ListView listView, WareAdapter wareAdapter) {
        super(listView);
        this.wareAdapter = wareAdapter;
    }

    public WareAdapter getWareAdapter() {
        return wareAdapter;
    }


    //获取付款总额
    public BigDecimal getTotal() {
        return wareAdapter.getTotalReal();
    }

    //获取收入
    public BigDecimal getIncome() {
        BigDecimal income = BigDecimal.ZERO;
        for (int i = 0; i < this.getCount(); i++) {
            PayBean bean = (PayBean) this.getItem(i);
            BigDecimal amount = bean.getAmount();
            if (bean.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                income = income.add(amount);
            }
        }
        return income;
    }

    //删除最后一次付款（回找金额）
    public void delLast() {
        int count = this.getCount();
        if (count > 0) {
            int index = count - 1;
            this.data.remove(index);
            this.cursor = index - 1;
            this.update();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1.填充图形
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_pay_bean, null);
            Cache cache = new Cache();
            cache.rowno = convertView.findViewById(R.id.rowno);
            cache.name = convertView.findViewById(R.id.name);
            cache.amount = convertView.findViewById(R.id.amount);
            convertView.setTag(cache);
        }
        //2.设置文本
        Cache cache = (Cache) convertView.getTag();
        PayBean bean = (PayBean) getItem(position);
        cache.rowno.setText((position + 1) + "");
        cache.name.setText(bean.getPayName());
        cache.amount.setText(bean.getAmount().toString());
        //3.选中与非选中设置
        Order order = (Order) cache.rowno;
        if (getCursor() == position) {
            convertView.setBackgroundColor(SELECT_COLOR);
            order.setColor(UN_SELECT_COLOR, SELECT_COLOR);
            cache.rowno.setTextColor(UN_SELECT_COLOR);
            cache.name.setTextColor(UN_SELECT_COLOR);
            cache.amount.setTextColor(UN_SELECT_COLOR);
        } else {
            convertView.setBackgroundColor(UN_SELECT_COLOR);
            order.setColor(SELECT_COLOR, UN_SELECT_COLOR);
            cache.rowno.setTextColor(SELECT_COLOR);
            cache.name.setTextColor(SELECT_COLOR);
            cache.amount.setTextColor(SELECT_COLOR);
        }
        cache.rowno.setTextSize(SystemConfig.font_size);
        cache.name.setTextSize(SystemConfig.font_size);
        cache.amount.setTextSize(SystemConfig.font_size);
        return convertView;
    }

    private static class Cache {
        TextView rowno;
        TextView name;
        TextView amount;
    }
}
