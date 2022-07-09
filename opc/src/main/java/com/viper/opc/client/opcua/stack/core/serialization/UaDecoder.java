/*
 * Copyright (c) 2019 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package com.viper.opc.client.opcua.stack.core.serialization;

import java.util.UUID;
import java.util.function.Function;

import com.viper.opc.client.opcua.stack.core.UaSerializationException;
import com.viper.opc.client.opcua.stack.core.serialization.codecs.DataTypeCodec;
import com.viper.opc.client.opcua.stack.core.types.builtin.ByteString;
import com.viper.opc.client.opcua.stack.core.types.builtin.DataValue;
import com.viper.opc.client.opcua.stack.core.types.builtin.DateTime;
import com.viper.opc.client.opcua.stack.core.types.builtin.DiagnosticInfo;
import com.viper.opc.client.opcua.stack.core.types.builtin.ExpandedNodeId;
import com.viper.opc.client.opcua.stack.core.types.builtin.ExtensionObject;
import com.viper.opc.client.opcua.stack.core.types.builtin.LocalizedText;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;
import com.viper.opc.client.opcua.stack.core.types.builtin.QualifiedName;
import com.viper.opc.client.opcua.stack.core.types.builtin.StatusCode;
import com.viper.opc.client.opcua.stack.core.types.builtin.Variant;
import com.viper.opc.client.opcua.stack.core.types.builtin.XmlElement;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UByte;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.ULong;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UShort;

public interface UaDecoder {

    Boolean readBoolean(String field) throws UaSerializationException;

    Byte readSByte(String field) throws UaSerializationException;

    Short readInt16(String field) throws UaSerializationException;

    Integer readInt32(String field) throws UaSerializationException;

    Long readInt64(String field) throws UaSerializationException;

    UByte readByte(String field) throws UaSerializationException;

    UShort readUInt16(String field) throws UaSerializationException;

    UInteger readUInt32(String field) throws UaSerializationException;

    ULong readUInt64(String field) throws UaSerializationException;

    Float readFloat(String field) throws UaSerializationException;

    Double readDouble(String field) throws UaSerializationException;

    String readString(String field) throws UaSerializationException;

    DateTime readDateTime(String field) throws UaSerializationException;

    UUID readGuid(String field) throws UaSerializationException;

    ByteString readByteString(String field) throws UaSerializationException;

    XmlElement readXmlElement(String field) throws UaSerializationException;

    NodeId readNodeId(String field) throws UaSerializationException;

    ExpandedNodeId readExpandedNodeId(String field) throws UaSerializationException;

    StatusCode readStatusCode(String field) throws UaSerializationException;

    QualifiedName readQualifiedName(String field) throws UaSerializationException;

    LocalizedText readLocalizedText(String field) throws UaSerializationException;

    ExtensionObject readExtensionObject(String field) throws UaSerializationException;

    DataValue readDataValue(String field) throws UaSerializationException;

    Variant readVariant(String field) throws UaSerializationException;

    DiagnosticInfo readDiagnosticInfo(String field) throws UaSerializationException;

    UaMessage readMessage(String field) throws UaSerializationException;

    <T extends Enum<?> & UaEnumeration> T readEnum(String field, Class<T> enumType) throws UaSerializationException;

    Object readStruct(String field, NodeId dataTypeId) throws UaSerializationException;

    Object readStruct(String field, ExpandedNodeId dataTypeId) throws UaSerializationException;

    Object readStruct(String field, DataTypeCodec codec) throws UaSerializationException;

    Boolean[] readBooleanArray(String field) throws UaSerializationException;

    Byte[] readSByteArray(String field) throws UaSerializationException;

    Short[] readInt16Array(String field) throws UaSerializationException;

    Integer[] readInt32Array(String field) throws UaSerializationException;

    Long[] readInt64Array(String field) throws UaSerializationException;

    UByte[] readByteArray(String field) throws UaSerializationException;

    UShort[] readUInt16Array(String field) throws UaSerializationException;

    UInteger[] readUInt32Array(String field) throws UaSerializationException;

    ULong[] readUInt64Array(String field) throws UaSerializationException;

    Float[] readFloatArray(String field) throws UaSerializationException;

    Double[] readDoubleArray(String field) throws UaSerializationException;

    String[] readStringArray(String field) throws UaSerializationException;

    DateTime[] readDateTimeArray(String field) throws UaSerializationException;

    UUID[] readGuidArray(String field) throws UaSerializationException;

    ByteString[] readByteStringArray(String field) throws UaSerializationException;

    XmlElement[] readXmlElementArray(String field) throws UaSerializationException;

    NodeId[] readNodeIdArray(String field) throws UaSerializationException;

    ExpandedNodeId[] readExpandedNodeIdArray(String field) throws UaSerializationException;

    StatusCode[] readStatusCodeArray(String field) throws UaSerializationException;

    QualifiedName[] readQualifiedNameArray(String field) throws UaSerializationException;

    LocalizedText[] readLocalizedTextArray(String field) throws UaSerializationException;

    ExtensionObject[] readExtensionObjectArray(String field) throws UaSerializationException;

    DataValue[] readDataValueArray(String field) throws UaSerializationException;

    Variant[] readVariantArray(String field) throws UaSerializationException;

    DiagnosticInfo[] readDiagnosticInfoArray(String field) throws UaSerializationException;

    <T extends Enum<?> & UaEnumeration> Object[] readEnumArray(
        String field,
        Class<T> enumType
    ) throws UaSerializationException;

    Object[] readStructArray(String field, NodeId dataTypeId) throws UaSerializationException;

    Object[] readStructArray(String field, ExpandedNodeId dataTypeId) throws UaSerializationException;

    <T> T[] readArray(String field, Function<String, T> decoder, Class<T> clazz) throws UaSerializationException;

}
