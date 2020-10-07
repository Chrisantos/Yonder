package com.chriseze.jp.processor.auth;

import com.chriseze.jp.processor.utils.SecurityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.security.Key;
import java.security.Principal;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Provider
@Authz
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    @Inject
    private SecurityUtil securityUtil;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authString = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authString == null || authString.isEmpty() || !authString.startsWith(SecurityUtil.BEARER)) {
            throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).build());
        }

        String token = authString.substring(SecurityUtil.BEARER.length()).trim();

        try {
            Key key = securityUtil.getSecurityKey(); //Some Security Key
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            SecurityContext originalContext = requestContext.getSecurityContext();

            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> claimsJws.getBody().getSubject();
                }

                @Override
                public boolean isUserInRole(String role) {
                    return originalContext.isUserInRole(role);
                }

                @Override
                public boolean isSecure() {
                    return originalContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return originalContext.getAuthenticationScheme();
                }
            });

        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
