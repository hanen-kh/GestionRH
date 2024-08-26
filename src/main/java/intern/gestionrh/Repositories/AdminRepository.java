package intern.gestionrh.Repositories;

import intern.gestionrh.Entities.Administrateur;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Administrateur,Long> {
}
