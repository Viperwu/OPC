package com.viper.opc.client.opcua.sdk.client.model.nodes.objects;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.viper.opc.client.opcua.sdk.client.OpcUaClient;
import com.viper.opc.client.opcua.sdk.client.model.nodes.variables.SessionDiagnosticsArrayTypeNode;
import com.viper.opc.client.opcua.sdk.client.model.nodes.variables.SessionSecurityDiagnosticsArrayTypeNode;
import com.viper.opc.client.opcua.sdk.client.model.types.objects.SessionsDiagnosticsSummaryType;
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

public class SessionsDiagnosticsSummaryTypeNode extends BaseObjectTypeNode implements SessionsDiagnosticsSummaryType {
    public SessionsDiagnosticsSummaryTypeNode(OpcUaClient client, NodeId nodeId, NodeClass nodeClass,
                                              QualifiedName browseName, LocalizedText displayName, LocalizedText description,
                                              UInteger writeMask, UInteger userWriteMask, UByte eventNotifier) {
        super(client, nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask, eventNotifier);
    }

    @Override
    public SessionDiagnosticsDataType[] getSessionDiagnosticsArray() throws UaException {
        SessionDiagnosticsArrayTypeNode node = getSessionDiagnosticsArrayNode();
        return cast(node.getValue().getValue().getValue(), SessionDiagnosticsDataType[].class);
    }

    @Override
    public void setSessionDiagnosticsArray(SessionDiagnosticsDataType[] sessionDiagnosticsArray)
        throws UaException {
        SessionDiagnosticsArrayTypeNode node = getSessionDiagnosticsArrayNode();
        ExtensionObject[] encoded = ExtensionObject.encodeArray(client.getStaticSerializationContext(), sessionDiagnosticsArray);
        node.setValue(new Variant(encoded));
    }

    @Override
    public SessionDiagnosticsDataType[] readSessionDiagnosticsArray() throws UaException {
        try {
            return readSessionDiagnosticsArrayAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public void writeSessionDiagnosticsArray(SessionDiagnosticsDataType[] sessionDiagnosticsArray)
        throws UaException {
        try {
            writeSessionDiagnosticsArrayAsync(sessionDiagnosticsArray).get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends SessionDiagnosticsDataType[]> readSessionDiagnosticsArrayAsync(
    ) {
        return getSessionDiagnosticsArrayNodeAsync().thenCompose(node -> node.readAttributeAsync(AttributeId.Value)).thenApply(v -> cast(v.getValue().getValue(), SessionDiagnosticsDataType[].class));
    }

    @Override
    public CompletableFuture<StatusCode> writeSessionDiagnosticsArrayAsync(
        SessionDiagnosticsDataType[] sessionDiagnosticsArray) {
        ExtensionObject[] encoded = ExtensionObject.encodeArray(client.getStaticSerializationContext(), sessionDiagnosticsArray);
        DataValue value = DataValue.valueOnly(new Variant(encoded));
        return getSessionDiagnosticsArrayNodeAsync()
            .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
    }

    @Override
    public SessionDiagnosticsArrayTypeNode getSessionDiagnosticsArrayNode() throws UaException {
        try {
            return getSessionDiagnosticsArrayNodeAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends SessionDiagnosticsArrayTypeNode> getSessionDiagnosticsArrayNodeAsync(
    ) {
        CompletableFuture<UaNode> future = getMemberNodeAsync("http://opcfoundation.org/UA/", "SessionDiagnosticsArray", ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=47"), false);
        return future.thenApply(node -> (SessionDiagnosticsArrayTypeNode) node);
    }

    @Override
    public SessionSecurityDiagnosticsDataType[] getSessionSecurityDiagnosticsArray() throws
        UaException {
        SessionSecurityDiagnosticsArrayTypeNode node = getSessionSecurityDiagnosticsArrayNode();
        return cast(node.getValue().getValue().getValue(), SessionSecurityDiagnosticsDataType[].class);
    }

    @Override
    public void setSessionSecurityDiagnosticsArray(
        SessionSecurityDiagnosticsDataType[] sessionSecurityDiagnosticsArray) throws UaException {
        SessionSecurityDiagnosticsArrayTypeNode node = getSessionSecurityDiagnosticsArrayNode();
        ExtensionObject[] encoded = ExtensionObject.encodeArray(client.getStaticSerializationContext(), sessionSecurityDiagnosticsArray);
        node.setValue(new Variant(encoded));
    }

    @Override
    public SessionSecurityDiagnosticsDataType[] readSessionSecurityDiagnosticsArray() throws
        UaException {
        try {
            return readSessionSecurityDiagnosticsArrayAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public void writeSessionSecurityDiagnosticsArray(
        SessionSecurityDiagnosticsDataType[] sessionSecurityDiagnosticsArray) throws UaException {
        try {
            writeSessionSecurityDiagnosticsArrayAsync(sessionSecurityDiagnosticsArray).get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends SessionSecurityDiagnosticsDataType[]> readSessionSecurityDiagnosticsArrayAsync(
    ) {
        return getSessionSecurityDiagnosticsArrayNodeAsync().thenCompose(node -> node.readAttributeAsync(AttributeId.Value)).thenApply(v -> cast(v.getValue().getValue(), SessionSecurityDiagnosticsDataType[].class));
    }

    @Override
    public CompletableFuture<StatusCode> writeSessionSecurityDiagnosticsArrayAsync(
        SessionSecurityDiagnosticsDataType[] sessionSecurityDiagnosticsArray) {
        ExtensionObject[] encoded = ExtensionObject.encodeArray(client.getStaticSerializationContext(), sessionSecurityDiagnosticsArray);
        DataValue value = DataValue.valueOnly(new Variant(encoded));
        return getSessionSecurityDiagnosticsArrayNodeAsync()
            .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
    }

    @Override
    public SessionSecurityDiagnosticsArrayTypeNode getSessionSecurityDiagnosticsArrayNode() throws
        UaException {
        try {
            return getSessionSecurityDiagnosticsArrayNodeAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends SessionSecurityDiagnosticsArrayTypeNode> getSessionSecurityDiagnosticsArrayNodeAsync(
    ) {
        CompletableFuture<UaNode> future = getMemberNodeAsync("http://opcfoundation.org/UA/", "SessionSecurityDiagnosticsArray", ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=47"), false);
        return future.thenApply(node -> (SessionSecurityDiagnosticsArrayTypeNode) node);
    }
}
