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

import com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.UByte;

public interface ViewNode extends Node {

    Boolean getContainsNoLoops();

    UByte getEventNotifier();

    void setContainsNoLoops(Boolean containsNoLoops);

    void setEventNotifier(UByte eventNotifier);

}
