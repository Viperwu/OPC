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
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UByte;
import com.viper.opc.client.opcua.stack.core.types.enumerated.ServerState;

@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder(
    toBuilder = true
)
@ToString
public class RedundantServerDataType extends Structure implements UaStructure {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=853");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=854");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=855");

    private final String serverId;

    private final UByte serviceLevel;

    private final ServerState serverState;

    public RedundantServerDataType(String serverId, UByte serviceLevel, ServerState serverState) {
        this.serverId = serverId;
        this.serviceLevel = serviceLevel;
        this.serverState = serverState;
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

    public String getServerId() {
        return serverId;
    }

    public UByte getServiceLevel() {
        return serviceLevel;
    }

    public ServerState getServerState() {
        return serverState;
    }

    public static final class Codec extends GenericDataTypeCodec<RedundantServerDataType> {
        @Override
        public Class<RedundantServerDataType> getType() {
            return RedundantServerDataType.class;
        }

        @Override
        public RedundantServerDataType decode(SerializationContext context, UaDecoder decoder) {
            String serverId = decoder.readString("ServerId");
            UByte serviceLevel = decoder.readByte("ServiceLevel");
            ServerState serverState = decoder.readEnum("ServerState", ServerState.class);
            return new RedundantServerDataType(serverId, serviceLevel, serverState);
        }

        @Override
        public void encode(SerializationContext context, UaEncoder encoder,
                           RedundantServerDataType value) {
            encoder.writeString("ServerId", value.getServerId());
            encoder.writeByte("ServiceLevel", value.getServiceLevel());
            encoder.writeEnum("ServerState", value.getServerState());
        }
    }
}
