package com.cmpe281.project1.authorization;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

/**
 * Created by Serdar Demirci
 */
public class JwtTokenProvider {

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            URL url = new URL("https://cognito-idp.us-east-2.amazonaws.com/us-east-2_xgwjxtYAV/.well-known/jwks.json");
            JwkProvider provider = new UrlJwkProvider(url);
            DecodedJWT decodedjwt = JWT.decode("{"+token+"}");
            Jwk jwk = provider.get(decodedjwt.getKeyId());
            RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException | JwkException | MalformedURLException exception) {
            System.out.println(exception);
            return false;
        }
    }

    public Authentication getAuthentication(String token)  {
        GrantedAuthority authority = new SimpleGrantedAuthority("myAuthority");
        return new UsernamePasswordAuthenticationToken(getUsername(token), null, Arrays.asList(authority));
    }

    public String getUsername(String token) {
        return JWT.decode(token).getClaim("username").asString();
    }
}
