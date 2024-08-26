package intern.gestionrh.Repositories;

import intern.gestionrh.Entities.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ValidationRepository extends JpaRepository<Validation,Long> {


    Validation findByCode(String code);

}

