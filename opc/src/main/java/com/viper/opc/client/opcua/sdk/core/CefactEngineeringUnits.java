package com.viper.opc.client.opcua.sdk.core;

import com.viper.opc.client.opcua.stack.core.types.structured.EUInformation;

public abstract class CefactEngineeringUnits extends CefactEngineeringUnits0 {
    private CefactEngineeringUnits() {
    }

    public static EUInformation[] getAll() {
        return BY_UNIT_ID.values()
            .toArray(new EUInformation[0]);
    }

    public static EUInformation getByUnitId(int unitId) {
        return BY_UNIT_ID.get(unitId);
    }
}
