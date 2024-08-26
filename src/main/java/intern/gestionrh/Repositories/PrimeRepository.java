package intern.gestionrh.Repositories;

import intern.gestionrh.Entities.Prime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrimeRepository extends JpaRepository<Prime,Long> {
    List<Prime> findByUtilisateurId(Long idUtilisateur);
}
