package com.pragmaticcoders.loadbalancer;

import lombok.extern.slf4j.Slf4j;
import org.apache.gossip.GossipSettings;
import org.apache.gossip.Member;
import org.apache.gossip.RemoteMember;
import org.apache.gossip.event.GossipListener;
import org.apache.gossip.event.GossipState;
import org.apache.gossip.manager.GossipCore;
import org.apache.gossip.manager.GossipManager;
import org.apache.gossip.manager.GossipManagerBuilder;
import org.apache.gossip.manager.handlers.MessageHandler;
import org.apache.gossip.model.Base;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class GossipConnector {

    private final Cluster cluster;
    private final Node host;
    private final Node seed;

    private GossipManager gossipManager;

    public GossipConnector(Cluster cluster, Node host, Node seed) {
        this.cluster = cluster;
        this.host = host;
        this.seed = seed;
    }

    public void listen() {
        GossipSettings s = new GossipSettings();
        s.setWindowSize(1000);
        s.setGossipInterval(100);

        log.info("Registering host: " + host);
        log.info("Registering seed: " + seed);

        List<Member> seeds = new LinkedList<>();
        if (seed != null) {
            RemoteMember seedNode = new RemoteMember(cluster.getName(), seed.getURI(), seed.getId());
            seeds.add(seedNode);
        }

        MessageHandler messageHandler = new MessageHandler() {
            @Override
            public boolean invoke(GossipCore gossipCore, GossipManager gossipManager, Base base) {
                log.info("handle gossip message");
                log.info(gossipCore.toString());
                log.info(gossipManager.toString());
                log.info(base.toString());

                return false;
            }
        };
        GossipManager gossipService = GossipManagerBuilder.newBuilder()
                .cluster(cluster.getName())
                .id(host.getId())
                .uri(host.getURI())
                .listener(new GossipListener() {
                    @Override
                    public void gossipEvent(Member member, GossipState gossipState) {
                        log.info("received gossip event");
                        log.info(member.toString());
                        log.info(gossipState.toString());
                    }
                })
//                .messageHandler(messageHandler)
                .gossipMembers(seeds)
                .gossipSettings(s)
                .build();

        gossipService.init();

        this.gossipManager = gossipService;
    }

    public GossipManager getGossipManager() {
        return gossipManager;
    }
}
