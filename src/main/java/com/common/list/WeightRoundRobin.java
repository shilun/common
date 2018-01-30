//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.list;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WeightRoundRobin {
    private int currentIndex = -1;
    private int currentWeight = 0;
    private int maxWeight;
    private int gcdWeight;
    private int serverCount;
    private List<Server> servers = new ArrayList();

    public WeightRoundRobin() {
    }

    public int greaterCommonDivisor(int a, int b) {
        BigInteger aBig = new BigInteger(String.valueOf(a));
        BigInteger bBig = new BigInteger(String.valueOf(b));
        return aBig.gcd(bBig).intValue();
    }

    public int greatestCommonDivisor(List<Server> servers) {
        int divisor = 0;
        int index = 0;

        for(int len = servers.size(); index < len - 1; ++index) {
            if(index == 0) {
                divisor = this.greaterCommonDivisor(((Server)servers.get(index)).getWeight(), ((Server)servers.get(index + 1)).getWeight());
            } else {
                divisor = this.greaterCommonDivisor(divisor, ((Server)servers.get(index)).getWeight());
            }
        }

        return divisor;
    }

    public int greatestWeight(List<Server> servers) {
        int weight = 0;
        Iterator var3 = servers.iterator();

        while(var3.hasNext()) {
            Server server = (Server)var3.next();
            if(weight < server.getWeight()) {
                weight = server.getWeight();
            }
        }

        return weight;
    }

    public Server getServer() {
        do {
            this.currentIndex = (this.currentIndex + 1) % this.serverCount;
            if(this.currentIndex == 0) {
                this.currentWeight -= this.gcdWeight;
                if(this.currentWeight <= 0) {
                    this.currentWeight = this.maxWeight;
                    if(this.currentWeight == 0) {
                        return null;
                    }
                }
            }
        } while(((Server)this.servers.get(this.currentIndex)).getWeight() < this.currentWeight);

        return (Server)this.servers.get(this.currentIndex);
    }

    public void init(List<Server> list) {
        Iterator var2 = list.iterator();

        while(var2.hasNext()) {
            Server s = (Server)var2.next();
            this.servers.add(s);
        }

        this.maxWeight = this.greatestWeight(this.servers);
        this.gcdWeight = this.greatestCommonDivisor(this.servers);
        this.serverCount = this.servers.size();
    }

    public static void main(String[] args) {
        WeightRoundRobin weightRoundRobin = new WeightRoundRobin();
        ArrayList servers = new ArrayList();
        servers.add(new Server("192.168.1.101", 1));
        servers.add(new Server("192.168.1.102", 1));
        servers.add(new Server("192.168.1.103", 1));
        servers.add(new Server("192.168.1.104", 1));
        servers.add(new Server("192.168.1.105", 1));
        weightRoundRobin.init(servers);

        for(int i = 0; i < 100; ++i) {
            Server server = weightRoundRobin.getServer();
            System.out.println("server " + server.getIp() + " weight=" + server.getWeight());
        }

    }
}
