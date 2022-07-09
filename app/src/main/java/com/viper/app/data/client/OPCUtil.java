package com.viper.app.data.client;

import static com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;
import androidx.lifecycle.MutableLiveData;

import com.viper.app.R;
import com.viper.app.data.bean.PageNode;
import com.viper.app.data.bean.OPCNode;
import com.viper.app.data.bean.SubscriptionOPCNode;
import com.viper.app.domain.message.OpcData2Result;
import com.viper.app.domain.message.OpcDataResult;
import com.viper.app.util.U;

import com.viper.opc.client.opcua.sdk.client.OpcUaClient;
import com.viper.opc.client.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import com.viper.opc.client.opcua.sdk.client.api.subscriptions.UaSubscription;
import com.viper.opc.client.opcua.sdk.client.nodes.UaNode;
import com.viper.opc.client.opcua.sdk.client.nodes.UaVariableNode;
import com.viper.opc.client.opcua.stack.core.AttributeId;
import com.viper.opc.client.opcua.stack.core.UaException;
import com.viper.opc.client.opcua.stack.core.types.builtin.DataValue;
import com.viper.opc.client.opcua.stack.core.types.builtin.DateTime;
import com.viper.opc.client.opcua.stack.core.types.builtin.LocalizedText;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;
import com.viper.opc.client.opcua.stack.core.types.builtin.QualifiedName;
import com.viper.opc.client.opcua.stack.core.types.builtin.Variant;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UByte;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.ULong;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UShort;
import com.viper.opc.client.opcua.stack.core.types.enumerated.MonitoringMode;
import com.viper.opc.client.opcua.stack.core.types.enumerated.TimestampsToReturn;
import com.viper.opc.client.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import com.viper.opc.client.opcua.stack.core.types.structured.MonitoringParameters;
import com.viper.opc.client.opcua.stack.core.types.structured.ReadValueId;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;


/**
 * opc 工具类
 */
public final class OPCUtil {

    private static SimpleArrayMap<OpcUaClient, UaSubscription> map;
    private OPCUtil() {
    }

