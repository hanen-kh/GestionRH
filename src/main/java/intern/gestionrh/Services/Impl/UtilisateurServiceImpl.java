package intern.gestionrh.Services.Impl;

import intern.gestionrh.Entities.*;
import intern.gestionrh.Repositories.*;
import intern.gestionrh.Services.UtilisateurService;
import intern.gestionrh.dto.UtilisateurDto;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Primary
@AllArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService , UserDetailsService {
    @Autowired
    private UtilisateurRepository utilisateurRepo;

    @Autowired
    private DepartementRepository departementRepo;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private RhRepository rhRepository;

    @Autowired
   private AdminRepository adminRepository;

    @Autowired
    private ValidationRepository validationRepository;



    @Autowired
    private ModelMapper modelMapper ;

@Autowired
private EmailService emailService;






   /* @Override
    public void demandeDeNouveauMotDePasse(Map<String, String> parametres){
       //"email":"khmiletthanen@gmail.com"
        validationService.codeActivation(parametres.get("email"));

    }*/

   /* @Override
    @PostConstruct
    public void modifierMotDePasse(Map<String, String> parametres){
        Utilisateur utilisateur=loadUserByUsername(parametres.get("email"));
        final Validation validation=validationRepository.findByCode(parametres.get("email"));
if(validation.getUtilisateur().getEmail().equals(utilisateur.getEmail())){
    String motDePasse=passwordEncoder.encode(parametres.get("password"));
    utilisateur.setMotDePasseUser(motDePasse);
    utilisateurRepo.save(utilisateur);
}
    }*/


    @Override
    @Transactional
    public UtilisateurDto saveUtilisateur(UtilisateurDto utilisateurDto) {
        String DEFAULT_PASSWORD = "AutonomousSystemsEngineering";
        String EMAIL_DOMAIN = "systems.tn";

        // Map UtilisateurDto to Utilisateur
        Utilisateur utilisateur=new Utilisateur();
        utilisateur = modelMapper.map(utilisateurDto, Utilisateur.class);

        // Set default password and hash it
        utilisateur.setMotDePasseUser(DEFAULT_PASSWORD);
        String hashedPassword = passwordEncoder.encode(utilisateur.getMotDePasseUser());
        utilisateur.setMotDePasseUser(hashedPassword);

        // Build professional email
        String emailProfessionnel = utilisateurDto.getPrenomUser() + "." + utilisateurDto.getNomUser() + "@" + EMAIL_DOMAIN;
        //utilisateur.setEmail(emailProfessionnel);
        // Envoyer un email à l'utilisateur
        String subject = "Création de votre compte chez ASE";
        String body = "Votre compte a été créé avec succès. Votre email Professionnel est " + emailProfessionnel
                + ". Votre mot de passe est : "+ DEFAULT_PASSWORD +" . \n Veuillez changer votre mot de passe dès que possible.";
        emailService.sendSimpleEmail(utilisateur.getEmail(), subject, body);


        // Enregistrer l'utilisateur
        Utilisateur savedUtilisateur=utilisateurRepo.save(utilisateur);


      return modelMapper.map(savedUtilisateur,UtilisateurDto.class);


    }




    @Override
    public List<UtilisateurDto> getAllUtilisateurs() {
        return utilisateurRepo.findAll().stream()
                .map(utilisateur -> modelMapper.map(utilisateur, UtilisateurDto.class))
                .collect(Collectors.toList());
    }
    @Override
    public UtilisateurDto getUtilisateurById(Long id) {
        Utilisateur utilisateur = utilisateurRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé pour cet ID : " + id));
        return modelMapper.map(utilisateur, UtilisateurDto.class);
    }
    @Override
    public List<UtilisateurDto> getUtilisateursByNomDepartement(String nomDepartement) {
        return utilisateurRepo.findByDepartementNom(nomDepartement).stream()
                .map(utilisateur -> modelMapper.map(utilisateur, UtilisateurDto.class))
                .collect(Collectors.toList());
    }

    public void deleteUtilisateurById(Long id) {
        Utilisateur utilisateur = utilisateurRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé pour cet ID : " + id));
        utilisateurRepo.delete(utilisateur);
    }

    @Override
    @Transactional
    public UtilisateurDto updateUtilisateur(Long id, UtilisateurDto utilisateurDto) {
        Utilisateur utilisateur = utilisateurRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé pour cet ID : " + id));

        // Mettre à jour les champs
        utilisateur.setNomUser(utilisateurDto.getNomUser());
        utilisateur.setPrenomUser(utilisateurDto.getPrenomUser());
        utilisateur.setEmail(utilisateurDto.getEmail());
        utilisateur.setDateEmbauche(utilisateurDto.getDateEmbauche());

        // Mettre à jour le département
        if (utilisateurDto.getDepartementId() != null) {
            Departement departement = departementRepo.findById(utilisateurDto.getDepartementId())
                    .orElseThrow(() -> new ResourceNotFoundException("Département non trouvé pour cet ID : " + utilisateurDto.getDepartementId()));
            utilisateur.setDepartement(departement);
        }

        Utilisateur updatedUtilisateur = utilisateurRepo.save(utilisateur);
        return modelMapper.map(updatedUtilisateur, UtilisateurDto.class);
    }

    @Override
    public void deleteAllUtilisateurs() {
        utilisateurRepo.deleteAll();
    }

    @Override
    public Utilisateur loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.utilisateurRepo
                .findByEmail(username)
                .orElseThrow(() -> new  UsernameNotFoundException("Aucun utilisateur ne corespond à cet identifiant"));
    }
}
