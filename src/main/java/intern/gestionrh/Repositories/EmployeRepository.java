package intern.gestionrh.Repositories;

import intern.gestionrh.Entities.Employe;
import intern.gestionrh.Entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeRepository extends JpaRepository<Employe,Long> {
    Employe findByMatricule(String matricule);

}
