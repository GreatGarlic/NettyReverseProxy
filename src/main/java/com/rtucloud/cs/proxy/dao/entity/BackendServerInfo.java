package com.rtucloud.cs.proxy.dao.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "backend_server_info")
public class BackendServerInfo {
    @Id
    @GeneratedValue(generator = "backendServerIdGenerator")
    @GenericGenerator(name = "backendServerIdGenerator", strategy = "native")
    private String id;
    private int port;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
