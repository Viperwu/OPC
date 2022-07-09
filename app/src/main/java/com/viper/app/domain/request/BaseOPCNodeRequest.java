package com.viper.app.domain.request;

import androidx.collection.ArrayMap;
import androidx.lifecycle.MutableLiveData;

import com.viper.app.data.bean.OPCNode;
import com.viper.app.data.bean.SubscriptionOPCNode;
import com.viper.app.data.repository.BaseOpcDataRepository;
import com.viper.app.domain.message.OpcData2Result;
import com.viper.app.domain.message.OpcDataResult;
import com.viper.app.ui.view.IBooleanCallBack;
import com.viper.base.domain.request.BaseRequest;
import com.viper.base.livedata.ProtectedUnPeekLiveData;
import com.viper.base.livedata.UnPeekLiveData;

import com.viper.opc.client.opcua.sdk.client.nodes.UaNode;
import com.viper.opc.client.opcua.sdk.client.nodes.UaVariableNode;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;

import java.util.List;


/**
 * opc 数据请求类
 */
public class BaseOPCNodeRequest extends BaseRequest implements IRequest{

    protected final UnPeekLiveData<OpcDataResult<List<OPCNode>>> opcNodesLiveData = new UnPeekLiveData<>();
    protected final UnPeekLiveData<OpcDataResult<List<UaNode>>> uaNodeList = new UnPeekLiveData<>();
    protected final UnPeekLiveData<OpcDataResult<SubscriptionOPCNode>> subOPCNode = new UnPeekLiveData<>();
    protected final UnPeekLiveData<OpcData2Result<List<OPCNode>,NodeId>> onlineList = new UnPeekLiveData<>();
   // protected final UnPeekLiveData<OpcDataResult<SimpleArrayMap<Integer,OPCNode>>> globalOPCNodeMap = new UnPeekLiveData<>();
    protected BaseOpcDataRepository repository;
    public ProtectedUnPeekLiveData<OpcDataResult<List<UaNode>>> getUaNodeList() {
        return uaNodeList;
    }

    public BaseOPCNodeRequest(BaseOpcDataRepository repository) {
        this.repository = repository;
    }

    public void requestFirstScan() {

       // repository.firstScan(opcNodesLiveData::postValue, globalOPCNodeMap::postValue);
        repository.firstScan(opcNodesLiveData::postValue, null);
    }

    public void reqSubscription(NodeId nodeId, MutableLiveData<Boolean> flag) {
        repository.subscription(nodeId,subOPCNode::postValue,flag);
    }

    public UaVariableNode getVariableNode(NodeId nodeId){
        return repository.getVariableNode(nodeId);
    }



    public ProtectedUnPeekLiveData<OpcDataResult<List<OPCNode>>> getOpcNodesLiveData() {
        return opcNodesLiveData;
    }

    public ProtectedUnPeekLiveData<OpcData2Result<List<OPCNode>,NodeId>> getOnlineList() {
        return onlineList;
    }

 /*   public ProtectedUnPeekLiveData<OpcDataResult<SimpleArrayMap<Integer,OPCNode>>> getGlobalOPCNodeMap() {
        return globalOPCNodeMap;
    }*/

    public ProtectedUnPeekLiveData<OpcDataResult<SubscriptionOPCNode>> getSubOPCNode() {
        return subOPCNode;
    }

    public BaseOpcDataRepository getRepository() {
        return repository;
    }

    public void setRepository(BaseOpcDataRepository repository) {
        this.repository = repository;
    }

    public void reBrowse(){
        opcNodesLiveData.setValue(null);
       // globalOPCNodeMap.clear();
        uaNodeList.clear();
       // repository.reBrowse(opcNodesLiveData::postValue, globalOPCNodeMap::postValue);
        repository.reBrowse(opcNodesLiveData::postValue, null);

    }
    public void reqWriteDataToOPC(OPCNode item, String value, IBooleanCallBack callBack){
        repository.writeValueToPLC(item,value,callBack);
    }


    public void reqInitRepository(){
        repository.initData(opcNodesLiveData::postValue,null);
    }

    public void reqBrowseNode(NodeId nodeId, MutableLiveData<Boolean> flag,MutableLiveData<ArrayMap<Integer, List<OPCNode>>> cache) {
        repository.browseNode(nodeId,onlineList::postValue,cache);
    }

    public void reqRefreshValue(List<OPCNode> list,MutableLiveData<Boolean> flag) {
        repository.refreshValue(list,flag);
    }

    public void stopRefresh(){
        repository.stopRefresh();
    }
}
