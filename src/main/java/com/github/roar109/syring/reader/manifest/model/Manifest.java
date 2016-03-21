package com.github.roar109.syring.reader.manifest.model;

import java.util.Map;

public class Manifest {

	private String name;
	private Map<String, String> entries;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getEntries() {
		return entries;
	}

	public void setEntries(Map<String, String> entries) {
		this.entries = entries;
	}

}