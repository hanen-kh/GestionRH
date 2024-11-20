package intern.gestionrh.dto;

import intern.gestionrh.Entities.TypePrime;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

import java.util.Date;
@Hidden
@Data
public class PrimeDto {
    private long id;
    private float montant;
    private Date dateAttribution;
    private TypePrime typePrime;
    private Long utilisateurId;
}
