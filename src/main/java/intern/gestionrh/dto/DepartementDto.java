package intern.gestionrh.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class DepartementDto {
    private Long id;
    private String nomDepartement;
    private List<UtilisateurDto> employesDepartement;

}
