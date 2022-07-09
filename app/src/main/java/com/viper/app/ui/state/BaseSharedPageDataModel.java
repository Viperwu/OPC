package com.viper.app.ui.state;

import androidx.databinding.ObservableArrayMap;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.viper.app.data.bean.OPCNode;
import com.viper.app.data.bean.PageSetting;
import com.viper.app.data.bean.SubscriptionOPCNode;
import com.viper.app.data.repository.BasePageDataRepository;
import com.viper.app.domain.request.BaseOPCNodeRequest;
import com.viper.app.ui.view.IBooleanCallBack;
import com.viper.base.data.response.DataResult;
import com.viper.base.livedata.UnPeekLiveData;

import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;

public class BaseSharedPageDataModel extends ViewModel {

    public final ObservableArrayMap<Integer, OPCNode> map = new ObservableArrayMap<>();

    protected final UnPeekLiveData<DataResult<SubscriptionOPCNode>> subOPCNode = new UnPeekLiveData<>();
    public final  MutableLiveData<PageSetting> pageSetting  =new MutableLiveData<>();
    protected BasePageDataRepository repository;
    public BaseOPCNodeRequest plcNodeRequest;
    public void reqWriteDataToOPC(OPCNode item, String value, IBooleanCallBack callBack){
        plcNodeRequest.reqWriteDataToOPC(item,value,callBack);
    }

    public void reqSubscription(NodeId nodeId) {
       // repository.subscription(nodeId,subOPCNode::postValue);
    }

}
