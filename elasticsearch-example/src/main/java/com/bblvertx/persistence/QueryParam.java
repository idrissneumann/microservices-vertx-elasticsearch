package com.bblvertx.persistence;

import static com.bblvertx.utils.CommonUtils.safeCompare;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL query param.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class QueryParam implements Serializable, Comparable<QueryParam> {
  private static final long serialVersionUID = 1L;

  private Integer order;

  private String name;

  private Object value;

  private Class<?> clazz;

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the value
   */
  public Object getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(Object value) {
    this.value = value;
  }

  /**
   * @return the clazz
   */
  public Class<?> getClazz() {
    return clazz;
  }

  /**
   * @param clazz the clazz to set
   */
  public void setClazz(Class<?> clazz) {
    this.clazz = clazz;
  }

  /**
   * @return the order
   */
  public Integer getOrder() {
    return order;
  }

  /**
   * @param order the order to set
   */
  public void setOrder(Integer order) {
    this.order = order;
  }

  /**
   * Return the param as List.
   * 
   * @return List<QueryParam>
   */
  public List<QueryParam> asList() {
    List<QueryParam> lst = new ArrayList<QueryParam>();
    lst.add(this);
    return lst;
  }

  /**
   * Return the param asList.
   * 
   * @param List <QueryParam>
   * @return List<QueryParam>
   */
  public List<QueryParam> asList(List<QueryParam> l) {
    List<QueryParam> lst = new ArrayList<QueryParam>();
    lst.add(this);
    lst.addAll(l);
    return lst;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(QueryParam o) {
    return safeCompare(this.order, o.getOrder());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "QueryParam [order=" + order + ", name=" + name + ", value=" + value + "]";
  }
}
