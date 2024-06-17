package com.csye6220.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import com.csye6220.dao.UserDAO;
import com.csye6220.model.User;
import com.csye6220.util.AESUtil;

import java.util.Collections;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDAO userDAO;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("User not found");
        }

        try {
            String decryptedPassword = AESUtil.decrypt(user.getPassword(), "YourSecretKey");
            if (decryptedPassword != null && decryptedPassword.equals(password)) {
                // Create an empty list of authorities, since you are not using any
                List<GrantedAuthority> authorities = Collections.emptyList();
                return new UsernamePasswordAuthenticationToken(username, password, authorities);
            } else {
                throw new BadCredentialsException("Invalid credentials");
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Failed to decrypt password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
