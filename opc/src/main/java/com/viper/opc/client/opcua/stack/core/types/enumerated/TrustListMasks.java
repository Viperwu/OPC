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

public enum TrustListMasks implements UaEnumeration {
    None(0),

    TrustedCertificates(1),

    TrustedCrls(2),

    IssuerCertificates(4),

    IssuerCrls(8),

    All(15);

    private final int value;

    TrustListMasks(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Nullable
    public static TrustListMasks from(int value) {
        switch (value) {
            case 0:
                return None;
            case 1:
                return TrustedCertificates;
            case 2:
                return TrustedCrls;
            case 4:
                return IssuerCertificates;
            case 8:
                return IssuerCrls;
            case 15:
                return All;
            default:
                return null;
        }
    }

    public static ExpandedNodeId getTypeId() {
        return ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12552");
    }

    public static class Codec extends GenericDataTypeCodec<TrustListMasks> {
        @Override
        public Class<TrustListMasks> getType() {
            return TrustListMasks.class;
        }

        @Override
        public TrustListMasks decode(SerializationContext context, UaDecoder decoder) {
            return decoder.readEnum(null, TrustListMasks.class);
        }

        @Override
        public void encode(SerializationContext context, UaEncoder encoder, TrustListMasks value) {
            encoder.writeEnum(null, value);
        }
    }
}
