package intern.gestionrh.dto;

import intern.gestionrh.Entities.Role;
import intern.gestionrh.Entities.TypeRole;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

import java.util.Date;
import java.util.List;
@Hidden
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UtilisateurDto {
    private Long id;
    private String nomUser;
    private String prenomUser;
    private String email;
    private Date dateEmbauche;
    private Long departementId;
    private Role role;
    private List<PrimeDto> primes;
    private boolean actif=false;
}
