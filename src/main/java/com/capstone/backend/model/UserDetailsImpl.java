package com.capstone.backend.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {
  private String username;
  private String password;
  private List<GrantedAuthority> authorities;
  private boolean locked;
  private boolean enabled;

  public UserDetailsImpl(User user) {
    this.username = user.getEmail();
    this.password = user.getPassword();
    this.authorities = Arrays
        .stream(user
            .getRole()
            .name()
            .split(","))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
    this.locked = user.isLocked();
    this.enabled = user.isEnabled();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !this.locked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

}
