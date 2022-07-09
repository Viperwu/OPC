package com.viper.app.ui.state;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    public final ObservableBoolean isDrawerOpened = new ObservableBoolean();
    public final ObservableBoolean isEndDrawerOpened = new ObservableBoolean();
    public final MutableLiveData<Boolean> openDrawer = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> openEndDrawer = new MutableLiveData<>(false);

    public final MutableLiveData<Boolean> allowDrawerOpen = new MutableLiveData<>(true);
}
