package intern.gestionrh.Repositories;

import intern.gestionrh.Entities.TypeRole;
import intern.gestionrh.Entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
  Utilisateur findByNomUser(String nomUser);
  @Query("SELECT u FROM Utilisateur u WHERE u.departement.nomDepartement = :nomDepartement")
  List<Utilisateur> findByDepartementNom(@Param("nomDepartement") String nomDepartement);

    List<Utilisateur> findByDepartementId(Long departementId);

    Optional<Utilisateur> findByEmail(String email);

  List<Utilisateur> findByRoleLibelle(TypeRole roleLibelle);


}
