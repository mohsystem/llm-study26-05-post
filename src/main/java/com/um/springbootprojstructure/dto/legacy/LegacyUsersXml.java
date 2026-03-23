package com.um.springbootprojstructure.dto.legacy;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.ArrayList;
import java.util.List;

public class LegacyUsersXml {

    @JacksonXmlProperty(localName = "user")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<LegacyUserXml> users = new ArrayList<>();

    public List<LegacyUserXml> getUsers() { return users; }
    public void setUsers(List<LegacyUserXml> users) { this.users = users; }
}
