package com.viper.app.ui.page;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.navigation.Navigation;
import com.viper.app.BR;
import com.viper.app.R;
import com.viper.app.data.bean.OPCSetting;
import com.viper.app.domain.message.SharedViewModel;
import com.viper.app.ui.page.adapter.DrawOpcItemAdapter;
import com.viper.app.ui.page.adapter.DrawPageItemAdapter;
import com.viper.app.ui.state.BaseSharedOPCDataModel;
import com.viper.app.ui.state.DrawerStartViewModel;
import com.viper.app.ui.state.SettingViewModel;
import com.viper.app.util.U;
import com.viper.base.databinding.page.DataBindingConfig;
import com.viper.base.databinding.recyclerview.adapter.BaseDataBindingAdapter;
import com.viper.base.ui.page.BaseFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * 左侧抽屉
 */
public class DrawerStartFragment extends BaseFragment {
    private DrawerStartViewModel mState;
    private SettingViewModel settingViewModel;
    private SharedViewModel mEvent;


    @Override
    protected void initViewModel() {
        mState = getFragmentScopeViewModel(DrawerStartViewModel.class);
        mEvent = getApplicationScopeViewModel(SharedViewModel.class);
        settingViewModel = getApplicationScopeViewModel(SettingViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        DrawOpcItemAdapter drawOpcItemAdapter = new DrawOpcItemAdapter(getContext());
        DrawPageItemAdapter drawPageItemAdapter = new DrawPageItemAdapter(getContext());
        if (U.isTimeBefore()){
            drawPageItemAdapter.setOnItemClickListener((viewId, item, position) -> {
                Bundle bundle = new Bundle();
                bundle.putString("id",String.valueOf(item.getId()));
                bundle.putString("title",item.getPageName());
                if (getActivity()!=null) Navigation.findNavController(getActivity(), R.id.main_fragment_host)
                    .navigate(R.id.navigation_page,bundle);
                mEvent.requestToOpenOrCloseDrawer(false);
            });

            drawOpcItemAdapter.setOnItemClickListener((viewId, item, position) -> {
                Bundle bundle = new Bundle();
                bundle.putString("id",String.valueOf(item.getId()));
                bundle.putString("title",item.getTitle());
                if (getActivity()!=null) Navigation.findNavController(getActivity(), R.id.main_fragment_host)
                    .navigate(R.id.navigation_opc,bundle);
                mEvent.requestToOpenOrCloseDrawer(false);
            });
        }

        return new DataBindingConfig(R.layout.fragment_drawer_start, BR.vm, mState)
            .addBindingParam(BR.click, new ClickProxy())
            .addBindingParam(BR.data,settingViewModel)
            .addBindingParam(BR.adapterPage, drawPageItemAdapter)
            .addBindingParam(BR.adapterOpc, drawOpcItemAdapter)
           // .addBindingParam(BR.adapter, new DrawerAdapter(getContext()))
            ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public class ClickProxy {


        public void setting(){
            if (getActivity()!=null) {
                Navigation.findNavController(getActivity(), R.id.main_fragment_host)
                    .navigate(R.id.settingFragment);
            }
            mEvent.requestToOpenOrCloseDrawer(false);

        }

        public void openAboutFragment(){
            if (getActivity()!=null) Navigation.findNavController(getActivity(), R.id.main_fragment_host)
                .navigate(R.id.aboutFragment);
            mEvent.requestToOpenOrCloseDrawer(false);
        }

        public void openCalculatorFragment(){
            if (getActivity()!=null) Navigation.findNavController(getActivity(), R.id.main_fragment_host)
                .navigate(R.id.calculatorFragment);
            mEvent.requestToOpenOrCloseDrawer(false);
        }

    }

}
