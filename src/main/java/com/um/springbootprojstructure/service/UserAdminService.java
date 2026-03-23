package com.um.springbootprojstructure.service;

public interface UserAdminService {
    void ensureAdminAccount(String email, String firstName, String lastName, String password);
}
