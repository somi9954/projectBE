package org.project.models.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.project.entities.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
public class MemberInfo implements UserDetails {

    private String email;
    private String password;
    private Member member;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean social;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public String getUsername() {
        return email;
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
        return true;
    }
}
