package com.bblvertx.pojo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.bblvertx.SeConstants;

/**
 * Page result.
 * 
 * @author Idriss Neumann <neumann.idriss@gamil.com>
 */
@XmlRootElement(name = "searchResult", namespace = SeConstants.NAMESPACE_URI)
public class SearchResult<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long startIndex = 0L;

  private Long maxResults = 10L;

  private Long totalResults = 0L;

  private List<? extends T> results;

  /**
   * Default constructor.
   */
  public SearchResult() {}

  /**
   * Constructor.
   * 
   * @param startIndex init startIndex
   * @param maxResults init maxResults
   */
  public SearchResult(int startIndex, int maxResults) {
    this(Long.valueOf(startIndex), Long.valueOf(maxResults));
  }

  /**
   * Constructor
   * 
   * @param startIndex init startIndex
   * @param maxResults init maxResults
   */
  public SearchResult(Long startIndex, Long maxResults) {
    this.startIndex = startIndex;
    this.maxResults = maxResults;
  }

  /**
   * Constructor
   * 
   * @param results init result list
   */
  public SearchResult(List<? extends T> results) {
    if (null != results) {
      this.startIndex = 0L;
      this.maxResults = Long.valueOf(results.size());
      this.totalResults = Long.valueOf(results.size());
      this.results = results;
    }
  }

  /**
   * @return the startIndex
   */
  public Long getStartIndex() {
    return startIndex;
  }

  /**
   * @param startIndex the startIndex to set
   */
  public void setStartIndex(Long startIndex) {
    this.startIndex = startIndex;
  }

  /**
   * @return the maxResults
   */
  public Long getMaxResults() {
    return maxResults;
  }

  /**
   * @param maxResults the maxResults to set
   */
  public void setMaxResults(Long maxResults) {
    this.maxResults = maxResults;
  }

  /**
   * @return the totalResults
   */
  public Long getTotalResults() {
    return totalResults;
  }

  /**
   * @param totalResults the totalResults to set
   */
  public void setTotalResults(Long totalResults) {
    this.totalResults = totalResults;
  }

  /**
   * @return the results
   */
  public List<? extends T> getResults() {
    return results;
  }

  /**
   * @param results the results to set
   */
  public void setResults(List<? extends T> results) {
    this.results = results;
  }
}
