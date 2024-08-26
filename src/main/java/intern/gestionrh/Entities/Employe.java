package intern.gestionrh.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name="employe_id")
public class Employe extends Utilisateur implements Serializable {
    private static final long SerialVersionUID=1L;

    @Column(nullable = false,unique = true)
    private String matricule;

    @Column
    private long soldeConges;

    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL)

    private Set<Conge> conges=new HashSet<>();


}
