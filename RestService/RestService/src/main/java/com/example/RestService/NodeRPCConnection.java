package com.example.RestService;

import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.utilities.NetworkHostAndPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Wraps a node RPC proxy.
 */
@Component
public class NodeRPCConnection implements AutoCloseable {
    private final String host;
    private final String username;
    private final String password;
    private final int rpcPort;


    private CordaRPCConnection rpcConnection;
    private CordaRPCOps proxy; // The RPC proxy


    public NodeRPCConnection(

    ) {
        this.host = "localhost";
        this.username = "user1";
        this.password = "test";
        this.rpcPort = 10006;
    }

    @PostConstruct
    public void initialiseNodeRPCConnection() {
        NetworkHostAndPort rpcAddress = new NetworkHostAndPort(host, rpcPort);
        CordaRPCClient rpcClient = new CordaRPCClient(rpcAddress);
        this.rpcConnection = rpcClient.start(username, password);
        this.proxy = rpcConnection.getProxy();
    }

    public CordaRPCOps getProxy() {
        return proxy;
    }

    @PreDestroy
    @Override
    public void close() throws Exception {
        rpcConnection.notifyServerAndClose();
    }
}
