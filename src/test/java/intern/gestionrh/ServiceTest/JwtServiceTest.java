package intern.gestionrh.ServiceTest;

import intern.gestionrh.Config.JwtService;
import intern.gestionrh.Entities.Utilisateur;
import intern.gestionrh.Services.Impl.UtilisateurServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Key;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class JwtServiceTest {
    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UtilisateurServiceImpl utilisateurService;

    private final String username = "khmiletthanen@gmail.com";
    private Utilisateur utilisateur;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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


}
