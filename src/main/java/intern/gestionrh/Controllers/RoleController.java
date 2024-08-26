package intern.gestionrh.Controllers;

import intern.gestionrh.Entities.Role;
import intern.gestionrh.Entities.TypeRole;
import intern.gestionrh.Repositories.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    RoleRepository roleRepository;
    RoleController(RoleRepository roleRepository){
        this.roleRepository=roleRepository;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RH')")

    public ResponseEntity<?> createRole(@RequestBody Role role) {
        TypeRole typeRole = role.getLibelle();
        Optional<Role> existingRole = roleRepository.findByLibelle(typeRole);

        // Check if the role already exists
        if (existingRole.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Role avec libelle " + typeRole + " existe déja.");
        }

        // Save the new role
        Role savedRole = roleRepository.save(role);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
    }


}
