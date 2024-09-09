package intern.gestionrh.Config;

import intern.gestionrh.Entities.Jwt;
import intern.gestionrh.Entities.Utilisateur;
import intern.gestionrh.Repositories.JwtRepository;
import intern.gestionrh.Services.Impl.UtilisateurServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Getter
@Slf4j
@EnableScheduling
@Transactional
public class JwtService {
    private final String ENCRIPTION_KEY = "608f36e92dc66d97d5933f0e6371493cb4fc05b1aa8f8de64014732472303a7c";
    private UtilisateurServiceImpl utilisateurService;
private JwtRepository jwtRepository;

public Jwt tokenByValeur(String valeur){
    return jwtRepository.findByValeur(valeur)
            .orElseThrow(() -> new RuntimeException("aucun token"));
}
    public Map<String, String> generate(String username) {
        Utilisateur utilisateur = utilisateurService.loadUserByUsername(username);
        this.disableTokens(utilisateur);
       final Map<String, String> jwtMap = generateJwt(utilisateur);
       final Jwt jwt= Jwt
               .builder()
               .desactive(false)
               .expire(false)
               .valeur(jwtMap.get("bearer"))
               .utilisateur(utilisateur)
               .build();
       jwtRepository.save(jwt);
        return jwtMap;
    }

    public  void disableTokens(Utilisateur utilisateur){
     final List<Jwt> jwtList = jwtRepository.findTokens(utilisateur.getEmail()).map(
             jwt ->{
                 jwt.setDesactive(true);
                 jwt.setExpire(true);
                 return jwt;
             }

     ).collect(Collectors.toList());
     jwtRepository.saveAll(jwtList);
    }

    public Map<String, String> generateJwt(Utilisateur utilisateur) {
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + 30 * 60 * 1000;

        final Map<String, Object> claims = Map.of(
                "nom", utilisateur.getNomUser(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT, utilisateur.getEmail()
        );

        final String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(utilisateur.getEmail())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of("bearer", bearer);
    }

    public Key getKey() {
        final byte[] decoder = Decoders.BASE64.decode(ENCRIPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }

    public Date getExpirationDateFromToken(String token) {
        return this.getClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    public  <T> T getClaim(String token, Function<Claims, T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    public Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public void deconnexion(){
       Utilisateur utilisateur= (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       Jwt jwt = jwtRepository.findValidToken(utilisateur.getEmail(), false, false)
               .orElseThrow(()->new RuntimeException("Token invalide"));
       jwt.setDesactive(true);
       jwt.setExpire(true);
       jwtRepository.save(jwt);

    }

   // @Scheduled(cron = "@daily")
    @Scheduled(cron = "0 */1 * * * * ")
    public void removeUselessTokens(){
    log.info("Supprission des Tokens à {}", Instant.now());
    jwtRepository.deleteAllByExpireAndDesactive(true,true);
    }
}
