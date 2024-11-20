package intern.gestionrh.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

import java.util.List;
import java.util.Set;
@Hidden
@Data
public class DepartementDto {
    private Long id;
    private String nomDepartement;
    private List<UtilisateurDto> employesDepartement;

}
