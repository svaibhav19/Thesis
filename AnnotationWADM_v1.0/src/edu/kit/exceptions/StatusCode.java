package edu.kit.exceptions;

public enum StatusCode {
	  BAD_REQUEST(400),
	  NOT_FOUND(404),
	  CONFLICT(409),
	  INTERNAL_SERVER_ERROR(500),
	  SERVICE_UNAVAILABLE(503);
	  /** 
	   * Status code.
	   */
	  private final int statusCode;
	  /**
	   * Constructor
	   * @param status status code
	   */
	  StatusCode(int status) {
	    statusCode = status;
	  }
	  /**
	   * Get status code.
	   * @return status code.
	   */
	  public int getStatusCode() {
	    return statusCode;
	  }
	}
