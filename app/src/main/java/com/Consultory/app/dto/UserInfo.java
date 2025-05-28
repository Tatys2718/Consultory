package com.Consultory.app.dto;

import com.Consultory.app.model.ERol;

import java.util.Set;

public record UserInfo(String username, String password, String email, Set<ERol> roles) {
}
