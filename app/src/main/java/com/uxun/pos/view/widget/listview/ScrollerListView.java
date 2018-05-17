package com.uxun.pos.view.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.uxun.pos.view.adapter.Adapter;


public class ScrollerListView extends ListView {

    private Adapter adapter;

    private float downX;
    private int touchIndex;
    private boolean scrollable;
    private int scaledTouchSlop;
    private ScrollerLayout touchItem;

    public ScrollerListView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void addScrollerMenuBuilders(ScrollerMenu.Builder... builders) {
        super.setAdapter(new ScrollerAdapter(this.getContext(), this.adapter, builders));
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        this.adapter = (Adapter) adapter;
        addScrollerMenuBuilders(null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                scrollable = false;
                touchIndex = pointToPosition(x, y);
                View view = getChildAt(touchIndex - getFirstVisiblePosition());
                if (touchItem != null && touchItem.isOpen()) {
                    touchItem.smoothCloseMenu();
                    touchItem = null;
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    return super.onTouchEvent(ev);
                }
                if (view instanceof ScrollerLayout) {
                    touchItem = (ScrollerLayout) view;
                }
                if (touchItem != null) {
                    if (adapter.getCursor() == touchItem.getPosition()) {
                        touchItem.onSwipe(ev);
                    } else {
                        touchItem = null;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchItem != null) {
                    touchIndex = pointToPosition(x, y) - getHeaderViewsCount();
                    if (touchIndex == touchItem.getPosition()) {
                        float dx = Math.abs(x - downX);
                        if (scrollable) {
                            touchItem.onSwipe(ev);
                            ev.setAction(MotionEvent.ACTION_CANCEL);
                            return super.onTouchEvent(ev);
                        } else {
                            if (dx > scaledTouchSlop) {
                                scrollable = true;
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (touchItem != null) {
                    if (scrollable) {
                        touchItem.onSwipe(ev);
                        boolean isAfterOpen = touchItem.isOpen();
                        if (!isAfterOpen) {
                            touchIndex = -1;
                            touchItem = null;
                        }
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        return super.onTouchEvent(ev);
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }
}
