package ua.vg.msg.shared;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public enum UserType {
    USER {
        @Override
        public List<SimpleGrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    },
    ADMIN {
        @Override
        public List<SimpleGrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
    };


    public abstract List<SimpleGrantedAuthority> getAuthorities();
}
