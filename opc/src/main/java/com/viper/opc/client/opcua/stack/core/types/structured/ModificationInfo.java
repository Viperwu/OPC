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
import com.viper.opc.client.opcua.stack.core.types.enumerated.HistoryUpdateType;

@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder(
    toBuilder = true
)
@ToString
public class ModificationInfo extends Structure implements UaStructure {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11216");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11226");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11218");

    private final DateTime modificationTime;

    private final HistoryUpdateType updateType;

    private final String userName;

    public ModificationInfo(DateTime modificationTime, HistoryUpdateType updateType,
                            String userName) {
        this.modificationTime = modificationTime;
        this.updateType = updateType;
        this.userName = userName;
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

    public DateTime getModificationTime() {
        return modificationTime;
    }

    public HistoryUpdateType getUpdateType() {
        return updateType;
    }

    public String getUserName() {
        return userName;
    }

    public static final class Codec extends GenericDataTypeCodec<ModificationInfo> {
        @Override
        public Class<ModificationInfo> getType() {
            return ModificationInfo.class;
        }

        @Override
        public ModificationInfo decode(SerializationContext context, UaDecoder decoder) {
            DateTime modificationTime = decoder.readDateTime("ModificationTime");
            HistoryUpdateType updateType = decoder.readEnum("UpdateType", HistoryUpdateType.class);
            String userName = decoder.readString("UserName");
            return new ModificationInfo(modificationTime, updateType, userName);
        }

        @Override
        public void encode(SerializationContext context, UaEncoder encoder, ModificationInfo value) {
            encoder.writeDateTime("ModificationTime", value.getModificationTime());
            encoder.writeEnum("UpdateType", value.getUpdateType());
            encoder.writeString("UserName", value.getUserName());
        }
    }
}
