package com.example.smartrestaurant.security;

import com.example.smartrestaurant.model.Role;
import com.example.smartrestaurant.model.Users;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


public class UserDetailsImpl implements UserDetails {
    // Users.status values that block login. Null/blank counts as active.
    private static final Set<String> INACTIVE_STATUSES =
            Set.of("0", "INA", "INACTIVE", "DISABLED", "LOCKED", "BLOCKED");

    @Getter
    private final int id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    @Getter
    private final int tokenVersion;
    private final boolean enabled;

    private UserDetailsImpl(int id, String username, String password,
                            Collection<? extends GrantedAuthority> authorities,
                            int tokenVersion, boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.tokenVersion = tokenVersion;
        this.enabled = enabled;
    }

    public static UserDetailsImpl build(Users user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Role role = user.getRole();
        if (role != null) {
            String roleName = role.getName();
            if (roleName != null && !roleName.isBlank()) {
                authorities.add(new SimpleGrantedAuthority(
                        roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName));
            }
            if (role.getPermissions() != null) {
                role.getPermissions().stream()
                        .map(p -> p.getName())
                        .filter(name -> name != null && !name.isBlank())
                        .forEach(name -> authorities.add(new SimpleGrantedAuthority(name)));
            }
        }
        int tokenVersion = user.getTokenVersion() == null ? 0 : user.getTokenVersion();
        String status = user.getStatus();
        boolean enabled = status == null || status.isBlank()
                || !INACTIVE_STATUSES.contains(status.trim().toUpperCase());
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword(),
                authorities, tokenVersion, enabled);
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return enabled; }
}