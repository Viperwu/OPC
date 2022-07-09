package com.viper.app.data.bean;

import com.viper.opc.client.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import com.viper.opc.client.opcua.stack.core.types.builtin.DataValue;


/**
 * opc订阅实体类
 */
public class SubscriptionOPCNode {
    private final UaMonitoredItem item;
    private final DataValue dataValue;

    public SubscriptionOPCNode(UaMonitoredItem item, DataValue dataValue) {
        this.item = item;
        this.dataValue = dataValue;
    }

    public int getId(){
        return item.getReadValueId().getNodeId().hashCode();
    }

    public UaMonitoredItem getItem() {
        return item;
    }

    public DataValue getDataValue() {
        return dataValue;
    }
}
