package com.mx.context.support;

import com.mx.context.AbstractContextVar;

public class PrincipalContextVar extends AbstractContextVar<DefaultPrincipal> {
  private static final String PRINCIPAL_LABEL = "principal";

  @Override
  public String name() {
    return PRINCIPAL_LABEL;
  }
}
