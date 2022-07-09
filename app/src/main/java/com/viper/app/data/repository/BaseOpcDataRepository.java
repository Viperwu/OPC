package com.viper.app.data.repository;


import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;
import androidx.lifecycle.MutableLiveData;

import com.viper.app.R;
import com.viper.app.data.bean.OPCNode;
import com.viper.app.data.bean.SubscriptionOPCNode;
import com.viper.app.data.client.ClientManger;
import com.viper.app.domain.message.OpcData2Result;
import com.viper.app.domain.message.OpcDataResult;
import com.viper.app.ui.view.IBooleanCallBack;
import com.viper.app.util.U;
import com.viper.app.data.client.OPCUtil;

import com.viper.opc.client.opcua.sdk.client.OpcUaClient;
import com.viper.opc.client.opcua.sdk.client.nodes.UaNode;
import com.viper.opc.client.opcua.sdk.client.nodes.UaVariableNode;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * opc 数据仓库
 */
public class BaseOpcDataRepository {
    protected String opcUri;
    protected ScheduledExecutorService scheduledFuture;
    protected ExecutorService scheduledFuture1;
    protected ScheduledExecutorService scheduledFuture2;
    protected OPCNode opcRootNode;
  //  protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected UaNode uaRootNode;
    // protected final CompletableFuture<OpcUaClient> future = new CompletableFuture<>();
   // protected SimpleArrayMap<Integer, OPCNode> map;
    //  private static final Logger logger = LoggerFactory.getLogger(PLCDataRepository.class);
    protected boolean firstScanFinished;
    protected boolean dataInit;
    protected boolean startInitData;
    protected boolean reqNewData;
    protected int failTimes;
    protected List<OPCNode> currentShowList;
    protected boolean firstScanStarted;
    //  protected OpcUaClient client;
    protected NodeId scanNodeId;
    protected NodeId browseNodeId;
    protected boolean loadDataFromLastScan;

    public BaseOpcDataRepository() {
    }


    public void setScanNodeId(NodeId scanNodeId) {
        this.scanNodeId = scanNodeId;
    }


    public synchronized void initData(OpcDataResult.Result<List<OPCNode>> resultList,
                                      OpcDataResult.Result<SimpleArrayMap<Integer, OPCNode>> resultMap) {
        if (!dataInit) {
            if (!startInitData) {
                startInitData = true;
                 U.getCacheThreadPool().execute(() -> {
                     try {
                         getClient().connect().get();
                         try {
                             //uaRootNode = client.getAddressSpace().getNode(Identifiers.RootFolder);
                             if (loadDataFromLastScan) return;
                             if (scanNodeId == null) {
                                 U.showShortToast(this.opcUri + U.getString(R.string.nodeid_no_init));
                                 return;
                             }

                             uaRootNode = getClient().getAddressSpace().getNode(scanNodeId);

                             opcRootNode = new OPCNode(uaRootNode);

                             // opcRootNode.setLevel(-1);
                             // opcRootNode.setExpand(false);
                             currentShowList = new ArrayList<>();
                             currentShowList.add(opcRootNode);
                             dataInit = true;
                             resultList.onResult(new OpcDataResult<>(currentShowList));
                             U.showShortToast(this.opcUri + U.getString(R.string.connect_success));

                             // logger.info("init finish");
                         } catch (Exception e) {
                             // opcRootNode1 = new OPCNode("root", "");
                             // logger.error("{}",scanNodeId.getIdentifier());
                             startInitData = false;
                             U.showShortToast(this.opcUri + U.getString(R.string.browse_root_fail));
                             e.printStackTrace();
                         }
                     } catch (Exception e) {
                         startInitData = false;
                         dataInit = false;
                         e.printStackTrace();
                         U.showNotification(this.opcUri + U.getString(R.string.connection_fail), 1);
                     }
                 });


                // });
            }

        }
    }

    public OpcUaClient getClient() {

        return ClientManger.getOpcUaClient(opcUri);
    }

    public synchronized void firstScan(OpcDataResult.Result<List<OPCNode>> resultList,
                                       OpcDataResult.Result<SimpleArrayMap<Integer, OPCNode>> resultMap) {

        if (!firstScanFinished) {
            if (!firstScanStarted) {

                CompletableFuture<Object> c = CompletableFuture.supplyAsync(() -> {
                    initData(resultList, resultMap);
                    firstScanStarted = true;
                    return null;
                });
                c.whenComplete((v, a) -> {
                    resultList.onResult(new OpcDataResult<>(currentShowList));
                    try {
                        getClient().connect().get();
                    } catch (Exception e) {
                        e.printStackTrace();
                        //todo 连接失败
                        U.showShortToast(U.getString(R.string.connection_fail));

                        firstScanStarted = false;
                        firstScanFinished = false;

                    }

                    if (firstScanStarted) {
                        //map = OPCUtil.browseNode(getClient(), scanNodeId, opcRootNode);
                        OPCUtil.browseNode(getClient(), scanNodeId, opcRootNode);
                      // resultMap.onResult(new OpcDataResult<>(map));
                        firstScanFinished = true;
                        //TODO 遍历成功
                        U.showNotification(this.opcUri + U.getString(R.string.browse_success),9);
                    }
                });
            }

        } else {
            resultList.onResult(new OpcDataResult<>(currentShowList));
           // resultMap.onResult(new OpcDataResult<>(map));
        }

    }


