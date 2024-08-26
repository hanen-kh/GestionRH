package intern.gestionrh.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity

@Inheritance(strategy=InheritanceType.JOINED)
public class Utilisateur implements Serializable , UserDetails {
    private static final long SerialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Column
    protected String nomUser;

    @Column
    protected String prenomUser;

    @Column
    @JsonIgnore
    protected String motDePasseUser;

    @Column(unique = true)
    private String email;
    @Temporal(TemporalType.DATE)
    private Date dateEmbauche;

    @ManyToOne(fetch = FetchType.EAGER)
    protected Departement departement;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="role_id")
    private Role role;

    protected boolean actif = false;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)

    protected Set<Prime> primes=new HashSet<>();

    @Override
    public String getPassword() {
        return this.motDePasseUser;
    }
    @Override
    public String getUsername() {
        return this.email;
    }
    @Override
    public boolean isAccountNonExpired() {
        return this.actif;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+this.role.getLibelle()));
    }

    @Override
    public boolean isEnabled() {
        return this.actif;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.actif;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.actif;
    }

    public void addPrime(Prime prime) {
        primes.add(prime);
        prime.setUtilisateur(this);
    }
}
