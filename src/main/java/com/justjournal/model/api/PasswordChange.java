package com.justjournal.model.api;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;

/**
 * Represent password change request
 *
 * @author Lucas Holt
 */
public class PasswordChange implements Serializable {

    private static final long serialVersionUID = -5074915770855596258L;
    private String passCurrent;
    private String passNew;

    @JsonCreator
    public PasswordChange() {
        super();
    }

    public String getPassCurrent() {
        return passCurrent;
    }

    public void setPassCurrent(final String passCurrent) {
        this.passCurrent = passCurrent;
    }

    public String getPassNew() {
        return passNew;
    }

    public void setPassNew(final String passNew) {
        this.passNew = passNew;
    }
}