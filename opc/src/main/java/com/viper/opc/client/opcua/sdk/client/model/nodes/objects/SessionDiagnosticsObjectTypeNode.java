package com.viper.opc.client.opcua.sdk.client.model.nodes.objects;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.viper.opc.client.opcua.sdk.client.OpcUaClient;
import com.viper.opc.client.opcua.sdk.client.model.nodes.variables.SessionDiagnosticsVariableTypeNode;
import com.viper.opc.client.opcua.sdk.client.model.nodes.variables.SessionSecurityDiagnosticsTypeNode;
import com.viper.opc.client.opcua.sdk.client.model.nodes.variables.SubscriptionDiagnosticsArrayTypeNode;
import com.viper.opc.client.opcua.sdk.client.model.types.objects.SessionDiagnosticsObjectType;
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
import com.viper.opc.client.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import com.viper.opc.client.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import com.viper.opc.client.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;

public class SessionDiagnosticsObjectTypeNode extends BaseObjectTypeNode implements SessionDiagnosticsObjectType {
    public SessionDiagnosticsObjectTypeNode(OpcUaClient client, NodeId nodeId, NodeClass nodeClass,
                                            QualifiedName browseName, LocalizedText displayName, LocalizedText description,
                                            UInteger writeMask, UInteger userWriteMask, UByte eventNotifier) {
        super(client, nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask, eventNotifier);
    }

    @Override
    public SessionDiagnosticsDataType getSessionDiagnostics() throws UaException {
        SessionDiagnosticsVariableTypeNode node = getSessionDiagnosticsNode();
        return cast(node.getValue().getValue().getValue(), SessionDiagnosticsDataType.class);
    }

    @Override
    public void setSessionDiagnostics(SessionDiagnosticsDataType sessionDiagnostics) throws
        UaException {
        SessionDiagnosticsVariableTypeNode node = getSessionDiagnosticsNode();
        ExtensionObject value = ExtensionObject.encode(client.getStaticSerializationContext(), sessionDiagnostics);
        node.setValue(new Variant(value));
    }

