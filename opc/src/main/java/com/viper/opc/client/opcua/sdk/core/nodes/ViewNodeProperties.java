/*
 * Copyright (c) 2019 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package com.viper.opc.client.opcua.sdk.core.nodes;

import com.viper.opc.client.opcua.sdk.core.QualifiedProperty;
import com.viper.opc.client.opcua.sdk.core.ValueRanks;
import com.viper.opc.client.opcua.stack.core.Identifiers;
import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.viper.opc.client.opcua.stack.core.util.Namespaces;

public final class ViewNodeProperties {

    private ViewNodeProperties() {}

    /**
     * The NodeVersion Property is used to indicate the version of a Node.
     * <p>
     * The NodeVersion Property is updated each time a Reference is added or
     * removed from the Node the Property belongs to.
     */
    public static final QualifiedProperty<String> NodeVersion = new QualifiedProperty<>(
        Namespaces.OPC_UA,
        "NodeVersion",
        Identifiers.String.expanded(),
        ValueRanks.Scalar,
        String.class
    );

    /**
     * The version number for the View. When Nodes are added to or removed
     * from a View, the value of the ViewVersion Property is updated.
     * <p>
     * Clients may detect changes to the composition of a View using this
     * Property.
     * <p>
     * The value of the ViewVersion shall always be greater than 0.
     */
    public static final QualifiedProperty<UInteger> ViewVersion = new QualifiedProperty<>(
        Namespaces.OPC_UA,
        "ViewVersion",
        Identifiers.UInt32.expanded(),
        ValueRanks.Scalar,
        UInteger.class
    );

}