    public boolean isFirstScanFinished() {
        return firstScanFinished;
    }


    public synchronized void writeValueToPLC(OPCNode node, String str, IBooleanCallBack callBack) {
        U.getCacheThreadPool().execute(() -> {
            try {
                callBack.callBack(OPCUtil.writeDataToOpc(getClient(), node, str));
            } catch (Exception e) {
                //todo 写入失败
                callBack.callBack(false);
                e.printStackTrace();
                // U.showShortToast(this.opcUri + U.getString(R.string.write_to_node_wrong));
            }
        });
    }

    public void destroy() {
        // future.complete(client);
        if (scheduledFuture != null) {
            scheduledFuture.shutdownNow();
            scheduledFuture = null;
        }

        if (scheduledFuture2 != null){
            scheduledFuture2.shutdownNow();
            scheduledFuture2 = null;
        }
        if (scheduledFuture1 != null){
            scheduledFuture1.shutdownNow();
            scheduledFuture1 = null;
        }
    }

    public void stopRefresh(){
        if (scheduledFuture2 != null){
            scheduledFuture2.shutdownNow();

        }
        if (scheduledFuture1 != null){
            scheduledFuture1.shutdownNow();

        }
        while(true){
            if ((scheduledFuture2==null || scheduledFuture2.isShutdown())&&(scheduledFuture1==null||scheduledFuture1.isShutdown()))
            break;
        }
        scheduledFuture2 = null;
        scheduledFuture1 = null;
    }

    public synchronized void browseNode(@NonNull NodeId nodeId, OpcData2Result.Result<List<OPCNode>, NodeId> result, MutableLiveData<ArrayMap<Integer, List<OPCNode>>> cache) {
        stopRefresh();

        if (scheduledFuture1 == null || scheduledFuture1.isShutdown()) {
            scheduledFuture1 = Executors.newSingleThreadExecutor();
            scheduledFuture1.execute(() -> {
                try {
                    //getClient().connect().get();
                    List<? extends UaNode> list = OPCUtil.browseNode(getClient(), nodeId);
                    if (list != null && !list.isEmpty()) {
                        List<OPCNode> opcNodeList = new ArrayList<>();
                        list.forEach(i -> {
                            opcNodeList.add(new OPCNode(i));
                        });
                        ArrayMap<Integer, List<OPCNode>> map = cache.getValue();
                        if (map==null){
                            map = new ArrayMap<>();
                        }
                        map.put(nodeId.hashCode(),opcNodeList);
                        cache.postValue(map);
                        result.onResult(new OpcData2Result<>(opcNodeList, nodeId));
                    }

//                            Thread.sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                    //todo 遍历出错
                   // U.showShortToast(BaseOpcDataRepository.this.opcUri + U.getString(R.string.browse_fail));
                }
            });

        }


    }


    public synchronized void refreshValue(List<OPCNode> list, MutableLiveData<Boolean> flag) {
        stopRefresh();
        failTimes = 0;
        if (scheduledFuture2 == null || scheduledFuture2.isShutdown()) {
            scheduledFuture2 = Executors.newSingleThreadScheduledExecutor();
            scheduledFuture2.scheduleWithFixedDelay(() -> {

                if (U.isTrue(flag)) {
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
                    }

                } else {
                    scheduledFuture2.shutdownNow();
                    scheduledFuture2 = null;
                }

            }, 0, 200, TimeUnit.MILLISECONDS);

        }
    }


    public void setOpcUri(String opcUri) {
        this.opcUri = opcUri;
    }

    public void reBrowse(OpcDataResult.Result<List<OPCNode>> resultList,
                         OpcDataResult.Result<SimpleArrayMap<Integer, OPCNode>> resultMap) {
        reInit();
        firstScan(resultList, resultMap);
    }

    public void reInit() {
        firstScanFinished = false;
        dataInit = false;
        startInitData = false;
        reqNewData = false;
        loadDataFromLastScan = false;
        currentShowList = null;
        firstScanStarted = false;
        if (scheduledFuture != null) {
            scheduledFuture.shutdownNow();
            scheduledFuture = null;
        }

    }


    public void subscription(List<NodeId> list, OpcDataResult.Result<SubscriptionOPCNode> result, MutableLiveData<Boolean> flag) {
        U.getCacheThreadPool().execute(() -> {
            try {
                OPCUtil.subscription(getClient(), list, result, flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    public void subscription(NodeId nodeId, OpcDataResult.Result<SubscriptionOPCNode> result, MutableLiveData<Boolean> flag) {

        U.getCacheThreadPool().execute(() -> {
            try {
                OPCUtil.subscription(getClient(), nodeId, result, flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    public UaVariableNode getVariableNode(NodeId nodeId) {
        return OPCUtil.getVariableNode(getClient(), nodeId);
    }
}
