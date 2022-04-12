package org.camunda.platform7.extension.graphql.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by danielvogel on 14.06.17.
 */

public class JWTAuthenticationFilter implements Filter {

    private String secret;
    private String issuer;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.secret = filterConfig.getInitParameter("secret");
        this.issuer = filterConfig.getInitParameter("issuer");
    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        ObjectMapper mapper = new ObjectMapper();

        String header = req.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            ErrorResponse error = new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "No JWT token found in request header");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(mapper.writeValueAsString(error));
        } else {
            String token = header.substring(7);

            try {
                Algorithm algorithm = Algorithm.HMAC256(secret);
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer(issuer)
                        .build();
                verifier.verify(token);

                chain.doFilter(request, response);
            } catch (JWTVerificationException exception){
                ErrorResponse error = new ErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write(mapper.writeValueAsString(error));
            }
        }

    }
}
