/*
 * Copyright (c) 2019 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package com.viper.opc.client.opcua.stack.core.channel;

import com.viper.opc.client.opcua.stack.core.types.builtin.StatusCode;

public class MessageAbortException extends Exception {

    private final long requestId;
    private final StatusCode statusCode;

    MessageAbortException(String message, long requestId, StatusCode statusCode) {
        super(message);

        this.statusCode = statusCode;
        this.requestId = requestId;
    }

    public long getRequestId() {
        return requestId;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

}
