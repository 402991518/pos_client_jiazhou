package com.uxun.pos.view.member;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.domain.constant.ConstantMember;
import com.uxun.pos.domain.dto.Member;
import com.uxun.pos.utils.GsonUtils;
import com.uxun.pos.utils.HttpClientUtils;
import com.uxun.pos.view.camera.ZbarScan;
import com.uxun.pos.view.dialog.DialogOK;
import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.root.RootActivity;
import com.uxun.pos.view.utils.KeyBoardService;
import com.uxun.pos.view.widget.CustomButtom;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberQuery extends RootActivity {


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            try {
                if (message.what == 0) {
                    Map<String, Object> content = GsonUtils.getInstance().fromJson(message.obj.toString(), Map.class);
                    int code = (int) ((double) content.get("Statuscode"));
                    if (0 == code) {
                        new DialogOK(MemberQuery.this, "提示", content.get("Message") + "");
                    } else {
                        //1.解析结果
                        List<Map<String, Object>> list = (List<Map<String, Object>>) content.get("data");
                        Map<String, Object> result = list.get(0);
                        Member member = new Member();
                        member.CARDNO = (String) result.get("CARDNO");//卡号
                        member.KJE = (String) result.get("KJE");//卡余额
                        member.KYJF = (String) result.get("KYJF");//可用积分
                        member.STATE = (String) result.get("STATE");//卡状态
                        member.KJB = (String) result.get("KJB");//卡级别
                        member.FDM = (String) result.get("FDM");//所属分店
                        member.DISCOUNT = (String) result.get("DISCOUNT");//享受折扣
                        member.TEMP_CARDNO = (String) content.get("TEMP_CARDNO");//临时会员卡号
                        member.YueLangID = (String) content.get("YueLangID");//业务操作ID
                        //2.返回结果
                        ConstantMember.member_temp = member;
                        Intent intent = new Intent();
                        intent.putExtra("code", 0);
                        setResult(code, intent);
                        finish();
                    }
                } else {
                    new DialogOK(MemberQuery.this, "错误", message.obj + "");
                }
            } catch (Exception e) {
                new DialogOK(MemberQuery.this, "错误", "查询会员卡出错，错误信息：" + e.getMessage());
            } finally {
                running = false;
                msg.setText("请输入或点击扫描输入会员卡号");
                input.setText("");
            }
        }
    };


    protected EditText input;
    protected TextView msg;
    protected CustomButtom scan;
    protected CustomButtom cancel;
    private int code;
    private boolean running = false;//是否正在查询


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new KeyBoardService(this.getWindow());
        this.input.setShowSoftInputOnFocus(false);
        this.code = getIntent().getIntExtra("code", -1);
        scan.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                if (!running) {
                    startActivityForResult(new Intent(MemberQuery.this, ZbarScan.class), 1);
                }
            }
        });
        cancel.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                if (!running) {
                    Intent intent = new Intent();
                    intent.putExtra("code", 1);
                    setResult(code, intent);
                    finish();
                }
            }
        });
    }

    private synchronized void queryMember(String id) {
        if (!running) {
            running = true;
            msg.setText("正在查询会员卡的信息，请稍后...");
            Map<String, Object> wrapper = new HashMap<>(2);
            wrapper.put("cmd_type", "902");
            if (ConstantLogin.loginData.zhaokeyi == null) {
                Message message = handler.obtainMessage();
                message.what = 1;
                message.obj = "后台没有设置招客易会员接口参数,无法查询!";
                message.sendToTarget();
                return;
            }
            if (ConstantLogin.loginData.zhaokeyi.crm_url == null) {
                Message message = handler.obtainMessage();
                message.what = 1;
                message.obj = "后台没有设置易招客会员接口地址，无法查询";
                message.sendToTarget();
                return;
            }

            Map<String, Object> temp = new HashMap<>(4);
            temp.put("MACID", ConstantLogin.loginData.zhaokeyi.macid);
            temp.put("UCARDNO", ConstantLogin.loginData.zhaokeyi.ucardno);
            temp.put("UPASS", ConstantLogin.loginData.zhaokeyi.upass);
            temp.put("CARD_KEY", id);
            Object[] object = new Object[]{temp};
            wrapper.put("data", object);
            final String jsonParams = GsonUtils.getInstance().toJson(wrapper);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = handler.obtainMessage();
                    try {
                        message.what = 0;
                        message.obj = HttpClientUtils.requestHttp(ConstantLogin.loginData.zhaokeyi.crm_url, jsonParams);
                    } catch (Exception e) {
                        message.what = 1;
                        message.obj = "查询会员卡信息出错。" + e.getMessage();
                    } finally {
                        message.sendToTarget();
                    }
                }
            }).start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1) {
            queryMember(data.getStringExtra("message"));
        }
    }

    @Override
    protected void clickEnter() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > ClickListener.MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            String id = this.input.getText().toString();
            if (id != null && !"".equals(id)) {
                queryMember(id);
            }
        }
    }

    private long lastClickTime = 0;


    @Override
    protected void clickEscape() {
    }
}
