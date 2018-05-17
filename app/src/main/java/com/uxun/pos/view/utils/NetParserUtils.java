package com.uxun.pos.view.utils;

import android.content.Context;
import android.os.Message;

import com.uxun.pos.utils.GsonUtils;
import com.uxun.pos.view.dialog.DialogOK;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/8.
 */

//解析网络结果
public class NetParserUtils {

    //具体的结果处理器材
    public interface ResponseParser {
        void parse(String json);
    }

    public static void parseReponse(Context context, Message msg, ResponseParser parser) {
        try {
            if (msg.what == 0) {
                Map<String, Object> content = GsonUtils.getInstance().fromJson(msg.obj.toString(), Map.class);
                String code = (String) content.get("code");
                if ("1".equals(code)) {
                    new DialogOK(context, "提示", (String) content.get("message"));
                } else if ("2".equals(code)) {
                    new DialogOK(context, "RPC服务出错", "错误信息：" + content.get("message"));
                } else if ("0".equals(code)) {
                    parser.parse(GsonUtils.getInstance().toJson(content.get("data")));
                }
            } else {
                new DialogOK(context, "提示", msg.obj.toString());
            }
        } catch (Exception e) {
            new DialogOK(context, "提示", "应用程序出错！" + e.getMessage());
        }
    }

}
