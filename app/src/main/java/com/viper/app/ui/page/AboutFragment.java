package com.viper.app.ui.page;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.viper.app.BR;
import com.viper.app.R;
import com.viper.app.domain.message.SharedViewModel;
import com.viper.app.ui.state.AboutViewModel;
import com.viper.base.databinding.page.DataBindingConfig;
import com.viper.base.ui.page.BaseFragment;

/**
 * 关于 使用说明等
 */
public class AboutFragment extends BaseFragment {

    private AboutViewModel mState;

    private SharedViewModel mEvent;

    @Override
    protected void initViewModel() {
       // Log.e("哈哈", getArguments() != null ? getArguments().getString("title") : "null");;
        mState = getFragmentScopeViewModel(AboutViewModel.class);
        mEvent = getApplicationScopeViewModel(SharedViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {



        return new DataBindingConfig(R.layout.fragment_about,BR.vm,mState)
            .addBindingParam(BR.click,new ClickProxy())

            ;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public class ClickProxy {

        public void openDraw() {
         //   nav().navigateUp();
            mEvent.requestToOpenOrCloseDrawer(true);
        }
    }




}
