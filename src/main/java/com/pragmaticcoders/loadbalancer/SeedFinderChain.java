package com.pragmaticcoders.loadbalancer;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeedFinderChain implements SeedFinder {

    private List<SeedFinder> seedFinders;

    @Autowired
    public SeedFinderChain(List<SeedFinder> seedFinders) {
        this.seedFinders = seedFinders;
    }

    @Override
    public Set<Node> lookup() {
        Set<Node> seeds = new HashSet<>();
        for (SeedFinder seedFinder : seedFinders) {
            seeds.addAll(seedFinder.lookup());
        }
        return seeds;
    }
}
