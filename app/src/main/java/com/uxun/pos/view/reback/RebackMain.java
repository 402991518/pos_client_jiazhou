package com.uxun.pos.view.reback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.domain.dto.Proc;
import com.uxun.pos.global.NetworkConfig;
import com.uxun.pos.utils.GsonUtils;
import com.uxun.pos.view.dialog.DialogNet;
import com.uxun.pos.view.dialog.DialogOK;
import com.uxun.pos.view.dialog.DialogSaleno;
import com.uxun.pos.view.dialog.Toast;
import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.root.RootActivity;
import com.uxun.pos.view.utils.NetParserUtils;
import com.uxun.pos.view.widget.Order;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RebackMain extends RootActivity {

    //填充数据
    public class Ware {
        public String itemname;
        public String realPrice;
        public String quantity;
        public String subRealPrice;

        public Ware(String itemname, String realPrice, String quantity, String subRealPrice) {
            this.itemname = itemname;
            this.realPrice = realPrice;
            this.quantity = quantity;
            this.subRealPrice = subRealPrice;
        }
    }

    public class Pay {
        public String name;
        public String amount;

        public Pay(String name, String amount) {
            this.name = name;
            this.amount = amount;
        }
    }

    //适配器
    public class WareAdapter extends RecyclerView.Adapter<WareAdapter.ViewHolder> {
        public class ViewHolder extends RecyclerView.ViewHolder {
            Order rowno;
            TextView itemname;
            TextView realPrice;
            TextView quantity;
            TextView subRealPrice;

            public ViewHolder(View itemView) {
                super(itemView);
                rowno = itemView.findViewById(R.id.rowno);
                itemname = itemView.findViewById(R.id.itemname);
                realPrice = itemView.findViewById(R.id.realPrice);
                quantity = itemView.findViewById(R.id.quantity);
                subRealPrice = itemView.findViewById(R.id.subRealPrice);
            }
        }

        private Context context;

        private List<Ware> list = new ArrayList<>();

        public WareAdapter(Context context) {
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.reback_main_item_ware, parent, false);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, parent.getHeight() / 3);
            view.setLayoutParams(layoutParams);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Ware ware = list.get(position);
            holder.rowno.setText((position + 1) + "");
            holder.itemname.setText(ware.itemname);
            holder.realPrice.setText(ware.realPrice);
            holder.quantity.setText(ware.quantity);
            holder.subRealPrice.setText(ware.subRealPrice);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setList(List<Ware> list) {
            this.list.clear();
            if (list != null) {
                this.list.addAll(list);
            }
            this.notifyDataSetChanged();
        }
    }

    public class PayAdapter extends RecyclerView.Adapter<PayAdapter.ViewHolder> {
        public class ViewHolder extends RecyclerView.ViewHolder {
            Order rowno;
            TextView name;
            TextView amount;

            public ViewHolder(View itemView) {
                super(itemView);
                rowno = itemView.findViewById(R.id.rowno);
                name = itemView.findViewById(R.id.name);
                amount = itemView.findViewById(R.id.amount);
            }
        }

        private Context context;

        private List<Pay> list = new ArrayList<>();

        public PayAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.reback_main_item_pay, parent, false);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, parent.getHeight() / 3);
            view.setLayoutParams(layoutParams);
            return new PayAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Pay pay = list.get(position);
            holder.rowno.setText((position + 1) + "");
            holder.name.setText(pay.name);
            holder.amount.setText(pay.amount);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setList(List<Pay> list) {
            this.list.clear();
            if (list != null) {
                this.list.addAll(list);
            }
            this.notifyDataSetChanged();
        }

    }

    protected RecyclerView listPay;
    protected RecyclerView listWare;

    protected TextView inputSaleno;
    protected TextView cancelReback;
    protected TextView confirmReback;

    private Proc proc;
    private PayAdapter payAdapter;
    private WareAdapter wareAdapter;

    private String returnMan;//退货人

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        returnMan = this.getIntent().getStringExtra("returnMan");
        Log.i("RebackMain", "returnMan=" + returnMan);
        initView();
        initEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                this.finish();
            }
        }
    }

    private void initView() {
        payAdapter = new PayAdapter(this);
        wareAdapter = new WareAdapter(this);
        RecyclerView.LayoutManager layoutManager_pay = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager_ware = new LinearLayoutManager(this);
        listPay.setLayoutManager(layoutManager_pay);
        listWare.setLayoutManager(layoutManager_ware);
        listPay.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        listWare.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        listPay.setAdapter(payAdapter);
        listWare.setAdapter(wareAdapter);
    }

    private void initEvent() {
        inputSaleno.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                new DialogSaleno(RebackMain.this, "注意，仅能退当天销售单，请输入单号后5位（如00102,只需输入102即可）！") {
                    @Override
                    public void processInput(String input) {
                        if (input != null && !"".equals(input) && input.length() <= 5) {
                            if (input.length() < 5) {
                                while (input.length() < 5) {
                                    input = "0" + input;
                                }
                            }
                            String saleno = ConstantLogin.loginData.saleno.substring(0, ConstantLogin.loginData.saleno.length() - 5) + input;
                            query(saleno);
                        } else {
                            Toast.makeText(getContext(), "销售单号输入错误！", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
            }
        });

        cancelReback.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                clickEscape();
            }
        });
        confirmReback.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                reback();
            }
        });
    }

    @Override
    protected void clickEnter() {
    }

    @Override
    protected void clickEscape() {
        finish();
    }

    //查询销售单号
    public void query(String saleno) {
        payAdapter.setList(null);
        wareAdapter.setList(null);
        this.proc = null;
        new DialogNet(this, "正在查询销售信息，请稍后...", NetworkConfig.getUrl() + "proc/reback", "saleno", saleno) {
            @Override
            public void onNetRequestResult(Message message) {
                NetParserUtils.parseReponse(getContext(), message, new NetParserUtils.ResponseParser() {
                    @Override
                    public void parse(String json) {
                        parseResult(json);
                    }
                });
            }
        };
    }

    //解析结果
    private void parseResult(String json) {
        proc = GsonUtils.getInstance().fromJson(json, Proc.class);
        if (proc != null) {
            List<Pay> pays = new ArrayList<>();
            List<Ware> wares = new ArrayList<>();
            if (proc.ds != null) {
                for (int i = 0; i < proc.ds.size(); i++) {
                    Proc.D d = proc.ds.get(i);
                    wares.add(new Ware(d.itemname + "\n" + d.BarCode, d.RealPrice.stripTrailingZeros().toPlainString(), d.ItemQty.stripTrailingZeros().toPlainString(), d.RealAmount.stripTrailingZeros().toPlainString()));
                }
            }
            if (proc.ps != null) {
                for (int i = 0; i < proc.ps.size(); i++) {
                    Proc.P p = proc.ps.get(i);
                    pays.add(new Pay(p.PayDescr, p.Amount.stripTrailingZeros().toPlainString()));
                }
            }
            payAdapter.setList(pays);
            wareAdapter.setList(wares);
        }
    }

    //退货
    private void reback() {
        if (!new SimpleDateFormat("yyyyMMdd").format(new Date()).equals(ConstantLogin.loginData.saleno.substring(0, 8))) {
            new DialogOK(this, "错误", "当前时间与销售流水号不一致，请退出系统，重新登录！");
            return;
        }
        if (proc != null) {
            if (proc.h.OldSaleNo != null) {
                new DialogOK(this, "提示", "本单已经是退货单，不允许退货");
                return;
            }
            List<Proc.D> ds = proc.ds;
            if (ds != null && ds.size() > 0) {
                for (int i = 0; i < proc.ds.size(); i++) {
                    Proc.D d = proc.ds.get(i);
                    if (d.ItemQty.compareTo(new BigDecimal(0)) == 0) {
                        new DialogOK(this, "提示", "本单已经退货,不允许再次退货");
                        return;
                    }
                    if (d.SaleQty.compareTo(d.ItemQty) != 0) {
                        new DialogOK(this, "提示", "第" + (i + 1) + "行商品可退数量与销售数量不相等，不可以退货");
                        return;
                    }
                }

                String json = GsonUtils.getInstance().toJson(proc);
                Intent intent = new Intent(RebackMain.this, RebackPay.class);
                intent.putExtra("json", json);
                intent.putExtra("returnMan", returnMan);
                startActivityForResult(intent, 1);
            }
        }
    }
}
