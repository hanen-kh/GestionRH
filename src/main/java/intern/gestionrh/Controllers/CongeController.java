package intern.gestionrh.Controllers;

import intern.gestionrh.Entities.Conge;
import intern.gestionrh.Entities.StatutConge;
import intern.gestionrh.Services.CongeService;
import intern.gestionrh.Services.EmployeService;
import intern.gestionrh.Services.Impl.CongeServiceImpl;
import intern.gestionrh.dto.CongeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conge")
public class CongeController {
    @Autowired
    private CongeService congeService;

    @Autowired
    private EmployeService employeService;


    @GetMapping("/employe/{idEmploye}")
    @PreAuthorize("hasAnyAuthority('ROLE_RH', 'ROLE_EMPLOYE')")
    public ResponseEntity<List<CongeDto>> findCongesByEmployeId(@PathVariable Long idEmploye) {
        // Retrieve the list of Conges for the given Employe ID
        List<CongeDto> conges = congeService.findCongesByEmployeId(idEmploye);
        return ResponseEntity.ok(conges);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYE')")
    public ResponseEntity<CongeDto> updateConge(@PathVariable Long id, @RequestBody CongeDto congeDto) {
        CongeDto updatedConge = congeService.updateConge(id, congeDto);
        return ResponseEntity.ok(updatedConge);
    }



    @PutMapping("/conges/{congeId}")
    @PreAuthorize("hasAuthority('ROLE_RH')")
    public ResponseEntity<String> reponseDemandeConge(@PathVariable Long congeId, @RequestParam StatutConge nouveauStatut) {
        try {
            congeService.reponseDemandeConge(congeId, nouveauStatut);
            return ResponseEntity.ok("Réponse à la demande de congé traitée avec succès.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue.");
        }
    }
}