    @Override
    public SessionDiagnosticsDataType readSessionDiagnostics() throws UaException {
        try {
            return readSessionDiagnosticsAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public void writeSessionDiagnostics(SessionDiagnosticsDataType sessionDiagnostics) throws
        UaException {
        try {
            writeSessionDiagnosticsAsync(sessionDiagnostics).get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends SessionDiagnosticsDataType> readSessionDiagnosticsAsync() {
        return getSessionDiagnosticsNodeAsync().thenCompose(node -> node.readAttributeAsync(AttributeId.Value)).thenApply(v -> cast(v.getValue().getValue(), SessionDiagnosticsDataType.class));
    }

    @Override
    public CompletableFuture<StatusCode> writeSessionDiagnosticsAsync(
        SessionDiagnosticsDataType sessionDiagnostics) {
        ExtensionObject encoded = ExtensionObject.encode(client.getStaticSerializationContext(), sessionDiagnostics);
        DataValue value = DataValue.valueOnly(new Variant(encoded));
        return getSessionDiagnosticsNodeAsync()
            .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
    }

    @Override
    public SessionDiagnosticsVariableTypeNode getSessionDiagnosticsNode() throws UaException {
        try {
            return getSessionDiagnosticsNodeAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends SessionDiagnosticsVariableTypeNode> getSessionDiagnosticsNodeAsync(
    ) {
        CompletableFuture<UaNode> future = getMemberNodeAsync("http://opcfoundation.org/UA/", "SessionDiagnostics", ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=47"), false);
        return future.thenApply(node -> (SessionDiagnosticsVariableTypeNode) node);
    }

    @Override
    public SessionSecurityDiagnosticsDataType getSessionSecurityDiagnostics() throws UaException {
        SessionSecurityDiagnosticsTypeNode node = getSessionSecurityDiagnosticsNode();
        return cast(node.getValue().getValue().getValue(), SessionSecurityDiagnosticsDataType.class);
    }

    @Override
    public void setSessionSecurityDiagnostics(
        SessionSecurityDiagnosticsDataType sessionSecurityDiagnostics) throws UaException {
        SessionSecurityDiagnosticsTypeNode node = getSessionSecurityDiagnosticsNode();
        ExtensionObject value = ExtensionObject.encode(client.getStaticSerializationContext(), sessionSecurityDiagnostics);
        node.setValue(new Variant(value));
    }

    @Override
    public SessionSecurityDiagnosticsDataType readSessionSecurityDiagnostics() throws UaException {
        try {
            return readSessionSecurityDiagnosticsAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public void writeSessionSecurityDiagnostics(
        SessionSecurityDiagnosticsDataType sessionSecurityDiagnostics) throws UaException {
        try {
            writeSessionSecurityDiagnosticsAsync(sessionSecurityDiagnostics).get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends SessionSecurityDiagnosticsDataType> readSessionSecurityDiagnosticsAsync(
    ) {
        return getSessionSecurityDiagnosticsNodeAsync().thenCompose(node -> node.readAttributeAsync(AttributeId.Value)).thenApply(v -> cast(v.getValue().getValue(), SessionSecurityDiagnosticsDataType.class));
    }

    @Override
    public CompletableFuture<StatusCode> writeSessionSecurityDiagnosticsAsync(
        SessionSecurityDiagnosticsDataType sessionSecurityDiagnostics) {
        ExtensionObject encoded = ExtensionObject.encode(client.getStaticSerializationContext(), sessionSecurityDiagnostics);
        DataValue value = DataValue.valueOnly(new Variant(encoded));
        return getSessionSecurityDiagnosticsNodeAsync()
            .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
    }

    @Override
    public SessionSecurityDiagnosticsTypeNode getSessionSecurityDiagnosticsNode() throws UaException {
        try {
            return getSessionSecurityDiagnosticsNodeAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends SessionSecurityDiagnosticsTypeNode> getSessionSecurityDiagnosticsNodeAsync(
    ) {
        CompletableFuture<UaNode> future = getMemberNodeAsync("http://opcfoundation.org/UA/", "SessionSecurityDiagnostics", ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=47"), false);
        return future.thenApply(node -> (SessionSecurityDiagnosticsTypeNode) node);
    }

    @Override
    public SubscriptionDiagnosticsDataType[] getSubscriptionDiagnosticsArray() throws UaException {
        SubscriptionDiagnosticsArrayTypeNode node = getSubscriptionDiagnosticsArrayNode();
        return cast(node.getValue().getValue().getValue(), SubscriptionDiagnosticsDataType[].class);
    }

    @Override
    public void setSubscriptionDiagnosticsArray(
        SubscriptionDiagnosticsDataType[] subscriptionDiagnosticsArray) throws UaException {
        SubscriptionDiagnosticsArrayTypeNode node = getSubscriptionDiagnosticsArrayNode();
        ExtensionObject[] encoded = ExtensionObject.encodeArray(client.getStaticSerializationContext(), subscriptionDiagnosticsArray);
        node.setValue(new Variant(encoded));
    }

    @Override
    public SubscriptionDiagnosticsDataType[] readSubscriptionDiagnosticsArray() throws UaException {
        try {
            return readSubscriptionDiagnosticsArrayAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public void writeSubscriptionDiagnosticsArray(
        SubscriptionDiagnosticsDataType[] subscriptionDiagnosticsArray) throws UaException {
        try {
            writeSubscriptionDiagnosticsArrayAsync(subscriptionDiagnosticsArray).get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends SubscriptionDiagnosticsDataType[]> readSubscriptionDiagnosticsArrayAsync(
    ) {
        return getSubscriptionDiagnosticsArrayNodeAsync().thenCompose(node -> node.readAttributeAsync(AttributeId.Value)).thenApply(v -> cast(v.getValue().getValue(), SubscriptionDiagnosticsDataType[].class));
    }

    @Override
    public CompletableFuture<StatusCode> writeSubscriptionDiagnosticsArrayAsync(
        SubscriptionDiagnosticsDataType[] subscriptionDiagnosticsArray) {
        ExtensionObject[] encoded = ExtensionObject.encodeArray(client.getStaticSerializationContext(), subscriptionDiagnosticsArray);
        DataValue value = DataValue.valueOnly(new Variant(encoded));
        return getSubscriptionDiagnosticsArrayNodeAsync()
            .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
    }

    @Override
    public SubscriptionDiagnosticsArrayTypeNode getSubscriptionDiagnosticsArrayNode() throws
        UaException {
        try {
            return getSubscriptionDiagnosticsArrayNodeAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends SubscriptionDiagnosticsArrayTypeNode> getSubscriptionDiagnosticsArrayNodeAsync(
    ) {
        CompletableFuture<UaNode> future = getMemberNodeAsync("http://opcfoundation.org/UA/", "SubscriptionDiagnosticsArray", ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=47"), false);
        return future.thenApply(node -> (SubscriptionDiagnosticsArrayTypeNode) node);
    }
}
