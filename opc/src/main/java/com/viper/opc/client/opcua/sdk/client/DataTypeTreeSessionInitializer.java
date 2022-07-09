/*
 * Copyright (c) 2019 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package com.viper.opc.client.opcua.sdk.client;

import java.util.concurrent.CompletableFuture;

import com.viper.opc.client.opcua.sdk.client.session.SessionFsm;
import com.viper.opc.client.opcua.sdk.core.DataTypeTree;
import com.viper.opc.client.opcua.stack.client.UaStackClient;
import com.viper.opc.client.opcua.stack.core.util.Unit;

/**
 * Builds a {@link DataTypeTree} and stores it on an {@link OpcUaSession} as an attribute under
 * the key {@link DataTypeTreeSessionInitializer#SESSION_ATTRIBUTE_KEY}.
 */
public class DataTypeTreeSessionInitializer implements SessionFsm.SessionInitializer {

    /**
     * The attribute key that the {@link DataTypeTree} will be stored under in the {@link OpcUaSession}.
     *
     * @see OpcUaSession#getAttribute(String)
     * @see OpcUaSession#setAttribute(String, Object)
     */
    public static final String SESSION_ATTRIBUTE_KEY = "dataTypeTree";

    @Override
    public CompletableFuture<Unit> initialize(UaStackClient stackClient, OpcUaSession session) {
        return DataTypeTreeBuilder.buildAsync(stackClient, session)
            .thenAccept(tree -> session.setAttribute(SESSION_ATTRIBUTE_KEY, tree))
            .thenApply(v -> Unit.VALUE);
    }

}
