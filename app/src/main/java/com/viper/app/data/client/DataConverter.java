package com.viper.app.data.client;

import com.google.gson.Gson;
import com.viper.app.util.P;
import com.viper.opc.client.opcua.stack.core.types.builtin.LocalizedText;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UByte;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.ULong;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UShort;

public final class DataConverter {
    private static Gson gson;
    private DataConverter(){}

    public static Gson getGson() {
        if (gson==null){
            gson = new Gson();
        }
        return gson;
    }

    public static String[] str2StringArray(String jonStrOfStringArray){
        return getGson().fromJson(jonStrOfStringArray,String[].class);
    }

    public static String array2ItsJsonStr(int[] array){
        return getGson().toJson(array,int[].class);
    }

    public static String array2ItsJsonStr(float[] array){
        return getGson().toJson(array,float[].class);
    }

    public static String array2ItsJsonStr(Float[] array){
        return getGson().toJson(array,Float[].class);
    }
    public static String array2ItsJsonStr(Double[] array){
        return getGson().toJson(array,Double[].class);
    }

    public static String array2ItsJsonStr(double[] array){
        return getGson().toJson(array,double[].class);
    }

    public static String array2ItsJsonStr(char[] array){
        return getGson().toJson(array,char[].class);
    }

    public static String array2ItsJsonStr(Character[] array){
        return getGson().toJson(array,Character[].class);
    }

    public static String array2ItsJsonStr(long[] array){
        return getGson().toJson(array,long[].class);
    }

    public static String array2ItsJsonStr(byte[] array){
        return getGson().toJson(array,byte[].class);
    }

    public static String array2ItsJsonStr(Byte[] array){
        return getGson().toJson(array,Byte[].class);
    }

    public static String array2ItsJsonStr(short[] array){
        return getGson().toJson(array,short[].class);
    }

    public static String array2ItsJsonStr(Short[] array){
        return getGson().toJson(array,Short[].class);
    }

    public static String array2ItsJsonStr(boolean[] array){
        return getGson().toJson(array,boolean[].class);
    }

    public static String array2ItsJsonStr(Boolean[] array){
        return getGson().toJson(array,Boolean[].class);
    }

    public static String array2ItsJsonStr(Long[] array){
        return getGson().toJson(array,Long[].class);
    }

    public static String array2ItsJsonStr(String[] array){
        return getGson().toJson(array,String[].class);
    }

    public static String uLongArray2StingJsonStr(ULong[] array){
        return getGson().toJson(uLongArray2StingArray(array),String[].class);
    }

    public static String uShortArray2IntJsonStr(UShort[] array){
        return getGson().toJson(uShortArray2IntArray(array),int[].class);
    }


