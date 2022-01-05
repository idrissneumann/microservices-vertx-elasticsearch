package com.bblvertx.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Technical exception.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class TechnicalException extends RuntimeException {
  private static final Logger LOGGER = LogManager.getLogger(TechnicalException.class);
  private static final long serialVersionUID = 1L;

  /**
   * Default constructor.
   */
  public TechnicalException() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param error code
   * @param cause
   */
  public TechnicalException(String code, Throwable cause) {
    super(code, cause);
  }

  /**
   * Constructor.
   * 
   * @param error code
   */
  public TechnicalException(String code) {
    super(code);
  }

  /**
   * Constructor.
   * 
   * @param cause
   */
  public TechnicalException(Throwable cause) {
    super(cause);
    LOGGER.error(cause);
  }

}
