package com.apps.developerblog.app.ws.ui.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateUserDetailsModel {
    @NotNull(message="First name cannot be null")
    @Size(min=2, message="First name must not be less than 2 chars.")
    private String firstName;
    @NotNull(message="Last name cannot be null")
    @Size(min=2, message="Last name must not be less than 2 chars.")
    private String lastName;

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
}
