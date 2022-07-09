package com.viper.app.ui.page;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.viper.app.BR;
import com.viper.app.R;
import com.viper.app.ui.state.DrawerEndViewModel;
import com.viper.app.ui.state.DrawerStartViewModel;
import com.viper.base.databinding.page.DataBindingConfig;
import com.viper.base.ui.page.BaseFragment;

/**
 * 还未用到
 */
public class DrawerEndFragment extends BaseFragment {
    private DrawerEndViewModel mState;

    @Override
    protected void initViewModel() {
        mState = getFragmentScopeViewModel(DrawerEndViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        return new DataBindingConfig(R.layout.fragment_drawer_end, BR.vm, mState)
            .addBindingParam(BR.click, new ClickProxy())
           // .addBindingParam(BR.adapter, new DrawerAdapter(getContext()))
            ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public class ClickProxy {

        public void logoClick() {
            //openUrlInBrowser(getString(R.string.github_project));
        }
    }

}
