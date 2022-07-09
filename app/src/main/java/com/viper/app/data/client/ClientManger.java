package com.viper.app.data.client;

import android.util.ArrayMap;

import com.viper.opc.client.opcua.sdk.client.OpcUaClient;


/**
 * 客户端管理类
 */
public final class ClientManger {

    private static final ClientManger INSTANCE = new ClientManger();

    private ArrayMap<String,OPCClientRunner> clientArrayMap;


    private ClientManger(){

    }

    private static ArrayMap<String, OPCClientRunner> getClientSimpleArrayMap() {
        if (INSTANCE.clientArrayMap ==null){
            INSTANCE.clientArrayMap = new ArrayMap<>();
        }
        return INSTANCE.clientArrayMap;
    }

    public static OpcUaClient getOpcUaClient(String opcUri){
        OPCClientRunner runner = getClientSimpleArrayMap().get(opcUri);
        if (runner == null){
            runner = new OPCClientRunner(opcUri);
            OpcUaClient client = runner.getClient();
            getClientSimpleArrayMap().put(opcUri,runner);
            return client;
        }else {
            OpcUaClient client = runner.getClient();
            if (client == null){
                runner.run();
                return runner.getClient();
            }else {
                return client;
            }
        }
    }

    public static void destroy(){
        if (INSTANCE.clientArrayMap !=null && !INSTANCE.clientArrayMap.isEmpty()){
            INSTANCE.clientArrayMap.forEach((k, v)->{
                v.complete();
            });
        }
    }

}
