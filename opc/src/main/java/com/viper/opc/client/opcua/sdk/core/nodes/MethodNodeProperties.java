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
import com.viper.opc.client.opcua.stack.core.types.structured.Argument;
import com.viper.opc.client.opcua.stack.core.util.Namespaces;

public final class MethodNodeProperties {

    private MethodNodeProperties() {}

    /**
     * The NodeVersion Property is used to indicate the version of a Node. It
     * does not apply to Properties.
     * <p>
     * The NodeVersion Property is updated each time a Reference is added or
     * removed from the Node the Property belongs to.
     * <p>
     * Although the relationship of a Variable to its DataType is not modelled
     * using References, changes to the DataType Attribute of a Variable lead
     * to an update of the NodeVersion Property.
     * <p>
     * The usage of this Property is optional.
     */
    public static final QualifiedProperty<String> NodeVersion = new QualifiedProperty<>(
        Namespaces.OPC_UA,
        "NodeVersion",
        Identifiers.String.expanded(),
        ValueRanks.Scalar,
        String.class
    );

    /**
     * The InputArguments Property is used to specify the arguments that shall
     * be used by a client when calling the Method.
     */
    public static final QualifiedProperty<Argument[]> InputArguments = new QualifiedProperty<>(
        Namespaces.OPC_UA,
        "InputArguments",
        Identifiers.Argument.expanded(),
        ValueRanks.OneDimension,
        Argument[].class
    );

    /**
     * The OutputArguments Property specifies the result returned from the
     * Method call.
     */
    public static final QualifiedProperty<Argument[]> OutputArguments = new QualifiedProperty<>(
        Namespaces.OPC_UA,
        "OutputArguments",
        Identifiers.Argument.expanded(),
        ValueRanks.OneDimension,
        Argument[].class
    );

}
