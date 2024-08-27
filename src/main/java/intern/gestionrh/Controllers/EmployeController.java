package intern.gestionrh.Controllers;

import intern.gestionrh.Services.EmployeService;
import intern.gestionrh.Services.Impl.EmployeServiceImpl;
import intern.gestionrh.dto.CongeDto;
import intern.gestionrh.dto.EmployeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/employe")
public class EmployeController {
    @Autowired
    private EmployeService employeService;

    @GetMapping("/departement/{nomDepartement}")
    @PreAuthorize("hasAnyAuthority('ROLE_RH', 'ROLE_ADMIN')")
    public ResponseEntity<List<EmployeDto>> getEmployesByNomDepartement(@PathVariable String nomDepartement) {
        List<EmployeDto> employes = employeService.getEmployesByNomDepartement(nomDepartement);
        return ResponseEntity.ok(employes);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_RH', 'ROLE_ADMIN')")
    public ResponseEntity<List<EmployeDto>> getAllEmployes() {
        List<EmployeDto> employes = employeService.findAllEmployes();
        return ResponseEntity.ok(employes);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_RH', 'ROLE_ADMIN')")
    public ResponseEntity<EmployeDto> getEmployeById(@PathVariable Long id) {
        EmployeDto employe = employeService.findEmployeById(id);
        return ResponseEntity.ok(employe);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_RH', 'ROLE_ADMIN')")
    public ResponseEntity<EmployeDto> updateEmploye(@PathVariable Long id, @RequestBody EmployeDto employeDto) {
        EmployeDto updatedEmploye = employeService.updateEmploye(id, employeDto);
        return ResponseEntity.ok(updatedEmploye);
    }
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_RH', 'ROLE_ADMIN')")
    public ResponseEntity<EmployeDto> createEmploye( @RequestBody EmployeDto employeDto) {
        employeService.saveEmploye( employeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(employeDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        employeService.deleteEmployeById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{employeId}/conges")
    public ResponseEntity<CongeDto> demanderConge(@PathVariable Long employeId, @RequestBody CongeDto congeDto) {
        CongeDto conge = employeService.demanderConge(employeId, congeDto);
        return status(HttpStatus.CREATED).body(conge);
    }

    @GetMapping("/{employeId}/conges")
    public ResponseEntity<List<CongeDto>> consulterReponsesConges(@PathVariable Long employeId) {
        List<CongeDto> conges = employeService.consulterReponsesConges(employeId);
        return ResponseEntity.ok(conges);
    }

    @GetMapping("/{departementId}/historique-employes")
    public List<EmployeDto> getHistoriqueEmployes(@PathVariable Long departementId,
                                                  @RequestParam("dateEmbauche") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEmbauche) {
        return employeService.getHistoriqueEmployes(departementId, dateEmbauche);
    }
}
