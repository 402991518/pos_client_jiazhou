package com.uxun.pos.view;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uxun.pos.domain.bo.Ware;
import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.domain.constant.ConstantMember;
import com.uxun.pos.domain.constant.ConstantWareAdapter;
import com.uxun.pos.domain.dto.Goods;
import com.uxun.pos.domain.dto.User;
import com.uxun.pos.global.NetworkConfig;
import com.uxun.pos.utils.GsonUtils;
import com.uxun.pos.view.adapter.WareAdapter;
import com.uxun.pos.view.dialog.CartInfo;
import com.uxun.pos.view.dialog.DialogAuth;
import com.uxun.pos.view.dialog.DialogExit;
import com.uxun.pos.view.dialog.DialogNet;
import com.uxun.pos.view.dialog.DialogOK;
import com.uxun.pos.view.dialog.DialogYN;
import com.uxun.pos.view.dialog.InputPrice;
import com.uxun.pos.view.dialog.MainMore;
import com.uxun.pos.view.dialog.ModifyPassword;
import com.uxun.pos.view.dialog.PosInfo;
import com.uxun.pos.view.dialog.Quantity;
import com.uxun.pos.view.dialog.Rebate;
import com.uxun.pos.view.dialog.RebateAll;
import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.member.MemberMain;
import com.uxun.pos.view.reback.RebackMain;
import com.uxun.pos.view.root.RootActivity;
import com.uxun.pos.view.utils.NetParserUtils;
import com.uxun.pos.view.widget.CustomButtom;
import com.uxun.pos.view.widget.listview.ScrollerLayout;
import com.uxun.pos.view.widget.listview.ScrollerListView;
import com.uxun.pos.view.widget.listview.ScrollerMenu;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends RootActivity {

    protected CustomButtom total;
    protected LinearLayout more;
    protected View memberTip;
    protected TextView member;
    protected CustomButtom settle;
    protected TextView grdname;

    protected ScrollerListView scrollerListView;
    private WareAdapter wareAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initView();
        this.initEvent();
    }

    private void initView() {
        grdname.setText(ConstantLogin.loginData.device.Descr + "");
        wareAdapter = new WareAdapter(scrollerListView);
    }

    private void initEvent() {
        grdname.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                new PosInfo(Main.this);
            }
        });
        total.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                new CartInfo(Main.this, wareAdapter);
            }
        });
        member.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                startActivityForResult(new Intent(Main.this, MemberMain.class), 1);
            }
        });
        scrollerListView.addScrollerMenuBuilders(
                new ScrollerMenu.Builder("数量", Color.BLACK, Color.rgb(244, 68, 68), 200, new ScrollerMenu.OnMenuClickListener() {
                    @Override
                    public void onClick() {
                        setQuantity();
                    }
                }),
                new ScrollerMenu.Builder("删除", Color.BLACK, Color.rgb(255, 0, 0), 200, new ScrollerMenu.OnMenuClickListener() {
                    @Override
                    public void onClick() {
                        delWare();
                    }
                })
        );
        more.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                new MainMore(Main.this) {
                    @Override
                    public void onMoreButtonClick(String text) {
                        switch (text) {
                            case key_1:
                                rebate();
                                break;
                            case key_2:
                                rebateAll();
                                break;
                            case key_3:
                                delWare();
                                break;
                            case key_4:
                                delAll();
                                break;
                            case key_5:
                                reprint();
                                break;
                            case key_6:
                                unionEntrance();
                                break;
                            case key_7:
                                lock();
                                break;
                            case key_8:
                                exit();
                                break;
                            case key_9:
                                modify();
                                break;
                            case key_0:
                                reback();
                                break;
                        }
                    }
                };
            }
        });
        settle.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                if (wareAdapter.getCount() > 0) {
                    ConstantWareAdapter.wareAdapter = wareAdapter;
                    startActivityForResult(new Intent(Main.this, Settle.class), 2);
                }
            }
        });
    }

    //修改数量
    public void setQuantity() {
        if (wareAdapter.get() != null) {
            new Quantity(Main.this, wareAdapter.get()) {
                @Override
                public void onDialogResult(BigDecimal quatity) {
                    wareAdapter.get().setQuantity(quatity);
                    wareAdapter.update();
                    total.setText(wareAdapter.getTotalReal().toString());
                }

                @Override
                public void onDialogDissmiss() {
                    //关闭条目
                    ScrollerLayout item = (ScrollerLayout) scrollerListView.getChildAt(wareAdapter.getCursor());
                    item.smoothCloseMenu();
                }
            };
        }
    }

    //添加商品
    private void addWare(final Goods goods) {
        if (!new SimpleDateFormat("yyyyMMdd").format(new Date()).equals(ConstantLogin.loginData.saleno.substring(0, 8))) {
            new DialogOK(this, "警告", "当前时间与销售流水号不一致，请退出系统，重新登录！");
        } else {
            new InputPrice(Main.this, goods) {
                @Override
                public void processGoods(BigDecimal salePrice, BigDecimal realPrice) {
                    goods.SALEPRICE = salePrice;
                    Ware ware = new Ware(goods, new BigDecimal(1));
                    ware.setRealPrice(realPrice);
                    wareAdapter.add(ware);
                    total.setText(wareAdapter.getTotalReal().toString());
                    dismiss();
                }
            };
        }
    }

    //删除商品
    private void delWare() {
        if (wareAdapter.get() != null) {
            if (new Byte("1").equals(ConstantLogin.loginData.user.Cancel)) {
                realDelWare();
            } else {
                new DialogAuth(this, "请输入用户名和密码检查单品取消权限") {
                    @Override
                    public void processAuth(User user) {
                        if (new Byte("1").equals(user.Reprint)) {
                            realDelWare();
                        } else {
                            new DialogOK(Main.this, "提示", "用户:" + user.UserId + "没有单品取消的权限！");
                        }
                    }
                };
            }
        }
    }

    private void realDelWare() {
        new DialogYN(Main.this, new DialogYN.Builder()) {
            @Override
            public void clickY() {
                showAnimateY();
                wareAdapter.del();
                total.setText(wareAdapter.getTotalReal().toString());
            }

            @Override
            public void clickN() {
                showAnimateN();
            }

            @Override
            public void clickO(boolean ok) {
                if (!ok) {
                    ScrollerLayout item = (ScrollerLayout) scrollerListView.getChildAt(wareAdapter.getCursor());
                    item.smoothCloseMenu();
                }
                dismiss();
                if (wareAdapter.getCount() <= 0) {
                    cancelMember();
                }
            }
        };
    }

    //整单删除
    private void delAll() {
        if (wareAdapter.getCount() > 0) {
            if (new Byte("1").equals(ConstantLogin.loginData.user.Cancel)) {
                realDelAll();
            } else {
                new DialogAuth(this, "请输入用户名和密码检查整单取消权限") {
                    @Override
                    public void processAuth(User user) {
                        if (new Byte("1").equals(user.Reprint)) {
                            realDelAll();
                        } else {
                            new DialogOK(Main.this, "提示", "用户:" + user.UserId + "没有整单取消的权限！");
                        }
                    }
                };
            }
        }
    }

    private void realDelAll() {
        DialogYN.Builder builder = new DialogYN.Builder();
        builder.defaultTitle = "警告！";
        builder.defaultMessage = "你确定要删除整单商品吗？";
        builder.defaultButtonY = "确定删除";
        builder.defaultButtonN = "不删除";
        builder.clickYesTitle = "提示";
        builder.clickYesMessage = "你已经删除了整单商品！";
        builder.clickYesButton = "确定";
        builder.clickNoTitle = "提示";
        builder.clickNoMessage = "你取消了整单删除的操作！";
        builder.clickNoButton = "确定";
        new DialogYN(this, builder) {
            @Override
            public void clickY() {
                showAnimateY();
                while (wareAdapter.get() != null) {
                    wareAdapter.del();
                }
                total.setText(wareAdapter.getTotalReal().toString());
            }

            @Override
            public void clickN() {
                showAnimateN();
            }

            @Override
            public void clickO(boolean ok) {
                if (ok) {
                    dismiss();
                    cancelMember();
                }
            }
        };
    }

    //单品折扣
    public void rebate() {
        if (wareAdapter.get() != null) {
            if (new Byte("1").equals(ConstantLogin.loginData.user.Discount)) {
                realRebate();
            } else {
                new DialogAuth(this, "请输入用户名和密码检打折权限") {
                    @Override
                    public void processAuth(User user) {
                        if (new Byte("1").equals(user.Reprint)) {
                            realRebate();
                        } else {
                            new DialogOK(Main.this, "提示", "用户:" + user.UserId + "没有打折权限！");
                        }
                    }
                };
            }
        }
    }

    private void realRebate() {
        new Rebate(this, wareAdapter.get()) {
            @Override
            public void OnRebateResult() {
                wareAdapter.update();
                total.setText(wareAdapter.getTotalReal().toString());
            }
        };

    }

    //整单折扣
    private void rebateAll() {
        if (wareAdapter.getCount() > 0) {
            if (new Byte("1").equals(ConstantLogin.loginData.user.Discount)) {
                realRebateAll();
            } else {
                new DialogAuth(this, "请输入用户名和密码检打折权限") {
                    @Override
                    public void processAuth(User user) {
                        if (new Byte("1").equals(user.Reprint)) {
                            realRebateAll();
                        } else {
                            new DialogOK(Main.this, "提示", "用户:" + user.UserId + "没有打折权限！");
                        }
                    }
                };
            }
        }
    }

    private void realRebateAll() {
        new RebateAll(this, wareAdapter) {
            @Override
            public void OnRebateResult() {
                wareAdapter.update();
                total.setText(wareAdapter.getTotalReal().toString());
            }
        };
    }

    //取消会员
    private void cancelMember() {
        if (ConstantMember.member != null) {
            DialogYN.Builder builder = new DialogYN.Builder();
            builder.defaultTitle = "提示！";
            builder.defaultMessage = "购物车已经不存在商品,是否取消会员？";
            builder.defaultButtonY = "取消会员";
            builder.defaultButtonN = "不取消";
            builder.clickYesTitle = "提示";
            builder.clickYesMessage = "你已经取消了会员！";
            builder.clickYesButton = "确定";
            builder.clickNoTitle = "提示";
            builder.clickNoMessage = "顾客可继续使用此会员卡进行积分！";
            builder.clickNoButton = "确定";
            new DialogYN(this, builder) {
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
                        memberTip.setVisibility(View.GONE);
                    }
                }
            };
        }
    }

    private void unionEntrance() {
        startActivity(new Intent(this, UnionEntrance.class));
    }

    //锁屏
    private void lock() {
        startActivity(new Intent(this, Lock.class));
    }

    //修改密码
    private void modify() {
        if (wareAdapter.getCount() == 0) {
            new ModifyPassword(this);
        }
    }

    //重打小票
    private void reprint() {
        if (wareAdapter.getCount() == 0) {
            if (new Byte("1").equals(ConstantLogin.loginData.user.Reprint)) {
                startActivity(new Intent(this, Reprint.class));
            } else {
                //验证权限
                Intent intent = new Intent(this, Auth.class);
                intent.putExtra("tip", "请输入用户名和密码检查重打小票权限");
                this.startActivityForResult(intent, 3);
            }
        } else {
            new DialogOK(this, "警告", "购物车已存在商品，不允许重打小票！");
        }
    }

    //退货
    private void reback() {
        if (wareAdapter.getCount() == 0) {
            if (new Byte("1").equals(ConstantLogin.loginData.user.Returned)) {
                Intent intent = new Intent(this, RebackMain.class);
                intent.putExtra("returnMan", ConstantLogin.loginData.user.UserId);
                startActivity(intent);
            } else {
                //验证权限
                Intent intent = new Intent(this, Auth.class);
                intent.putExtra("tip", "请输入用户名和密码检查退货权限！");
                this.startActivityForResult(intent, 4);
            }

        } else {
            new DialogOK(this, "警告", "购物车已存在商品，不允许退货！");
        }
    }

    //退出系统
    private void exit() {
        if (wareAdapter.getCount() > 0) {
            new DialogOK(this, "警告", "购物车中存在商品，你不能退出！");
        } else {
            if (new Byte((byte) 1).equals(ConstantLogin.loginData.device.LogoutCheck)) {
                new DialogExit(this) {
                    @Override
                    public void result() {
                        finish();
                    }
                };
            } else {
                finish();
            }
        }
    }

    //查找商品
    public void searchGoods(String itemno) {
        new DialogNet(this, "正在查询商品信息，请稍后...", NetworkConfig.getUrl() + "ware/list", "orgCode", ConstantLogin.loginData.device.OrgCode, "goodsCode", itemno) {
            @Override
            public void onNetRequestResult(Message message) {
                NetParserUtils.parseReponse(Main.this, message, new NetParserUtils.ResponseParser() {
                    @Override
                    public void parse(String json) {
                        addWare(GsonUtils.getInstance().fromJson(json, Goods.class));
                    }
                });
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //会员查询后返回
        if (requestCode == 1 && requestCode == 1) {
            if (ConstantMember.member != null) {
                this.memberTip.setVisibility(View.VISIBLE);
            } else {
                this.memberTip.setVisibility(View.GONE);
            }
        }
        //付款完成后返回
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                ConstantMember.member = null;
                memberTip.setVisibility(View.GONE);
                while (wareAdapter.get() != null) {
                    wareAdapter.del();
                }
                total.setText(wareAdapter.getTotalReal().toString());
            }
        }

        //查询重打小票权限后返回
        if (requestCode == 3 && resultCode == 3) {
            String json = data.getStringExtra("json");
            User user = GsonUtils.getInstance().fromJson(json, User.class);
            if (user == null) {
                new DialogOK(Main.this, "提示", "没有查询到用户");
                return;
            }
            if (new Byte("1").equals(user.Reprint)) {
                startActivity(new Intent(this, Reprint.class));
            } else {
                new DialogOK(Main.this, "提示", "用户:" + user.UserId + "没有重打小票的权限！");
            }
        }

        //检查退货权限
        if (requestCode == 4 && resultCode == 4) {
            String json = data.getStringExtra("json");
            User user = GsonUtils.getInstance().fromJson(json, User.class);
            if (user == null) {
                new DialogOK(Main.this, "提示", "没有查询到用户");
                return;
            }
            if (new Byte("1").equals(user.Returned)) {
                Intent intent = new Intent(this, RebackMain.class);
                intent.putExtra("returnMan", user.UserId);
                startActivity(intent);
            } else {
                new DialogOK(Main.this, "提示", "用户:" + user.UserId + "没有退货的权限！");
            }
        }
    }

    @Override
    protected void clickEnter() {
    }

    @Override
    protected void clickEscape() {
    }
}
