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

import com.viper.opc.client.opcua.stack.core.types.builtin.LocalizedText;

public interface ReferenceTypeNode extends Node {

    Boolean getIsAbstract();

    Boolean getSymmetric();

    LocalizedText getInverseName();

    void setIsAbstract(Boolean isAbstract);

    void setSymmetric(Boolean symmetric);

    void setInverseName(LocalizedText inverseName);

}
