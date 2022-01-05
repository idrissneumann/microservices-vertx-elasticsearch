package com.bblvertx.exception;

/**
 * Functional exception
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class FunctionalException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Default constructor.
   */
  public FunctionalException() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param error code
   * @param cause
   */
  public FunctionalException(String code, Throwable cause) {
    super(code, cause);
  }

  /**
   * Constructor.
   * 
   * @param error code
   */
  public FunctionalException(String code) {
    super(code);
  }

  /**
   * Constructor.
   * 
   * @param cause
   */
  public FunctionalException(Throwable cause) {
    super(cause);
  }

}
