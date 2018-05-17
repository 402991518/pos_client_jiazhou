package com.uxun.pos.view.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.domain.dto.GrdGoods;
import com.uxun.pos.global.Application;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.view.Main;
import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.widget.GoodsLabel;

import java.util.ArrayList;
import java.util.List;

public class WareList extends Fragment {


    private class Adapter extends PagerAdapter {

        private List<View> views;

        Adapter() {
            views = getViewPages();
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }

    private PagerAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewPager viewPager = new ViewPager(getActivity());
        viewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.pagerAdapter = new Adapter();
        viewPager.setAdapter(this.pagerAdapter);
        viewPager.setBackgroundColor(ContextCompat.getColor(Application.getContext(), R.color.translucent));
        return viewPager;
    }

    //获取各个页面的数据
    public List<View> getViewPages() {
        //1.无列表商品
        List<GrdGoods> goodss = ConstantLogin.loginData.grdGoodss;
        if (goodss == null || goodss.size() <= 0) {
            TextView textView = new TextView(getActivity());
            textView.setTextSize(SystemConfig.font_size * 1.38f);
            textView.setTextColor(Color.RED);
            textView.setText("对不起，没有列表商品！");
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            List<View> result = new ArrayList<>(1);
            result.add(textView);
            return result;
        }
        //2.不破坏原始数据
        List<GrdGoods> list = new ArrayList<>(goodss);
        List<Cache> temp = new ArrayList<>();
        List<View> result = new ArrayList<>();
        //2.创建分页
        int index = 1;
        int length = list.size();
        while (list.size() > 0) {
            temp.add(new Cache(index++, length, list.remove(0)));
            if (temp.size() == 3) {
                result.add(createViewPage2(temp));
                temp.clear();
            }
        }
        //3.尾页
        if (temp.size() > 0) {
            result.add(createViewPage2(temp));
        }
        return result;
    }

    //创建单个页面
    private View createViewPage2(List<Cache> list) {
        //1.根节点
        LinearLayout root = new LinearLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        root.setBackgroundColor(ContextCompat.getColor(Application.getContext(), R.color.translucent));
        //2.添加子节点
        int len = list.size();
        for (int i = 0; i < len; i++) {
            //1.创建图标
            Cache cache = list.get(i);
            final GoodsLabel label = new GoodsLabel(getActivity(), cache.length, cache.index, cache.goods);
            LinearLayout.LayoutParams layoutparamsLabel = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutparamsLabel.weight = 1;
            layoutparamsLabel.leftMargin = 8;
            layoutparamsLabel.rightMargin = 8;
            layoutparamsLabel.topMargin = 8;
            layoutparamsLabel.bottomMargin = 8;

            label.setLayoutParams(layoutparamsLabel);
            root.addView(label);
            //4.添加监听
            label.setOnClickListener(new ClickListener() {
                @Override
                protected void click(View view) {
                    ((Main) getActivity()).searchGoods((String) label.getTag());
                }
            });
        }
        return root;
    }

    private class Cache {
        int index;//索引
        int length;//总长度
        GrdGoods goods;

        public Cache(int index, int length, GrdGoods goods) {
            this.index = index;
            this.length = length;
            this.goods = goods;
        }
    }
}
