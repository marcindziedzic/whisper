package com.pragmaticcoders.loadbalancer;

import lombok.Value;

import java.net.URI;

@Value
public class Node {
    private final String id;
    private final String ipAddress;
    private final int port;

    public Node(String id, String ipAddress, int port) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port + 1000;
    }

    public URI getURI() {
        return URI.create("udp://localhost:" + String.valueOf(port));
    }

}
