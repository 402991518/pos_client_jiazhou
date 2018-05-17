package com.uxun.pos.utils;

import android.os.Handler;
import android.os.Message;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uxun.pos.global.Application;

import java.util.HashMap;
import java.util.Map;


//负责网络请求
public class VolleyUtils {

    private static RequestQueue requestQueue;

    static {
        requestQueue = Volley.newRequestQueue(Application.getContext());
    }

    public static void post(String url, final Handler handler, final String... params) {

        StringRequest stringRequest = new StringRequest(Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Message messge = handler.obtainMessage();
                messge.what = 0;
                messge.obj = response;
                messge.sendToTarget();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message messge = handler.obtainMessage();
                messge.what = 1;
                messge.obj = "无法连接至RPC服务器，请检网络设置";
                messge.sendToTarget();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                if (params != null) {
                    for (int i = 0; i < params.length - 1; ) {
                        String key = params[i++];
                        String val = params[i++];
                        if (key != null) {
                            map.put(key, val);
                        }
                    }
                }
                return map;
            }
        };
        RetryPolicy retryPolicy = new DefaultRetryPolicy(60 * 1000, 0, 0);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }
}
