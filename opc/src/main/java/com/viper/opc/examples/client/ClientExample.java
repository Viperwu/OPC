/*
 * Copyright (c) 2021 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package com.viper.opc.examples.client;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import com.viper.opc.client.opcua.sdk.client.OpcUaClient;
import com.viper.opc.client.opcua.sdk.client.api.identity.AnonymousProvider;
import com.viper.opc.client.opcua.sdk.client.api.identity.IdentityProvider;
import com.viper.opc.client.opcua.stack.core.security.SecurityPolicy;
import com.viper.opc.client.opcua.stack.core.types.structured.EndpointDescription;


public interface ClientExample {

    default String getEndpointUrl() {
        return "opc.tcp://localhost:12686/viper";
    }

    default Predicate<EndpointDescription> endpointFilter() {
        return e -> getSecurityPolicy().getUri().equals(e.getSecurityPolicyUri());
    }

    default SecurityPolicy getSecurityPolicy() {
        return SecurityPolicy.Basic256Sha256;
    }

    default IdentityProvider getIdentityProvider() {
        return new AnonymousProvider();
    }

    void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception;

}
