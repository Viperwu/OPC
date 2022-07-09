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
import com.viper.opc.client.opcua.sdk.client.nodes.UaNode;
import com.viper.opc.client.opcua.sdk.client.nodes.UaObjectNode;
import com.viper.opc.client.opcua.stack.core.types.builtin.LocalizedText;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;
import com.viper.opc.client.opcua.stack.core.types.builtin.QualifiedName;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UByte;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.viper.opc.client.opcua.stack.core.types.enumerated.NodeClass;

public class ObjectTypeManager {

    private final ConcurrentMap<NodeId, ObjectTypeDefinition> typeDefinitions = Maps.newConcurrentMap();

    public void registerObjectType(
        NodeId typeDefinition,
        Class<? extends UaObjectNode> nodeClass,
        ObjectNodeConstructor objectNodeConstructor
    ) {

        typeDefinitions.put(typeDefinition, new ObjectTypeDefinition(nodeClass, objectNodeConstructor));
    }

    public Optional<ObjectNodeConstructor> getNodeConstructor(NodeId typeDefinition) {
        ObjectTypeDefinition def = typeDefinitions.get(typeDefinition);

        return Optional.ofNullable(def).map(d -> d.nodeFactory);
    }

    private static class ObjectTypeDefinition {
        final Class<? extends UaNode> nodeClass;
        final ObjectNodeConstructor nodeFactory;

        private ObjectTypeDefinition(
            Class<? extends UaNode> nodeClass,
            ObjectNodeConstructor nodeFactory) {

            this.nodeClass = nodeClass;
            this.nodeFactory = nodeFactory;
        }
    }

    @FunctionalInterface
    public interface ObjectNodeConstructor {

        UaObjectNode apply(
            OpcUaClient client,
            NodeId nodeId,
            NodeClass nodeClass,
            QualifiedName browseName,
            LocalizedText displayName,
            LocalizedText description,
            UInteger writeMask,
            UInteger userWriteMask,
            UByte eventNotifier
        );

    }

}
