package intern.gestionrh.Services;

import intern.gestionrh.Entities.Conge;
import intern.gestionrh.Entities.StatutConge;
import intern.gestionrh.dto.CongeDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface CongeService {



    Set<Conge> findCongesByEmployeId(Long idUEmploye);

    CongeDto updateConge(Long id, CongeDto congeDto);


    @Transactional
    void reponseDemandeConge(Long congeId, StatutConge nouveauStatut);
}
