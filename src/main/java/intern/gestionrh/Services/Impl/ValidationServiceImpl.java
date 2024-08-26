package intern.gestionrh.Services.Impl;

import intern.gestionrh.Entities.Utilisateur;
import intern.gestionrh.Entities.Validation;
import intern.gestionrh.Repositories.UtilisateurRepository;
import intern.gestionrh.Repositories.ValidationRepository;
import intern.gestionrh.Services.ValidationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@AllArgsConstructor
public class ValidationServiceImpl implements ValidationService {
    @Autowired
    private ValidationRepository validationRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired

    private UtilisateurServiceImpl utilisateurService;


    @Override
    public void codeActivation(String username) {
        // Charger l'utilisateur en utilisant loadUserByUsername
        Utilisateur utilisateur = new Utilisateur();
        utilisateur= utilisateurService.loadUserByUsername(username);

        // Créer un nouvel objet Validation
        Validation validation = new Validation();
        validation.setUtilisateur(utilisateur);

        // Définir les instants d'activation et d'expiration
        Instant activation = Instant.now();
        validation.setActivation(activation);
        Instant expiration = activation.plus(3, MINUTES);
        validation.setExpiration(expiration);

        // Générer un code de validation aléatoire à 6 chiffres
        Random random = new Random();
        int randomInteger = random.nextInt(999999);
        String code = String.format("%06d", randomInteger);
        validation.setCode(code);

        // Enregistrer l'objet Validation
        validationRepository.save(validation);

        // Envoyer la notification à l'utilisateur
        String subject = "Votre code de validation";
        String body = "Votre code de validation est : " + validation.getCode();
        emailService.sendSimpleEmail(utilisateur.getEmail(), subject, body);
    }






    @Override
    public void demandeDeNouveauMotDePasse(Map<String, String> parametres){
        //"email":"khmiletthanen@gmail.com"
        codeActivation(parametres.get("email"));}


    @Override

    public void modifierMotDePasse(Map<String, String> parametres){
        //parametres-->
        //{"email":"khmiletthanen@gmail.com",
        //"code":"999999" ,
        //"password":"khmiletttttt"}
        // Extract parameters
        String email = parametres.get("email");
        String password = parametres.get("password");
        String code = parametres.get("code");

        // Valider code d'activation
        Validation validation = validationRepository.findByCode(code);
        if (validation == null) {
            throw new RuntimeException("Code de validation invalide");
        }
        if (Instant.now().isAfter(validation.getExpiration())) {
            throw new RuntimeException("Votre code a expiré");
        }

        // Load user by email
        Utilisateur utilisateur = utilisateurService.loadUserByUsername(email);
        if (utilisateur == null) {
            throw new RuntimeException("Utilisateur inconnu");
        }

        // Activer le user s'il n'est pas actif
        if (!utilisateur.isActif()) {
            utilisateur.setActif(true);
            utilisateurRepository.save(utilisateur);
        }

        // Update le password
        if (password != null && !password.isEmpty()) {
            String hashedPassword = passwordEncoder.encode(password);
            utilisateur.setMotDePasseUser(hashedPassword);
            utilisateurRepository.save(utilisateur);
        }

        // Option: Remove la validation
        //validationRepository.delete(validation);
    }
    }
