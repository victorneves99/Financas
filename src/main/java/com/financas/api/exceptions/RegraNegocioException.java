package com.financas.api.exceptions;

public class RegraNegocioException extends RuntimeException {

  public RegraNegocioException() {
  }

  public RegraNegocioException(String message) {
    super(message);
  }

  public RegraNegocioException(String message, Throwable cause) {
    super(message, cause);
  }

}
