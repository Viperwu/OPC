package com.viper.opc.client.opcua.sdk.client.model.nodes.variables;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.viper.opc.client.opcua.sdk.client.OpcUaClient;
import com.viper.opc.client.opcua.sdk.client.model.types.variables.SamplingIntervalDiagnosticsArrayType;
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
import com.viper.opc.client.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;

public class SamplingIntervalDiagnosticsArrayTypeNode extends BaseDataVariableTypeNode implements SamplingIntervalDiagnosticsArrayType {
    public SamplingIntervalDiagnosticsArrayTypeNode(OpcUaClient client, NodeId nodeId,
                                                    NodeClass nodeClass, QualifiedName browseName, LocalizedText displayName,
                                                    LocalizedText description, UInteger writeMask, UInteger userWriteMask, DataValue value,
                                                    NodeId dataType, Integer valueRank, UInteger[] arrayDimensions, UByte accessLevel,
                                                    UByte userAccessLevel, Double minimumSamplingInterval, Boolean historizing) {
        super(client, nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask, value, dataType, valueRank, arrayDimensions, accessLevel, userAccessLevel, minimumSamplingInterval, historizing);
    }

    @Override
    public SamplingIntervalDiagnosticsDataType getSamplingIntervalDiagnostics() throws UaException {
        SamplingIntervalDiagnosticsTypeNode node = getSamplingIntervalDiagnosticsNode();
        return cast(node.getValue().getValue().getValue(), SamplingIntervalDiagnosticsDataType.class);
    }

    @Override
    public void setSamplingIntervalDiagnostics(
        SamplingIntervalDiagnosticsDataType samplingIntervalDiagnostics) throws UaException {
        SamplingIntervalDiagnosticsTypeNode node = getSamplingIntervalDiagnosticsNode();
        ExtensionObject value = ExtensionObject.encode(client.getStaticSerializationContext(), samplingIntervalDiagnostics);
        node.setValue(new Variant(value));
    }

    @Override
    public SamplingIntervalDiagnosticsDataType readSamplingIntervalDiagnostics() throws UaException {
        try {
            return readSamplingIntervalDiagnosticsAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public void writeSamplingIntervalDiagnostics(
        SamplingIntervalDiagnosticsDataType samplingIntervalDiagnostics) throws UaException {
        try {
            writeSamplingIntervalDiagnosticsAsync(samplingIntervalDiagnostics).get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends SamplingIntervalDiagnosticsDataType> readSamplingIntervalDiagnosticsAsync(
    ) {
        return getSamplingIntervalDiagnosticsNodeAsync().thenCompose(node -> node.readAttributeAsync(AttributeId.Value)).thenApply(v -> cast(v.getValue().getValue(), SamplingIntervalDiagnosticsDataType.class));
    }

    @Override
    public CompletableFuture<StatusCode> writeSamplingIntervalDiagnosticsAsync(
        SamplingIntervalDiagnosticsDataType samplingIntervalDiagnostics) {
        ExtensionObject encoded = ExtensionObject.encode(client.getStaticSerializationContext(), samplingIntervalDiagnostics);
        DataValue value = DataValue.valueOnly(new Variant(encoded));
        return getSamplingIntervalDiagnosticsNodeAsync()
            .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
    }

    @Override
    public SamplingIntervalDiagnosticsTypeNode getSamplingIntervalDiagnosticsNode() throws
        UaException {
        try {
            return getSamplingIntervalDiagnosticsNodeAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends SamplingIntervalDiagnosticsTypeNode> getSamplingIntervalDiagnosticsNodeAsync(
    ) {
        CompletableFuture<UaNode> future = getMemberNodeAsync("http://opcfoundation.org/UA/", "SamplingIntervalDiagnostics", ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=47"), false);
        return future.thenApply(node -> (SamplingIntervalDiagnosticsTypeNode) node);
    }
}
