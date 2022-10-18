package com.mzvzm.config;

import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class EsConfig {
    private String host;
    private int port;
    private String scheme;
    private String username;
    private String password;
    private final Pool pool = new Pool();

    public Pool getPool() {
        return pool;
    }

    public String getHost() {
        return host;
    }

    @Value("${elasticsearch.config.host}")
    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    @Value("${elasticsearch.config.port}")
    public void setPort(int port) {
        this.port = port;
    }

    public String getScheme() {
        return scheme;
    }

    @Value("${elasticsearch.config.scheme}")
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getUsername() {
        return username;
    }

    @Value("${elasticsearch.config.username}")
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    @Value("${elasticsearch.config.password}")
    public void setPassword(String password) {
        this.password = password;
    }

    public static class Pool extends GenericObjectPoolConfig<RestHighLevelClient> {
        public Pool() {
            super();
        }
    }
}
