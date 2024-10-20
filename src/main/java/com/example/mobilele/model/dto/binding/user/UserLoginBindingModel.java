package com.example.mobilele.model.dto.binding.user;

import com.example.mobilele.validator.validator.ValidLogin;

@ValidLogin
public class UserLoginBindingModel {
  private String username;
  private String password;

  public UserLoginBindingModel() {
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
