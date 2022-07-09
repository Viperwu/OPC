/*
 * Copyright (c) 2021 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package com.viper.app.data.client;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import com.viper.opc.client.opcua.sdk.client.OpcUaClient;
import com.viper.opc.client.opcua.sdk.client.api.identity.AnonymousProvider;
import com.viper.opc.client.opcua.sdk.client.api.identity.IdentityProvider;
import com.viper.opc.client.opcua.stack.client.security.DefaultClientCertificateValidator;
import com.viper.opc.client.opcua.stack.core.Stack;
import com.viper.opc.client.opcua.stack.core.security.DefaultTrustListManager;
import com.viper.opc.client.opcua.stack.core.security.SecurityPolicy;
import com.viper.opc.client.opcua.stack.core.types.builtin.LocalizedText;
import com.viper.opc.client.opcua.stack.core.types.structured.EndpointDescription;
import com.viper.opc.examples.client.KeyStoreLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.viper.opc.client.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;


/**
 * 客户端创建运行类
 */
public class OPCClientRunner {

   /* static {
        // Required for SecurityPolicy.Aes256_Sha256_RsaPss
        Security.addProvider(new BouncyCastleProvider());
    }*/
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String OPC_URI;
    private final CompletableFuture<OpcUaClient> future = new CompletableFuture<>();

    private DefaultTrustListManager trustListManager;
    private OpcUaClient client;

    public CompletableFuture<OpcUaClient> getFuture() {
        return future;
    }

    public OPCClientRunner(String uri) {
        this.OPC_URI = uri;
    }


    public OpcUaClient getClient() {
        if (client == null){
            run();
        }
        return client;
    }

    public void complete(){
        future.complete(client);
    }

    private OpcUaClient createClient() throws Exception {
        Path securityTempDir = Paths.get(System.getProperty("java.io.tmpdir"), "client", "security");
        Files.createDirectories(securityTempDir);
        if (!Files.exists(securityTempDir)) {
            throw new Exception("unable to create security dir: " + securityTempDir);
        }

        File pkiDir = securityTempDir.resolve("pki").toFile();

        LoggerFactory.getLogger(getClass())
            .info("security dir: {}", securityTempDir.toAbsolutePath());
        LoggerFactory.getLogger(getClass())
            .info("security pki dir: {}", pkiDir.getAbsolutePath());

        KeyStoreLoader loader = new KeyStoreLoader().load(securityTempDir);

        trustListManager = new DefaultTrustListManager(pkiDir);



        DefaultClientCertificateValidator certificateValidator =
            new DefaultClientCertificateValidator(trustListManager);


        return OpcUaClient.create(
            getEndpointUrl(),
            endpoints ->
                endpoints.stream()
                    .filter(endpointFilter())
                    .findFirst(),
            configBuilder ->
                configBuilder
                    .setApplicationName(LocalizedText.english("viper wu opc-ua client"))
                    .setApplicationUri("urn:viper:wu:examples:client")
                    .setKeyPair(loader.getClientKeyPair())
                    .setCertificate(loader.getClientCertificate())
                    .setCertificateChain(loader.getClientCertificateChain())
                    .setCertificateValidator(certificateValidator)
                    .setIdentityProvider(getIdentityProvider())
                    .setRequestTimeout(uint(5000))
                    .build()
        );
    }


    public void run() {
        try {
            client = createClient();
            future.whenCompleteAsync((c, ex) -> {
                if (ex != null) {
                    logger.error("Error running example: {}", ex.getMessage(), ex);
                }
               // logger.info("哈哈哈哈");
                try {
                    client.disconnect().get();
                    Stack.releaseSharedResources();
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("Error disconnecting: {}", e.getMessage(), e);
                }

                /*try {
                    Thread.sleep(1000);
                   // System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            });
        } catch (Throwable t) {
            logger.error("Error getting client: {}", t.getMessage(), t);

            future.completeExceptionally(t);

            /*try {
                Thread.sleep(1000);
                //System.exit(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }


    public String getEndpointUrl() {
        return OPC_URI;
    }
    public Predicate<EndpointDescription> endpointFilter() {
        return e -> getSecurityPolicy().getUri().equals(e.getSecurityPolicyUri());
    }

    //default SecurityPolicy getSecurityPolicy() {
    //    return SecurityPolicy.Basic256Sha256;
    // }
    public SecurityPolicy getSecurityPolicy() {
        return SecurityPolicy.None;
    }
    public IdentityProvider getIdentityProvider() {
        return new AnonymousProvider();
    }


}
