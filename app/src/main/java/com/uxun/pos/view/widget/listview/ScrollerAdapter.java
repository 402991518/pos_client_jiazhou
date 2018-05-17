package com.uxun.pos.view.widget.listview;


import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

import com.uxun.pos.view.adapter.Adapter;


public class ScrollerAdapter implements WrapperListAdapter {

    private Adapter adapter;
    private Context context;
    private ScrollerMenu.Builder[] builders;


    public ScrollerAdapter(Context context, Adapter adapter, ScrollerMenu.Builder... builders) {
        this.adapter = adapter;
        this.context = context;
        this.builders = builders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScrollerLayout scrollerLayout;
        if (convertView == null) {
            View content = adapter.getView(position, convertView, parent);
            ScrollerMenu menu = new ScrollerMenu(context, builders);
            scrollerLayout = new ScrollerLayout(content, menu, adapter.getListView().getMeasuredHeight() / adapter.getPHR() - 2);
        } else {
            scrollerLayout = (ScrollerLayout) convertView;
            scrollerLayout.closeMenu();
            adapter.getView(position, scrollerLayout.getContent(), parent);
        }
        scrollerLayout.setPosition(position);
        return scrollerLayout;
    }

    @Override
    public ListAdapter getWrappedAdapter() {
        return adapter;
    }

    @Override
    public int getCount() {
        return adapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return adapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return adapter.getItemId(position);
    }

    @Override
    public boolean hasStableIds() {
        return adapter.hasStableIds();
    }

    @Override
    public boolean isEnabled(int position) {
        return adapter.isEnabled(position);
    }

    @Override
    public int getItemViewType(int position) {
        return adapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return adapter.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return adapter.isEmpty();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return adapter.areAllItemsEnabled();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        adapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        adapter.unregisterDataSetObserver(observer);
    }
}
