/*
 * Copyright (c) 2019 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package com.viper.opc.client.opcua.sdk.client.api.identity;

import com.viper.opc.client.opcua.stack.core.types.builtin.ByteString;
import com.viper.opc.client.opcua.stack.core.types.structured.EndpointDescription;
import com.viper.opc.client.opcua.stack.core.types.structured.SignatureData;
import com.viper.opc.client.opcua.stack.core.types.structured.UserIdentityToken;

public interface IdentityProvider {

    /**
     * Return the {@link UserIdentityToken} and {@link SignatureData} (if applicable for the token) to use when
     * activating a session.
     *
     * @param endpoint the {@link EndpointDescription} being connected to.
     * @return a {@link SignedIdentityToken} containing the {@link UserIdentityToken} and {@link SignatureData}.
     */
    SignedIdentityToken getIdentityToken(EndpointDescription endpoint, ByteString serverNonce) throws Exception;

}
