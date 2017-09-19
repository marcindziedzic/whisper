package com.pragmaticcoders.loadbalancer;

import java.util.Set;

public interface SeedFinder {
    Set<Node> lookup();
}
