package com.uxun.pos.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.uxun.pos.global.Application;

import java.util.ArrayList;
import java.util.List;

public abstract class Adapter<T> extends BaseAdapter {

    public static final int SELECT_COLOR = Color.parseColor("#ee7700");
    //public static final int SELECT_COLOR = ContextCompat.getColor(Application.getContext(), R.color.default_theme_color);
    public static final int UN_SELECT_COLOR = Color.WHITE;

    protected final LayoutInflater layoutInflater = (LayoutInflater) Application.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    protected List<T> data = new ArrayList<>();// 数据

    protected int cursor = -1;// 游标

    private ListView listView;

    private int PHR = 4;//在ListView中，可显示条目数默认为4个

    //初始化适配器指定listView,并为其指定OnItemClickListener监听器
    public Adapter(ListView listView) {
        this.listView = listView;
        if (listView != null) {
            listView.setAdapter(this);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (cursor != position) {
                        cursor = position;
                        update();
                    }
                }
            });
        }
    }

    public int getPHR() {
        return PHR;
    }

    public void setPHR(int PHR) {
        this.PHR = PHR;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void update() {
        notifyDataSetChanged();
        listView.smoothScrollToPosition(cursor);
    }

    //添加数据至末尾
    public void add(T entity) {
        if (entity != null) {
            this.data.add(entity);
            this.cursor = this.data.size() - 1;
            update();
        }
    }

    //删除游标处的数据
    public void del() {
        if (cursor >= 0) {
            data.remove(cursor--);
            if (cursor < 0) {
                if (data.size() > 0) {
                    cursor = 0;
                }
            }
            update();
        }
    }

    //获取游标处的数据
    public T get() {
        int size = data.size();
        if (size > 0) {
            if (cursor >= 0 && cursor < size) {
                return data.get(cursor);
            }
        }
        return null;
    }

    public int getCursor() {
        return cursor;
    }

    public ListView getListView() {
        return listView;
    }
}
