package edu.kit.exceptions;

public class AnnotationExceptions extends Exception {
	  /** 
	   * Status of service.
	   */
	  private int httpStatus;
	  /**
	   * Constructor with specific http status code.
	   */
	  public AnnotationExceptions() {
	    this(StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
	  }
	  /**
	   * Constructor with message and specific http status code.
	    * @param status http status code
	   */
	  public AnnotationExceptions(int status) {
	    super();
	    httpStatus = status;
	  }
	  /**
	   * Constructor with message and specific http status code.
	   * @param message message
	   */
	  public AnnotationExceptions(String message) {
	    super(message);
	  }
	  /**
	   * Constructor with message and specific http status code.
	   * @param cause caused by this exception
	   */
	  public AnnotationExceptions(Throwable cause) {
	    super(cause);
	  }
	  /**
	   * Constructor with message and specific http status code.
	   * @param message message
	   * @param cause caused by this exception
	   */
	  public AnnotationExceptions(String message, Throwable cause) {
	    super(message, cause);
	  }
	  /**
	   * Constructor with message and provided http status code.
	   * @param message message
	   * @param status http status code
	   */
	  public AnnotationExceptions(String message, int status) {
	    super(message);
	   httpStatus = status;
	  }
	  /**
	   * Constructor with message and provided http status code.
	   * @param cause caused by this exception
	   * @param status http status code
	   */
	  public AnnotationExceptions(Throwable cause, int status) {
	    super(cause);
	   httpStatus = status;
	  }
	  /**
	   * Constructor with message and provided http status code.
	   * @param message message
	   * @param cause caused by this exception
	   * @param status http status code
	   */
	  public AnnotationExceptions(String message, Throwable cause, int status) {
	    super(message, cause);
	   httpStatus = status;
	  }
	  
	public AnnotationExceptions(String message, StatusCode badRequest) {
		// TODO Auto-generated constructor stub
	}
	/**
	   * Get http status code.
	   * @return http status code.
	   */
	  public int getHttpStatus() {
	    return httpStatus;
	  }
	}
