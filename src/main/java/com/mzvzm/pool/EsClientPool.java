package com.mzvzm.pool;

import com.mzvzm.factory.EsClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EsClientPool {
    private final GenericObjectPool<RestHighLevelClient> pool;

    public EsClientPool(EsClientFactory esClientFactory) {
        this.pool = new GenericObjectPool<>(esClientFactory, esClientFactory.getEsConfig().getPool());
        // 在从对象池获取对象时是否检测对象有效(true : 是) , 配置true会降低性能；
        this.pool.setTestOnBorrow(true);
        // 在向对象池中归还对象时是否检测对象有效(true : 是) , 配置true会降低性能
        this.pool.setTestOnReturn(true);
        // 连接是否被空闲连接回收器(如果有)进行检验.如果检测失败,则连接将被从池中去除
        this.pool.setTestWhileIdle(true);
    }

    public RestHighLevelClient borrowObject() {
        try {
            RestHighLevelClient restHighLevelClient = pool.borrowObject();
            log.info("借用连接池连接" + restHighLevelClient.hashCode());
            return restHighLevelClient;
        } catch (Exception e) {
            log.info("获取ES连接池连接失败："+e.getMessage());
            return null;
        }
    }

    public void returnObject(RestHighLevelClient restHighLevelClient) {
        if (restHighLevelClient != null) {
            log.info("回收连接池连接" + restHighLevelClient.hashCode());
            pool.returnObject(restHighLevelClient);
        }
    }
}
