package com.viper.app.data.client;

import com.viper.opc.client.opcua.stack.core.Identifiers;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.Unsigned;

public final class SiemensTypeId {

    public static final int Bool = init(0,1);
    public static final int SInt = init(0,2);
    public static final int USInt = init(0,3);
    public static final int Int = init(0,4);
    public static final int UInt = init(0,5);
    public static final int DInt = init(0,6);
    public static final int UDInt = init(0,7);
    public static final int LInt = init(0,8);
    public static final int UInt64 = init(0,9);
    public static final int Real = init(0,10);
    public static final int LReal = init(0,11);
    public static final int WString = init(0,12);
    public static final int LDT = init(0,13);
    public static final int LocalizedText = init(0,21);
    public static final int Structure = init(0,22);
    public static final int Enumeration = init(0,29);
    public static final int Organizes = init(0,35);
    public static final int HasModellingRule = init(0,37);
    public static final int HasTypeDefinition = init(0,40);
    public static final int HasSubtype = init(0,45);
    public static final int HasProperty = init(0,46);
    public static final int HasComponent = init(0,47);

    public static final int Byte = init(3,3001);
    public static final int Word = init(3,3002);
    public static final int DWord = init(3,3003);
    public static final int LWord = init(3,3004);
    public static final int S5Time = init(3,3005);
    public static final int Time = init(3,3006);
    public static final int LTime = init(3,3007);
    public static final int Date = init(3,3008);

    public static final int Date_And_Time = init(3,3011);
    public static final int Char = init(3,3012);
    public static final int WChar = init(3,3013);
    public static final int StringX = init(3,3014);
    public static final int Timer = init(3,3015);

    public static final int HW_ANY = init(3,3024);

    public static final int AOM_IDENT = init(3,3026);
    public static final int OB_ANY = init(3,3027);
    public static final int RTM = init(3,3028);
    public static final int PIP = init(3,3029);

    public static final int CONN_ANY = init(3,3030);
    public static final int CONN_R_ID = init(3,3031);
    public static final int DB_ANY = init(3,3032);

    public static final int HW_DEVICE = init(3,3033);
    public static final int HW_IO = init(3,3034);
    public static final int HW_SUBMODULE = init(3,3036);

    public static final int HW_INTERFACE = init(3,3038);
    public static final int HW_IEPORT = init(3,3039);
    public static final int HW_HSC = init(3,3040);

    public static final int HW_DPMASTER = init(3,3044);
    public static final int HW_DPSLAVE = init(3,3045);
    public static final int EVENT_ANY = init(3,3046);
    public static final int EVENT_ATT = init(3,3047);
    public static final int EVENT_HWINT = init(3,3048);
    public static final int OB_DELAY = init(3,3049);
    public static final int OB_TOD = init(3,3050);
    public static final int OB_CYCLIC = init(3,3051);
    public static final int OB_ATT = init(3,3052);
    public static final int OB_PCYCLE = init(3,3053);

    public static final int OB_HWINT = init(3,3054);
    public static final int OB_DIAG = init(3,3055);
    public static final int OB_TIMEERROR = init(3,3056);
    public static final int OB_STARTUP = init(3,3057);
    public static final int PORT = init(3,3058);
    public static final int CONN_PRG = init(3,3059);
    public static final int CONN_OUC = init(3,3060);
    public static final int DB_WWW = init(3,3061);
    public static final int DB_DYN = init(3,3062);
    public static final int EnumValueType = init(3,3063);
    public static final int HasInOutVar = init(3,4002);
    public static final int HasInputVar = init(3,4003);
    public static final int HasOutputVar = init(3,4004);
    public static final int HasLocalVar = init(3,4005);
    public static final int HasBaseObject = init(3,4006);
  //  public static final int EnumValueType = init(0,7594);












    private static int init(Integer ns,Integer id) {
        return  31 * ns.hashCode() + id.hashCode();
       // return result;

       /* return new NodeId(Unsigned.ushort(ns), Unsigned.uint(id)).hashCode();*/
    }
}
