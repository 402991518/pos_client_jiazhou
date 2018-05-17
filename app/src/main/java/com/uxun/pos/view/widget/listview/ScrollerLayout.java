package com.uxun.pos.view.widget.listview;

import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.OverScroller;


public class ScrollerLayout extends FrameLayout {

    private View content;
    private View menu;

    private OverScroller openScroller;
    private OverScroller closeScroller;
    private GestureDetectorCompat gestureDetector;

    private int baseX;
    private int downX;
    private boolean isFling;
    private int scaledTouchSlop;

    private boolean openable;
    private int position = 0;

    private int height;

    public ScrollerLayout(View content, ScrollerMenu menu, int height) {
        super(content.getContext());
        this.content = content;
        this.menu = menu;
        this.height = height;
        scaledTouchSlop = ViewConfiguration.get(content.getContext()).getScaledTouchSlop();
        initLayout();
    }

    private void initLayout() {
        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
        content.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        menu.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        openScroller = new OverScroller(getContext(), new BounceInterpolator());
        closeScroller = new OverScroller(getContext(), new BounceInterpolator());
        gestureDetector = new GestureDetectorCompat(getContext(), new SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                isFling = false;
                return true;
            }

            @Override
            public boolean onFling(MotionEvent sta, MotionEvent end, float velocityX, float velocityY) {
                if (Math.abs(sta.getX() - end.getX()) > scaledTouchSlop && velocityX < scaledTouchSlop) {
                    isFling = true;
                }
                return super.onFling(sta, end, velocityX, velocityY);
            }
        });
        addView(content);
        addView(menu);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        content.layout(0, 0, content.getMeasuredWidth(), content.getMeasuredHeight());
        menu.layout(content.getMeasuredWidth(), 0, content.getMeasuredWidth() + menu.getMeasuredWidth(), menu.getMeasuredHeight());
    }

    public boolean onSwipe(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                isFling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int dis = (int) (downX - event.getX());
                if (openable) {
                    dis += menu.getWidth();
                }
                swipe(dis);
                break;
            case MotionEvent.ACTION_UP:
                if ((isFling || Math.abs(downX - event.getX()) > (menu.getWidth() / 2)) && Math.signum(downX - event.getX()) == 1) {
                    if (!openable) {
                        smoothOpenMenu();
                    }
                } else {
                    smoothCloseMenu();
                    return false;
                }
                break;
        }
        return true;
    }


    private void swipe(int dis) {
        if (Math.signum(dis) != 1) {
            dis = 0;
        } else if (Math.abs(dis) > menu.getWidth()) {
            dis = menu.getWidth();
        }
        content.layout(-dis, content.getTop(), content.getWidth() - dis, getMeasuredHeight());
        menu.layout(content.getWidth() - dis, menu.getTop(), content.getWidth() + menu.getWidth() - dis, menu.getBottom());
    }


    @Override
    public void computeScroll() {
        if (openable) {
            if (openScroller.computeScrollOffset()) {
                swipe(openScroller.getCurrX());
                postInvalidate();
            }
        } else {
            if (closeScroller.computeScrollOffset()) {
                swipe((baseX - closeScroller.getCurrX()));
                postInvalidate();
            }
        }
    }

    public void smoothCloseMenu() {
        openable = false;
        baseX = -content.getLeft();
        closeScroller.startScroll(0, 0, menu.getWidth(), 0, 350);
        postInvalidate();
    }

    public void smoothOpenMenu() {
        openable = true;
        openScroller.startScroll(-content.getLeft(), 0, menu.getWidth(), 0, 350);
        postInvalidate();
    }

    public void closeMenu() {
        if (closeScroller.computeScrollOffset()) {
            closeScroller.abortAnimation();
        }
        if (openable) {
            openable = false;
            swipe(0);
        }
    }

    public View getContent() {
        return content;
    }

    public boolean isOpen() {
        return openable;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
