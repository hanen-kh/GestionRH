package intern.gestionrh.Services;

import intern.gestionrh.Entities.StatutConge;
import intern.gestionrh.dto.CongeDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CongeService {



    List<CongeDto> findCongesByEmployeId(Long idUEmploye);

    CongeDto updateConge(Long id, CongeDto congeDto);


    @Transactional
    void reponseDemandeConge(Long congeId, StatutConge nouveauStatut);
}
