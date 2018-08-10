package com.learn.springcloud.auth.jwt;



import com.learn.springcloud.auth.jwt.model.JwtAuthenticationToken;
import com.learn.springcloud.auth.jwt.model.JwtUser;
import com.learn.springcloud.auth.jwt.util.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Used for checking the token from the request and supply the UserDetails if the token is valid
 * @author shunzhong.deng
 */
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String token = jwtAuthenticationToken.getToken();

        Claims claims= jwtTokenUtils.parseToken(token);
        String scopes = "USER";
        if(null != claims.get(JwtTokenUtils.CLAIM_KEY_AUTHORITIES)) {
            scopes = String.valueOf(claims.get(JwtTokenUtils.CLAIM_KEY_AUTHORITIES));
        }

        List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(scopes);

        return new JwtUser(claims.getAudience(), authorityList);
    }

}
