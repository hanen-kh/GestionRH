package intern.gestionrh.Services;

import intern.gestionrh.Entities.StatutConge;
import intern.gestionrh.dto.CongeDto;
import intern.gestionrh.dto.EmployeDto;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface EmployeService  {
    List<EmployeDto> findAllEmployes();
    EmployeDto findEmployeById(Long id);

    void deleteEmployeById(Long id);
    EmployeDto updateEmploye(Long id, EmployeDto employeDto);
    CongeDto demanderConge(Long employeId, CongeDto congeDto);

    List<CongeDto> consulterReponsesConges(Long employeId);

    List<EmployeDto> getEmployesByNomDepartement(String nomDepartement);


    List<EmployeDto> getHistoriqueEmployes(Long departementId, LocalDate dateEmbauche);



    EmployeDto saveEmploye(EmployeDto employeDto);
}
