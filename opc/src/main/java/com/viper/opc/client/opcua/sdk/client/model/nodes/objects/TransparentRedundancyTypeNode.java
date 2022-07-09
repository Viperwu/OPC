package com.viper.opc.client.opcua.sdk.client.model.nodes.objects;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.viper.opc.client.opcua.sdk.client.OpcUaClient;
import com.viper.opc.client.opcua.sdk.client.model.nodes.variables.PropertyTypeNode;
import com.viper.opc.client.opcua.sdk.client.model.types.objects.TransparentRedundancyType;
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
import com.viper.opc.client.opcua.stack.core.types.structured.RedundantServerDataType;

public class TransparentRedundancyTypeNode extends ServerRedundancyTypeNode implements TransparentRedundancyType {
    public TransparentRedundancyTypeNode(OpcUaClient client, NodeId nodeId, NodeClass nodeClass,
                                         QualifiedName browseName, LocalizedText displayName, LocalizedText description,
                                         UInteger writeMask, UInteger userWriteMask, UByte eventNotifier) {
        super(client, nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask, eventNotifier);
    }

    @Override
    public String getCurrentServerId() throws UaException {
        PropertyTypeNode node = getCurrentServerIdNode();
        return (String) node.getValue().getValue().getValue();
    }

    @Override
    public void setCurrentServerId(String currentServerId) throws UaException {
        PropertyTypeNode node = getCurrentServerIdNode();
        node.setValue(new Variant(currentServerId));
    }

    @Override
    public String readCurrentServerId() throws UaException {
        try {
            return readCurrentServerIdAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public void writeCurrentServerId(String currentServerId) throws UaException {
        try {
            writeCurrentServerIdAsync(currentServerId).get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends String> readCurrentServerIdAsync() {
        return getCurrentServerIdNodeAsync().thenCompose(node -> node.readAttributeAsync(AttributeId.Value)).thenApply(v -> (String) v.getValue().getValue());
    }

    @Override
    public CompletableFuture<StatusCode> writeCurrentServerIdAsync(String currentServerId) {
        DataValue value = DataValue.valueOnly(new Variant(currentServerId));
        return getCurrentServerIdNodeAsync()
            .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
    }

    @Override
    public PropertyTypeNode getCurrentServerIdNode() throws UaException {
        try {
            return getCurrentServerIdNodeAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends PropertyTypeNode> getCurrentServerIdNodeAsync() {
        CompletableFuture<UaNode> future = getMemberNodeAsync("http://opcfoundation.org/UA/", "CurrentServerId", ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=46"), false);
        return future.thenApply(node -> (PropertyTypeNode) node);
    }

    @Override
    public RedundantServerDataType[] getRedundantServerArray() throws UaException {
        PropertyTypeNode node = getRedundantServerArrayNode();
        return cast(node.getValue().getValue().getValue(), RedundantServerDataType[].class);
    }

    @Override
    public void setRedundantServerArray(RedundantServerDataType[] redundantServerArray) throws
        UaException {
        PropertyTypeNode node = getRedundantServerArrayNode();
        ExtensionObject[] encoded = ExtensionObject.encodeArray(client.getStaticSerializationContext(), redundantServerArray);
        node.setValue(new Variant(encoded));
    }

    @Override
    public RedundantServerDataType[] readRedundantServerArray() throws UaException {
        try {
            return readRedundantServerArrayAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public void writeRedundantServerArray(RedundantServerDataType[] redundantServerArray) throws
        UaException {
        try {
            writeRedundantServerArrayAsync(redundantServerArray).get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends RedundantServerDataType[]> readRedundantServerArrayAsync() {
        return getRedundantServerArrayNodeAsync().thenCompose(node -> node.readAttributeAsync(AttributeId.Value)).thenApply(v -> cast(v.getValue().getValue(), RedundantServerDataType[].class));
    }

    @Override
    public CompletableFuture<StatusCode> writeRedundantServerArrayAsync(
        RedundantServerDataType[] redundantServerArray) {
        ExtensionObject[] encoded = ExtensionObject.encodeArray(client.getStaticSerializationContext(), redundantServerArray);
        DataValue value = DataValue.valueOnly(new Variant(encoded));
        return getRedundantServerArrayNodeAsync()
            .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
    }

    @Override
    public PropertyTypeNode getRedundantServerArrayNode() throws UaException {
        try {
            return getRedundantServerArrayNodeAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends PropertyTypeNode> getRedundantServerArrayNodeAsync() {
        CompletableFuture<UaNode> future = getMemberNodeAsync("http://opcfoundation.org/UA/", "RedundantServerArray", ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=46"), false);
        return future.thenApply(node -> (PropertyTypeNode) node);
    }
}
