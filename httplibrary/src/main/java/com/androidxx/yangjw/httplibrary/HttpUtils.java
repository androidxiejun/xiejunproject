package com.androidxx.yangjw.httplibrary;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangjw on 2016/8/11.
 */
public class HttpUtils {


    private static ExecutorService executorService;

    /*
    入口
     */
    public static HttpThread load(String path) {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(3);
        }
        HttpThread httpThread = new HttpThread();
        httpThread.start(path);
        return httpThread;
    }


   public static class HttpThread {
        private ICallback callback;
       private int requestCode;
       private boolean isPost;
       private String params;
          /*
           出口
            */
        public void callback(ICallback callback,int requestCode) {
            this.callback = callback;
            this.requestCode = requestCode;
        }

       public HttpThread post(Map<String,Object> param) {
           isPost = true;
           this.params = formatParams(param);
           return this;
       }

       private String formatParams(Map<String,Object> param) {
           Set<String> keySet = param.keySet();
           StringBuilder builder = new StringBuilder();
           for(String key:keySet) {
               Object value = param.get(key);
               builder.append(key).append("=").append(value).append("&");
           }
           return builder.toString();

       }

       protected void start(String path) {
//           new Thread(new HttpRunnable(path)).start();
           executorService.execute(new HttpRunnable(path));
       }

        private Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = msg.obj.toString();
                if (callback != null) {
                    callback.success(result,requestCode);
                }
            }
        };

        class HttpRunnable implements Runnable {

            private String path;

            public HttpRunnable(String path) {
                this.path = path;
            }

            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    if (isPost) {
                        urlConnection.setDoOutput(true);
                        urlConnection.setRequestMethod("POST");
                        urlConnection.getOutputStream().write(params.getBytes());
                        urlConnection.getOutputStream().flush();
                    }

                    urlConnection.connect();
                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = urlConnection.getInputStream();
                        int len = 0;
                        byte[] buffer = new byte[1024];
                        StringBuilder builder = new StringBuilder();
                        while((len = inputStream.read(buffer)) != -1) {
                            builder.append(new String(buffer,0,len));
                        }


                        String result = builder.toString();
                        Message message = mHandler.obtainMessage();
                        message.obj = result;
                        message.sendToTarget();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }





}
