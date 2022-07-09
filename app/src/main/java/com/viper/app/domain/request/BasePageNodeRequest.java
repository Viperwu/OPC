package com.viper.app.domain.request;

import androidx.collection.ArrayMap;
import androidx.lifecycle.MutableLiveData;

import com.viper.app.data.bean.PageNode;
import com.viper.app.data.repository.BasePageDataRepository;
import com.viper.app.domain.message.OpcData2Result;
import com.viper.app.ui.view.IBooleanCallBack;
import com.viper.base.domain.request.BaseRequest;
import com.viper.base.livedata.ProtectedUnPeekLiveData;
import com.viper.base.livedata.UnPeekLiveData;

import com.viper.opc.client.opcua.sdk.client.nodes.UaVariableNode;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;

/**
 * 页面 数据请求类
 */
public class BasePageNodeRequest extends BaseRequest implements IRequest{

   protected final UnPeekLiveData<OpcData2Result<Integer, String>> dataResult = new UnPeekLiveData<>();

    protected BasePageDataRepository repository;

    public BasePageNodeRequest(BasePageDataRepository repository) {
        this.repository = repository;
    }

    public void reqSubscription(PageNode nodeId, MutableLiveData<Boolean> flag) {
        repository.subscription(nodeId,flag);
    }

    public void reqSubscriptionAll(ArrayMap<Integer, PageNode> map, MutableLiveData<Boolean> flag) {
        //  repository.subscription(list,flag);
        repository.subscriptionAll(map,dataResult::postValue,flag);
    }
    public void reqBrowseAll(ArrayMap<Integer, PageNode> map, MutableLiveData<Boolean> flag) {
        //  repository.subscription(list,flag);
        repository.reqBrowseAll(map,dataResult::postValue,flag);
    }

    public void reqWriteDataToOPC(PageNode item, String value, IBooleanCallBack callBack){
        repository.writeValueToPLC(item,value,callBack);
    }

    public void reqWriteDataToOPC(PageNode item, boolean value, IBooleanCallBack callBack){
        repository.writeValueToPLC(item,value,callBack);
    }

    public UaVariableNode getVariableNode(NodeId nodeId){
        return repository.getVariableNode(nodeId);
    }

    public ProtectedUnPeekLiveData<OpcData2Result<Integer, String>> getDataResult() {
        return dataResult;
    }

/*public ProtectedUnPeekLiveData<DataResult<SubscriptionOPCNode>> getSubOPCNode() {
        return subOPCNode;
    }*/

    public BasePageDataRepository getRepository() {
        return repository;
    }


}
