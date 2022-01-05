package com.bblvertx.pojo.vo;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

/**
 * User VO.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class UserVO implements Serializable {
  private static final long serialVersionUID = 1L;

  private String name;

  private String firstname;

  private String email;

  private String id;

  private String skill;

  private Calendar dateConnect;

  private Calendar dateUpdate;

  private Integer rsSearch;

  private String randomId = UUID.randomUUID().toString();

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
   * @return the firstname
   */
  public String getFirstname() {
    return firstname;
  }

  /**
   * @param firstname the firstname to set
   */
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  /**
   * @return the randomId
   */
  public String getRandomId() {
    return randomId;
  }

  /**
   * @param randomId the randomId to set
   */
  public void setRandomId(String randomId) {
    this.randomId = randomId;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return the dateConnect
   */
  public Calendar getDateConnect() {
    return dateConnect;
  }

  /**
   * @param dateConnect the dateConnect to set
   */
  public void setDateConnect(Calendar dateConnect) {
    this.dateConnect = dateConnect;
  }

  /**
   * @return the dateUpdate
   */
  public Calendar getDateUpdate() {
    return dateUpdate;
  }

  /**
   * @param dateUpdate the dateUpdate to set
   */
  public void setDateUpdate(Calendar dateUpdate) {
    this.dateUpdate = dateUpdate;
  }

  /**
   * @return the rsSearch
   */
  public Integer getRsSearch() {
    return rsSearch;
  }

  /**
   * @param rsSearch the rsSearch to set
   */
  public void setRsSearch(Integer rsSearch) {
    this.rsSearch = rsSearch;
  }

  /**
   * @return the skill
   */
  public String getSkill() {
    return skill;
  }

  /**
   * @param skill the skill to set
   */
  public void setSkill(String skill) {
    this.skill = skill;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    if (isNotEmpty(name) && isNotEmpty(firstname)) {
      return String.format("%s %s", firstname, name);
    } else if (isNotEmpty(name)) {
      return name;
    } else if (isNotEmpty(firstname)) {
      return firstname;
    } else if (isNotEmpty(email)) {
      return email;
    } else {
      return randomId;
    }
  }
}
