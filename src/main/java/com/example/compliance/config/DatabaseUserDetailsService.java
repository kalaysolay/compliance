package com.example.compliance.config;

import com.example.compliance.model.User;
import com.example.compliance.service.UserService;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsService, использующий сущности из базы данных.
 */
@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public DatabaseUserDetailsService(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        try {
            User user = userService.loadByUsername(username);
            return new PortalUserDetails(user);
        } catch (IllegalArgumentException ex) {
            throw new UsernameNotFoundException("User not found", ex);
        }
    }

    /**
     * Обёртка над сущностью пользователя.
     */
    private static class PortalUserDetails implements UserDetails {
        private final User user;

        PortalUserDetails(final User user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        }

        @Override
        public String getPassword() {
            return user.getPasswordHash();
        }

        @Override
        public String getUsername() {
            return user.getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return user.isEnabled();
        }
    }
}
