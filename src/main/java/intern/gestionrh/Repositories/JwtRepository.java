package intern.gestionrh.Repositories;

import intern.gestionrh.Entities.Jwt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepository extends CrudRepository<Jwt,Long> {

    @Query("SELECT j FROM Jwt j WHERE j.valeur = :value")
    Optional<Jwt> findByValeur(@Param("value") String value);


    @Query("FROM Jwt j WHERE j.desactive=:desactive AND j.expire=:expire AND j.utilisateur.email=:email")
    Optional<Jwt> findValidToken(String email, boolean expire, boolean desactive);

    @Query("FROM Jwt j WHERE j.utilisateur.email=:email")
    Stream<Jwt> findTokens(String email);

   // @Query("DELETE FROM Jwt j WHERE j.expire=:expire AND j.desactive=:desactive")
    void deleteAllByExpireAndDesactive(boolean expire, boolean desactive);
}
