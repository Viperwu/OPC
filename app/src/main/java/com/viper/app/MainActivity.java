package com.viper.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.reflect.TypeToken;
import com.viper.app.account_service.AccountUtils;
import com.viper.app.data.bean.OPCSetting;
import com.viper.app.data.bean.PageSetting;
import com.viper.app.data.repository.BaseOpcDataRepository;
import com.viper.app.domain.message.SharedViewModel;
import com.viper.app.domain.request.BaseOPCNodeRequest;
import com.viper.app.domain.request.RequestManger;
import com.viper.app.ui.state.BaseOpcViewModel;
import com.viper.app.ui.state.MainActivityViewModel;
import com.viper.app.ui.state.SettingViewModel;
import com.viper.base.databinding.page.DataBindingConfig;
import com.viper.base.permission.OnPermissionCallback;
import com.viper.base.permission.Permission;
import com.viper.base.permission.XXPermissions;
import com.viper.base.ui.page.BaseActivity;
import com.viper.base.utils.SPUtils;
import com.viper.app.util.U;

import java.lang.reflect.Type;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private MainActivityViewModel mState;
    private SettingViewModel settingViewModel;
    private SharedViewModel mEvent;
    private boolean useSplashFragment = false;
    private boolean mIsListened = false;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void initViewModel() {
        // opcDataModelList = new ArrayList<>();


        mState = getActivityScopeViewModel(MainActivityViewModel.class);
        mEvent = getApplicationScopeViewModel(SharedViewModel.class);
        settingViewModel = getApplicationScopeViewModel(SettingViewModel.class);
    }


    @Override
    protected DataBindingConfig getDataBindingConfig() {


        return new DataBindingConfig(R.layout.activity_main, BR.vm, mState)
            // .addBindingParam(BR.listener,new ListenerHandler())
            //   .addBindingParam(BR.adapter,adapter)
            //  .addBindingParam(BR.ac,this)
            ;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mState.activityInit.getValue() != null && mState.activityInit.getValue()) {
            NavController nav = Navigation.findNavController(this, R.id.activity_fragment_host);
            if (nav.getCurrentDestination() != null && nav.getCurrentDestination().getId() == R.id.splashFragment) {
                nav.navigate(R.id.action_splashFragment_to_rootFragment);
            }
        }

        mState.activityInit.setValue(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (useSplashFragment) {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    MainActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    Navigation.findNavController(MainActivity.this, R.id.activity_fragment_host)
                        .navigate(R.id.action_splashFragment_to_rootFragment);

                }
            }, 3000);
        }
        super.onCreate(savedInstanceState);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)!= PackageManager.PERMISSION_GRANTED){
                XXPermissions.with(this).permission(Permission.GET_ACCOUNTS)
                    .request(new OnPermissionCallback() {
                        @Override
                        public void onGranted(List<String> permissions, boolean all) {
                            AccountUtils.addAccount(MainActivity.this);
                            AccountUtils.autoSyncStart();
                        }

                        @Override
                        public void onDenied(List<String> permissions, boolean never) {
                            OnPermissionCallback.super.onDenied(permissions, never);
                        }
                    });
            }

            if(!Environment.isExternalStorageManager()){
                XXPermissions.with(this)
                    .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                    // .permission(Permission.READ_EXTERNAL_STORAGE)
                    .request(new OnPermissionCallback() {
                        @Override
                        public void onGranted(List<String> permissions, boolean all) {
                            init();
                        }

                        @Override
                        public void onDenied(List<String> permissions, boolean never) {
                            Log.e("权限获取失败", "onDenied: ****************************************************" );
                            finish();
                        }
                    });
            }else {
                init();
            }


        }else {
            init();
        }



       /* //TODO tip：Android 12 部分权限的处理

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            XXPermissions.with(this)
                .permission(Permission.READ_PHONE_STATE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        init();
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        finish();
                    }
                });
        } else {
            init();
        }*/

    }

    private void init() {
        // NavController nav = Navigation.findNavController(this, R.id.nav_root);
        mEvent.isToCloseActivityIfAllowed().observe(this, aBoolean -> {
            NavController nav = Navigation.findNavController(this, R.id.activity_fragment_host);
            if (nav.getCurrentDestination() != null && nav.getCurrentDestination().getId() != R.id.mainFragment) {
                nav.navigateUp();

            } else if (mEvent.isToOpenOrCloseDrawer().getValue() != null) {

                //TODO 同 tip 2

                mEvent.requestToOpenOrCloseDrawer(false);

            } else {
                super.onBackPressed();
            }
        });
        //   navController = Navigation.findNavController(this, R.id.main_fragment_host);



        /*mEvent.getToOpenAboutFragment().observe(this, aBoolean -> {
            if (aBoolean){
                Navigation.findNavController(this, R.id.root_fragment_host).navigate(R.id.action_rootFragment_to_aboutFragment);
            }
        });*/
        mEvent.requestToOpenOrCloseDrawer(false);


        U.getCacheThreadPool().execute(() -> {
            boolean settingInit = SPUtils.getInstance(getString(R.string.setting)).getBoolean(getString(R.string.setting_init));
            if (!settingInit) {
                settingViewModel.resetAll();


            }
            else {
                Type type = new TypeToken<List<Long>>() {
                }.getType();
                List<Long> opcSettingIds = U.fromJson(SPUtils.getInstance(getString(R.string.setting)).
                    getString(getString(R.string.opc_setting_ids), ""), type);

                List<Long> pageSettingIds = U.fromJson(SPUtils.getInstance(getString(R.string.setting)).
                    getString(getString(R.string.page_setting_ids), ""), type);

                if (opcSettingIds != null) {
                    settingViewModel.opcSettingIds.addAll(opcSettingIds);
                    Log.d("opcSettingIds", String.valueOf(opcSettingIds));
                    for (long id : opcSettingIds) {

                        OPCSetting opcSetting = U.fromJson(SPUtils.getInstance(getString(R.string.setting)).
                            getString(String.valueOf(id), ""), OPCSetting.class);

                        if (opcSetting != null) {

                            settingViewModel.opcSettings.add(opcSetting);
                        }
                    }

                }

                if (pageSettingIds != null) {

                    settingViewModel.pageSettingIds.addAll(pageSettingIds);


                    for (Long id : pageSettingIds) {
                        PageSetting pageSetting = U.fromJson(SPUtils.getInstance(getString(R.string.setting)).
                            getString(String.valueOf(id), ""), PageSetting.class);
                        if (pageSetting != null) {
                            settingViewModel.pageSettings.add(pageSetting);
                        }
                    }
                }

            }
        });


    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!mIsListened) {
            mEvent.requestToAddSlideListener(true);

            mIsListened = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (mEvent.isToOpenOrCloseDrawer() != null && mEvent.isToOpenOrCloseEndDrawer() != null) {
            if (mEvent.isToOpenOrCloseDrawer().getValue() != null && mEvent.isToOpenOrCloseEndDrawer().getValue() != null) {
                if (mEvent.isToOpenOrCloseDrawer().getValue() || mEvent.isToOpenOrCloseEndDrawer().getValue()) {
                    mEvent.requestToOpenOrCloseDrawer(false);
                    mEvent.requestToOpenOrCloseEndDrawer(false);
                } else {
                    super.onBackPressed();
                }
            } else if (mEvent.isToOpenOrCloseDrawer().getValue() != null) {
                if (mEvent.isToOpenOrCloseDrawer().getValue()) {
                    mEvent.requestToOpenOrCloseDrawer(false);
                } else {
                    super.onBackPressed();
                }
            } else if (mEvent.isToOpenOrCloseEndDrawer().getValue() != null) {
                if (mEvent.isToOpenOrCloseEndDrawer().getValue()) {
                    mEvent.requestToOpenOrCloseEndDrawer(false);
                } else {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }

        } else if (mEvent.isToOpenOrCloseDrawer() != null) {
            if (mEvent.isToOpenOrCloseDrawer().getValue()) {
                mEvent.requestToOpenOrCloseDrawer(false);
            } else {
                super.onBackPressed();
            }

        } else if (mEvent.isToOpenOrCloseEndDrawer() != null) {
            if (mEvent.isToOpenOrCloseEndDrawer().getValue()) {
                mEvent.requestToOpenOrCloseEndDrawer(false);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }

    }


   /* public class ListenerHandler extends DrawerLayout.SimpleDrawerListener {
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

    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
