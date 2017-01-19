//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.list;

public class Server {
    private String ip;
    private int weight;

    public Server(String ip, int weight) {
        this.ip = ip;
        this.weight = weight;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(obj instanceof Server) {
            Server server = (Server)obj;
            return this.getIp().equals(server.getIp());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.getIp().hashCode();
    }
}
