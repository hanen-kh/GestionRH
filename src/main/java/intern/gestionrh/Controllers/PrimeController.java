package intern.gestionrh.Controllers;

import intern.gestionrh.Services.Impl.PrimeServiceImpl;
import intern.gestionrh.Services.PrimeService;
import intern.gestionrh.dto.PrimeDto;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/prime")
public class PrimeController {
    @Autowired
    private PrimeService primeService;



    @GetMapping("/utilisateur/{idUtilisateur}")
    public ResponseEntity<List<PrimeDto>> getPrimesByUtilisateurId(@PathVariable Long idUtilisateur) {
        List<PrimeDto> primes = primeService.findAllPrimes(idUtilisateur);
        return ResponseEntity.ok(primes);
    }




    @PostMapping("/add/{UtilisateurId}")
    @PreAuthorize("hasAuthority('ROLE_RH')")
    public ResponseEntity<PrimeDto> createPrime(@PathVariable Long UtilisateurId, @RequestBody PrimeDto primeDto) {
        PrimeDto newPrime = primeService.savePrime(UtilisateurId, primeDto);
        return ResponseEntity.ok(newPrime);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_RH')")
    public ResponseEntity<PrimeDto> updatePrime(@PathVariable Long id, @RequestBody PrimeDto primeDto) {
        PrimeDto updatedPrime = primeService.updatePrime(id, primeDto);
        return ResponseEntity.ok(updatedPrime);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_RH')")
    public ResponseEntity<Void> deletePrime(@PathVariable Long id) {
        primeService.deletePrimeById(id);
        return ResponseEntity.noContent().build();
    }


}
