
package com.example.testmissingnamespace;

import java.io.Serializable;
import java.util.Objects;

public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String passwordhash;
    private String firstname;
    private String lastname;

    public Users() {
    }

    public Users(Long id, String username, String passwordhash, String firstname, String lastname) {
        this.id = id;
        this.username = username;
        this.passwordhash = passwordhash;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordhash() {
        return passwordhash;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Users other = (Users) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Users{" + "id=" + id + ", username=" + username + ", passwordhash=" + passwordhash + ", firstname=" + firstname + ", lastname=" + lastname + '}';
    }

}
