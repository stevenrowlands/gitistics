package org.gitistics.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Repo {

	@Id
	private String location;
	
	private String name;

	public Repo() {

	}

	public Repo(String location) {
		setLocation(location);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}