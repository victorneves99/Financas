package com.financas.api.exceptions;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {

  private final String message;
  private final int code;
  private final String status;
  private final String objectName;
  private final List<ErrorObject> errors;

}
