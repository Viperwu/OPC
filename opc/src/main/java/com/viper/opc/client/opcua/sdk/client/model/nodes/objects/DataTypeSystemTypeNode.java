package com.viper.opc.client.opcua.sdk.client.model.nodes.objects;

import com.viper.opc.client.opcua.sdk.client.OpcUaClient;
import com.viper.opc.client.opcua.sdk.client.model.types.objects.DataTypeSystemType;
import com.viper.opc.client.opcua.stack.core.types.builtin.LocalizedText;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;
import com.viper.opc.client.opcua.stack.core.types.builtin.QualifiedName;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UByte;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.viper.opc.client.opcua.stack.core.types.enumerated.NodeClass;

public class DataTypeSystemTypeNode extends BaseObjectTypeNode implements DataTypeSystemType {
    public DataTypeSystemTypeNode(OpcUaClient client, NodeId nodeId, NodeClass nodeClass,
                                  QualifiedName browseName, LocalizedText displayName, LocalizedText description,
                                  UInteger writeMask, UInteger userWriteMask, UByte eventNotifier) {
        super(client, nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask, eventNotifier);
    }
}
