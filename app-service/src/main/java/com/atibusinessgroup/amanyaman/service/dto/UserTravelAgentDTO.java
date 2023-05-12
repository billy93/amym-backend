package com.atibusinessgroup.amanyaman.service.dto;

import java.util.List;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserTravelAgentDTO{

    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String travelAgentName;
    private String custcode;
    private List<String> roles;
    
    public String getTravelAgentName() {
		return travelAgentName;
	}

	public void setTravelAgentName(String travelAgentName) {
		this.travelAgentName = travelAgentName;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getCustcode() {
		return custcode;
	}

	public void setCustcode(String custcode) {
		this.custcode = custcode;
	}

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", travelAgentName='" + travelAgentName + '\'' +
                ", custcode='" + custcode + '\'' +
                ", roles=" + roles +
                '}';
    }
}
