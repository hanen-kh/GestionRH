package intern.gestionrh.dto;

import intern.gestionrh.Entities.StatutConge;
import intern.gestionrh.Entities.TypeConge;
import lombok.*;

import java.util.Date;

@Data

public class CongeDto {
    private long id;
    private StatutConge statutConge;
    private TypeConge typeConge;
    private Date dateDebut;
    private Date dateFin;
    private Long employeId;
}
