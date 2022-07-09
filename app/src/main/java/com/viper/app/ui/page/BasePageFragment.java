package com.viper.app.ui.page;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SimpleArrayMap;
import com.google.gson.reflect.TypeToken;
import com.viper.app.BR;
import com.viper.app.R;
import com.viper.app.data.bean.PageNode;
import com.viper.app.domain.message.SharedViewModel;
import com.viper.app.domain.request.RequestManger;
import com.viper.app.ui.page.adapter.PageNodeAdapter;
import com.viper.app.ui.state.BasePageViewModel;
import com.viper.app.ui.state.BaseSharedOPCDataModel;
import com.viper.app.ui.state.SettingViewModel;
import com.viper.app.ui.view.CallBackView;
import com.viper.app.util.U;
import com.viper.app.util.P;
import com.viper.base.databinding.page.DataBindingConfig;
import com.viper.base.ui.page.BaseFragment;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BasePageFragment extends BaseFragment {
    private SettingViewModel settingViewModel;
    private BasePageViewModel mState;
    private SharedViewModel mEvent;
    private BaseSharedOPCDataModel sharedOPCDataModel;
    private String id;
    private String title;
    private PageNodeAdapter adapter;
    private ClickProxy clickProxy;

    @Override
    protected void initViewModel() {

        if (getArguments() != null) {
            id = getArguments().getString("id");
            title = getArguments().getString("title");
        }

        mState = getApplicationScopeViewModel(BasePageViewModel.class, id);
        settingViewModel = getApplicationScopeViewModel(SettingViewModel.class);
        mEvent = getApplicationScopeViewModel(SharedViewModel.class);
        sharedOPCDataModel = getApplicationScopeViewModel(BaseSharedOPCDataModel.class);

        mState.setDataRequest(RequestManger.getBasePageNodeRequest(id));
        mState.keepLive.observe(this,aBoolean -> {
            if (aBoolean){
                if (Objects.requireNonNull(mState.pageSetting.getValue()).getReadMode()==0){
                    if (!mState.subscribed.getValue()){
                        mState.reqSubscriptionAll();
                        mState.subscribed.setValue(true);
                    }

                }else {
                    mState.reqBrowseAll();
                }
            }
        });
        mState.pageSetting.observe(this, setting -> {
            mState.getDataRequest().getRepository().setOpcUri(setting.getDataSource());
            mState.settingInit.setValue(true);
        });
        mState.settingInit.observe(this,b->{
            if (b) {
                reqInitData();
            }
        });
        settingViewModel.pageSettings.forEach(i -> {
            if (String.valueOf(i.getId()).equals(id)) {
                mState.pageSetting.setValue(i);
               // mState.title.setValue(i.getPageName());
            }
        });




    }


    private void reqInitData() {
        if (U.isTrue(mState.dataInit)) {
            mState.keepLive.setValue(true);

        }else {
            U.getCacheThreadPool().execute(() -> {
                String content = U.readFile(getFileNameList());
                if (!U.isEmpty(content)) {
                    List<PageNode> list = U.fromJson(content, new TypeToken<List<PageNode>>() {
                    }.getType());
                    if (list != null) {

                        mState.map.clear();
                        SimpleArrayMap<Integer, PageNode> map = new SimpleArrayMap<>();
                        list.forEach(t -> {
                            int id = t.getId();
                            if (id!=0){
                                if (map.containsKey(id)) {
                                    PageNode pageNode = map.get(id);
                                    if (pageNode!=null){
                                        t.setValue(pageNode.getValue());
                                    }
                                } else {
                                    map.put(id, t);
                                }
                            }
                            List<PageNode> child = t.getChild();
                            if (child!=null){
                                child.forEach(v->{
                                    int id2 = v.getId();
                                    if (id2 != 0){
                                        if (map.containsKey(id2)) {
                                            PageNode pageNode2 = map.get(id2);
                                            if (pageNode2!=null){
                                                v.setValue(pageNode2.getValue());
                                            }
                                        } else {
                                            map.put(id2, v);
                                        }
                                    }

                                });
                            }

                        });
                        mState.map.putAll(map);
                        mState.pageItems.clear();
                        mState.pageItems.addAll(list);
                        mState.keepLive.postValue(true);
                        mState.dataInit.postValue(true);

                        // reqSubscription();
                    }
                }
            });
        }

    }


    @Override
    protected DataBindingConfig getDataBindingConfig() {

        mState.title.setValue(title);
        if (clickProxy == null){
            clickProxy = new ClickProxy();
        }

        if (adapter == null){
            adapter = new PageNodeAdapter(getContext(), mState,settingViewModel,sharedOPCDataModel.getCache().getValue());

            adapter.setOnItemClickListener((view, item1, position) -> P.itemClick(view, item1,position,mState));
            adapter.setOnItemLongClickListener((view, item12, position) -> P.itemLongClick(view, item12,position,mState,adapter,
                settingViewModel,mState.pageItems,null,-1));
        }




        return new DataBindingConfig(R.layout.fragment_page_base, BR.vm, mState)
            .addBindingParam(BR.click, clickProxy)
            .addBindingParam(BR.adapterPage, adapter)
            ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    private final static int STYLE_COUNT = 5;

    private void factoryList() {
        List<PageNode> list = new ArrayList<>();
        for (int i = 0; i < STYLE_COUNT; i++) {
            PageNode p = new PageNode();
            int m = i % STYLE_COUNT;
            switch (m){
                case 0:{  p.setLayout(R.layout.page_item_text);}break;
                case 1:{  p.setLayout(R.layout.page_item_kv);}break;
                case 2:{  p.setLayout(R.layout.page_item_switch);}break;
                case 3:{  p.setLayout(R.layout.page_item_spinner);}break;
                case 4:{  p.setLayout(R.layout.page_item_group);
                    p.addChild(new PageNode(R.layout.page_item_group_kv));}break;
            }
            p.setName("样式" +(i+1));
            list.add(p);
            mState.setData(list);
        }
    }

    @Override
    public void onDestroyView() {
        mState.keepLive.setValue(false);
        super.onDestroyView();

    }


    //数据文件名
    private String getFileNameList() {
        return title + id + ".page";
    }

    public class ClickProxy {
        //左侧抽屉
        public void openDraw() {
            mEvent.requestToOpenOrCloseDrawer(true);
        }

        //右上角菜单
        public void openAppbarMenu(View view) {
            List<String> data = new ArrayList<>();
            if (mState.showFloatingButton.getValue() != null) {
                if (mState.showFloatingButton.getValue()) {
                    data.add("隐藏悬浮按钮");
                } else {
                    data.add("显示悬浮按钮");
                }
            }
            data.add("保存页面配置");
            data.add("导出页面配置");
            data.add("导入页面配置");
            data.add("清空页面");
            data.add("重置页面");
            data.add("同步节点信息");
            data.add("更新画面");
            CallBackView.showPopup(view, data, i -> {
                switch (i) {
                    case 0:
                        mState.hideOrShowFloatingButton();
                        break;
                    case 1:
                        mState.saveResult(getFileNameList());
                        break;
                    case 2:
                        U.getCacheThreadPool().execute(() -> mState.saveResultToSdCard(getFileNameList()));
                        break;
                    case 3:
                        loadPageFromSdCard(view);
                        break;
                    case 4:
                        mState.clearData();
                        break;
                    case 5:
                        factoryList();
                        break;
                    case 6:
                        U.getCacheThreadPool().execute(() -> mState.reqSyncNode());
                        break;
                    case 7:
                        updateView();
                        break;
                }
            });
        }

        public void updateView() {
            adapter.getRecyclerView().setAdapter(adapter);
        }


        //悬浮按钮点击事件
        public void openMenu(View view) {
            List<String> data = new ArrayList<>();
            data.add("增加一个控件样式1");
            data.add("增加一个控件样式2");
            data.add("增加一个控件样式3");
            data.add("增加一个控件样式4");
            data.add("增加一个控件样式5");
            data.add("更多样式开发中...");

            CallBackView.showPopup(view, data, i -> {
                switch (i) {
                    case 0:
                        addItem(R.layout.page_item_text);
                        break;
                    case 1:
                        addItem(R.layout.page_item_kv);
                        break;
                    case 2:
                        addItem(R.layout.page_item_switch);
                        break;
                    case 3:
                        addItem(R.layout.page_item_spinner);
                        break;
                    case 4:
                        addItem(R.layout.page_item_group);
                        break;
                    case 5:break;

                }
            });
        }

        private void addItem(@LayoutRes int id) {
            mState.addItem(new PageNode(id));
        }

        private void loadPageFromSdCard(View view) {
            List<String> pageList = U.getPageFileNamesFromSdCard();
            if (pageList != null && !pageList.isEmpty()) {
                CallBackView.showListDialog(view, pageList.toArray(new String[0]), "请选择要加载的文件", i ->
                    U.getCacheThreadPool().execute(() -> mState.loadResultFromSdCard(pageList.get(i))));
            } else {
                U.showShortToast("未找到配置文件");
            }
        }
    }

}
