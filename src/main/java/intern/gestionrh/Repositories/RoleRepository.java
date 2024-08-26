package intern.gestionrh.Repositories;

import intern.gestionrh.Entities.Role;
import intern.gestionrh.Entities.TypeRole;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role,Long> {
    Optional<Role> findByLibelle(TypeRole libelle);
}
