package intern.gestionrh.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Hidden
//@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeDto extends UtilisateurDto{
    private String matricule;
    private long soldeConges;
    private Set<CongeDto> conges= new HashSet<>();
}
