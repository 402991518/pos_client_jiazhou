package com.uxun.pos.view;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.uxun.pos.view.dialog.DialogOK;
import com.uxun.pos.view.dialog.UnionParams;
import com.uxun.pos.view.dialog.UnionQuery;
import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.root.RootActivity;
import com.uxun.pos.view.widget.SuccessView;

public class UnionEntrance extends RootActivity {

    private static final String TYPE_CARD = "01";//银行卡
    private static final String TYPE_SCAN = "02";//扫码

    private static final String TRANS_NAME_1 = "签到";
    private static final String TRANS_NAME_2 = "结算";
    private static final String TRANS_NAME_3 = "打印";
    private static final String TRANS_NAME_4 = "末笔查询";
    private static final String TRANS_NAME_5 = "交易查询";
    private static final String TRANS_NAME_6 = "余额查询";
    private static final String TRANS_NAME_7 = "系统管理";
    private static final String TRANS_NAME_8 = "终端参数";
    private static final String TRANS_NAME_9 = "系统管理员密码";

    private static final int REQUEST_CODE_1 = 1;
    private static final int REQUEST_CODE_2 = 2;
    private static final int REQUEST_CODE_3 = 3;
    private static final int REQUEST_CODE_4 = 4;
    private static final int REQUEST_CODE_5 = 5;
    private static final int REQUEST_CODE_6 = 6;
    private static final int REQUEST_CODE_7 = 7;
    private static final int REQUEST_CODE_8 = 8;
    private static final int REQUEST_CODE_9 = 9;

    private static final class ParamsData {
        int requestCode;
        String transName;

        public ParamsData(String transName, int requestCode) {
            this.transName = transName;
            this.requestCode = requestCode;
        }
    }

    protected View button_1;//签到
    protected View button_2;//结算
    protected View button_3;//打印
    protected View button_4;//末笔查询
    protected View button_5;//交易查询
    protected View button_6;//余额查询
    protected View button_7;//系统管理
    protected View button_8;//终端参数
    protected View button_9;//系统管理员密码

    protected View button_0;//系统管理员密码

    protected LinearLayout container_1;
    protected LinearLayout container_2;

    protected SuccessView successView_1;
    protected SuccessView successView_2;

    private String type = TYPE_CARD;

    private ClickListener clickListener = new ClickListener() {
        @Override
        protected void click(View view) {
            ParamsData paramsData = (ParamsData) view.getTag();
            startActivityForResult(paramsData.transName, paramsData.requestCode);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        container_1.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                successView_1.setVisibility(View.VISIBLE);
                successView_2.setVisibility(View.INVISIBLE);
                successView_1.startAnimate();
                type = TYPE_CARD;
            }
        });
        container_2.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                successView_2.setVisibility(View.VISIBLE);
                successView_1.setVisibility(View.INVISIBLE);
                successView_2.startAnimate();
                type = TYPE_SCAN;
            }
        });
        button_1.setTag(new ParamsData(TRANS_NAME_1, REQUEST_CODE_1));
        button_2.setTag(new ParamsData(TRANS_NAME_2, REQUEST_CODE_2));
        button_3.setTag(new ParamsData(TRANS_NAME_3, REQUEST_CODE_3));
        button_4.setTag(new ParamsData(TRANS_NAME_4, REQUEST_CODE_4));
        button_5.setTag(new ParamsData(TRANS_NAME_5, REQUEST_CODE_5));
        button_6.setTag(new ParamsData(TRANS_NAME_6, REQUEST_CODE_6));
        button_7.setTag(new ParamsData(TRANS_NAME_7, REQUEST_CODE_7));
        button_8.setTag(new ParamsData(TRANS_NAME_8, REQUEST_CODE_8));
        button_9.setTag(new ParamsData(TRANS_NAME_9, REQUEST_CODE_9));


        button_1.setOnClickListener(clickListener);
        button_2.setOnClickListener(clickListener);
        button_3.setOnClickListener(clickListener);
        button_4.setOnClickListener(clickListener);
        button_5.setOnClickListener(clickListener);
        button_6.setOnClickListener(clickListener);
        button_7.setOnClickListener(clickListener);
        button_8.setOnClickListener(clickListener);
        button_9.setOnClickListener(clickListener);

        button_0.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                finish();
            }
        });
    }

    private void startActivityForResult(String transName, int requestCode) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.landicorp.thirdpayservice", "com.landicorp.thirdpayservice.MainActivity"));
            intent.putExtra("type", this.type);
            intent.putExtra("transName", transName);
            startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            new DialogOK(this, "提示", "本机尚未安装第三方接口服务！");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String transName = null;
        switch (requestCode) {
            case REQUEST_CODE_1:
                transName = TRANS_NAME_1;
                break;
            case REQUEST_CODE_2:
                transName = TRANS_NAME_2;
                break;
            case REQUEST_CODE_3:
                transName = TRANS_NAME_3;
                break;
            case REQUEST_CODE_4:
                transName = TRANS_NAME_4;
                break;
            case REQUEST_CODE_5:
                transName = TRANS_NAME_5;
                break;
            case REQUEST_CODE_6:
                transName = TRANS_NAME_6;
                break;
            case REQUEST_CODE_7:
                transName = TRANS_NAME_7;
                break;
            case REQUEST_CODE_8:
                transName = TRANS_NAME_8;
                break;
            case REQUEST_CODE_9:
                transName = TRANS_NAME_9;
                break;
        }
        if (transName != null) {
            try {
                if (resultCode == Activity.RESULT_OK) {
                    if (requestCode == REQUEST_CODE_1 || requestCode == REQUEST_CODE_2 || requestCode == REQUEST_CODE_3 || requestCode == REQUEST_CODE_5 || requestCode == REQUEST_CODE_6 || requestCode == REQUEST_CODE_7) {
                        new DialogOK(this, "提示", transName + "成功");
                    } else {
                        //末笔查询
                        if (requestCode == 4) {
                            new UnionQuery(this, data);
                        }
                        //系统参数
                        else if (requestCode == 8) {
                            new UnionParams(this, data);
                        }
                        //系统管理员密码
                        else if (requestCode == 9) {
                            new DialogOK(this, "系统管理员密码", data.getStringExtra("setPwd"));
                        }
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    new DialogOK(this, transName + "出错", data.getStringExtra("reason"));
                } else {
                    new DialogOK(this, transName + "出错", transName + "接口没有按照约定返回结果！");
                }
            } catch (Exception e) {
                new DialogOK(this, transName + "异常", e.getMessage());
            }
        }
    }


    @Override
    protected void clickEnter() {
    }

    @Override
    protected void clickEscape() {
        finish();
    }


}
