/*
 * Copyright (c) 2019 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package com.viper.opc.client.opcua.sdk.client.api;

import java.util.concurrent.CompletableFuture;

import com.viper.opc.client.opcua.sdk.client.AddressSpace;
import com.viper.opc.client.opcua.sdk.client.api.config.OpcUaClientConfig;
import com.viper.opc.client.opcua.sdk.client.api.services.AttributeServices;
import com.viper.opc.client.opcua.sdk.client.api.services.MethodServices;
import com.viper.opc.client.opcua.sdk.client.api.services.MonitoredItemServices;
import com.viper.opc.client.opcua.sdk.client.api.services.NodeManagementServices;
import com.viper.opc.client.opcua.sdk.client.api.services.SubscriptionServices;
import com.viper.opc.client.opcua.sdk.client.api.services.ViewServices;
import com.viper.opc.client.opcua.sdk.client.api.subscriptions.UaSubscriptionManager;
import com.viper.opc.client.opcua.sdk.client.subscriptions.OpcUaSubscriptionManager;
import com.viper.opc.client.opcua.stack.core.serialization.UaRequestMessage;
import com.viper.opc.client.opcua.stack.core.serialization.UaResponseMessage;

public interface UaClient extends AttributeServices,
    MethodServices, MonitoredItemServices, NodeManagementServices, SubscriptionServices, ViewServices {

    /**
     * @return the {@link OpcUaClientConfig} for this client.
     */
    OpcUaClientConfig getConfig();

    /**
     * Connect to the configured endpoint.
     *
     * @return a {@link CompletableFuture} holding this client instance.
     */
    CompletableFuture<? extends UaClient> connect();

    /**
     * Disconnect from the configured endpoint.
     *
     * @return a {@link CompletableFuture} holding this client instance.
     */
    CompletableFuture<? extends UaClient> disconnect();

    /**
     * @return a {@link CompletableFuture} holding the {@link UaSession}.
     */
    CompletableFuture<? extends UaSession> getSession();

    /**
     * @return the {@link AddressSpace}.
     */
    AddressSpace getAddressSpace();

    /**
     * @return the {@link OpcUaSubscriptionManager} for this client.
     */
    UaSubscriptionManager getSubscriptionManager();

    /**
     * Send a {@link UaRequestMessage}.
     *
     * @param request the request to send.
     * @return a {@link CompletableFuture} holding the response.
     */
    <T extends UaResponseMessage> CompletableFuture<T> sendRequest(UaRequestMessage request);

}
