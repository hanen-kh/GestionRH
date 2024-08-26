package intern.gestionrh.Controllers;

import intern.gestionrh.Config.JwtService;
import intern.gestionrh.Entities.Utilisateur;
import intern.gestionrh.Services.Impl.AdminServiceImpl;
import intern.gestionrh.Services.Impl.RhServiceImpl;
import intern.gestionrh.Services.Impl.UtilisateurServiceImpl;
import intern.gestionrh.Services.Impl.ValidationServiceImpl;
import intern.gestionrh.Services.UtilisateurService;
import intern.gestionrh.dto.AdminDto;
import intern.gestionrh.dto.AuthentificationDto;
import intern.gestionrh.dto.RhDto;
import intern.gestionrh.dto.UtilisateurDto;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/utilisateur")

public class UtilisateurController {
    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ValidationServiceImpl validationService;

    @Autowired
    private RhServiceImpl rhService;

    @Autowired
    private AdminServiceImpl adminService;



    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UtilisateurDto> createUtilisateur(@RequestBody UtilisateurDto utilisateurDto) {
       utilisateurService.saveUtilisateur(utilisateurDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(utilisateurDto);
    }

    @PostMapping("/create/rh")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RhDto> createRh(@RequestBody RhDto rhDto) {
        rhService.saveRH(rhDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(rhDto);
        }


    @PostMapping("/create/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AdminDto> createAdmin(@RequestBody AdminDto adminDto) {
        adminService.saveAdmin(adminDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(adminDto);
    }

@PostMapping(path="demande-nouveau-pass")
public void demandenouveauPass(@RequestBody Map<String,String> parametres){
        validationService.demandeDeNouveauMotDePasse(parametres);}


    @PostMapping(path="changerpass")
    public void changerPass (@RequestBody Map<String,String> parametres){
        validationService.modifierMotDePasse(parametres);
    }



    @PostMapping("/connexion")
    public Map<String, String> connexion(@RequestBody AuthentificationDto authentificationDto) {
        // Verify that jwtService is not null
        Assert.notNull(jwtService, "jwtService is null");

        // Generate the token
        Map<String, String> tokenMap = jwtService.generate(authentificationDto.username());
        return tokenMap;
    }

    @PostMapping("/deconnexion")
    public void deconnexion(){
        jwtService.deconnexion();
    }
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UtilisateurDto> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UtilisateurDto> getUtilisateurById(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.getUtilisateurById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UtilisateurDto> updateUtilisateur(@PathVariable Long id, @RequestBody UtilisateurDto utilisateurDto) {
        UtilisateurDto updatedUtilisateur = utilisateurService.updateUtilisateur(id, utilisateurDto);
        return ResponseEntity.ok(updatedUtilisateur);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUtilisateurById(@PathVariable Long id) {
        utilisateurService.deleteUtilisateurById(id);
        return ResponseEntity.noContent().build();
    }




    @GetMapping("/departement/{nomDepartement}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UtilisateurDto> getUtilisateursByNomDepartement(@PathVariable String nomDepartement) {
        return utilisateurService.getUtilisateursByNomDepartement(nomDepartement);
    }
    @DeleteMapping("/deleteAll")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteAllUtilisateurs() {
        utilisateurService.deleteAllUtilisateurs();
        return ResponseEntity.noContent().build();
    }



}
