package intern.gestionrh.Services;

import intern.gestionrh.dto.DepartementDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DepartementService {
    DepartementDto createDepartement(DepartementDto departementDto);
    DepartementDto getDepartementById(Long id);
    List<DepartementDto> getAllDepartements();

    @Transactional
    DepartementDto updateDepartement(Long id, DepartementDto departementDto);

    void deleteDepartementById(Long id);
}
