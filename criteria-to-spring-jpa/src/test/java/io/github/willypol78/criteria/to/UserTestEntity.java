package io.github.willypol78.criteria.to;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@SuppressWarnings("ALL")
@Entity
@Table(name = "users")
public class UserTestEntity {
	@Id
	String id;
	String  name;
	String  surname;
	Boolean enabled;
	Integer attempts;

	public UserTestEntity(final String id, final String name, final String surname, final Boolean enabled, final Integer attempts) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.enabled = enabled;
		this.attempts = attempts;
	}

	public UserTestEntity() {
	}

	public String id() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String name() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	public Boolean enabled() {
		return enabled;
	}

	public void setEnabled(final Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getAttempts() {
		return attempts;
	}

	public void setAttempts(final Integer attempts) {
		this.attempts = attempts;
	}
	
}
