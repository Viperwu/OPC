package com.viper.app.ui.page;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.viper.app.BR;
import com.viper.app.R;
import com.viper.app.domain.message.SharedViewModel;
import com.viper.app.ui.page.adapter.SimpleFragmentPagerAdapter;
import com.viper.app.ui.state.MainViewModel;
import com.viper.app.ui.view.XDrawerLayout;
import com.viper.base.databinding.page.DataBindingConfig;
import com.viper.base.ui.page.BaseFragment;

import java.util.Objects;

public class MainFragment extends BaseFragment {

    private MainViewModel mState;

    private SharedViewModel mEvent;

    @Override
    protected void initViewModel() {
        mState = getFragmentScopeViewModel(MainViewModel.class);

        mEvent = getApplicationScopeViewModel(SharedViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {



        return new DataBindingConfig(R.layout.fragment_main,BR.vm,mState)
                    //.addBindingParam(BR.listener,new ListenerHandler())
                   // .addBindingParam(BR.ac, requireActivity())
         //   .addBindingParam(BR.adapter,new SimpleFragmentPagerAdapter(this))

            ;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEvent.isToOpenOrCloseDrawer().observe(getViewLifecycleOwner(), aBoolean -> {
            mState.openDrawer.setValue(aBoolean);
        });

        mEvent.isToOpenOrCloseEndDrawer().observe(getViewLifecycleOwner(), aBoolean -> {
            mState.openEndDrawer.setValue(aBoolean);
        });


    }



    //抽屉事件监听
   public class ListenerHandler extends DrawerLayout.SimpleDrawerListener {
        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            mState.isDrawerOpened.set(true);
            mState.isEndDrawerOpened.set(true);

        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            mState.isDrawerOpened.set(false);
            mState.isEndDrawerOpened.set(false);
            mState.openDrawer.setValue(false);
            mState.openEndDrawer.setValue(false);

        }

        public void openMenu() {
            mEvent.requestToOpenOrCloseDrawer(true);
        }

        public void openEndMenu() {
            mEvent.requestToOpenOrCloseEndDrawer(true);
        }

    }




}
