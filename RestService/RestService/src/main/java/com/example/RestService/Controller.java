package com.example.RestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class Controller {

    @Autowired
    NodeRPCConnection nodeRPCConnection;

    @PostMapping(value = "/store")
    private String store(@RequestParam("hash") String hash) {
        NodeRPCConnection rpc = new NodeRPCConnection();
        return "Modify this";
    }

    @GetMapping(value = "/check")
    private String check(@RequestParam("hash") String hash) {
        return
                "Modify this";
    }




}
