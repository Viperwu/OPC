/*
 * Copyright (c) 2019 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package com.viper.opc.client.opcua.stack.core.types.enumerated;

import com.viper.opc.client.opcua.stack.core.serialization.SerializationContext;
import com.viper.opc.client.opcua.stack.core.serialization.UaDecoder;
import com.viper.opc.client.opcua.stack.core.serialization.UaEncoder;
import com.viper.opc.client.opcua.stack.core.serialization.UaEnumeration;
import com.viper.opc.client.opcua.stack.core.serialization.codecs.GenericDataTypeCodec;
import com.viper.opc.client.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.jetbrains.annotations.Nullable;

public enum ModelChangeStructureVerbMask implements UaEnumeration {
    NodeAdded(1),

    NodeDeleted(2),

    ReferenceAdded(4),

    ReferenceDeleted(8),

    DataTypeChanged(16);

    private final int value;

    ModelChangeStructureVerbMask(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Nullable
    public static ModelChangeStructureVerbMask from(int value) {
        switch (value) {
            case 1:
                return NodeAdded;
            case 2:
                return NodeDeleted;
            case 4:
                return ReferenceAdded;
            case 8:
                return ReferenceDeleted;
            case 16:
                return DataTypeChanged;
            default:
                return null;
        }
    }

    public static ExpandedNodeId getTypeId() {
        return ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11941");
    }

    public static class Codec extends GenericDataTypeCodec<ModelChangeStructureVerbMask> {
        @Override
        public Class<ModelChangeStructureVerbMask> getType() {
            return ModelChangeStructureVerbMask.class;
        }

        @Override
        public ModelChangeStructureVerbMask decode(SerializationContext context, UaDecoder decoder) {
            return decoder.readEnum(null, ModelChangeStructureVerbMask.class);
        }

        @Override
        public void encode(SerializationContext context, UaEncoder encoder,
                           ModelChangeStructureVerbMask value) {
            encoder.writeEnum(null, value);
        }
    }
}
