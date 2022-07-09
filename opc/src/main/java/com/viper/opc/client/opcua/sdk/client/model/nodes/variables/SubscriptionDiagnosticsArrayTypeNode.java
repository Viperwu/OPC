package com.viper.opc.client.opcua.sdk.client.model.nodes.variables;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.viper.opc.client.opcua.sdk.client.OpcUaClient;
import com.viper.opc.client.opcua.sdk.client.model.types.variables.SubscriptionDiagnosticsArrayType;
import com.viper.opc.client.opcua.sdk.client.nodes.UaNode;
import com.viper.opc.client.opcua.stack.core.AttributeId;
import com.viper.opc.client.opcua.stack.core.StatusCodes;
import com.viper.opc.client.opcua.stack.core.UaException;
import com.viper.opc.client.opcua.stack.core.types.builtin.DataValue;
import com.viper.opc.client.opcua.stack.core.types.builtin.ExpandedNodeId;
import com.viper.opc.client.opcua.stack.core.types.builtin.ExtensionObject;
import com.viper.opc.client.opcua.stack.core.types.builtin.LocalizedText;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;
import com.viper.opc.client.opcua.stack.core.types.builtin.QualifiedName;
import com.viper.opc.client.opcua.stack.core.types.builtin.StatusCode;
import com.viper.opc.client.opcua.stack.core.types.builtin.Variant;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UByte;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.viper.opc.client.opcua.stack.core.types.enumerated.NodeClass;
import com.viper.opc.client.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;

public class SubscriptionDiagnosticsArrayTypeNode extends BaseDataVariableTypeNode implements SubscriptionDiagnosticsArrayType {
    public SubscriptionDiagnosticsArrayTypeNode(OpcUaClient client, NodeId nodeId,
                                                NodeClass nodeClass, QualifiedName browseName, LocalizedText displayName,
                                                LocalizedText description, UInteger writeMask, UInteger userWriteMask, DataValue value,
                                                NodeId dataType, Integer valueRank, UInteger[] arrayDimensions, UByte accessLevel,
                                                UByte userAccessLevel, Double minimumSamplingInterval, Boolean historizing) {
        super(client, nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask, value, dataType, valueRank, arrayDimensions, accessLevel, userAccessLevel, minimumSamplingInterval, historizing);
    }

    @Override
    public SubscriptionDiagnosticsDataType getSubscriptionDiagnostics() throws UaException {
        SubscriptionDiagnosticsTypeNode node = getSubscriptionDiagnosticsNode();
        return cast(node.getValue().getValue().getValue(), SubscriptionDiagnosticsDataType.class);
    }

    @Override
    public void setSubscriptionDiagnostics(SubscriptionDiagnosticsDataType subscriptionDiagnostics)
        throws UaException {
        SubscriptionDiagnosticsTypeNode node = getSubscriptionDiagnosticsNode();
        ExtensionObject value = ExtensionObject.encode(client.getStaticSerializationContext(), subscriptionDiagnostics);
        node.setValue(new Variant(value));
    }

    @Override
    public SubscriptionDiagnosticsDataType readSubscriptionDiagnostics() throws UaException {
        try {
            return readSubscriptionDiagnosticsAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public void writeSubscriptionDiagnostics(SubscriptionDiagnosticsDataType subscriptionDiagnostics)
        throws UaException {
        try {
            writeSubscriptionDiagnosticsAsync(subscriptionDiagnostics).get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends SubscriptionDiagnosticsDataType> readSubscriptionDiagnosticsAsync(
    ) {
        return getSubscriptionDiagnosticsNodeAsync().thenCompose(node -> node.readAttributeAsync(AttributeId.Value)).thenApply(v -> cast(v.getValue().getValue(), SubscriptionDiagnosticsDataType.class));
    }

    @Override
    public CompletableFuture<StatusCode> writeSubscriptionDiagnosticsAsync(
        SubscriptionDiagnosticsDataType subscriptionDiagnostics) {
        ExtensionObject encoded = ExtensionObject.encode(client.getStaticSerializationContext(), subscriptionDiagnostics);
        DataValue value = DataValue.valueOnly(new Variant(encoded));
        return getSubscriptionDiagnosticsNodeAsync()
            .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
    }

    @Override
    public SubscriptionDiagnosticsTypeNode getSubscriptionDiagnosticsNode() throws UaException {
        try {
            return getSubscriptionDiagnosticsNodeAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends SubscriptionDiagnosticsTypeNode> getSubscriptionDiagnosticsNodeAsync(
    ) {
        CompletableFuture<UaNode> future = getMemberNodeAsync("http://opcfoundation.org/UA/", "SubscriptionDiagnostics", ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=47"), false);
        return future.thenApply(node -> (SubscriptionDiagnosticsTypeNode) node);
    }
}
