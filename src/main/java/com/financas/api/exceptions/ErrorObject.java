package com.financas.api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorObject {

  private final String message;
  private final String field;
  private final Object parameter;

}
