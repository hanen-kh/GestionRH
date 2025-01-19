classDiagram
direction BT
class Administrateur {
  - long SerialVersionUID
}
class Conge {
  - Date dateDebut
  - Employe employe
  - long SerialVersionUID
  - Date dateFin
  - TypeConge typeConge
  - long id
  - StatutConge statutConge
  + getDateDebut() Date
  + getEmploye() Employe
  + setEmploye(Employe) void
  + getId() long
  + getStatutConge() StatutConge
  + getTypeConge() TypeConge
  + getDateFin() Date
  + setId(long) void
  + setStatutConge(StatutConge) void
  + setTypeConge(TypeConge) void
  + setDateFin(Date) void
  + setDateDebut(Date) void
}
class Departement {
  - long SerialVersionUID
  - String nomDepartement
  - long id
  - Set~Utilisateur~ EmployesDepartement
  + getId() long
  + getNomDepartement() String
  + setId(long) void
  + setNomDepartement(String) void
  + getEmployesDepartement() Set~Utilisateur~
  + setEmployesDepartement(Set~Utilisateur~) void
}
class Employe {
  - long SerialVersionUID
  - String matricule
  - long soldeConges
  - Set~Conge~ conges
  + getMatricule() String
  + getSoldeConges() long
  + getConges() Set~Conge~
  + setMatricule(String) void
  + setSoldeConges(long) void
  + setConges(Set~Conge~) void
}
class Jwt {
  - String valeur
  - boolean desactive
  - long SerialVersionUID
  - long id
  - boolean expire
  - Utilisateur utilisateur
  + getId() long
  + isExpire() boolean
  + isDesactive() boolean
  + getValeur() String
  + getUtilisateur() Utilisateur
  + setId(long) void
  + setExpire(boolean) void
  + setUtilisateur(Utilisateur) void
  + setDesactive(boolean) void
  + builder() JwtBuilder
  + setValeur(String) void
}
class JwtBuilder {
  - long id
  - boolean expire
  - Utilisateur utilisateur
  - boolean desactive
  - String valeur
  + id(long) JwtBuilder
  + expire(boolean) JwtBuilder
  + desactive(boolean) JwtBuilder
  + valeur(String) JwtBuilder
  + utilisateur(Utilisateur) JwtBuilder
  + build() Jwt
  + toString() String
}
class Prime {
  - long id
  - long SerialVersionUID
  - Date dateAttribution
  - float Montant
  - TypePrime typePrime
  - Utilisateur utilisateur
  + setUtilisateur(Utilisateur) void
  + getId() long
  + getMontant() float
  + getDateAttribution() Date
  + getTypePrime() TypePrime
  + getUtilisateur() Utilisateur
  + setId(long) void
  + setMontant(float) void
  + setDateAttribution(Date) void
  + setTypePrime(TypePrime) void
}
class RH {
  - long SerialVersionUID
}
class Role {
  - long SerialVersionUID
  - Long id
  - TypeRole libelle
  + getId() Long
  + getLibelle() TypeRole
  + setId(Long) void
  + setLibelle(TypeRole) void
}
class StatutConge {
<<enumeration>>
  +  Valide
  +  EnCours
  +  Annule
  +  EnAttente
  +  Refuse
  +  Termine
  + values() StatutConge[]
  + valueOf(String) StatutConge
}
class TypeConge {
<<enumeration>>
  +  Maladie
  +  Maternite
  +  Annuel
  +  Personnel
  + valueOf(String) TypeConge
  + values() TypeConge[]
}
class TypePrime {
<<enumeration>>
  +  exceptionnelle
  +  performance
  +  anciennete
  + valueOf(String) TypePrime
  + values() TypePrime[]
}
class TypeRole {
<<enumeration>>
  +  ADMIN
  +  EMPLOYE
  +  RH
  + values() TypeRole[]
  + valueOf(String) TypeRole
}
class Utilisateur {
  - String email
  # boolean actif
  # String nomUser
  # String motDePasseUser
  - Role role
  # long id
  # Set~Prime~ primes
  - long SerialVersionUID
  # String prenomUser
  - Date dateEmbauche
  # Departement departement
  + getNomUser() String
  + setPrimes(Set~Prime~) void
  + isActif() boolean
  + setDateEmbauche(Date) void
  + setNomUser(String) void
  + setPrenomUser(String) void
  + setMotDePasseUser(String) void
  + setEmail(String) void
  + setDepartement(Departement) void
  + getId() long
  + getPrenomUser() String
  + getPrimes() Set~Prime~
  + builder() UtilisateurBuilder
  + getMotDePasseUser() String
  + getEmail() String
  + setId(long) void
  + getDateEmbauche() Date
  + getDepartement() Departement
  + getRole() Role
  + setActif(boolean) void
  + setRole(Role) void
  + addPrime(Prime) void
  + isAccountNonExpired() boolean
  + isCredentialsNonExpired() boolean
  + isEnabled() boolean
  + getPassword() String
  + getAuthorities() Collection~GrantedAuthority~
  + getUsername() String
  + isAccountNonLocked() boolean
}
class UtilisateurBuilder {
  - String email
  - Date dateEmbauche
  - Role role
  - long id
  - Departement departement
  - boolean actif
  - Set~Prime~ primes
  - String nomUser
  - String prenomUser
  - String motDePasseUser
  + id(long) UtilisateurBuilder
  + nomUser(String) UtilisateurBuilder
  + prenomUser(String) UtilisateurBuilder
  + motDePasseUser(String) UtilisateurBuilder
  + build() Utilisateur
  + toString() String
  + email(String) UtilisateurBuilder
  + dateEmbauche(Date) UtilisateurBuilder
  + departement(Departement) UtilisateurBuilder
  + role(Role) UtilisateurBuilder
  + actif(boolean) UtilisateurBuilder
  + primes(Set~Prime~) UtilisateurBuilder
}
class Validation {
  - long id
  - Instant expiration
  - Instant activation
  - Utilisateur utilisateur
  - long SerialVersionUID
  - String code
  + setExpiration(Instant) void
  + getId() long
  + getExpiration() Instant
  + setUtilisateur(Utilisateur) void
  + getActivation() Instant
  + getCode() String
  + getUtilisateur() Utilisateur
  + setId(long) void
  + setActivation(Instant) void
  + setCode(String) void
}

Administrateur  -->  Utilisateur 
Employe  -->  Utilisateur 
Jwt  -->  JwtBuilder 
RH  -->  Utilisateur 
Utilisateur  -->  UtilisateurBuilder 
