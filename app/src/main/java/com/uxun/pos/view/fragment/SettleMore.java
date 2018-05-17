package com.uxun.pos.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.domain.dto.PayType;
import com.uxun.pos.global.Application;
import com.uxun.pos.utils.PayTypeUtisl;
import com.uxun.pos.view.Settle;
import com.uxun.pos.view.adapter.PagerAdapter;
import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

public class SettleMore extends Fragment {

    private static LayoutInflater layoutInflater = (LayoutInflater) Application.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    private View select;
    private ViewPager viewPager;
    private LinearLayout container;

    private int diatance; //小圆点移动的位置
    private PagerAdapter pagerAdapter;
    private List<View> viewPages = new ArrayList<>();
    private List<List<PayType>> payPages = new ArrayList<>();


    //按钮上的ICON
    private Bitmap pay_wechat;
    private Bitmap pay_union;
    private Bitmap pay_alipay;
    private Bitmap pay_cash;
    private Bitmap pay_doller;
    private Bitmap pay_hk;
    private Bitmap pay_score;
    private Bitmap pay_check;
    private Bitmap pay_redbag;
    private Bitmap pay_credit;
    private Bitmap pay_ecard;
    private Bitmap pay_exchange;
    private Bitmap pay_cash_prize;
    private Bitmap pay_pin_money;
    private Bitmap pay_over_draft;
    private Bitmap pay_scan;
    private Bitmap pay_none_state;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settle_more, null);
        this.select = view.findViewById(R.id.select);
        this.container = view.findViewById(R.id.container);
        this.viewPager = view.findViewById(R.id.viewPager);
        initIcon();
        createPayPages();
        createViewPages();
        initSelectView();
        initSelectEvent();
        return view;
    }


    private void initIcon() {
        Resources resources = this.getActivity().getResources();
        pay_wechat = BitmapFactory.decodeResource(resources, R.mipmap.pay_wechat);
        pay_alipay = BitmapFactory.decodeResource(resources, R.mipmap.pay_alipay);
        pay_union = BitmapFactory.decodeResource(resources, R.mipmap.pay_union);
        pay_cash = BitmapFactory.decodeResource(resources, R.mipmap.pay_cash);
        pay_doller = BitmapFactory.decodeResource(resources, R.mipmap.pay_doller);
        pay_hk = BitmapFactory.decodeResource(resources, R.mipmap.pay_hk);
        pay_score = BitmapFactory.decodeResource(resources, R.mipmap.pay_score);
        pay_check = BitmapFactory.decodeResource(resources, R.mipmap.pay_check);
        pay_redbag = BitmapFactory.decodeResource(resources, R.mipmap.pay_redbag);
        pay_credit = BitmapFactory.decodeResource(resources, R.mipmap.pay_credit);
        pay_ecard = BitmapFactory.decodeResource(resources, R.mipmap.pay_ecard);
        pay_exchange = BitmapFactory.decodeResource(resources, R.mipmap.pay_exchange);
        pay_cash_prize = BitmapFactory.decodeResource(resources, R.mipmap.pay_cash_prize);
        pay_pin_money = BitmapFactory.decodeResource(resources, R.mipmap.pay_pin_money);
        pay_over_draft = BitmapFactory.decodeResource(resources, R.mipmap.pay_over_draft);
        pay_scan = BitmapFactory.decodeResource(resources, R.mipmap.pay_scan);
        pay_none_state = BitmapFactory.decodeResource(resources, R.mipmap.pay_none_state);
    }

    //付款功能键分页
    private void createPayPages() {
        List<PayType> can_use_paytype = PayTypeUtisl.getList();
        List<PayType> page = null;
        for (int i = 0; i < can_use_paytype.size(); i++) {
            if (i % 4 == 0) {
                page = new ArrayList<>();
                payPages.add(page);
            }
            page.add(can_use_paytype.get(i));
        }
    }

    //初始所有化页面
    private void createViewPages() {
        pagerAdapter = new PagerAdapter(viewPages);
        for (int i = 0; i < payPages.size(); i++) {
            List<PayType> payPage = payPages.get(i);
            View viewPage = createViewPage(payPage);
            viewPages.add(viewPage);
        }
        viewPager.setAdapter(pagerAdapter);
    }

    //创建单个页面
    private View createViewPage(List<PayType> group) {
        View view = layoutInflater.inflate(R.layout.item_settle_key, null);
        for (int i = 0; i < group.size(); i++) {
            //1.找到组件
            View container = view.findViewById(ResourceUtils.getResourceByID("container_" + i));
            View linearLayout = view.findViewById(ResourceUtils.getResourceByID("linearLayout_" + i));
            ImageView icon = view.findViewById(ResourceUtils.getResourceByID("icon_" + i));
            TextView text = view.findViewById(ResourceUtils.getResourceByID("text_" + i));
            //2.初始化图形显示
            linearLayout.setTag(group.get(i).PayType);
            text.setText(group.get(i).PayDescr);
            icon.setImageBitmap(getPayIcon(group.get(i).PayType));
            container.setVisibility(View.VISIBLE);
            //3.设置回调
            linearLayout.setOnClickListener(new ClickListener() {
                @Override
                protected void click(View view) {
                    Settle settle = (Settle) getActivity();
                    settle.onPayButtonClickResult((String) view.getTag());
                }
            });
        }
        Log.i("SettleMore", "createViewPage");
        return view;
    }

    //初始化小圆点个数
    private void initSelectView() {
        for (int i = 0; i < viewPages.size(); i++) {
            View white = new View(this.getActivity().getApplicationContext());
            white.setBackgroundResource(R.drawable.cir_white);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            if (i != 0) {
                params.leftMargin = 20;
            }
            white.setLayoutParams(params);
            container.addView(white);
        }
    }

    //小圆点滑动事件
    private void initSelectEvent() {
        select.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View child_0 = container.getChildAt(0);
                View child_1 = container.getChildAt(1);
                if (child_0 != null && child_1 != null) {
                    diatance = container.getChildAt(1).getLeft() - container.getChildAt(0).getLeft();
                }
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                position = position % viewPages.size();
                float leftMargin = diatance * (position + positionOffset);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) select.getLayoutParams();
                params.leftMargin = Math.round(leftMargin);
                select.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //获取付款编码ICON
    public Bitmap getPayIcon(String type) {
        switch (type) {
            case PayTypeUtisl.KEY_01:
                return pay_cash;
            case PayTypeUtisl.KEY_02:
                return pay_hk;
            case PayTypeUtisl.KEY_03:
                return pay_union;
            case PayTypeUtisl.KEY_04:
                return pay_ecard;
            case PayTypeUtisl.KEY_05:
                return pay_exchange;
            case PayTypeUtisl.KEY_06:
                return pay_score;
            case PayTypeUtisl.KEY_08:
                return pay_doller;
            case PayTypeUtisl.KEY_09:
                return pay_check;
            case PayTypeUtisl.KEY_11:
                return pay_pin_money;
            case PayTypeUtisl.KEY_12:
                return pay_cash_prize;
            case PayTypeUtisl.KEY_13:
                return pay_wechat;
            case PayTypeUtisl.KEY_14:
                return pay_alipay;
            case PayTypeUtisl.KEY_15:
                return pay_redbag;
            case PayTypeUtisl.KEY_16:
                return pay_over_draft;
            case PayTypeUtisl.KEY_17:
                return pay_credit;
            case PayTypeUtisl.KEY_18:
                return pay_scan;
            default:
                return pay_none_state;
        }
    }


}
