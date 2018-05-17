package com.uxun.pos.view.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.uxun.pos.domain.constant.ConstantMember;
import com.uxun.pos.domain.dto.Member;
import com.uxun.pos.view.dialog.DialogYN;
import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.root.RootActivity;
import com.uxun.pos.view.widget.CustomButtom;


public class MemberMain extends RootActivity {

    protected TextView title;//标题
    protected TextView cardno;//卡号
    protected TextView cardValue;//余额
    protected TextView canUseValue;//可用积分
    protected TextView cardLevel;//卡级别
    protected TextView cardState;//卡状态
    protected TextView cardOrgcode;//所属门店
    protected TextView cardRebate;//享受的折扣

    protected CustomButtom back;
    protected CustomButtom cancelMember;

    private int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.code = this.getIntent().getIntExtra("code", -1);
        if (ConstantMember.member == null) {
            this.startActivityForResult(new Intent(this, MemberQuery.class), 1);
        } else {
            showMemberInfo(ConstantMember.member);
            cancelMember.setText("取消会员");
        }
        initEvent();
    }

    private void initEvent() {
        back.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                if ("立即使用".equals(cancelMember.getText())) {
                    DialogYN.Builder builder = new DialogYN.Builder();

                    builder.defaultTitle = "警告！";
                    builder.defaultMessage = "你确定不使用此张会员卡进行积分等操作？";
                    builder.defaultButtonY = "确定使用";
                    builder.defaultButtonN = "确定不使用";
                    builder.clickYesTitle = "提示";
                    builder.clickYesMessage = "你选择了这张信用卡进行积分等操作！";
                    builder.clickYesButton = "确定";
                    builder.clickNoTitle = "提示";
                    builder.clickNoMessage = "你放弃了使用这张会员卡进行积分等操作！";
                    builder.clickNoButton = "确定";

                    new DialogYN(MemberMain.this, builder) {
                        @Override
                        public void clickY() {
                            showAnimateY();
                        }

                        @Override
                        public void clickN() {
                            showAnimateN();
                        }

                        @Override
                        public void clickO(boolean ok) {
                            if (ok) {
                                ConstantMember.member = ConstantMember.member_temp;
                            }
                            complete();
                        }
                    };
                } else {
                    complete();
                }

            }
        });
        cancelMember.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                if ("立即使用".equals(cancelMember.getText())) {
                    ConstantMember.member = ConstantMember.member_temp;
                    complete();
                } else if ("取消会员".equals(cancelMember.getText())) {

                    DialogYN.Builder builder = new DialogYN.Builder();

                    builder.defaultTitle = "警告！";
                    builder.defaultMessage = "你确定要取消使用此会员卡吗？";
                    builder.defaultButtonY = "取消使用";
                    builder.defaultButtonN = "不取消";
                    builder.clickYesTitle = "提示";
                    builder.clickYesMessage = "你已经取消了会员！";
                    builder.clickYesButton = "确定";
                    builder.clickNoTitle = "提示";
                    builder.clickNoMessage = "你放弃了取消操作！";
                    builder.clickNoButton = "确定";

                    new DialogYN(MemberMain.this, builder) {
                        @Override
                        public void clickY() {
                            showAnimateY();
                        }

                        @Override
                        public void clickN() {
                            showAnimateN();
                        }

                        @Override
                        public void clickO(boolean ok) {
                            if (ok) {
                                ConstantMember.member = null;
                                complete();
                            }
                        }
                    };
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1) {
            //如果在商品查询界面点击了取消按钮，则直接返回
            int code = data.getIntExtra("code", -1);
            if (code == 1) {
                complete();
            }
            //搜索到了会员卡
            else if (code == 0) {
                showMemberInfo(ConstantMember.member_temp);
                cancelMember.setText("立即使用");
            }
        }
    }

    private void complete() {
        setResult(code);
        finish();
    }

    private void showMemberInfo(Member member) {
        cardno.setText(member.CARDNO + "");
        cardValue.setText(member.KJE + "");
        canUseValue.setText(member.KYJF + "");
        cardLevel.setText(member.KJB + "");
        cardState.setText(member.STATE + "");
        cardOrgcode.setText(member.FDM + "");
        cardRebate.setText(member.DISCOUNT + "");
    }

    @Override
    protected void clickEnter() {
    }

    @Override
    protected void clickEscape() {
    }
}
