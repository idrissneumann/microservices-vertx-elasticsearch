package com.bblvertx.utils.singleton;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;

import java.io.IOException;

public interface IRestHighLevelClient {
  void delete(DeleteRequest deleteRequest, RequestOptions requestOptions) throws IOException;

  void index(IndexRequest source, RequestOptions requestOptions) throws IOException;

  SearchResponse search(SearchRequest source, RequestOptions requestOptions) throws IOException;

  SearchResponse scroll(SearchScrollRequest ssr, RequestOptions requestOptions) throws IOException;

  void deleteIndices(DeleteIndexRequest deleteIndexRequest, RequestOptions requestOptions) throws IOException;
}
