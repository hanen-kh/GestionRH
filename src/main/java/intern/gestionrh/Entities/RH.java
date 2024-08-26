package intern.gestionrh.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name="rh_id")
public class RH extends Utilisateur implements Serializable {
    private static final long SerialVersionUID =1L;
}
