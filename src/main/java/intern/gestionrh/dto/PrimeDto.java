package intern.gestionrh.dto;

import intern.gestionrh.Entities.TypePrime;
import lombok.Data;

import java.util.Date;

@Data
public class PrimeDto {
    private long id;
    private float montant;
    private Date dateAttribution;
    private TypePrime typePrime;
    private Long utilisateurId;
}
