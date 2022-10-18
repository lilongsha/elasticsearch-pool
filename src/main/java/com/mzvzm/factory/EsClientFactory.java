package com.mzvzm.factory;

import com.mzvzm.config.EsConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EsClientFactory extends BasePooledObjectFactory<RestHighLevelClient> {
    private final EsConfig esConfig;

    public EsConfig getEsConfig() {
        return esConfig;
    }

    public EsClientFactory(EsConfig esConfig) {
        this.esConfig = esConfig;
    }

    @Override
    public RestHighLevelClient create() throws Exception {
        try {
            RestClientBuilder builder = RestClient.builder(new HttpHost(esConfig.getHost(), esConfig.getPort(), esConfig.getScheme()));
            RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
            log.info("创建连接：" + restHighLevelClient.hashCode());
            return restHighLevelClient;
        } catch (Exception e) {
            log.info("连接Client 失败：" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PooledObject<RestHighLevelClient> wrap(RestHighLevelClient restClient) {
        return new DefaultPooledObject<>(restClient);
    }

    @Override
    public void destroyObject(PooledObject<RestHighLevelClient> p) throws Exception {
        RestHighLevelClient client = p.getObject();
        log.info("连接ES销毁Hash：" + client.hashCode());
        client.close();
    }
}
