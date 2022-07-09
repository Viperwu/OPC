package com.viper.app.ui.page;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.viper.app.BR;
import com.viper.app.R;
import com.viper.app.domain.message.SharedViewModel;
import com.viper.app.ui.page.adapter.SettingOpcItemAdapter;
import com.viper.app.ui.page.adapter.SettingPageItemAdapter;
import com.viper.app.ui.state.BaseSharedOPCDataModel;
import com.viper.app.ui.state.SettingViewModel;
import com.viper.app.ui.view.CallBackView;
import com.viper.app.util.U;
import com.viper.base.databinding.page.DataBindingConfig;
import com.viper.base.ui.page.BaseFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * 全局设置
 */
public class SettingFragment extends BaseFragment {

    private SettingViewModel mState;
    private SharedViewModel mEvent;

    @Override
    protected void initViewModel() {
        mState = getApplicationScopeViewModel(SettingViewModel.class);
      /*  mEvent = getApplicationScopeViewModel(SharedViewModel.class);*/
        mEvent = getApplicationScopeViewModel(SharedViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        return new DataBindingConfig(R.layout.fragment_setting,BR.vm,mState)
                .addBindingParam(BR.click,new ClickProxy())
                .addBindingParam(BR.adapterOpc, new SettingOpcItemAdapter(getActivity(), mState))
                .addBindingParam(BR.adapterPage, new SettingPageItemAdapter(getActivity(), mState))

            ;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    public class ClickProxy {
        public void openDraw() {
            mEvent.requestToOpenOrCloseDrawer(true);
          //  nav().navigateUp();
        }

        //重置设定
        public void reset(View view) {

            CallBackView.showConfigDialog(view, U.getDrawable(R.drawable.ic_baseline_clear_all_24),U.getSting(R.string.reset_all_setting),
                U.getSting(R.string.reset_all_setting_or_not)
               , text -> {
                    mState.resetAll();
            });
        }

        public void exportAll(){
            //todo 导出所有

        }

        public void importAll(){
            //todo 导入所有

        }

        //右上角菜单
        public void openMenu(View view){
            List<String> data = new ArrayList<>();
            data.add(U.getString(R.string.add_page));
            data.add(U.getString(R.string.add_opc));
            data.add(U.getString(R.string.reset_all_setting));
            data.add(U.getString(R.string.export_all));
            data.add(U.getString(R.string.import_all));
            CallBackView.showPopup(view, data, i -> {
                switch (i){
                    case 0:mState.addPage();break;
                    case 1:mState.addOpc();break;
                    case 2: reset(view);break;
                    case 3: exportAll();break;
                    case 4: importAll();break;
                }
            });
        }

    }



}
