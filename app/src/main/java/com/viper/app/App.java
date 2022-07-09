package com.viper.app;

import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.viper.app.data.client.ClientManger;
import com.viper.app.data.repository.RepositoryManger;
import com.viper.base.BaseApplication;

import com.viper.base.utils.Utils;
import com.viper.app.util.U;



public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        U.init(this);
        QMUISwipeBackActivityManager.init(this);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        ClientManger.destroy();
        RepositoryManger.destroy();
        U.destroy();
        System.gc();
        System.runFinalization();
    }

}
