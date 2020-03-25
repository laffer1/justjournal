package com.justjournal.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * @author Lucas Holt
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LoginResponse implements Serializable {
    private static final long serialVersionUID = 6809817814936626615L;
    private String status = null;
    private String username = null;

    @JsonCreator
    public LoginResponse() {
        super();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    @JsonIgnore
    @Override
    public String toString() {
        return "LoginResponse{" +
                "status='" + status + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}