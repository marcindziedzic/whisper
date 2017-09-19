package com.pragmaticcoders.loadbalancer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Configuration
@ComponentScan(value = {"com.pragmaticcoders.loadbalancer"})
public class GossipsConfiguration {

    @Value("${cluster.name:cluster}")
    private String clusterName;

    @Value("${cluster.seed.id:}")
    private String seedId;

    @Value("${cluster.seed.ipAddress:}")
    private String seedIpAddress;

    @Value("${cluster.seed.port:0}")
    private int seedPort;

    @Value("${spring.application.name}")
    private String hostId;

    @Value("${spring.cloud.client.ipAddress}")
    private String hostIpAddress;

    @Value("${server.port}")
    private int hostPort;

    @Bean
    public SeedFinder seedsFinder() {
        List<SeedFinder> seedFinderList = new LinkedList<>();

        if (!StringUtils.isEmpty(seedId)) {
            seedFinderList.add(new StaticSeedFinder(seedId, seedIpAddress, seedPort));
        }

//        if (seedFinderList.isEmpty()) {
//            seedFinderList.add(new SelfSeedFinder(hostId, hostPort, hostIpAddress));
//        }

        return new SeedFinderChain(seedFinderList);
    }

    @Bean
    public GossipConnector gossipConnector() {
        Set<Node> lookup = seedFinder.lookup();
        Node seed = null;
        if (!lookup.isEmpty()) {
            seed = lookup.iterator().next();
        }

        Node host = new Node(hostId, hostIpAddress, hostPort);
        Cluster cluster = new Cluster(clusterName);

        return new GossipConnector(cluster, host, seed);
    }

    @Autowired
    private SeedFinder seedFinder;

    @Autowired
    private GossipConnector gossipConnector;

    @PostConstruct
    public void init() {
        gossipConnector.listen();
    }


    // TODO inject IP address of instance on EC2
    // TODO inject PORT used by this instance on EC2
}
