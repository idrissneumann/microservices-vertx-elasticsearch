package com.bblvertx.utils.singleton.impl;

import com.bblvertx.utils.singleton.IRestHighLevelClient;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class RestHighLevelClientImpl implements IRestHighLevelClient {
  private RestHighLevelClient client;

  public RestHighLevelClientImpl(RestHighLevelClient client) {
    this.client = client;
  }

  @Override
  public void delete(DeleteRequest deleteRequest, RequestOptions requestOptions)
      throws IOException {
    client.delete(deleteRequest, requestOptions);
  }

  @Override
  public void index(IndexRequest source, RequestOptions requestOptions) throws IOException {
    client.index(source, requestOptions);
  }

  @Override
  public SearchResponse search(SearchRequest source, RequestOptions requestOptions)
      throws IOException {
    return client.search(source, requestOptions);
  }

  @Override
  public SearchResponse scroll(SearchScrollRequest ssr, RequestOptions requestOptions)
      throws IOException {
    return client.scroll(ssr, requestOptions);
  }

  @Override
  public void deleteIndices(DeleteIndexRequest deleteIndexRequest, RequestOptions requestOptions)
      throws IOException {
    client.indices().delete(deleteIndexRequest, requestOptions);
  }
}
