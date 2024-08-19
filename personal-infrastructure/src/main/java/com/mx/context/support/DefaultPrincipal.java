package com.mx.context.support;

public class DefaultPrincipal {
  private final String displayName;

  public DefaultPrincipal(String displayName) {
    this.displayName = displayName;
  }

	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String toString() {
		return "DefaultPrincipal{" + "displayName='" + displayName + '\'' + '}';}
}