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
import com.viper.opc.client.opcua.stack.core.types.builtin.ByteString;
import com.viper.opc.client.opcua.stack.core.types.builtin.ExpandedNodeId;

@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder(
    toBuilder = true
)
@ToString
public class SignatureData extends Structure implements UaStructure {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=456");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=458");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=457");

    private final String algorithm;

    private final ByteString signature;

    public SignatureData(String algorithm, ByteString signature) {
        this.algorithm = algorithm;
        this.signature = signature;
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

    public String getAlgorithm() {
        return algorithm;
    }

    public ByteString getSignature() {
        return signature;
    }

    public static final class Codec extends GenericDataTypeCodec<SignatureData> {
        @Override
        public Class<SignatureData> getType() {
            return SignatureData.class;
        }

        @Override
        public SignatureData decode(SerializationContext context, UaDecoder decoder) {
            String algorithm = decoder.readString("Algorithm");
            ByteString signature = decoder.readByteString("Signature");
            return new SignatureData(algorithm, signature);
        }

        @Override
        public void encode(SerializationContext context, UaEncoder encoder, SignatureData value) {
            encoder.writeString("Algorithm", value.getAlgorithm());
            encoder.writeByteString("Signature", value.getSignature());
        }
    }
}
