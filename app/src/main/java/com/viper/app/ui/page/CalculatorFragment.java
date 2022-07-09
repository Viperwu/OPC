package com.viper.app.ui.page;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.viper.app.BR;
import com.viper.app.R;
import com.viper.app.domain.message.SharedViewModel;
import com.viper.app.ui.state.AboutViewModel;
import com.viper.app.ui.state.CalculatorViewModel;
import com.viper.base.databinding.page.DataBindingConfig;
import com.viper.base.ui.page.BaseFragment;

/**
 * 关于 使用说明等
 */
public class CalculatorFragment extends BaseFragment {

    private CalculatorViewModel mState;

    private SharedViewModel mEvent;

    @Override
    protected void initViewModel() {

        mState = getFragmentScopeViewModel(CalculatorViewModel.class);

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
