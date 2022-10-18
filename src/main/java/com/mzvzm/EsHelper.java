package com.mzvzm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mzvzm.pool.EsClientPool;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

@Slf4j
@Component
public class EsHelper {
    private final EsClientPool clientPool;

    public EsHelper(EsClientPool clientPool) {
        this.clientPool = clientPool;
    }
    
    public Boolean createIndex(String index) {
        RestHighLevelClient restHighLevelClient = clientPool.borrowObject();
        Boolean result = null;
        try {
            CreateIndexRequest request = new CreateIndexRequest(index);
            CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            result = response.isAcknowledged();
        } catch (IOException e) {
            log.info("elasticsearch create index is failed");
            e.printStackTrace();
        } finally {
            clientPool.returnObject(restHighLevelClient);
        }
        return result;
    }

    public Object add(JSONObject jsonObject) {
        RestHighLevelClient restHighLevelClient = clientPool.borrowObject();
        IndexRequest request = new IndexRequest();
        IndexResponse responses = null;
        try {
            String index = jsonObject.getString("index");
            JSONObject json = jsonObject.getJSONObject("object");
            if (StringUtils.hasText(index)) {
                request.index(index);
                request.source(XContentType.JSON, "name", json.getString("name"), "price", json.getDouble("price"), "saleTotal", json.getInteger("saleTotal"));
                responses = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            }
        } catch (Exception e) {
            log.info("elasticsearch batch add object is failed");
            e.printStackTrace();
        } finally {
            clientPool.returnObject(restHighLevelClient);
        }
        return responses;
    }

    public Object batchAdd(JSONObject jsonObject) {
        RestHighLevelClient restHighLevelClient = clientPool.borrowObject();
        BulkResponse responses = null;
        try {
            String index = jsonObject.getString("index");
            if (StringUtils.hasText(index)) {
                JSONArray item = jsonObject.getJSONArray("item");
                BulkRequest request = getBatchAdd(item, index);
                responses = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
            }
        } catch (Exception e) {
            log.info("elasticsearch batch add object is failed");
            e.printStackTrace();
        } finally {
            clientPool.returnObject(restHighLevelClient);
        }
        return responses;
    }

    private BulkRequest getBatchAdd(@Nullable JSONArray jsonArray, @Nullable String index) {
        BulkRequest bulkRequest = new BulkRequest();
        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                IndexRequest request = new IndexRequest();
                request.index(index);
                Set<String> keys = jsonObject.keySet();
                if (keys.contains("id")) {
                    request.id(jsonObject.getString("id"));
                    keys.remove("id");
                }
                ArrayList<Object> sources = new ArrayList<>();
                for (String key : keys) {
                    sources.add(key);
                    sources.add(jsonObject.getString(key));
                }
                request.source(XContentType.JSON, sources.toArray());
                bulkRequest.add(request);
            }
        }
        System.out.println(bulkRequest);
        return bulkRequest;
    }
}
