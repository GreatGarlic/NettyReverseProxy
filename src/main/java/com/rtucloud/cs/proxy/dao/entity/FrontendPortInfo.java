package com.rtucloud.cs.proxy.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


@TableName("frontend_port_info")
public class FrontendPortInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    @TableField(value = "port")
    private  int port;

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
