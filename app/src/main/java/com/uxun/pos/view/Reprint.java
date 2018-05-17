package com.uxun.pos.view;


import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allinpay.usdk.core.data.BaseData;
import com.allinpay.usdk.core.data.Busi_Data;
import com.allinpay.usdk.core.data.RequestData;
import com.allinpay.usdk.core.data.ResponseData;
import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.domain.dto.Proc;
import com.uxun.pos.global.NetworkConfig;
import com.uxun.pos.utils.GsonUtils;
import com.uxun.pos.utils.ReprintUtils;
import com.uxun.pos.utils.VolleyUtils;
import com.uxun.pos.view.dialog.DialogOK;
import com.uxun.pos.view.dialog.DialogSaleno;
import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.root.RootActivity;
import com.uxun.pos.view.widget.LoadingView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Reprint extends RootActivity {

    private class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        public class Holder extends RecyclerView.ViewHolder {
            public Holder(View itemView) {
                super(itemView);
            }
        }

        private List<String> dates = new ArrayList<>();

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(Reprint.this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64);
            textView.setLayoutParams(layoutParams);
            return new Holder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            String text = dates.get(position);
            ((TextView) holder.itemView).setText(text);
        }

        @Override
        public int getItemCount() {
            return dates.size();
        }

        public void clear() {
            dates.clear();
        }

        public void add(String text) {
            if (text != null) {
                dates.add(text);
            }
        }

        public List<String> getData() {
            return dates;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            query.setClickable(true);
            print.setClickable(true);
            loadingView.setVisibility(View.GONE);
            try {
                if (msg.what == 0) {
                    Map<String, Object> content = GsonUtils.getInstance().fromJson(msg.obj.toString(), Map.class);
                    String code = (String) content.get("code");
                    if ("1".equals(code) || "2".equals(code)) {
                        Reprint.this.msg.setText("单号:" + saleno + "," + "查询出错:" + content.get("message"));
                    } else if ("0".equals(code)) {
                        Reprint.this.msg.setText("单号:" + saleno + "," + "查询成功");
                        parse(GsonUtils.getInstance().toJson(content.get("data")));
                    }
                } else {
                    Reprint.this.msg.setText("单号:" + saleno + "," + "查询出错:" + msg.obj.toString());
                }
            } catch (Exception e) {
                Reprint.this.msg.setText("单号:" + saleno + "," + "查询出错:" + e.getMessage());
            }
        }
    };

    protected Adapter adapter;
    protected RecyclerView recyclerView;

    protected TextView cancel;
    protected TextView query;
    protected TextView print;

    protected TextView msg;
    protected LoadingView loadingView;

    private String saleno;

    private Proc proc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
        initSaleNo();
    }

    private void initView() {
        adapter = new Adapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initEvent() {
        query.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                new DialogSaleno(Reprint.this, "若重打的销售单为当天的销售单，仅需输入销售单号后5位不为0的数字!（如当天的第1号单，仅需输入1）") {
                    @Override
                    public void processInput(String input) {
                        String saleno;
                        if (input.length() <= 5) {
                            while (input.length() < 5) {
                                input = "0" + input;
                            }
                            String prefix = ConstantLogin.loginData.saleno.substring(0, ConstantLogin.loginData.saleno.length() - 5);
                            saleno = prefix + input;
                        } else {
                            saleno = input;
                        }
                        query(saleno);
                    }
                };
            }
        });
        cancel.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                clickEscape();
            }
        });
        print.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                print();
            }
        });
    }

    //初始化销售单号
    public void initSaleNo() {
        try {
            String saleno = ConstantLogin.loginData.saleno;
            Integer order = Integer.parseInt(saleno.substring(saleno.length() - 5)) - 1;
            if (order > 0) {
                String temp = order + "";
                while (temp.length() < 5) {
                    temp = "0" + temp;
                }
                String lastSaleno = saleno.substring(0, saleno.length() - 5) + temp;
                query(lastSaleno);
            }
        } catch (NumberFormatException e) {

        }
    }

    //查询销售单
    public void query(String saleno) {

        adapter.clear();
        adapter.notifyDataSetChanged();

        //1.验证输入
        if (null == saleno || "".equals(saleno)) {
            msg.setText("销售单号输入错误！");
            return;
        }

        //2.校验日期
        String ymd;
        try {
            ymd = saleno.substring(0, 8);
            if (ymd.length() != 8) {
                throw new RuntimeException();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String temp = dateFormat.format(dateFormat.parse(ymd));
            if (!ymd.equals(temp)) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            msg.setText("销售单号输入错误，请检查输入日期:" + saleno + "!");
            return;
        }

        //3.校验组织机构
        String cur_orgcode = ConstantLogin.loginData.device.OrgCode;
        int len_orgcode = cur_orgcode.length();
        try {
            String orgcode = saleno.substring(8, 8 + len_orgcode);
            if (!cur_orgcode.equals(orgcode)) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            msg.setText("销售单号输入错误，请检查输入的组织机构:" + saleno + "!(当前组织机构:" + cur_orgcode + ")");
            return;
        }

        //4.校验收银机号
        String cur_posno = ConstantLogin.loginData.device.PosNo;
        int len_posno = cur_posno.length();
        try {
            String posno = saleno.substring(8 + len_orgcode, 8 + len_orgcode + len_posno);
            if (!cur_posno.equals(posno)) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            msg.setText("销售单号输入错误，请检查输入的POS机号:" + saleno + "!(当前POS机号:" + cur_posno + ")");
            return;
        }

        //5.校验5位流水号
        try {
            saleno.substring(8 + len_orgcode + len_posno, 8 + len_orgcode + len_posno + 5);
        } catch (Exception e) {
            msg.setText("销售单号输入错误，请检查流水号！");
            return;
        }

        //6.校验5位流水号
        if (saleno.length() > 8 + len_orgcode + len_posno + 5) {
            msg.setText("销售单号输入错误，请检查长度！");
            return;
        }

        this.saleno = saleno;
        msg.setText("正在查询销售单信息，请稍后...");
        loadingView.setVisibility(View.VISIBLE);
        query.setClickable(false);
        print.setClickable(false);
        VolleyUtils.post(NetworkConfig.getUrl() + "proc/query", handler, "saleno", saleno);
    }

    //解析结果
    private void parse(String json) {
        proc = GsonUtils.getInstance().fromJson(json, Proc.class);
        if (!ConstantLogin.loginData.device.PosNo.equals(proc.h.PosNo)) {
            new DialogOK(this, "错误", "该单非本机销售！");
            return;
        }
        //头
        ReprintUtils.Bean_H bean_h = new ReprintUtils.Bean_H(proc.h.SaleNo, proc.h.OldSaleNo, proc.h.BeginDate, proc.h.Amount, proc.h.RealAmt);
        //商品明细
        List<ReprintUtils.Bean_D> bean_ds = new ArrayList<>();
        if (proc.ds != null) {
            for (int i = 0; i < proc.ds.size(); i++) {
                Proc.D d = proc.ds.get(i);
                bean_ds.add(new ReprintUtils.Bean_D(d.BarCode, d.itemname, d.ItemQty, d.RealPrice, d.RealAmount));
            }
        }
        //付款明细
        List<ReprintUtils.Bean_P> bean_ps = new ArrayList<>();
        if (proc.ps != null) {
            for (int i = 0; i < proc.ps.size(); i++) {
                Proc.P p = proc.ps.get(i);
                ReprintUtils.Bean_P bean_p = new ReprintUtils.Bean_P(p.Amount, p.PayDescr);
                //第三方支付的信息
                if ("13".equals(p.PayType) || "14".equals(p.PayType) || "03".equals(p.PayType) || "18".equals(p.PayType)) {
                    bean_p.ExtCol1 = p.ExtCol1;
                    bean_p.ExtCol2 = p.ExtCol2;
                    bean_p.ExtCol3 = p.ExtCol3;
                    bean_p.ExtCol4 = p.ExtCol4;
                    bean_p.ExtCol5 = p.ExtCol5;
                    bean_p.ExtCol6 = p.ExtCol6;
                }
                bean_ps.add(bean_p);
            }
        }

        ArrayList<String> printLines = ReprintUtils.print(bean_h, bean_ds, bean_ps, proc.h.ExtCol10, proc.h.PosNo, proc.h.Cashier, proc.h.OrgCode);
        for (int i = 0; i < printLines.size(); i++) {
            adapter.add(printLines.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    //打印
    private void print() {
        List<String> list = adapter.getData();
        if (list.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(list.get(i) + "\r\n");
            }
            try {
                msg.setText("正在重新打印销售小票，请稍后...");
                loadingView.setVisibility(View.VISIBLE);
                query.setClickable(false);
                print.setClickable(false);

                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.allinpay.usdk", "com.allinpay.usdk.MainActivity"));
                RequestData data = new RequestData();
                data.putValue(BaseData.BUSINESS_ID, Busi_Data.BUSI_MANAGER_SERVICE_PRINT);
                data.putValue("PRINT_VERSION", 0);
                data.putValue(BaseData.PRINT_APPEND_PAGE, 1);
                data.putValue("PRINT_APPEND_BOLD", false);
                data.putValue("PRINT_APPEND_SIZE", 32);
                data.putValue("PRINT_APPEND_LOCATE", 0);
                data.putValue("PRINT_APPEND_TEXT", stringBuilder.toString());
                Bundle bundle = new Bundle();
                bundle.putSerializable(RequestData.KEY_ERTRAS, data);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            } catch (Exception e) {
                msg.setText("本机尚未安装USDK,无法打印！");
                loadingView.setVisibility(View.GONE);
                query.setClickable(true);
                print.setClickable(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            loadingView.setVisibility(View.GONE);
            query.setClickable(true);
            print.setClickable(true);
            if (data != null) {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (resultCode == 0 && extras != null && "00".equals(((ResponseData) extras.getSerializable(ResponseData.KEY_ERTRAS)).getValue(BaseData.REJCODE))) {
                        msg.setText("打印成功");
                        finish();
                    } else {
                        msg.setText("打印出错");
                    }
                } else {
                    msg.setText("打印出错");
                }
            } else {
                msg.setText("打印出错");
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
