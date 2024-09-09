package intern.gestionrh.ServiceTest;

import intern.gestionrh.Config.JwtService;
import intern.gestionrh.Entities.Jwt;
import intern.gestionrh.Entities.Utilisateur;
import intern.gestionrh.Repositories.JwtRepository;
import intern.gestionrh.Services.Impl.UtilisateurServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Key;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JwtServiceTest {
    @InjectMocks
    private JwtService jwtService;

    @Mock
    JwtRepository jwtRepository;

    @Mock
    private UtilisateurServiceImpl utilisateurService;

    private final String username = "khmiletthanen@gmail.com";
    private Utilisateur utilisateur;

    private String encryptionKey;
    private Key key;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        utilisateur = new Utilisateur();
        utilisateur.setNomUser("Dali");
        utilisateur.setEmail(username);
    }

    @Test
    public void testGenerateJwt() {

        when(utilisateurService.loadUserByUsername(username)).thenReturn(utilisateur);


        Map<String, String> jwt = jwtService.generate(username);


        assertNotNull(jwt);
        assertNotNull(jwt.get("bearer"));

        String token = jwt.get("bearer");
        System.out.println("Generated JWT Token: " + token);
        //  decode the JWT to check the claims
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("608f36e92dc66d97d5933f0e6371493cb4fc05b1aa8f8de64014732472303a7c"));
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt.get("bearer"));
    }

    @Test
    void testIsTokenExpired() {
        String token = Jwts.builder()
                .setSubject(utilisateur.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + 10000)) // Token n'est pas expiré
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode("608f36e92dc66d97d5933f0e6371493cb4fc05b1aa8f8de64014732472303a7c")), io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact();

        assertFalse(jwtService.isTokenExpired(token));
    }


    @Test
    void testDeconnexion() {

        Jwt jwt = new Jwt();
        jwt.setValeur("some-jwt-value");
        jwt.setDesactive(false);
        jwt.setExpire(false);
        jwt.setUtilisateur(utilisateur);
        // Configurer le contexte de sécurité
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(utilisateur, null));

        // Simuler la recherche du JWT valide
        when(jwtRepository.findValidToken(utilisateur.getEmail(), false, false)).thenReturn(Optional.of(jwt));

        // Appeler la méthode de déconnexion
        jwtService.deconnexion();

        // Vérifier que le JWT a été mis à jour
        assertTrue(jwt.isDesactive());
        assertTrue(jwt.isExpire());

        // Vérifier que la méthode save() a été appelée avec le JWT mis à jour
        verify(jwtRepository).save(jwt);
    }


    @Test
    void testGetAllClaimsAndGetClaim() {

        encryptionKey = "608f36e92dc66d97d5933f0e6371493cb4fc05b1aa8f8de64014732472303a7c";
        key = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(encryptionKey));
        // Prepare a test JWT token
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                .claim("role", "ADMIN")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Call getAllClaims
        Claims claims = jwtService.getAllClaims(token);

        // Verify the claims
        assertNotNull(claims);
        assertEquals(username, claims.getSubject());
        assertEquals("ADMIN", claims.get("role"));
        assertTrue(claims.getExpiration().after(new Date()));

        // Utiliser getClaim pour extraire la date d'expiration
        Date expiration = jwtService.getClaim(token, Claims::getExpiration);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }


    @Test
    void testDisableTokens() {


        Jwt token1 = new Jwt();
        token1.setUtilisateur(utilisateur);
        Jwt token2 = new Jwt();
        token2.setUtilisateur(utilisateur);
        List<Jwt> tokens = List.of(token1, token2);

        // Simuler le comportement du repository
        when(jwtRepository.findTokens(utilisateur.getEmail())).thenReturn(tokens.stream());

        // Appeler la méthode à tester
        jwtService.disableTokens(utilisateur);

        // Vérifier que les tokens ont été mis à jour correctement
        assertTrue(token1.isDesactive());
        assertTrue(token1.isExpire());
        assertTrue(token2.isDesactive());
        assertTrue(token2.isExpire());

        // Vérifier que saveAll a été appelé avec les tokens modifiés
        verify(jwtRepository).saveAll(tokens);
    }

}
