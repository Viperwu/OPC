/*
 * Copyright (c) 2019 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package com.viper.opc.client.opcua.stack.core.types.structured;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import com.viper.opc.client.opcua.stack.core.serialization.SerializationContext;
import com.viper.opc.client.opcua.stack.core.serialization.UaDecoder;
import com.viper.opc.client.opcua.stack.core.serialization.UaEncoder;
import com.viper.opc.client.opcua.stack.core.serialization.UaStructure;
import com.viper.opc.client.opcua.stack.core.serialization.codecs.GenericDataTypeCodec;
import com.viper.opc.client.opcua.stack.core.types.builtin.ExpandedNodeId;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UInteger;

@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder(
    toBuilder = true
)
@ToString
public class ServiceCounterDataType extends Structure implements UaStructure {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=871");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=872");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=873");

    private final UInteger totalCount;

    private final UInteger errorCount;

    public ServiceCounterDataType(UInteger totalCount, UInteger errorCount) {
        this.totalCount = totalCount;
        this.errorCount = errorCount;
    }

    @Override
    public ExpandedNodeId getTypeId() {
        return TYPE_ID;
    }

    @Override
    public ExpandedNodeId getXmlEncodingId() {
        return XML_ENCODING_ID;
    }

    @Override
    public ExpandedNodeId getBinaryEncodingId() {
        return BINARY_ENCODING_ID;
    }

    public UInteger getTotalCount() {
        return totalCount;
    }

    public UInteger getErrorCount() {
        return errorCount;
    }

    public static final class Codec extends GenericDataTypeCodec<ServiceCounterDataType> {
        @Override
        public Class<ServiceCounterDataType> getType() {
            return ServiceCounterDataType.class;
        }

        @Override
        public ServiceCounterDataType decode(SerializationContext context, UaDecoder decoder) {
            UInteger totalCount = decoder.readUInt32("TotalCount");
            UInteger errorCount = decoder.readUInt32("ErrorCount");
            return new ServiceCounterDataType(totalCount, errorCount);
        }

        @Override
        public void encode(SerializationContext context, UaEncoder encoder,
                           ServiceCounterDataType value) {
            encoder.writeUInt32("TotalCount", value.getTotalCount());
            encoder.writeUInt32("ErrorCount", value.getErrorCount());
        }
    }
}
