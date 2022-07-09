/*
 * Copyright (c) 2019 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package com.viper.opc.client.opcua.sdk.client.api.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.viper.opc.client.opcua.stack.core.types.structured.AddNodesItem;
import com.viper.opc.client.opcua.stack.core.types.structured.AddNodesResponse;
import com.viper.opc.client.opcua.stack.core.types.structured.AddReferencesItem;
import com.viper.opc.client.opcua.stack.core.types.structured.AddReferencesResponse;
import com.viper.opc.client.opcua.stack.core.types.structured.DeleteNodesItem;
import com.viper.opc.client.opcua.stack.core.types.structured.DeleteNodesResponse;
import com.viper.opc.client.opcua.stack.core.types.structured.DeleteReferencesItem;
import com.viper.opc.client.opcua.stack.core.types.structured.DeleteReferencesResponse;

public interface NodeManagementServices {

    CompletableFuture<AddNodesResponse> addNodes(List<AddNodesItem> nodesToAdd);

    CompletableFuture<AddReferencesResponse> addReferences(List<AddReferencesItem> referencesToAdd);

    CompletableFuture<DeleteNodesResponse> deleteNodes(List<DeleteNodesItem> nodesToDelete);

    CompletableFuture<DeleteReferencesResponse> deleteReferences(List<DeleteReferencesItem> referencesToDelete);

}
