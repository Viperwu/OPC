/*
 * Copyright (c) 2019 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package com.viper.opc.client.opcua.sdk.client;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;
import com.viper.opc.client.opcua.sdk.client.nodes.UaVariableNode;
import com.viper.opc.client.opcua.stack.core.types.builtin.DataValue;
import com.viper.opc.client.opcua.stack.core.types.builtin.LocalizedText;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;
import com.viper.opc.client.opcua.stack.core.types.builtin.QualifiedName;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UByte;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.viper.opc.client.opcua.stack.core.types.enumerated.NodeClass;

public class VariableTypeManager {

    private final ConcurrentMap<NodeId, VariableTypeDefinition> typeDefinitions = Maps.newConcurrentMap();

    public void registerVariableType(
        NodeId typeDefinition,
        Class<? extends UaVariableNode> nodeClass,
        VariableNodeConstructor variableNodeConstructor
    ) {

        typeDefinitions.put(typeDefinition, new VariableTypeDefinition(nodeClass, variableNodeConstructor));
    }

    public Optional<VariableNodeConstructor> getNodeConstructor(NodeId typeDefinition) {
        VariableTypeDefinition def = typeDefinitions.get(typeDefinition);

        return Optional.ofNullable(def).map(d -> d.nodeFactory);
    }

    private static class VariableTypeDefinition {
        final Class<? extends UaVariableNode> nodeClass;
        final VariableNodeConstructor nodeFactory;

        private VariableTypeDefinition(
            Class<? extends UaVariableNode> nodeClass,
            VariableNodeConstructor nodeFactory) {

            this.nodeClass = nodeClass;
            this.nodeFactory = nodeFactory;
        }
    }

    @FunctionalInterface
    public interface VariableNodeConstructor {

        UaVariableNode apply(
            OpcUaClient client,
            NodeId nodeId,
            NodeClass nodeClass,
            QualifiedName browseName,
            LocalizedText displayName,
            LocalizedText description,
            UInteger writeMask,
            UInteger userWriteMask,
            DataValue value,
            NodeId dataType,
            Integer valueRank,
            UInteger[] arrayDimensions,
            UByte accessLevel,
            UByte userAccessLevel,
            Double minimumSamplingInterval,
            Boolean historizing
        );

    }

}
