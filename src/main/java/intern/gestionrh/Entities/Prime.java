package intern.gestionrh.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Prime implements Serializable {
    private static final long SerialVersionUID=1L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private float Montant;

    @Temporal(TemporalType.DATE)
    private Date dateAttribution;
    @Enumerated(EnumType.STRING)
    private TypePrime typePrime;

    @Setter
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Utilisateur utilisateur;

}
