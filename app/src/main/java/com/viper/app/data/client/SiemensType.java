package com.viper.app.data.client;

import com.google.common.collect.ImmutableMap;

public final class SiemensType {
    private static  ImmutableMap<Integer, String> map;

    private synchronized static void builder () {
        if (map!=null) return;
        ImmutableMap.Builder<Integer, String> builder = ImmutableMap.builder();
      //  builder = new SimpleArrayMap<>();
        builder.put(SiemensTypeId.Bool,"Bool");
        builder.put(SiemensTypeId.Byte,"Byte");
        builder.put(SiemensTypeId.Int,"Int");
        builder.put(SiemensTypeId.UInt,"UInt");
        builder.put(SiemensTypeId.UDInt,"UDInt");
        builder.put(SiemensTypeId.USInt,"USInt");
        builder.put(SiemensTypeId.SInt,"SInt");
        builder.put(SiemensTypeId.DInt,"DInt");
        builder.put(SiemensTypeId.Char,"Char");
        builder.put(SiemensTypeId.Word,"Word");
        builder.put(SiemensTypeId.DWord,"DWord");
        builder.put(SiemensTypeId.WChar,"WChar");
        builder.put(SiemensTypeId.Date,"Date");
        builder.put(SiemensTypeId.Date_And_Time,"Date_And_Time");
        builder.put(SiemensTypeId.Real,"Real");
        builder.put(SiemensTypeId.LReal,"LReal");
        builder.put(SiemensTypeId.Time,"Time");
        builder.put(SiemensTypeId.LTime,"LTime");
        builder.put(SiemensTypeId.S5Time,"S5Time");
        builder.put(SiemensTypeId.LWord,"LWord");
        builder.put(SiemensTypeId.StringX,"StringX");
        builder.put(SiemensTypeId.DB_ANY,"DB_ANY");
        builder.put(SiemensTypeId.CONN_ANY,"CONN_ANY");
        builder.put(SiemensTypeId.AOM_IDENT,"AOM_IDENT");
        builder.put(SiemensTypeId.CONN_OUC,"CONN_OUC");
        builder.put(SiemensTypeId.CONN_PRG,"CONN_PRG");
        builder.put(SiemensTypeId.CONN_R_ID,"CONN_R_ID");
        builder.put(SiemensTypeId.DB_DYN,"DB_DYN");
        builder.put(SiemensTypeId.DB_WWW,"DB_WWW");
        builder.put(SiemensTypeId.EVENT_ANY,"EVENT_ANY");
        builder.put(SiemensTypeId.EVENT_ATT,"EVENT_ATT");
        builder.put(SiemensTypeId.EVENT_HWINT,"EVENT_HWINT");
        builder.put(SiemensTypeId.HW_ANY,"HW_ANY");
        builder.put(SiemensTypeId.HW_DEVICE,"HW_DEVICE");
        builder.put(SiemensTypeId.HW_INTERFACE,"HW_INTERFACE");
        builder.put(SiemensTypeId.HW_DPMASTER,"HW_DPMASTER");
        builder.put(SiemensTypeId.HW_DPSLAVE,"HW_DPSLAVE");
        builder.put(SiemensTypeId.HW_HSC,"HW_HSC");
        builder.put(SiemensTypeId.HW_IEPORT,"HW_IEPORT");
        builder.put(SiemensTypeId.HW_IO,"HW_IO");
        builder.put(SiemensTypeId.OB_ANY,"OB_ANY");
        builder.put(SiemensTypeId.OB_ATT,"OB_ATT");
        builder.put(SiemensTypeId.OB_CYCLIC,"OB_CYCLIC");
        builder.put(SiemensTypeId.OB_DELAY,"OB_DELAY");
        builder.put(SiemensTypeId.OB_DIAG,"OB_DIAG");
        builder.put(SiemensTypeId.OB_HWINT,"OB_HWINT");
        builder.put(SiemensTypeId.OB_PCYCLE,"OB_PCYCLE");
        builder.put(SiemensTypeId.OB_STARTUP,"OB_STARTUP");
        builder.put(SiemensTypeId.OB_TIMEERROR,"OB_TIMEERROR");
        builder.put(SiemensTypeId.OB_TOD,"OB_TOD");
        builder.put(SiemensTypeId.PIP,"PIP");
        builder.put(SiemensTypeId.WString,"WString");
        builder.put(SiemensTypeId.LocalizedText,"LocalizedText");
        builder.put(SiemensTypeId.LDT,"LDT");
        builder.put(SiemensTypeId.LInt,"LInt");
        builder.put(SiemensTypeId.EnumValueType,"EnumValueType");
        builder.put(SiemensTypeId.PORT,"PORT");
        builder.put(SiemensTypeId.RTM,"RTM");
        builder.put(SiemensTypeId.HW_SUBMODULE,"HW_SUBMODULE");

        map = builder.build();
    }

    /*public static ImmutableMap<Integer, String> getTypeMapById() {
        return TypeMapById;
    }*/

    public static String getType(int id){
        if (map==null){
            builder();
        }
        return map.get(id);
    }
}
