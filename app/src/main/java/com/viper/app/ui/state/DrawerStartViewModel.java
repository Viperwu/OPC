package com.viper.app.ui.state;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.viper.app.data.bean.OPCSetting;
import com.viper.app.data.bean.PageSetting;

import java.util.List;

public class DrawerStartViewModel extends ViewModel {

    public final MutableLiveData<List<OPCSetting>> opcSettings = new MutableLiveData<>();
    public final MutableLiveData<List<PageSetting>> pageSettings = new MutableLiveData<>();
}
