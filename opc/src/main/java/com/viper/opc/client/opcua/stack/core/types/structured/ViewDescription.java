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
import com.viper.opc.client.opcua.stack.core.types.builtin.DateTime;
import com.viper.opc.client.opcua.stack.core.types.builtin.ExpandedNodeId;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UInteger;

@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder(
    toBuilder = true
)
@ToString
public class ViewDescription extends Structure implements UaStructure {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=511");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=513");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=512");

    private final NodeId viewId;

    private final DateTime timestamp;

    private final UInteger viewVersion;

    public ViewDescription(NodeId viewId, DateTime timestamp, UInteger viewVersion) {
        this.viewId = viewId;
        this.timestamp = timestamp;
        this.viewVersion = viewVersion;
    }

    @Override
    public ExpandedNodeId getTypeId() {
        return TYPE_ID;
    }

    @Override
    public ExpandedNodeId getBinaryEncodingId() {
        return BINARY_ENCODING_ID;
    }

    @Override
    public ExpandedNodeId getXmlEncodingId() {
        return XML_ENCODING_ID;
    }

    public NodeId getViewId() {
        return viewId;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public UInteger getViewVersion() {
        return viewVersion;
    }

    public static final class Codec extends GenericDataTypeCodec<ViewDescription> {
        @Override
        public Class<ViewDescription> getType() {
            return ViewDescription.class;
        }

        @Override
        public ViewDescription decode(SerializationContext context, UaDecoder decoder) {
            NodeId viewId = decoder.readNodeId("ViewId");
            DateTime timestamp = decoder.readDateTime("Timestamp");
            UInteger viewVersion = decoder.readUInt32("ViewVersion");
            return new ViewDescription(viewId, timestamp, viewVersion);
        }

        @Override
        public void encode(SerializationContext context, UaEncoder encoder, ViewDescription value) {
            encoder.writeNodeId("ViewId", value.getViewId());
            encoder.writeDateTime("Timestamp", value.getTimestamp());
            encoder.writeUInt32("ViewVersion", value.getViewVersion());
        }
    }
}
