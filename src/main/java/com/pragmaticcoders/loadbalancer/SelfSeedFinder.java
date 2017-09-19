package com.pragmaticcoders.loadbalancer;

import java.util.HashSet;
import java.util.Set;

public class SelfSeedFinder implements SeedFinder {

    private final String applicationName;
    private final int port;
    private final String ipAddress;

    public SelfSeedFinder(String applicationName, int port, String ipAddress) {
        this.applicationName = applicationName;
        this.port = port;
        this.ipAddress = ipAddress;
    }

    @Override
    public Set<Node> lookup() {
        Node seed = new Node(applicationName, ipAddress, port);

        Set<Node> seeds = new HashSet<>();
        seeds.add(seed);
        return seeds;
    }

}
