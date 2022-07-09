package com.viper.app.ui.page;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SimpleArrayMap;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;
import com.viper.app.BR;
import com.viper.app.R;
import com.viper.app.data.bean.OPCNode;
import com.viper.app.data.bean.OPCSetting;
import com.viper.app.data.bean.SubscriptionOPCNode;
import com.viper.app.domain.message.OpcData2Result;
import com.viper.app.domain.message.SharedViewModel;
import com.viper.app.domain.request.RequestManger;
import com.viper.app.ui.page.adapter.OpcAppBarItemAdapter;
import com.viper.app.ui.page.adapter.OpcAdapter;
import com.viper.app.ui.state.BaseOpcViewModel;
import com.viper.app.ui.state.BaseSharedOPCDataModel;
import com.viper.app.ui.state.SettingViewModel;
import com.viper.app.ui.view.CallBackView;
import com.viper.app.util.P;
import com.viper.app.util.U;
import com.viper.base.databinding.page.DataBindingConfig;
import com.viper.base.databinding.recyclerview.layout_manager.WrapContentLinearLayoutHorizontalManager;
import com.viper.base.ui.page.BaseFragment;

import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class BaseOpcFragment extends BaseFragment {

    protected BaseOpcViewModel mState;
    protected OpcAdapter adapter;
    protected OpcAppBarItemAdapter adapter2;
    protected SharedViewModel mEvent;
    protected BaseSharedOPCDataModel sharedOPCData;
    protected String id;
    protected String title;
    private SettingViewModel settingViewModel;


    //通过传入id获取VM并初始化
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void initViewModel() {
        if (getArguments() != null) {
            id = getArguments().getString("id");
            title = getArguments().getString("title");
        }


        mEvent = getApplicationScopeViewModel(SharedViewModel.class);
        settingViewModel = getApplicationScopeViewModel(SettingViewModel.class);
        mState = getApplicationScopeViewModel(BaseOpcViewModel.class, id);
        sharedOPCData = getApplicationScopeViewModel(BaseSharedOPCDataModel.class);

        mState.setRequest(RequestManger.getBaseOPCNodeRequest(id));
       /* mState.request.getGlobalOPCNodeMap().observe(this, dataResult -> {
            if (dataResult.getResult() != null) {
                if (mState.map.isEmpty()) {
                    mState.map.putAll(dataResult.getResult());
                }
            }
        });*/
       /* mState.plcNodeRequest.getUaNodeList().observe(this, dataResult -> {
            if (!dataResult.getResponseStatus().isSuccess()) return;
            List<UaNode> uaNodeList = dataResult.getResult();
            if (uaNodeList != null && !uaNodeList.isEmpty()) {
                //   Log.d("TAG", "onViewCreated: List<UaNode> changed");
                //mState.setNewValue(uaNodeList);
                browseCallback(uaNodeList);
            }
        });*/

        mState.request.getOpcNodesLiveData().observe(this, dataResult -> {
            List<OPCNode> list = dataResult.getResult();
            if (list != null && !list.isEmpty()) {
                if (mState.rootNode.getValue() == null) {
                    OPCNode node = list.get(0);
                    if (node != null) {
                        mState.rootNode.postValue(node);

                    }
                }
            }
        });


        mState.request.getOnlineList().observe(this, this::onLineDataCallback);

       /* mState.currentSelectNode.observe(this,i->{
            mState.getNodeOnline();
        });*/

        mState.rootNode.observe(this, node -> {
            if (node != null) {
                if (mState.opcNodes.isEmpty()) {
                    mState.opcNodes.add(node);
                }
            }
        });
        mState.request.getSubOPCNode().observe(this, dataResult -> {
            SubscriptionOPCNode node = dataResult.getResult();
            if (node != null) {
                //   Log.d("TAG", "onViewCreated: List<UaNode> changed");
                subscriptionCallback(node);
                // mState.setNewValue(node);
            }
        });
/*
        mState.opcSetting.observe(this, setting -> {
            //  Log.d("opcSetting changed", "initViewModel: ");
            mState.getPlcNodeRequest().getRepository().setScanNodeId(setting.getNodeId());
            // mState.getPlcNodeRequest().getRepository().setBrowseNodeId(setting.getNodeId());
            mState.getPlcNodeRequest().getRepository().setOpcUri(setting.getAddress());
        });*/

        settingViewModel.opcSettings.forEach(i -> {
            if (String.valueOf(i.getId()).equals(id)) {
                mState.opcSetting.setValue(i);
                mState.title.setValue(i.getTitle());
               // mState.currentSelectNode.setValue(i.getNodeId());
                mState.getRequest().getRepository().setScanNodeId(i.getNodeId());
                // mState.getPlcNodeRequest().getRepository().setBrowseNodeId(setting.getNodeId());
                mState.getRequest().getRepository().setOpcUri(i.getAddress());
            }
        });

        mState.keepLiveData.observe(this,b->{
            mState.reqLiveData();
        });

       /* if (U.isTrue(mState.keepLiveDataLastState)) {
            mState.reqStopOrStartLiveData(true);
        }*/
        mState.keepSubscription.setValue(true);
        mState.keepLiveData.setValue(mState.keepLiveDataLastState.getValue());

        /*if(mState.cache.getValue()==null||mState.cache.getValue().isEmpty()){
            U.getCacheThreadPool().execute(() -> {
                String content = U.readFile(getFileName()+".cache");
                if (!U.isEmpty(content)) {
                    ArrayMap<Integer, List<OPCNode>> map = U.fromJson(content,  new TypeToken<ArrayMap<Integer, List<OPCNode>>>() {
                    }.getType());
                    if (map != null) {
                        mState.cache.postValue(map);
                    }
                }
            });
        }*/

        sharedOPCData.getCache();



    }


    //不可见时取消获取数据
    @Override
    public void onDestroyView() {
        // Log.d("onDestroyView", "onDestroyView: ");
        mState.keepLiveDataLastState.setValue(mState.keepLiveData.getValue());
        mState.keepSubscription.setValue(false);
        mState.keepLiveData.setValue(false);
        super.onDestroyView();
    }



    public synchronized void onLineDataCallback(OpcData2Result<List<OPCNode>,NodeId> dataResult){
        if (mState.currentSelectNode.getValue()!=null){
            NodeId nodeId = dataResult.getResultM();
            List<OPCNode> list = dataResult.getResultT();
            if (nodeId.hashCode() == mState.currentSelectNode.getValue().hashCode()) {
                if (list != null && list.size() > 0) {
                    mState.stopRefresh();
                    mState.opcNodes.clear();
                    mState.opcNodes.addAll(list);
                    mState.reqLiveData();
                }
            }
        }
    }


    @Override
    protected DataBindingConfig getDataBindingConfig() {

        //初始化adapter设置点击事件
        adapter = new OpcAdapter(getContext());
        adapter.setOnItemClickListener((view, item, position) -> {
            if (!item.isCanExpand()) {
                return;
            }
            // itemClick(item,position);
            opcItemClick(item, position);
        });
        adapter.setOnItemLongClickListener(this::longClick);

        adapter2 = new OpcAppBarItemAdapter(getContext());
        adapter2.setOnItemLongClickListener(this::longClickAppbar);
        adapter2.setOnItemClickListener((viewId, item, position) -> {
            if (!item.isCanExpand()) {
                return;
            }
            // itemClick(item,position);
            appBarItemClick(item, position);
        });

        return new DataBindingConfig(R.layout.fragment_opc_base, BR.vm, mState)
            .addBindingParam(BR.adapter, adapter)
            .addBindingParam(BR.adapter2, adapter2)
            .addBindingParam(BR.click, new ClickProxy())
            //  .addBindingParam(BR.sd,mShared)

            ;
    }

    //appbar item点击事件
    private void appBarItemClick(OPCNode item, int position) {

        //changeSelect(item);

        mState.appbarNodeList.forEach(i -> i.setCurrentSelected(false));
        item.setCurrentSelected(true);
        mState.currentSelectNode.setValue(item.getNodeId());
        newListForContent(item.getChildList(), item);
        adapter2.notifyItemChanged(position);
        scrollToPosition(position);

    }

    //自动滚动到选中的item
    private void scrollToPosition(int position) {
        WrapContentLinearLayoutHorizontalManager linearLayoutHorizontalManager = (WrapContentLinearLayoutHorizontalManager) adapter2
            .getRecyclerView().getLayoutManager();
        if (linearLayoutHorizontalManager != null) {
            linearLayoutHorizontalManager.smoothScrollToPosition(adapter2.getRecyclerView(),
                new RecyclerView.State(), position);
        }
    }

    //新的数据
    private void newListForContent(List<OPCNode> list, OPCNode item) {
        mState.stopRefresh();
        mState.opcNodes.clear();
        if (list!=null && !list.isEmpty()){
            mState.opcNodes.addAll(list);
        }else {
            SimpleArrayMap<Integer,List<OPCNode>> map = sharedOPCData.getCache().getValue();
            if (map!=null){
                List<OPCNode> opcNodeListCache = map.get(item.getId());
                if (opcNodeListCache!=null){
                    mState.opcNodes.addAll(opcNodeListCache);
                 //   Log.e("TAG", "newListForContent: 从缓存中加载");
                }
            }
        }

        if (U.isTrue(mState.keepLiveData)) {
            reqNewList(item);
        }
    }

    //appbar长按事件
    private void longClickAppbar(View view, OPCNode node, int position) {
        QMUIPopups.quickAction(view.getContext(),
            QMUIDisplayHelper.dp2px(view.getContext(), 56),
            QMUIDisplayHelper.dp2px(view.getContext(), 56))
            .shadow(true)
            .skinManager(QMUISkinManager.defaultInstance(view.getContext()))
            .edgeProtection(QMUIDisplayHelper.dp2px(view.getContext(), 20))
            .addAction(new QMUIQuickAction.Action().icon(R.drawable.ic_baseline_save_24).text("保存为遍历节点").onClick(
                (quickAction, action, i) -> {
                    quickAction.dismiss();
                    OPCSetting setting = mState.opcSetting.getValue();
                    if (setting != null) {
                        setting.setNodeId(node.getNodeId());
                        setting.setBrowseNodeShortName(U.getString(R.string.customer_node));
                        settingViewModel.saveOpcSettingToLocal(setting);
                    }
                }
            ))
            .show(view);
    }

    //opc item 长按事件
    private void longClick(View view, OPCNode node, int position) {
        LinkedHashMap<Integer, Integer> hashMap = new LinkedHashMap<>();
        hashMap.put(R.string.order_current, R.drawable.ic_baseline_assignment_turned_in_24);
        hashMap.put(R.string.item_detail, R.drawable.ic_baseline_info_24);
        hashMap.put(R.string.write_value, R.drawable.ic_baseline_create_24);
        hashMap.put(R.string.save_to_browse_node, R.drawable.ic_baseline_save_24);
        hashMap.put(R.string.copy_node_info, R.drawable.ic_baseline_copyright_24);
        CallBackView.showQuickAction(view, hashMap, i -> {
            switch (i) {
                case 0: {
                    mState.reqSubscription(node.getNodeId());
                }
                break;
                case 1: {
                    CallBackView.showLoadingPopup(view, node);
                }
                break;
                case 2: {
                    CallBackView.showEditTextDialog(view, "请输入数值", node.getValue().get(), text -> {
                        mState.reqWriteDataToOPC(node, text.trim(), P::showWriteCallback);
                    });
                }
                break;
                case 3: {
                    OPCSetting item = mState.opcSetting.getValue();
                    if (item != null) {
                        item.setNodeId(node.getNodeId());
                        item.setBrowseNodeShortName(U.getString(R.string.customer_node));
                        settingViewModel.saveOpcSettingToLocal(item);
                    }
                }
                break;
                case 4: {
                    U.copyTextToClipboardManager(node.getNodeInfo());
                    settingViewModel.getCopyNode().setValue(node);
                }
                break;
            }
        });
    }

    //更改选中的item
    private void changeSelect(OPCNode currentClickItem) {
        if (mState.appbarNodeList.isEmpty()) {
            mState.appbarNodeList.add(currentClickItem);
            currentClickItem.setCurrentSelected(true);
        } else {

            for (int i = mState.appbarNodeList.size() - 1; i >= 0; i--) {
                OPCNode item = mState.appbarNodeList.get(i);
                if (mState.currentSelectNode.getValue() != null) {
                    if (mState.currentSelectNode.getValue().hashCode() == item.getId()) {
                        if (i == mState.appbarNodeList.size() - 1) {
                            if (currentClickItem.getId() != item.getId()) {
                                mState.appbarNodeList.add(currentClickItem);
                                currentClickItem.setCurrentSelected(true);
                            }
                        } else {
                            if (currentClickItem.getId() != mState.appbarNodeList.get(i + 1).getId()) {
                                mState.appbarNodeList.subList(i + 1, mState.appbarNodeList.size()).clear();
                                mState.appbarNodeList.add(currentClickItem);
                                currentClickItem.setCurrentSelected(true);
                            }
                        }
                        currentClickItem.setCurrentSelected(true);
                        scrollToPosition(i);
                        item.setCurrentSelected(false);
                        break;
                    }
                }
                item.setCurrentSelected(false);
                if (currentClickItem.getId() == item.getId()) {
                    item.setCurrentSelected(true);
                    scrollToPosition(i);
                }
            }
        }
        mState.currentSelectNode.setValue(currentClickItem.getNodeId());
    }

    //通知adapter状态改变
    private void notifyItemChanged(OPCNode item, ListAdapter adapter) {
        if (adapter.getCurrentList().contains(item)) {
            adapter.notifyItemChanged(adapter.getCurrentList().indexOf(item));
        }
    }

    //opc item点击事件
    private void opcItemClick(OPCNode item, int position) {
        changeSelect(item);
        newListForContent(item.getChildList(), item);
    }

    private void reqNewList(OPCNode item) {
        mState.reqBrowseCurrentNode(item.getNodeId(),sharedOPCData.getCache());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //connectToOpc();

    }


    public void connectToOpc() {
       mState.reqInit();

    }


    //订阅回调
    public void subscriptionCallback(SubscriptionOPCNode node) {
        mState.opcNodes.forEach(i->{
            if (i.getId() == node.getId()){
                i.setDataValue(node.getDataValue());
            }
        });
    }

    public class ClickProxy {

        //快速回到根视图
        public void toRootNode() {
            if (mState.rootNode.getValue() != null) {
                mState.stopRefresh();
                mState.opcNodes.clear();
                mState.opcNodes.add(mState.rootNode.getValue());
                mState.currentSelectNode.setValue(null);
                mState.appbarNodeList.forEach(i -> {
                    i.setCurrentSelected(false);
                });
            }
        }

        //是否持续刷新数据
        public void keepLiveData() {
            mState.reqInit();
            mState.keepLiveData.setValue(!mState.keepLiveData.getValue());

           // mState.reqLiveData();
        }

        //打开左边抽屉
        public void openMenu() {
            mEvent.requestToOpenOrCloseDrawer(true);
        }

        //右上角菜单
        public void showDialog(View view) {
            List<String> data = new ArrayList<>();
            data.add("遍历节点");
            data.add("连接到OPC");
            if (U.isTrue(mState.keepLiveData)) {
                data.add("取消刷新");
            } else {
                data.add("保持刷新");
            }

            data.add("重新遍历");
            data.add("保存本次数据");
            data.add("加载上次数据");
            data.add("回到根节点");
            CallBackView.showPopup(view, data, i -> {
                switch (i) {
                    case 0:
                        browseNode();
                        break;
                    case 1:
                        connectToOpc();
                        break;
                    case 2:
                        keepLiveData();
                        break;
                    case 3:
                        reBrowse();
                        break;
                    case 4:
                        saveResult();
                        break;
                    case 5:
                        loadResult();
                        break;
                    case 6:
                        toRootNode();
                        break;
                }
            });
        }

        //重新遍历
        private void reBrowse() {
            mState.reBrowse();
            //  mShared.reBrowse();
        }

        //遍历节点
        private void browseNode() {
            mState.reqBrowseNode();
        }

        //保存数据
        private void saveResult() {
            mState.saveResult(getFileName(),sharedOPCData.getCache());
        }

        //加载数据
        private void loadResult() {
            U.getCacheThreadPool().execute(() -> {
                String content = U.readFile(getFileName());
                if (content != null && !content.isEmpty()) {
                    OPCNode node = U.fromJson(content.trim(), OPCNode.class);
                    if (node != null) {
                        mState.loadDataFromLastScan(node);
                    }
                }
            });
        }


    }

    //数据文件名
    private String getFileName() {
        return title + id + ".data";
    }


}
