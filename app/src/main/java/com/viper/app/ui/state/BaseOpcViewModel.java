package com.viper.app.ui.state;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayMap;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.reflect.TypeToken;
import com.viper.app.R;
import com.viper.app.data.bean.OPCNode;
import com.viper.app.data.bean.OPCSetting;
import com.viper.app.data.bean.PageNode;
import com.viper.app.domain.request.BaseOPCNodeRequest;
import com.viper.app.ui.view.IBooleanCallBack;
import com.viper.app.util.U;

import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BaseOpcViewModel extends ViewModel {
    public final MutableLiveData<String> title = new MutableLiveData<>();
    public final ObservableArrayList<OPCNode> appbarNodeList = new ObservableArrayList<>();
    public final MutableLiveData<NodeId> currentSelectNode = new MutableLiveData<>();
    public final MutableLiveData<Boolean> keepSubscription = new MutableLiveData<>(true);
    public final ObservableArrayList<OPCNode> opcNodes = new ObservableArrayList<>();
    public final MutableLiveData<OPCNode> rootNode = new MutableLiveData<>();
    public final MutableLiveData<Boolean> keepLiveData = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> keepLiveDataLastState = new MutableLiveData<>(false);
    public final MutableLiveData<OPCSetting> opcSetting = new MutableLiveData<>();
    public BaseOPCNodeRequest request;


    public void setRequest(BaseOPCNodeRequest request) {
        this.request = request;
    }


    public void reqWriteDataToOPC(OPCNode item, String value, IBooleanCallBack callBack) {
        request.reqWriteDataToOPC(item, value, callBack);
    }


    public void reqBrowseCurrentNode(NodeId id,MutableLiveData<ArrayMap<Integer, List<OPCNode>>> cache) {
        request.reqBrowseNode(id, keepLiveData, cache);
    }

    public void reqLiveData() {
        request.reqRefreshValue(opcNodes, keepLiveData);
    }


    public BaseOPCNodeRequest getRequest() {
        return request;
    }

    public void reBrowse() {

        keepLiveData.setValue(false);
        rootNode.setValue(null);
        keepLiveData.setValue(false);
        request.reBrowse();
        appbarNodeList.clear();
        opcNodes.clear();
    }


    public void loadDataFromLastScan(OPCNode rootNode) {
        this.rootNode.postValue(rootNode);
      //  request.loadDataFromLastScan(rootNode);
        appbarNodeList.clear();
        opcNodes.clear();
        opcNodes.add(rootNode);
    }


    public void saveResult(String fileName,MutableLiveData<ArrayMap<Integer, List<OPCNode>>> cache) {
        U.getCacheThreadPool().execute(() ->
            {
                OPCNode root = rootNode.getValue();
                ArrayMap<Integer, List<OPCNode>> map = cache.getValue();
                if (map == null) {
                    map = new ArrayMap<>();
                }
                if (root != null) {
                    beforeSave(root, map);
                    U.writeFile(fileName, U.toJson(rootNode.getValue()));
                }

                cache.postValue(map);

                if (!map.isEmpty()) {
                    Type type = new TypeToken<ArrayMap<Integer, List<OPCNode>>>() {
                    }.getType();
                    U.writeFile(U.getString(R.string.cache_file_name), U.toJson(map, type));
                }
            }
        );
    }

    private void beforeSave(OPCNode root, ArrayMap<Integer, List<OPCNode>> map) {

        if (root != null) {
            List<OPCNode> child = root.getChildList();
            if (child != null && !child.isEmpty()) {
                if (!map.containsKey(root.getId())) {
                    map.put(root.getId(), child);
                }
                child.forEach(i -> {
                    beforeSave(i, map);
                });
            }

        }
    }


    public void reqSubscription(NodeId nodeId) {
        request.reqSubscription(nodeId, keepSubscription);
    }

    public void reqBrowseNode() {
        request.requestFirstScan();
    }

    public void reqInit() {
        request.reqInitRepository();

    }

    public void stopRefresh() {
        request.stopRefresh();
    }

}
