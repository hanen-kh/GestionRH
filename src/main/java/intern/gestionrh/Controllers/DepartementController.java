package intern.gestionrh.Controllers;

import intern.gestionrh.Services.DepartementService;
import intern.gestionrh.Services.Impl.DepartementServiceImpl;
import intern.gestionrh.dto.DepartementDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departement")
public class DepartementController {
    @Autowired
    private DepartementService departementService;

    @GetMapping
    public List<DepartementDto> getAllDepartements() {
        return departementService.getAllDepartements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartementDto> getDepartementById(@PathVariable Long id) {
        DepartementDto departementDto = departementService.getDepartementById(id);
        return ResponseEntity.ok(departementDto);
    }

    @PostMapping
    public ResponseEntity<DepartementDto> createDepartement(@RequestBody DepartementDto departementDto) {
        DepartementDto createdDepartement = departementService.createDepartement(departementDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartementDto> updateDepartement(@PathVariable Long id, @RequestBody DepartementDto departementDto) {
        DepartementDto updatedDepartement = departementService.updateDepartement(id, departementDto);
        return ResponseEntity.ok(updatedDepartement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartement(@PathVariable Long id) {
        departementService.deleteDepartementById(id);
        return ResponseEntity.noContent().build();
    }
}
