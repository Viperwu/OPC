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
import com.viper.opc.client.opcua.stack.core.serialization.UaResponseMessage;
import com.viper.opc.client.opcua.stack.core.serialization.codecs.GenericDataTypeCodec;
import com.viper.opc.client.opcua.stack.core.types.builtin.DateTime;
import com.viper.opc.client.opcua.stack.core.types.builtin.ExpandedNodeId;

@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder(
    toBuilder = true
)
@ToString
public class FindServersOnNetworkResponse extends Structure implements UaResponseMessage {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12191");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12209");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12197");

    private final ResponseHeader responseHeader;

    private final DateTime lastCounterResetTime;

    private final ServerOnNetwork[] servers;

    public FindServersOnNetworkResponse(ResponseHeader responseHeader, DateTime lastCounterResetTime,
                                        ServerOnNetwork[] servers) {
        this.responseHeader = responseHeader;
        this.lastCounterResetTime = lastCounterResetTime;
        this.servers = servers;
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

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public DateTime getLastCounterResetTime() {
        return lastCounterResetTime;
    }

    public ServerOnNetwork[] getServers() {
        return servers;
    }

    public static final class Codec extends GenericDataTypeCodec<FindServersOnNetworkResponse> {
        @Override
        public Class<FindServersOnNetworkResponse> getType() {
            return FindServersOnNetworkResponse.class;
        }

        @Override
        public FindServersOnNetworkResponse decode(SerializationContext context, UaDecoder decoder) {
            ResponseHeader responseHeader = (ResponseHeader) decoder.readStruct("ResponseHeader", ResponseHeader.TYPE_ID);
            DateTime lastCounterResetTime = decoder.readDateTime("LastCounterResetTime");
            ServerOnNetwork[] servers = (ServerOnNetwork[]) decoder.readStructArray("Servers", ServerOnNetwork.TYPE_ID);
            return new FindServersOnNetworkResponse(responseHeader, lastCounterResetTime, servers);
        }

        @Override
        public void encode(SerializationContext context, UaEncoder encoder,
                           FindServersOnNetworkResponse value) {
            encoder.writeStruct("ResponseHeader", value.getResponseHeader(), ResponseHeader.TYPE_ID);
            encoder.writeDateTime("LastCounterResetTime", value.getLastCounterResetTime());
            encoder.writeStructArray("Servers", value.getServers(), ServerOnNetwork.TYPE_ID);
        }
    }
}