package com.androidxx.yangjw.httplibrary;

/**
 * Created by yangjw on 2016/8/11.
 */
public interface ICallback {

    /**
     * 请求成功时，回调的接口方法
     * @param result
     */
    void success(String result,int requestCode) ;
}
