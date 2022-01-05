package com.bblvertx.exception;

/**
 * Invalid parameters exception.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class BadRequestException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Constructor with message.
   * 
   * @param msg
   */
  public BadRequestException(String msg) {
    super(msg);
  }

  /**
   * Default constructor.
   */
  public BadRequestException() {
    super();
  }
}
