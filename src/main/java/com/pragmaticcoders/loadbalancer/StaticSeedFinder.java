package com.pragmaticcoders.loadbalancer;

import java.util.HashSet;
import java.util.Set;

public class StaticSeedFinder implements SeedFinder {

    private final String ipAddress;
    private final int port;
    private final String id;

    public StaticSeedFinder(String id, String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.id = id;
    }

    @Override
    public Set<Node> lookup() {
        Node seed = new Node(id, ipAddress, port);

        Set<Node> seeds = new HashSet<>();
        seeds.add(seed);
        return seeds;
    }
}
