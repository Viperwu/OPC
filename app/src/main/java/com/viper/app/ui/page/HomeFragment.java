package com.viper.app.ui.page;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.viper.app.BR;
import com.viper.app.R;
import com.viper.app.domain.message.SharedViewModel;
import com.viper.app.ui.state.HomeViewModel;
import com.viper.base.databinding.page.DataBindingConfig;
import com.viper.base.ui.page.BaseFragment;

//还未用到
public class HomeFragment extends BaseFragment {

    private HomeViewModel mState;

    private SharedViewModel mEvent;



    @Override
    protected void initViewModel() {


        mState = getFragmentScopeViewModel(HomeViewModel.class);

        mEvent = getApplicationScopeViewModel(SharedViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {


        return new DataBindingConfig(R.layout.fragment_home,BR.vm,mState)
                    .addBindingParam(BR.click,new ClickProxy())

            ;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    public class ClickProxy {

        public void openAboutFragment(){

            //nav().navigate(R.id.action_rootFragment_to_aboutFragment);
         //   nav().navigate(R.id.action_mainFragment_to_loginFragment);
            if (getActivity()!=null) Navigation.findNavController(getActivity(), R.id.main_fragment_host)
                .navigate(R.id.aboutFragment);

            // mEvent.requestToOpenAboutFragment(true);
        }
        public void toMySelf(){

        }

        public void openMenu() {
            mEvent.requestToOpenOrCloseDrawer(true);
        }

    }




}