    public static String[] uLongArray2StingArray(ULong[] array){
        String[] strings = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            strings[i] = array[i].toString();
        }
        return strings;
    }

    public static String uShortArray2CharJsonStr(UShort[] array){
        return array2ItsJsonStr(uShortArray2CharArray(array));
    }

    public static char[] uShortArray2CharArray(UShort[] array){
        char[] chars = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            chars[i] = (char) array[i].intValue();
        }
        return chars;
    }


    public static String intArray2JsonStr(Integer[] array){
        return getGson().toJson(array,Integer[].class);
    }

    public static LocalizedText[] str2LocalizedTextArray(String jonStrOfLocalizedTextArray){
        return getGson().fromJson(jonStrOfLocalizedTextArray,LocalizedText[].class);
    }

    public static boolean[] str2BooleanArray(String jonStrOfBooleanArray){
        return getGson().fromJson(jonStrOfBooleanArray,boolean[].class);
    }

    public static float[] str2FloatArray(String jonStrOfFloatArray){
        return getGson().fromJson(jonStrOfFloatArray,float[].class);
    }

    public static double[] str2DoubleArray(String jonStrOfDoubleArray){
        return getGson().fromJson(jonStrOfDoubleArray,double[].class);
    }

    public static long[] str2LongArray(String jonStrOfLongArray){
        return getGson().fromJson(jonStrOfLongArray,long[].class);
    }

    public static int[] str2IntArray(String jonStrOfIntArray){
        return getGson().fromJson(jonStrOfIntArray,int[].class);
    }
    public static short[] str2ShortArray(String jonStrOfShortArray){
        return getGson().fromJson(jonStrOfShortArray,short[].class);
    }
    public static UByte[] str2UByteArray(String jonStrOfUByteArray){
        return getGson().fromJson(jonStrOfUByteArray,UByte[].class);
    }

    public static byte[] str2ByteArray(String jonStrOfByteArray){
        return getGson().fromJson(jonStrOfByteArray,byte[].class);
    }

    public static char[] str2CharArray(String jonStrOfCharArray){
        return getGson().fromJson(jonStrOfCharArray,char[].class);
    }

    public static UShort[] intArray2UShortArray(int[] ints){
        UShort[] uShort = new UShort[ints.length];
        for (int i = 0; i < ints.length; i++) {
            uShort[i] = UShort.valueOf(ints[i]);
        }
        return uShort;
    }

    public static ULong[] stringArray2ULongArray(String[] strings){
        ULong[] uLongs = new ULong[strings.length];
        for (int i = 0; i < strings.length; i++) {
            uLongs[i] = ULong.valueOf(strings[i]);
        }
        return uLongs;
    }

    public static ULong[] jsonStrStringArray2ULongArray(String jsonStrStringArray){
        return stringArray2ULongArray(str2StringArray(jsonStrStringArray));
    }

    public static UShort[] charArray2UShortArray(char[] chars){
        UShort[] uShort = new UShort[chars.length];
        for (int i = 0; i < chars.length; i++) {
            uShort[i] = UShort.valueOf(chars[i]);
        }
        return uShort;
    }

    public static UByte[] charArray2UByteArray(char[] chars){
        UByte[] uBytes = new UByte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            uBytes[i] = UByte.valueOf(chars[i]);
        }
        return uBytes;
    }
    public static UByte[] shortArray2UByteArray(short[] shorts){
        UByte[] uBytes = new UByte[shorts.length];
        for (int i = 0; i < shorts.length; i++) {
            uBytes[i] = UByte.valueOf(shorts[i]);
        }
        return uBytes;
    }



    public static int[] uShortArray2IntArray(UShort[] uShorts){
        int[] ints = new int[uShorts.length];
        for (int i = 0; i < uShorts.length; i++) {
            ints[i] = uShorts[i].intValue();
        }
        return ints;
    }


    public static UByte[] jsonStrShortArray2UByteArray(String jsonStrShortArray){
        return shortArray2UByteArray(str2ShortArray(jsonStrShortArray));
    }

    public static UShort[] jsonStrIntArray2UShortArray(String jsonStrIntArray){
        return intArray2UShortArray(str2IntArray(jsonStrIntArray));
    }

    public static UShort[] jsonStrCharArray2UShortArray(String jsonStrCharArray){
        return charArray2UShortArray(str2CharArray(jsonStrCharArray));
    }

    public static UByte[] jsonStrCharArray2UByteArray(String jsonStrCharArray){
        return charArray2UByteArray(str2CharArray(jsonStrCharArray));
    }

    public static UInteger[] longArray2UIntegerArray(long[] longs){
        UInteger[] uIntegers = new UInteger[longs.length];
        for (int i = 0; i < longs.length; i++) {
            uIntegers[i] = UInteger.valueOf(longs[i]);
        }
        return uIntegers;
    }

    public static UInteger[] jsonStrLongArray2UIntegerArray(String jsonStrLongArray){
        return longArray2UIntegerArray(str2LongArray(jsonStrLongArray));
    }

    public static String uIntegerArray2LongJonStr(UInteger[] uIntegers){
        long[] longs = new long[uIntegers.length];
        for (int i = 0; i < uIntegers.length; i++) {
            longs[i] = uIntegers[i].longValue();
        }
        return getGson().toJson(longs,long[].class);
    }

    public static String uByteArray2CharJonStr(UByte[] uBytes){
        char[] chars = new char[uBytes.length];
        for (int i = 0; i < uBytes.length; i++) {
            chars[i] = (char) uBytes[i].intValue();
        }
        return getGson().toJson(chars,char[].class);
    }

    public static String uByteArray2ShortJonStr(UByte[] uBytes){
        short[] shorts = new short[uBytes.length];
        for (int i = 0; i < uBytes.length; i++) {
            shorts[i] = uBytes[i].shortValue();
        }
        return getGson().toJson(shorts,short[].class);
    }

}
