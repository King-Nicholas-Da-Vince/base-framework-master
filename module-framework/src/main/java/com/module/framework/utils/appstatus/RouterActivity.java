package com.module.framework.utils.appstatus;

import android.net.Uri;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;

public class RouterActivity {

    private volatile static RouterActivity instance = null;

    private RouterActivity() {
    }


    /**
     * Get instance of router. A
     * All feature U use, will be starts here.
     */
    public static RouterActivity getInstance() {
        if (instance == null) {
            synchronized (ARouter.class) {
                if (instance == null) {
                    instance = new RouterActivity();
                }
            }
        }
        return instance;
    }

    public Postcard path(String path){
        return ARouter.getInstance().build(path);
    }
    public Postcard uri(Uri uri){
        return ARouter.getInstance().build(uri);
    }

}
