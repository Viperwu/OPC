package com.viper.app.ui.state;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayMap;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.reflect.TypeToken;
import com.viper.app.data.bean.PageNode;
import com.viper.app.data.bean.OPCNode;
import com.viper.app.data.bean.PageSetting;
import com.viper.app.domain.request.BasePageNodeRequest;
import com.viper.app.ui.view.IBooleanCallBack;
import com.viper.app.util.U;

import com.viper.opc.client.opcua.sdk.client.nodes.UaVariableNode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BasePageViewModel extends ViewModel {
   // public final ObservableArrayList<Integer> keys = new ObservableArrayList<>();
    public final ObservableArrayMap<Integer, PageNode> map = new ObservableArrayMap<>();
    public final MutableLiveData<Boolean> subscribed= new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> settingInit= new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> dataInit= new MutableLiveData<>(false);

    public final MutableLiveData<Integer> positionOfItemChanged= new MutableLiveData<>(-1);
    public final MutableLiveData<Boolean> keepLive = new MutableLiveData<>(true);
    public final MutableLiveData<Boolean> keepSubscriptionLastState= new MutableLiveData<>(true);
    public final MutableLiveData<String> title = new MutableLiveData<>();
    public final MutableLiveData<Boolean> showFloatingButton = new MutableLiveData<>(true);
   // public final MutableLiveData<List<Multi>> list = new MutableLiveData<>();
    public final ObservableArrayList<PageNode> pageItems = new ObservableArrayList<>();
    public final MutableLiveData<PageSetting> pageSetting  =new MutableLiveData<>();
    public BasePageNodeRequest dataRequest;



    public void saveResult(String fileName) {
        Type type = new TypeToken<ArrayList<PageNode>>(){}.getType();
      //  U.getCacheThreadPool().execute(() -> U.writeFile(fileName, U.toJson(list.getValue(),type)));
        U.getCacheThreadPool().execute(() -> U.writeFile(fileName, U.toJson(pageItems,type)));

    }
    public void reqWriteDataToOPC(PageNode item, String value, IBooleanCallBack callBack){
        dataRequest.reqWriteDataToOPC(item,value,callBack);
    }

    public void reqWriteDataToOPC(PageNode item, boolean value, IBooleanCallBack callBack){
        dataRequest.reqWriteDataToOPC(item,value,callBack);
    }

    public void saveResultToSdCard(String fileName) {
        Type type = new TypeToken<List<PageNode>>(){}.getType();
      //  U.getCacheThreadPool().execute(() -> U.writeFileToSdCard(fileName, U.toJson(list.getValue(),type)));

        U.writeFileToSdCard(fileName, U.toJson(pageItems,type));

    }

    public void loadResultFromSdCard(String fileName){
        String content = U.readFileFromSdCard(fileName);
        if (content != null && !content.isEmpty()) {
            List<PageNode> list  = U.fromJson(content, new TypeToken<List<PageNode>>(){}.getType());
            if (list!=null && !list.isEmpty()){
                pageItems.clear();
                pageItems.addAll(list);
            }

        }
    }

    public void addItem(PageNode item){
        pageItems.add(item);
     //   keys.add(item.getId());
       // map.put(item.getId(),item);

    }

    public void setData(List<PageNode> list){
        pageItems.clear();
        pageItems.addAll(list);
    }

    public void removeItem(PageNode item){
        pageItems.remove(item);
    //    map.remove(item.getId());
        //keys.remove(item.getId());
    }

    public void clearData(){
       // map.clear();
        pageItems.clear();
      //  keys.clear();
    }
    public void hideOrShowFloatingButton(){
       showFloatingButton.setValue(!showFloatingButton.getValue());
    }

    public BasePageNodeRequest getDataRequest() {
        return dataRequest;
    }

    public void setDataRequest(BasePageNodeRequest dataRequest) {
        this.dataRequest = dataRequest;
    }


    //todo 节点信息改变时应及时更新datatype


    private void syncNode(PageNode node){
        if (node.getNodeId()!=null){
            UaVariableNode variableNode = dataRequest.getVariableNode(node.getNodeId());
            if (variableNode!=null){
                //  i.setDataType(variableNode.getDataType());
                node.setTypeId(variableNode.getDataType().hashCode());
                node.setDataValue(variableNode.getValue());

            }
            if (!node.isSubscribed()){
                reqSubscription(node);
            }
            if (subscribed.getValue()!=null && !subscribed.getValue())
                reqSubscription(node);
        }

    }

    //同步节点信息
    public void reqSyncNode(){
        pageItems.forEach(i->{

            syncNode(i);
            List<PageNode> list = i.getChild();
            if (list!=null && !list.isEmpty()){
                list.forEach(this::syncNode);
            }

        });
    }


    public void pasteItemSimple(PageNode multi, OPCNode node){
        if (multi.getId() != node.getId()){
            multi.setNodeInfo(node.getNodeInfo());
           // multi.setDataTypeNodeInfo(node.getDataTypeNodeInfo());
            multi.setTypeId(node.getTypeId());
            U.getCacheThreadPool().execute(() ->  reqSubscription(multi));

        }else {
            multi.setTypeId(node.getTypeId());
          //  multi.setDataType(node.getDataType());
        }
    }

    public void pasteItemAll(PageNode multi, OPCNode node){
        multi.setName(node.getName());
        multi.setValue(node.getValue().get());
        pasteItemSimple(multi,node);
    }

    public  void reqSubscription(PageNode node){
        dataRequest.reqSubscription(node, keepLive);

        //  U.getCacheThreadPool().execute(() -> dataRequest.reqSubscription(multi,keepSubscription));

    }
    public  void reqSubscriptionAll(){
        dataRequest.reqSubscriptionAll(map, keepLive);

        //  U.getCacheThreadPool().execute(() -> dataRequest.reqSubscription(multi,keepSubscription));

    }

    public  void reqBrowseAll(){
        dataRequest.reqBrowseAll(map, keepLive);

        //  U.getCacheThreadPool().execute(() -> dataRequest.reqSubscription(multi,keepSubscription));

    }


    public void changeNodeInfo(PageNode item, String info, IBooleanCallBack callBack){

        if (item.setNodeInfo(info)){
            U.getCacheThreadPool().execute(() -> {
                UaVariableNode variableNode = dataRequest.getVariableNode(item.getNodeId());
                if (variableNode!=null){
                    item.setDataValue(variableNode.getValue());
                   // item.setDataType(variableNode.getDataType());
                    item.setTypeId(variableNode.getDataType().hashCode());
                }
                callBack.callBack(true);
            });
        }else {
            callBack.callBack(false);
        }
    }


}