    private static UaSubscription getUaSubscription(@NonNull OpcUaClient client) {
        if (getMap().get(client) == null) {
            try {
                UaSubscription subscription = client.getSubscriptionManager().createSubscription(100.0).get();
                getMap().put(client, subscription);
                return subscription;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return getMap().get(client);
        }
    }

    private static SimpleArrayMap<OpcUaClient, UaSubscription> getMap() {
        if (map == null) {
            map = new SimpleArrayMap<>();
        }
        return map;
    }


    public static DataValue booleanToDataValue(boolean b) {
        return new DataValue(new Variant(b), null, null);
    }


    public static DataValue strToDataValue(OPCNode node, String str) throws Exception {
        if (U.isEmpty(str)) return null;
        int typeId = node.getTypeId();
        Variant variant = isArrayNode(node, str);
        if (variant != null) return new DataValue(variant, null, null);
        if (typeId == SiemensTypeId.Bool) {
            str = str.trim().toLowerCase();
            if (str.equals("1")) {
                variant = new Variant(true);
            } else {
                variant = new Variant(Boolean.valueOf(str));
            }
        } else if (typeId == SiemensTypeId.Byte) {
            variant = new Variant(UByte.valueOf(str));
        } else if (typeId == SiemensTypeId.Int) {
            variant = new Variant(Short.valueOf(str));
        } else if (typeId == SiemensTypeId.UInt) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.UDInt) {
            variant = new Variant(UInteger.valueOf(str));
        } else if (typeId == SiemensTypeId.USInt) {
            variant = new Variant(UByte.valueOf(str));
        } else if (typeId == SiemensTypeId.SInt) {
            variant = new Variant(Byte.valueOf(str));
        } else if (typeId == SiemensTypeId.DInt) {
            variant = new Variant(Integer.valueOf(str));
        } else if (typeId == SiemensTypeId.LInt) {
            variant = new Variant(Long.valueOf(str));
        } else if (typeId == SiemensTypeId.Char) {
            if (str.length() > 1) {
                return null;
            } else {
                char c = str.charAt(0);
                variant = new Variant(UByte.valueOf(c));
            }
        } else if (typeId == SiemensTypeId.Word) {
            variant = new Variant(Short.valueOf(str));
        } else if (typeId == SiemensTypeId.DWord) {
            variant = new Variant(Integer.valueOf(str));
        } else if (typeId == SiemensTypeId.WChar) {
            if (str.length() > 1) {
                return null;
            } else {
                char c = str.charAt(0);
                variant = new Variant(UShort.valueOf(c));
            }
        } else if (typeId == SiemensTypeId.Date) {
            variant = new Variant(UShort.valueOf((int) ChronoUnit.DAYS.between
                (LocalDate.parse(U.getSiemensDateStartDate()), LocalDate.parse(str))));
        } else if (typeId == SiemensTypeId.Date_And_Time) {
            //   Date date = TimeUtils.string2Date(str);
            LocalDateTime localDateTime = LocalDateTime.parse(str,
                DateTimeFormatter.ofPattern(U.getDateTimePattern()));
            if (localDateTime != null) {
                UByte[] uBytes = new UByte[8];
                parseDate(localDateTime, uBytes, str);
                variant = new Variant(uBytes);
            } else {
                LocalDateTime localDateTime2 = LocalDateTime.parse(str,
                    DateTimeFormatter.ofPattern(U.getDateTimePattern2()));
                //   Date date2 = TimeUtils.string2Date(str,TimeConstants.PATTERN_2);
                if (localDateTime2 != null) {
                    UByte[] uBytes = new UByte[8];
                    parseDate(localDateTime2, uBytes, str);
                    /* localDateTime2.getNano();*/
                    int msec = Integer.parseInt(str.substring(str.indexOf(".") + 1));
                    uBytes[6] = UByte.valueOf(Integer.parseInt(String.valueOf(msec), 16));
                    variant = new Variant(uBytes);
                }
            }
        } else if (typeId == SiemensTypeId.LDT) {

            variant = new Variant(new DateTime(LocalDateTime.parse(str,
                DateTimeFormatter.ofPattern(U.getDateTimePattern())).toInstant(ZoneOffset.UTC)));
        } else if (typeId == SiemensTypeId.Real) {
            variant = new Variant(Float.valueOf(str));
        } else if (typeId == SiemensTypeId.LReal) {
            variant = new Variant(Double.valueOf(str));
        } else if (typeId == SiemensTypeId.Time) {
            variant = new Variant(Integer.valueOf(str));
        } else if (typeId == SiemensTypeId.LTime) {
            variant = new Variant(Long.valueOf(str));
        } else if (typeId == SiemensTypeId.S5Time) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.LWord) {
            variant = new Variant(ULong.valueOf(str));
        } else if (typeId == SiemensTypeId.StringX) {
            variant = new Variant(new String(str.getBytes("gbk"), StandardCharsets.ISO_8859_1));
        } else if (typeId == SiemensTypeId.WString) {
            // variant = new Variant(U.utf8ToUnicode(str));unicode
            variant = new Variant(new String(str.getBytes("unicode"), "unicode"));
        } else if (typeId == SiemensTypeId.DB_ANY) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.CONN_ANY) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.AOM_IDENT) {
            variant = new Variant(UInteger.valueOf(str));
        } else if (typeId == SiemensTypeId.CONN_OUC) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.CONN_PRG) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.CONN_R_ID) {
            variant = new Variant(UInteger.valueOf(str));
        } else if (typeId == SiemensTypeId.DB_DYN) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.DB_WWW) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.EVENT_ANY) {
            variant = new Variant(UInteger.valueOf(str));
        } else if (typeId == SiemensTypeId.EVENT_ATT) {
            variant = new Variant(UInteger.valueOf(str));
        } else if (typeId == SiemensTypeId.EVENT_HWINT) {
            variant = new Variant(UInteger.valueOf(str));
        } else if (typeId == SiemensTypeId.HW_ANY) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.HW_DEVICE) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.HW_INTERFACE) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.HW_DPMASTER) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.HW_DPSLAVE) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.HW_HSC) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.HW_IEPORT) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.HW_IO) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.OB_ANY) {
            variant = new Variant(Short.valueOf(str));
        } else if (typeId == SiemensTypeId.OB_ATT) {
            variant = new Variant(Short.valueOf(str));
        } else if (typeId == SiemensTypeId.OB_CYCLIC) {
            variant = new Variant(Short.valueOf(str));
        } else if (typeId == SiemensTypeId.OB_DELAY) {
            variant = new Variant(Short.valueOf(str));
        } else if (typeId == SiemensTypeId.OB_DIAG) {
            variant = new Variant(Short.valueOf(str));
        } else if (typeId == SiemensTypeId.OB_HWINT) {
            variant = new Variant(Short.valueOf(str));
        } else if (typeId == SiemensTypeId.OB_PCYCLE) {
            variant = new Variant(Short.valueOf(str));
        } else if (typeId == SiemensTypeId.OB_STARTUP) {
            variant = new Variant(Short.valueOf(str));
        } else if (typeId == SiemensTypeId.OB_TIMEERROR) {
            variant = new Variant(Short.valueOf(str));
        } else if (typeId == SiemensTypeId.OB_TOD) {
            variant = new Variant(Short.valueOf(str));
        } else if (typeId == SiemensTypeId.PIP) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.EnumValueType) {
            variant = new Variant(Integer.valueOf(str));
        } else if (typeId == SiemensTypeId.LocalizedText) {
            variant = new Variant(new LocalizedText(str));
        } else if (typeId == SiemensTypeId.PORT) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.RTM) {
            variant = new Variant(UShort.valueOf(str));
        } else if (typeId == SiemensTypeId.HW_SUBMODULE) {
            variant = new Variant(UShort.valueOf(str));
        }
        if (variant != null) {
            return new DataValue(variant, null, null);
        } else {
            return null;
        }


    }

    //todo
    private static Variant isArrayNode(OPCNode node, String str) {
        if (!node.isArray()) return null;
        Variant variant = null;
        int typeId = node.getTypeId();
        if (typeId == SiemensTypeId.Bool) {
            variant = new Variant(DataConverter.str2BooleanArray(str));
        } else if (typeId == SiemensTypeId.Byte) {
          //  variant = new Variant(getGson().fromJson(str, UByte[].class));
            variant = new Variant(DataConverter.jsonStrShortArray2UByteArray(str));

        } else if (typeId == SiemensTypeId.Int) {
            variant = new Variant(DataConverter.str2ShortArray(str));
        } else if (typeId == SiemensTypeId.UInt) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.UDInt) {
            variant = new Variant(DataConverter.jsonStrLongArray2UIntegerArray(str));
        } else if (typeId == SiemensTypeId.USInt) {
            variant = new Variant(DataConverter.jsonStrShortArray2UByteArray(str));
        } else if (typeId == SiemensTypeId.SInt) {
            variant = new Variant(DataConverter.str2ByteArray(str));
        } else if (typeId == SiemensTypeId.DInt) {
            variant = new Variant(DataConverter.str2IntArray(str));
        } else if (typeId == SiemensTypeId.LInt) {
            //variant = new Variant(getGson().fromJson(str, UInteger[].class));
            variant = new Variant(DataConverter.str2LongArray(str));
        } else if (typeId == SiemensTypeId.Char) {
            variant = new Variant(DataConverter.jsonStrCharArray2UByteArray(str));
        } else if (typeId == SiemensTypeId.Word) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.DWord) {
           // variant = new Variant(getGson().fromJson(str, UInteger[].class));
            variant = new Variant(DataConverter.jsonStrLongArray2UIntegerArray(str));
        } else if (typeId == SiemensTypeId.WChar) {
            /*char[] chars = getGson().fromJson(str, char[].class);
            UShort[] uShorts = new UShort[chars.length];
            for (int i = 0; i < chars.length; i++) {
                uShorts[i] = UShort.valueOf(chars[i]);
            }*/
            variant = new Variant(DataConverter.jsonStrCharArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.Date) {
            String[] strings = DataConverter.str2StringArray(str);
            UShort[] uShorts = new UShort[strings.length];
            for (int i = 0; i < strings.length; i++) {
                uShorts[i] = UShort.valueOf((int) ChronoUnit.DAYS.between
                    (LocalDate.parse(U.getSiemensDateStartDate()), LocalDate.parse(strings[i])));
            }
            variant = new Variant(uShorts);
            /*variant = new Variant(UShort.valueOf((int) ChronoUnit.DAYS.between
                (LocalDate.parse(U.getSiemensDateStartDate()), LocalDate.parse(str))));*/
        } else if (typeId == SiemensTypeId.Date_And_Time) {
            String[] strings = DataConverter.str2StringArray(str);
            UByte[][] uBytes = new UByte[strings.length][];
            for (int i = 0; i < strings.length; i++) {
                LocalDateTime localDateTime = LocalDateTime.parse(strings[i],
                    DateTimeFormatter.ofPattern(U.getDateTimePattern()));
                if (localDateTime != null) {
                     uBytes[i] = new UByte[8];
                    parseDate(localDateTime, uBytes[i], strings[i]);
                } else {
                    LocalDateTime localDateTime2 = LocalDateTime.parse(strings[i],
                        DateTimeFormatter.ofPattern(U.getDateTimePattern2()));
                    //   Date date2 = TimeUtils.string2Date(str,TimeConstants.PATTERN_2);
                    if (localDateTime2 != null) {
                        uBytes[i] = new UByte[8];
                        parseDate(localDateTime2,  uBytes[i], strings[i]);
                        /* localDateTime2.getNano();*/
                        int msec = Integer.parseInt(strings[i].substring(strings[i].indexOf(".") + 1));
                        uBytes[i][6] = UByte.valueOf(Integer.parseInt(String.valueOf(msec), 16));
                    }
                }

            }
            variant = new Variant(uBytes);

            //   Date date = TimeUtils.string2Date(str);


        } else if (typeId == SiemensTypeId.LDT) {
            String[] strings = DataConverter.str2StringArray(str);
            DateTime[] dateTimes = new DateTime[strings.length];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(U.getDateTimePattern());
            for (int i = 0; i < strings.length; i++) {
                dateTimes[i] = new DateTime(LocalDateTime.parse(strings[i],
                    formatter).toInstant(ZoneOffset.UTC));
            }
            variant = new Variant(dateTimes);
        } else if (typeId == SiemensTypeId.Real) {
            variant = new Variant(DataConverter.str2FloatArray(str));
        } else if (typeId == SiemensTypeId.LReal) {
            variant = new Variant(DataConverter.str2DoubleArray(str));
        } else if (typeId == SiemensTypeId.Time) {
            variant = new Variant(DataConverter.str2IntArray(str));
        } else if (typeId == SiemensTypeId.LTime) {
            variant = new Variant(DataConverter.str2LongArray(str));
        } else if (typeId == SiemensTypeId.S5Time) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.LWord) {
            variant = new Variant(DataConverter.jsonStrStringArray2ULongArray(str));
        } else if (typeId == SiemensTypeId.StringX) {
            String[] strings = DataConverter.str2StringArray(str);
            String[] strings2 = new String[strings.length];
            for (int i = 0; i < strings.length; i++) {
                try {
                    strings2[i] = new String(strings[i].getBytes("gbk"),StandardCharsets.ISO_8859_1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            variant = new Variant(strings2);
        } else if (typeId == SiemensTypeId.WString) {
            String[] strings = DataConverter.str2StringArray(str);
            String[] strings2 = new String[strings.length];
            for (int i = 0; i < strings.length; i++) {
                try {
                    strings2[i] = new String(strings[i].getBytes("unicode"),"unicode");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            variant = new Variant(strings2);
        } else if (typeId == SiemensTypeId.DB_ANY) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.CONN_ANY) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.AOM_IDENT) {
            variant = new Variant(DataConverter.jsonStrLongArray2UIntegerArray(str));
        } else if (typeId == SiemensTypeId.CONN_OUC) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.CONN_PRG) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.CONN_R_ID) {
            variant = new Variant(DataConverter.jsonStrLongArray2UIntegerArray(str));
        } else if (typeId == SiemensTypeId.DB_DYN) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.DB_WWW) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.EVENT_ANY) {
            variant = new Variant(DataConverter.jsonStrLongArray2UIntegerArray(str));
        } else if (typeId == SiemensTypeId.EVENT_ATT) {
            variant = new Variant(DataConverter.jsonStrLongArray2UIntegerArray(str));
        } else if (typeId == SiemensTypeId.EVENT_HWINT) {
            variant = new Variant(DataConverter.jsonStrLongArray2UIntegerArray(str));
        } else if (typeId == SiemensTypeId.HW_ANY) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.HW_DEVICE) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.HW_INTERFACE) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.HW_DPMASTER) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.HW_DPSLAVE) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.HW_HSC) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.HW_IEPORT) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.HW_IO) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.OB_ANY) {
            variant = new Variant(DataConverter.str2ShortArray(str));
        } else if (typeId == SiemensTypeId.OB_ATT) {
            variant = new Variant(DataConverter.str2ShortArray(str));
        } else if (typeId == SiemensTypeId.OB_CYCLIC) {
            variant = new Variant(DataConverter.str2ShortArray(str));
        } else if (typeId == SiemensTypeId.OB_DELAY) {
            variant = new Variant(DataConverter.str2ShortArray(str));
        } else if (typeId == SiemensTypeId.OB_DIAG) {
            variant = new Variant(DataConverter.str2ShortArray(str));
        } else if (typeId == SiemensTypeId.OB_HWINT) {
            variant = new Variant(DataConverter.str2ShortArray(str));
        } else if (typeId == SiemensTypeId.OB_PCYCLE) {
            variant = new Variant(DataConverter.str2ShortArray(str));
        } else if (typeId == SiemensTypeId.OB_STARTUP) {
            variant = new Variant(DataConverter.str2ShortArray(str));
        } else if (typeId == SiemensTypeId.OB_TIMEERROR) {
            variant = new Variant(DataConverter.str2ShortArray(str));
        } else if (typeId == SiemensTypeId.OB_TOD) {
            variant = new Variant(DataConverter.str2ShortArray(str));
        } else if (typeId == SiemensTypeId.PIP) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.EnumValueType) {
            variant = new Variant(DataConverter.str2IntArray(str));
        } else if (typeId == SiemensTypeId.LocalizedText) {
            variant = new Variant(DataConverter.str2LocalizedTextArray(str));
        } else if (typeId == SiemensTypeId.PORT) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.RTM) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        } else if (typeId == SiemensTypeId.HW_SUBMODULE) {
            variant = new Variant(DataConverter.jsonStrIntArray2UShortArray(str));
        }


        return variant;
    }

    public static boolean writeDataToOpc(@NonNull OpcUaClient client, OPCNode opcNode, String str) throws Exception {

        //  NodeId type = opcNode.getDataType();
        int type = opcNode.getTypeId();
        if (type == 0) {
            return false;
        }

        client.connect().get();
        DataValue dataValue = OPCUtil.strToDataValue(opcNode, str);
        if (dataValue != null) {
            return client.writeValue(opcNode.getNodeId(), dataValue).get().isGood();
        } else {
            return false;
        }
    }

    public static String isArrayObject(final OPCNode node, final Object o) {
        String result = null;
        int typeId = node.getTypeId();
        node.setArray(true);
        if (o instanceof Integer[]) {
            result = DataConverter.intArray2JsonStr((Integer[]) o);
        } else if (o instanceof UByte[]) {
            UByte[] array = (UByte[]) o;
            if (typeId == SiemensTypeId.Char) {
               /* char[] chars = new char[array.length];
                for (int i = 0; i < array.length; i++) {
                    chars[i] = (char) Integer.parseInt(o.toString());
                }
                result = getGson().toJson(chars, char[].class);*/
                result = DataConverter.uByteArray2CharJonStr(array);
            } else {

              //  result = getGson().toJson(array, Integer[].class);
                result = DataConverter.uByteArray2ShortJonStr(array);
            }
        } else if (o instanceof String[]) {
            String[] array = (String[]) o;
            if (typeId == SiemensTypeId.StringX) {
                String[] newString = new String[array.length];
                for (int i = 0; i < array.length; i++) {
                    try {
                        newString[i] = new String(array[i].getBytes(
                            StandardCharsets.ISO_8859_1), "gbk");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                result = DataConverter.array2ItsJsonStr(newString);
            } else if (typeId == SiemensTypeId.WString) {
                String[] newString = new String[array.length];
                for (int i = 0; i < array.length; i++) {
                    try {
                        newString[i] = new String(array[i].getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                result = DataConverter.array2ItsJsonStr(newString);
            } else {
                result = DataConverter.array2ItsJsonStr(array);
            }

        } else if (o instanceof Float[]) {
            result = DataConverter.array2ItsJsonStr((Float[]) o);
        } else if (o instanceof Double[]) {
            result = DataConverter.array2ItsJsonStr((Double[]) o);
        } else if (o instanceof UShort[]) {
            UShort[] array = (UShort[]) o;
            if (typeId == SiemensTypeId.Date){
                String[] strings = new String[array.length];
                for (int i = 0; i < array.length; i++) {
                    strings[i] = LocalDate.parse(U.getSiemensDateStartDate())
                        .plusDays(array[i].intValue()).toString();
                }
                result = DataConverter.array2ItsJsonStr(strings);
            }else if (typeId == SiemensTypeId.WChar) {
                result = DataConverter.uShortArray2CharJsonStr(array);
            }
            else {
                result =DataConverter.uShortArray2IntJsonStr(array);
            }

        } else if (o instanceof Short[]) {
            result = DataConverter.array2ItsJsonStr((Short[]) o);
        } else if (o instanceof UInteger[]) {
            result = DataConverter.uIntegerArray2LongJonStr((UInteger[]) o);
           // result = getGson().toJson(o, UInteger[].class);
        } else if (o instanceof Long[]) {
            result = DataConverter.array2ItsJsonStr((Long[]) o);
        } else if (o instanceof ULong[]) {
            result = DataConverter.uLongArray2StingJsonStr((ULong[]) o);
        } else if (o instanceof Byte[]) {
            result = DataConverter.array2ItsJsonStr((Byte[]) o);
        } else if (o instanceof Boolean[]) {
            result = DataConverter.array2ItsJsonStr((Boolean[]) o);
        } else if (o instanceof UByte[][]) {
            UByte[][] array = (UByte[][]) o;
            if (typeId == SiemensTypeId.Date_And_Time){
                String[] strings = new String[array.length];
                for (int i = 0; i < array.length; i++) {
                    strings[i] = parseDate_And_Time(array[i]);
                }
                result = DataConverter.array2ItsJsonStr(strings);

            }else {
                result = DataConverter.getGson().toJson(array, UByte[][].class);
            }

        } else if (o instanceof DateTime[]) {
            DateTime[] array = (DateTime[]) o;
            String[] strings = new String[array.length];
            for (int i = 0; i < array.length; i++) {
                strings[i] = parseLDT(array[i]);
            }
            result = DataConverter.array2ItsJsonStr(strings);
        } else {
            node.setArray(false);
        }
        return result;
    }


    private static String parseDate_And_Time(UByte[] uBytes){
        StringBuilder dateString = new StringBuilder();
        for (int i = 0; i < uBytes.length; i++) {
            String s = Integer.toHexString(uBytes[i].intValue());
            if (i < 6) {
                if (s.length() < 2) {
                    s = "0" + s;
                }
                if (i == 0) {
                    if (Integer.parseInt(s) < 90) {
                        dateString.append(20);
                    } else {
                        dateString.append(19);
                    }
                    dateString.append(s).append("-");
                } else if (i == 1) {
                    dateString.append(s).append("-");
                } else if (i == 2) {
                    dateString.append(s).append(" ");
                } else if (i < 5) {
                    dateString.append(s).append(":");
                } else {
                    dateString.append(s);
                }
            }
        }
        return dateString.toString();
    }

    private static String parseLDT(DateTime dateTime){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(dateTime.getJavaTime()),
            ZoneOffset.UTC);
        LocalDateTime startTime = U.getSiemensLDTeStart();
        if (localDateTime.isBefore(startTime)) {
            return DateTimeFormatter.ofPattern(U.getDateTimePattern())
                .format(startTime);
        } else {
            return DateTimeFormatter.ofPattern(U.getDateTimePattern())
                .format(localDateTime);
        }
    }

    public static String dataValue2Str(final OPCNode node, final Object o) {
        String result = isArrayObject(node, o);
        if (result == null) {
            int typeId = node.getTypeId();
            if (typeId == SiemensTypeId.Char) {
                result = String.valueOf((char) Integer.parseInt(o.toString()));
            } else if (typeId == SiemensTypeId.Date_And_Time) {
                result = parseDate_And_Time((UByte[]) o);

            } else if (typeId == SiemensTypeId.Date) {
                result = LocalDate.parse(U.getSiemensDateStartDate())
                    .plusDays(((UShort) o).intValue()).toString();
            } else if (typeId == SiemensTypeId.LDT) {
                result = parseLDT((DateTime) o);
            } else if (typeId == SiemensTypeId.WChar) {
                result = String.valueOf((char) Integer.parseInt(o.toString()));
            } else if (typeId == SiemensTypeId.StringX) {
                try {
                    result = new String(((String) o).getBytes(StandardCharsets.ISO_8859_1), "gbk");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (typeId == SiemensTypeId.WString) {
                //result = U.unicodeToUtf8((String) o);
                result = new String(((String) o).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                // result = U.unicodeToUtf8((String) o);
            } else {
                result = o.toString();
            }
        }
        return result;
    }


    public static boolean writeDataToOpc(@NonNull OpcUaClient client, OPCNode opcNode, boolean b, String uri) throws Exception {

        int type = opcNode.getTypeId();

        if (type == 0) {
            return false;
        }

        client.connect().get();

        if (client.writeValue(opcNode.getNodeId(), OPCUtil.booleanToDataValue(b)).get().isGood()) {

            //  U.showShortToast(uri+U.getString(R.string.write_success));
            return true;
        } else {

            //U.showShortToast(uri+U.getString(R.string.write_fail));
            return false;
        }


    }


    public static List<UaNode> browseAsync(@NonNull OpcUaClient client, NodeId nodeId) throws Exception {
        List<UaNode> list = new ArrayList<>();
        browseRecursive(client, nodeId, list).get();
        return list;

    }


    public static CompletableFuture<Void> browseRecursive(@NonNull OpcUaClient client, NodeId nodeId, List<UaNode> list) {
        return client.getAddressSpace().browseNodesAsync(nodeId).thenCompose(nodes -> {

            list.addAll(nodes);
            Stream<CompletableFuture<Void>> futures =
                nodes.stream().map(child -> browseRecursive(client, child.getNodeId(), list));

            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        });
    }


    private static void parseDate(LocalDateTime date, UByte[] uBytes, String str) {
        int year = date.getYear();
        // Log.e("TAG", "parseDate: " + year );
        if (year >= 2000) {
            year = year - 2000;
        } else {
            year = year - 1990;
        }
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        int hour = date.getHour();
        int minutes = date.getMinute();
        int seconds = date.getSecond();

        int week = date.getDayOfWeek().getValue();
        //Integer.parseInt(String.valueOf(year),16);
        uBytes[0] = UByte.valueOf(Integer.parseInt(String.valueOf(year), 16));
        uBytes[1] = UByte.valueOf(Integer.parseInt(String.valueOf(month), 16));
        uBytes[2] = UByte.valueOf(Integer.parseInt(String.valueOf(day), 16));
        uBytes[3] = UByte.valueOf(Integer.parseInt(String.valueOf(hour), 16));
        uBytes[4] = UByte.valueOf(Integer.parseInt(String.valueOf(minutes), 16));
        uBytes[5] = UByte.valueOf(Integer.parseInt(String.valueOf(seconds), 16));

        uBytes[7] = UByte.valueOf(Integer.parseInt(String.valueOf(week), 16));
    }

    public static SimpleArrayMap<Integer, OPCNode> browseNode(OpcUaClient client, NodeId browseRoot, OPCNode plcNode) {
        SimpleArrayMap<Integer, OPCNode> map = new SimpleArrayMap<>();
        try {
            client.connect().get();
            browseNode(client, browseRoot, plcNode, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }

    public static UaVariableNode getVariableNode(OpcUaClient client, NodeId nodeId) {

        try {
            client.connect().get();
            return client.getAddressSpace().getVariableNode(nodeId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void browseNode(OpcUaClient client, NodeId browseRoot, OPCNode plcNode, SimpleArrayMap<Integer, OPCNode> map) {
        try {
            List<? extends UaNode> nodes = client.getAddressSpace().browseNodes(browseRoot);

            for (UaNode node : nodes) {
                OPCNode n = new OPCNode(node);
                // PLCNode nodeInMap = map.get(n.getNodeId().hashCode());
                int code = n.hashCode();
                if (map.get(code) == null) {
                    map.put(code, n);
                    if (plcNode != null) {
                        plcNode.addChild(n);
                    }
                }

                // n.setLevel(level);

                browseNode(client, node.getNodeId(), n, map);

            }
        } catch (UaException e) {
            e.printStackTrace();
        }
    }


    public static List<? extends UaNode> browseNode(OpcUaClient client, NodeId nodeId) {
        try {
            return client.getAddressSpace().browseNodes(nodeId);
        } catch (Exception e) {
            return null;
        }
    }

    public static void subscription(@NonNull OpcUaClient client, @NonNull List<NodeId> nodeIdList, OpcDataResult.Result<SubscriptionOPCNode> result, MutableLiveData<Boolean> flag) throws Exception {
        UaSubscription subscription = getUaSubscription(client);
        if (subscription == null) return;
        List<MonitoredItemCreateRequest> requests = new ArrayList<>();
        for (NodeId nodeId : nodeIdList) {
            ReadValueId readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE);
            MonitoringParameters parameters = new MonitoringParameters(
                uint(nodeId.hashCode()),
                100.0,     // sampling interval
                null,       // filter, null means use default
                uint(10),   // queue size
                true        // discard oldest
            );
            MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(
                readValueId,
                MonitoringMode.Reporting,
                parameters
            );
            requests.add(request);
        }

        UaSubscription.ItemCreationCallback onItemCreated =
            (item, id) -> item.setValueConsumer((z, x) -> {
                onSubscriptionValue(z, x, result, flag);
            });

        List<UaMonitoredItem> items = subscription.createMonitoredItems(
            TimestampsToReturn.Both,
            requests,
            onItemCreated
        ).get();


        for (UaMonitoredItem item : items) {
            if (item.getStatusCode().isGood()) {
                U.showLongToast(item.getReadValueId().getNodeId().toString() + U.getString(R.string.subscription_success));
            } else {
                U.showLongToast(item.getReadValueId().getNodeId().toString() + U.getString(R.string.subscription_fail));
            }
        }
    }


    public static void subscription(@NonNull OpcUaClient client, @NonNull NodeId nodeId, OpcDataResult.Result<SubscriptionOPCNode> result, MutableLiveData<Boolean> flag) throws Exception {
        List<NodeId> nodeIds = new ArrayList<>();
        nodeIds.add(nodeId);
        OPCUtil.subscription(client, nodeIds, result, flag);
    }

    public static void onSubscriptionValue(UaMonitoredItem item, DataValue value, OpcDataResult.Result<SubscriptionOPCNode> result, MutableLiveData<Boolean> flag) {
        //
        if (flag.getValue() != null && flag.getValue()) {
            // Log.d("onSubscriptionValue", "onSubscriptionValue: ");
            result.onResult(new OpcDataResult<>(new SubscriptionOPCNode(item, value)));
        }
    }

    private static MonitoredItemCreateRequest subRequest(NodeId nodeId, UaSubscription subscription) {
        ReadValueId readValueId = new ReadValueId(
            nodeId,
            AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE
        );

        UInteger clientHandle = subscription.nextClientHandle();

        MonitoringParameters parameters = new MonitoringParameters(
            clientHandle,
            100.0,     // sampling interval
            null,       // filter, null means use default
            uint(10),   // queue size
            true        // discard oldest
        );


        return new MonitoredItemCreateRequest(
            readValueId,
            MonitoringMode.Reporting,
            parameters
        );
    }

    public static void subscription(@NonNull OpcUaClient client, @NonNull PageNode multi, MutableLiveData<Boolean> flag) throws Exception {
        UaSubscription subscription = getUaSubscription(client);
        if (subscription == null) return;
        List<MonitoredItemCreateRequest> requests = new ArrayList<>();
        MonitoredItemCreateRequest request = subRequest(multi.getNodeId(), subscription);


        requests.add(request);
        UaSubscription.ItemCreationCallback onItemCreated =
            (item, id) -> item.setValueConsumer((z, x) -> {
                if (U.isTrue(flag)) {
                    // dataResult.onResult(new OpcDataResult<>(multi,x));
                    multi.setDataValue(x);
                }
            });


        List<UaMonitoredItem> items = subState(subscription, onItemCreated, requests);


        for (UaMonitoredItem item : items) {
            if (item.getStatusCode().isGood()) {
                //   U.showLongToast(item.getReadValueId().getNodeId().toString() + U.getString(R.string.subscription_success));
                multi.setSubscribed(true);
            } else {
                //  U.showLongToast(item.getReadValueId().getNodeId().toString() + U.getString(R.string.subscription_fail));

            }
        }

    }

    private static List<UaMonitoredItem> subState(@NonNull UaSubscription subscription, UaSubscription.ItemCreationCallback callback, List<MonitoredItemCreateRequest> requests) throws Exception {
        return subscription.createMonitoredItems(
            TimestampsToReturn.Both,
            requests,
            callback
        ).get();
    }

    public static void subscription(@NonNull OpcUaClient client, @NonNull ArrayMap<Integer, PageNode> map, OpcData2Result.Result<Integer, String> result, MutableLiveData<Boolean> flag) throws Exception {
        UaSubscription subscription = getUaSubscription(client);
        if (subscription == null) return;
        List<MonitoredItemCreateRequest> requests = new ArrayList<>();
        map.forEach((i, t) -> {
            MonitoredItemCreateRequest request = subRequest(t.getNodeId(), subscription);
            requests.add(request);
        });
        UaSubscription.ItemCreationCallback onItemCreated =
            (item, id) -> item.setValueConsumer((z, x) -> {
                if (U.isTrue(flag)) {
                    //  itemCreationCallback(z,x,result);
                    Objects.requireNonNull(map.get(z.getReadValueId().getNodeId().hashCode())).setDataValue(x);
                }

            });
        List<UaMonitoredItem> items = subState(subscription, onItemCreated, requests);
        items.forEach(i -> {
            if (i.getStatusCode().isGood()) {
                int id = i.getReadValueId().getNodeId().hashCode();
                PageNode m = map.get(id);
                if (m != null) {
                    m.setSubscribed(true);
                }
            }
        });
    }

    private synchronized static void itemCreationCallback(UaMonitoredItem item, DataValue dataValue, OpcData2Result.Result<Integer, String> result) {
        try {
            result.onResult(new OpcData2Result<>(item.getReadValueId().getNodeId().hashCode(), dataValue
                .getValue().getValue().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
