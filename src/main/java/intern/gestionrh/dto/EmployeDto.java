package intern.gestionrh.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

//@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeDto extends UtilisateurDto{
    private String matricule;
    private long soldeConges;
    private List<CongeDto> conges;
}
