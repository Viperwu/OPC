package com.viper.opc.client.opcua.sdk.client.model.nodes.objects;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.viper.opc.client.opcua.sdk.client.OpcUaClient;
import com.viper.opc.client.opcua.sdk.client.model.nodes.variables.PropertyTypeNode;
import com.viper.opc.client.opcua.sdk.client.model.types.objects.ModellingRuleType;
import com.viper.opc.client.opcua.sdk.client.nodes.UaNode;
import com.viper.opc.client.opcua.stack.core.AttributeId;
import com.viper.opc.client.opcua.stack.core.StatusCodes;
import com.viper.opc.client.opcua.stack.core.UaException;
import com.viper.opc.client.opcua.stack.core.types.builtin.DataValue;
import com.viper.opc.client.opcua.stack.core.types.builtin.ExpandedNodeId;
import com.viper.opc.client.opcua.stack.core.types.builtin.LocalizedText;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;
import com.viper.opc.client.opcua.stack.core.types.builtin.QualifiedName;
import com.viper.opc.client.opcua.stack.core.types.builtin.StatusCode;
import com.viper.opc.client.opcua.stack.core.types.builtin.Variant;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UByte;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.viper.opc.client.opcua.stack.core.types.enumerated.NamingRuleType;
import com.viper.opc.client.opcua.stack.core.types.enumerated.NodeClass;

public class ModellingRuleTypeNode extends BaseObjectTypeNode implements ModellingRuleType {
    public ModellingRuleTypeNode(OpcUaClient client, NodeId nodeId, NodeClass nodeClass,
                                 QualifiedName browseName, LocalizedText displayName, LocalizedText description,
                                 UInteger writeMask, UInteger userWriteMask, UByte eventNotifier) {
        super(client, nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask, eventNotifier);
    }

    @Override
    public NamingRuleType getNamingRule() throws UaException {
        PropertyTypeNode node = getNamingRuleNode();
        Object value = node.getValue().getValue().getValue();
        if (value instanceof Integer) {
            return NamingRuleType.from((Integer) value);
        } else if (value instanceof NamingRuleType) {
            return (NamingRuleType) value;
        } else {
            return null;
        }
    }

    @Override
    public void setNamingRule(NamingRuleType namingRule) throws UaException {
        PropertyTypeNode node = getNamingRuleNode();
        node.setValue(new Variant(namingRule));
    }

    @Override
    public NamingRuleType readNamingRule() throws UaException {
        try {
            return readNamingRuleAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public void writeNamingRule(NamingRuleType namingRule) throws UaException {
        try {
            writeNamingRuleAsync(namingRule).get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends NamingRuleType> readNamingRuleAsync() {
        return getNamingRuleNodeAsync()
            .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
            .thenApply(v -> {
                Object value = v.getValue().getValue();
                if (value instanceof Integer) {
                    return NamingRuleType.from((Integer) value);
                } else {
                    return null;
                }
            });
    }

    @Override
    public CompletableFuture<StatusCode> writeNamingRuleAsync(NamingRuleType namingRule) {
        DataValue value = DataValue.valueOnly(new Variant(namingRule));
        return getNamingRuleNodeAsync()
            .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
    }

    @Override
    public PropertyTypeNode getNamingRuleNode() throws UaException {
        try {
            return getNamingRuleNodeAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    @Override
    public CompletableFuture<? extends PropertyTypeNode> getNamingRuleNodeAsync() {
        CompletableFuture<UaNode> future = getMemberNodeAsync("http://opcfoundation.org/UA/", "NamingRule", ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=46"), false);
        return future.thenApply(node -> (PropertyTypeNode) node);
    }
}
