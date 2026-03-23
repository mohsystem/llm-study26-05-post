package com.um.springbootprojstructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.ldap")
public class LdapValidationProperties {

    private boolean mock = true;

    private String userSearchBase = "ou=people";
    private String userObjectClass = "person";

    public boolean isMock() { return mock; }
    public void setMock(boolean mock) { this.mock = mock; }

    public String getUserSearchBase() { return userSearchBase; }
    public void setUserSearchBase(String userSearchBase) { this.userSearchBase = userSearchBase; }

    public String getUserObjectClass() { return userObjectClass; }
    public void setUserObjectClass(String userObjectClass) { this.userObjectClass = userObjectClass; }
}
