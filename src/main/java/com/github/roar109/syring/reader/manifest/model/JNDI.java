package com.github.roar109.syring.reader.manifest.model;

public class JNDI {

	private String jndiName;
	private String jndiValue;
	
	public JNDI(){}
	public JNDI(final String key,final String value){
		this.jndiName = key;
		this.jndiValue = value;
	}

	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	public String getJndiValue() {
		return jndiValue;
	}

	public void setJndiValue(String jndiValue) {
		this.jndiValue = jndiValue;
	}

}
