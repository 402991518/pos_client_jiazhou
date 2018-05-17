package com.uxun.pos.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class HttpClientUtils {


    public static class CustomX509TrustManager implements X509TrustManager {
        // 该方法检查客户端的证书，若不信任该证书则抛出异常。由于我们不需要对客户端进行认证，因此我们只需要执行默认的信任管理器的这个方法
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        // 该方法检查服务器的证书，若不信任该证书同样抛出异常。通过自己实现该方法，可以使之信任我们指定的任何证书。在实现该方法时，也可以简单的不做任何处理，即一个空的函数体，由于不会抛出异常，它就会信任任何证书。
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        // 返回受信任的X509证书数组。
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }


    @Deprecated
    public static String requestHttpForbiden(String requestUrl, String jsonParams) {
        OutputStream outputStream = null;
        BufferedInputStream bufferedInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        jsonParams = (jsonParams == null ? "" : jsonParams);
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            // 设置参数
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            // 设置请求属性
            httpURLConnection.setRequestProperty("Charset", "utf-8");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            // 连接
            outputStream = httpURLConnection.getOutputStream();
            outputStream.write(jsonParams.getBytes("utf-8"));
            outputStream.flush();
            // 获取响应
            int responseCode = httpURLConnection.getResponseCode();
            InputStream inputStream;
            if (HttpURLConnection.HTTP_OK == responseCode || HttpURLConnection.HTTP_CREATED == responseCode || HttpURLConnection.HTTP_ACCEPTED == responseCode) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }
            //获取结果
            bufferedInputStream = new BufferedInputStream(inputStream);
            byteArrayOutputStream = new ByteArrayOutputStream();
            int len;
            byte[] byteArray = new byte[1024];
            while ((len = bufferedInputStream.read(byteArray)) != -1) {
                byteArrayOutputStream.write(byteArray, 0, len);
                byteArrayOutputStream.flush();
            }
            return byteArrayOutputStream.toString("utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (outputStream != null) {
                try {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                    }
                } finally {
                    if (bufferedInputStream != null) {
                        try {
                            bufferedInputStream.close();
                        } catch (IOException e) {
                        } finally {
                            if (byteArrayOutputStream != null) {
                                try {
                                    byteArrayOutputStream.close();
                                } catch (IOException e) {
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public static String requestHttp(String requestUrl, String jsonParams) {
        // 1. 信任管理器初始化
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            TrustManager[] trustManagers = {new CustomX509TrustManager()};
            SecureRandom secureRandom = new SecureRandom();
            sslContext.init(null, trustManagers, secureRandom);
        } catch (Exception e) {
            throw new RuntimeException("https连接器信任管理器初始化异常", e);
        }
        // 2. 创建HTTPS连接
        HttpURLConnection httpURLConnection;
        try {
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            // 设置参数
            httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(sslSocketFactory);
            }
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            // 设置请求属性
            httpURLConnection.setRequestProperty("Charset", "utf-8");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
        } catch (Exception e) {
            Log.i("HttpClientUtils", "创建HTTPS连接异常", e);
            throw new RuntimeException("创建HTTPS连接异常", e);
        }
        // 3.提交参数
        jsonParams = (jsonParams == null ? "" : jsonParams);
        OutputStream outputStream = null;
        try {
            httpURLConnection.connect();
            outputStream = httpURLConnection.getOutputStream();
            outputStream.write(jsonParams.getBytes("UTF-8"));
            outputStream.flush();
        } catch (Exception e) {
            throw new RuntimeException("提交请求参数异常", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }
        }

        // 4.获取结果
        BufferedInputStream bufferedInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            int responseCode = httpURLConnection.getResponseCode();
            InputStream inputStream;
            if (HttpURLConnection.HTTP_OK == responseCode || HttpURLConnection.HTTP_CREATED == responseCode || HttpURLConnection.HTTP_ACCEPTED == responseCode) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }
            bufferedInputStream = new BufferedInputStream(inputStream);
            byteArrayOutputStream = new ByteArrayOutputStream();
            int len;
            byte[] byteArray = new byte[1024];
            while ((len = bufferedInputStream.read(byteArray)) != -1) {
                byteArrayOutputStream.write(byteArray, 0, len);
                byteArrayOutputStream.flush();
            }
            return byteArrayOutputStream.toString("utf-8");
        } catch (Exception e) {
            throw new RuntimeException("获取请求结果异常", e);
        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                } finally {
                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }
    }

}
