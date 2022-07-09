package com.viper.app.data.repository;


import android.util.Log;

import androidx.collection.ArrayMap;
import androidx.lifecycle.MutableLiveData;

import com.viper.app.R;
import com.viper.app.data.bean.OPCNode;
import com.viper.app.data.bean.PageNode;
import com.viper.app.data.client.ClientManger;
import com.viper.app.data.client.OPCUtil;
import com.viper.app.domain.message.OpcData2Result;
import com.viper.app.ui.view.IBooleanCallBack;
import com.viper.app.util.U;

import com.viper.opc.client.opcua.sdk.client.OpcUaClient;
import com.viper.opc.client.opcua.sdk.client.nodes.UaVariableNode;
import com.viper.opc.client.opcua.stack.core.types.builtin.DataValue;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;
import com.viper.opc.client.opcua.stack.core.types.enumerated.TimestampsToReturn;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 页面数据仓库
 */
public class BasePageDataRepository {
    protected String opcUri;

    protected ScheduledExecutorService scheduledFuture;

 //   protected OpcUaClient client;


    public BasePageDataRepository(String opcUri) {
        this.opcUri = opcUri;
    }

    public BasePageDataRepository() {
    }



    public synchronized void writeValueToPLC(PageNode node, String str, IBooleanCallBack callBack) {
        U.getCacheThreadPool().execute(() -> {
            try {
                callBack.callBack(OPCUtil.writeDataToOpc(getClient(), node, str));
            } catch (Exception e) {
                callBack.callBack(false);
                //todo 写入失败
                e.printStackTrace();
                U.showShortToast(this.opcUri + U.getString(R.string.write_to_node_wrong));
            }
        });
    }


    public synchronized void writeValueToPLC(PageNode node, boolean b, IBooleanCallBack callBack) {
        U.getCacheThreadPool().execute(() -> {
            try {
                callBack.callBack(OPCUtil.writeDataToOpc(getClient(), node, b, this.opcUri));
            } catch (Exception e) {
                callBack.callBack(false);
                //todo 写入失败
                e.printStackTrace();
                U.showShortToast(this.opcUri + U.getString(R.string.write_to_node_wrong));
            }
        });
    }


    public UaVariableNode getVariableNode(NodeId nodeId){
        return OPCUtil.getVariableNode(getClient(),nodeId);
    }

    public void setOpcUri(String opcUri) {
        this.opcUri = opcUri;
    }


    public OpcUaClient getClient() {
        if (U.isEmpty(opcUri)){
            try {
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return ClientManger.getOpcUaClient(opcUri);
    }


    public void subscription(PageNode node, MutableLiveData<Boolean> flag){

        CompletableFuture<OpcUaClient> c = CompletableFuture.supplyAsync(this::getClient);
        c.whenComplete((v, a) -> {
            try {
                OPCUtil.subscription(v,node,flag);
            }catch (Exception e){
                e.printStackTrace();
            }

        });

    }

    public void subscriptionAll(ArrayMap<Integer, PageNode> map, OpcData2Result.Result<Integer, String> result, MutableLiveData<Boolean> flag){
        try {
            getClient().connect().get();
            OPCUtil.subscription(getClient(),map,result,flag);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void reqBrowseAll(ArrayMap<Integer, PageNode> map, OpcData2Result.Result<Integer, String> result, MutableLiveData<Boolean> flag){

        if (scheduledFuture == null || scheduledFuture.isShutdown()) {
            final List<NodeId> nodeIdList = new ArrayList<>();
            final boolean[] booleans = new boolean[1];
            scheduledFuture = Executors.newSingleThreadScheduledExecutor();
            scheduledFuture.scheduleWithFixedDelay(() -> {

                if (U.isTrue(flag)) {

                    try {
                        getClient().connect().get();

                        if (!booleans[0]){
                            map.forEach((i,p)->{
                                nodeIdList.add(p.getNodeId());
                            });
                            booleans[0] = true;
                        }
                        List<DataValue> dataValues = getClient().readValues(0.0, TimestampsToReturn.Both, nodeIdList).get();
                        for (int i = 0; i < dataValues.size(); i++) {
                            Objects.requireNonNull(map.get(nodeIdList.get(i).hashCode())).setDataValue(dataValues.get(i));
                        }

                      //  OPCUtil.subscription(getClient(),map,result,flag);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                   /*
                    try {
                        getClient().connect().get();
                        if (list.size()>0){
                            list.forEach(OPCNode::getNewValue);
                        }

                    } catch (Exception e) {
                        failTimes += 1;
                        if (failTimes > 5) {
                            scheduledFuture.shutdownNow();
                        }
                        e.printStackTrace();
                        //todo 遍历出错
                        // U.showShortToast(BaseOpcDataRepository.this.opcUri + U.getString(R.string.browse_fail));
                    }*/

                } else {
                    scheduledFuture.shutdownNow();
                    scheduledFuture = null;
                }

            }, 0, 1000, TimeUnit.MILLISECONDS);

        }


    }
    public void stopRefresh(){
        if (scheduledFuture != null){
            scheduledFuture.shutdownNow();

        }

        while(true){
            if ((scheduledFuture==null || scheduledFuture.isShutdown()))
                break;
        }
        scheduledFuture = null;

    }

    public void destroy() {
        if (scheduledFuture != null) {
            scheduledFuture.shutdownNow();
            scheduledFuture = null;
        }
    }
}
