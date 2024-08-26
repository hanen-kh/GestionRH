package intern.gestionrh.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@Entity
public class Conge implements Serializable {
    private static final long SerialVersionUID=1L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    @Enumerated(EnumType.STRING)
    private StatutConge statutConge;

    @Enumerated(EnumType.STRING)
    private TypeConge typeConge;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dateFin;


    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Getter
    @Setter
    private Employe employe;


}
