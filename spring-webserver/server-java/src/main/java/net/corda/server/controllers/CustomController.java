package net.corda.server.controllers;

import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.messaging.FlowHandle;
import net.corda.examples.yo.flows.CheckHash;
import net.corda.examples.yo.flows.StoreHash;
import net.corda.server.NodeRPCConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

/**
 * Define CorDapp-specific endpoints in a controller such as this.
 */
@RestController
@RequestMapping("") // The paths for GET and POST requests are relative to this base path.
public class CustomController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);
    private final CordaRPCOps proxy;

    public CustomController(NodeRPCConnection rpc) {
        this.proxy = rpc.getProxy();
    }

    @PostMapping(value = "/store", produces = TEXT_PLAIN_VALUE)
    private String storeHash(@RequestParam(value = "hash") String hash) {
        try{
            String transactionId = proxy.startFlowDynamic(StoreHash.class,hash).getReturnValue().get();
            return transactionId;
        }
        catch (Exception e){
            throw new IllegalArgumentException("Invalid flow");
        }

    }

    @GetMapping(value = "/check")
    private boolean checkHash(@RequestParam(value = "hash") String hash) {
        try{
            boolean status = proxy.startFlowDynamic(CheckHash.class,hash).getReturnValue().get();
            return status;
        }
        catch (Exception e){
            throw new IllegalArgumentException("Invalid flow");
        }

    }
}
