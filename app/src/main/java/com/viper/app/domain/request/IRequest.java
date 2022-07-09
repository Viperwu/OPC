package com.viper.app.domain.request;

import com.viper.opc.client.opcua.sdk.client.nodes.UaVariableNode;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;

public interface IRequest {
    UaVariableNode getVariableNode(NodeId nodeId);
}
