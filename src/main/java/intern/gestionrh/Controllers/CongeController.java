package intern.gestionrh.Controllers;

import intern.gestionrh.Entities.Conge;
import intern.gestionrh.Entities.StatutConge;
import intern.gestionrh.Services.CongeService;
import intern.gestionrh.Services.EmployeService;
import intern.gestionrh.dto.CongeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/conge")
public class CongeController {
    @Autowired
    private CongeService congeService;

    @Autowired
    private EmployeService employeService;


    @GetMapping("/employe/{idEmploye}")
    @PreAuthorize("hasAnyAuthority('ROLE_RH', 'ROLE_EMPLOYE')")
    public ResponseEntity<Set<Conge>> findCongesByEmployeId(@PathVariable Long idEmploye) {
        Set<Conge> conges = congeService.findCongesByEmployeId(idEmploye);
        return ResponseEntity.ok(conges);
    }



    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYE')")
    public ResponseEntity<CongeDto> updateConge(@PathVariable Long id, @RequestBody CongeDto congeDto) {
        CongeDto updatedConge = congeService.updateConge(id, congeDto);
        return ResponseEntity.ok(updatedConge);
    }



    @PostMapping("/{congeId}")
    @PreAuthorize("hasAuthority('ROLE_RH')")
    public ResponseEntity<String> reponseDemandeConge(@PathVariable Long congeId, @RequestParam StatutConge nouveauStatut) {

            congeService.reponseDemandeConge(congeId, nouveauStatut);
            return ResponseEntity.ok("Réponse à la demande de congé traitée avec succès.");

    }


}
