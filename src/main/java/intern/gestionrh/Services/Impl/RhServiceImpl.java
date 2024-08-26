package intern.gestionrh.Services.Impl;


import intern.gestionrh.Entities.RH;
import intern.gestionrh.Entities.Role;
import intern.gestionrh.Entities.TypeRole;
import intern.gestionrh.Repositories.RhRepository;
import intern.gestionrh.Repositories.RoleRepository;
import intern.gestionrh.Services.RhService;

import intern.gestionrh.dto.RhDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class RhServiceImpl implements RhService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EmailService emailService;

    @Autowired
    RhRepository rhRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RhDto saveRH(RhDto rhDto) {

        Optional<Role> RhRole = roleRepository.findByLibelle(TypeRole.RH);
        if (RhRole.isPresent()) {
            rhDto.setRole(RhRole.get());
        } else {
            throw new RuntimeException("Role RH n'existe pas");
        }
        String DEFAULT_PASSWORD = "AutonomousSystemsEngineering";
        String EMAIL_DOMAIN = "systems.tn";

        // Construire l'email professionnel
        String emailProfessionnel = rhDto.getPrenomUser() + "." + rhDto.getNomUser() + "@" + EMAIL_DOMAIN;

       RH rh = new RH();
        rh = modelMapper.map(rhDto, RH.class);

        // Définir le mot de passe par défaut et le hasher
        rh.setMotDePasseUser(DEFAULT_PASSWORD);
        String hashedPassword = passwordEncoder.encode(rh.getMotDePasseUser());
        rh.setMotDePasseUser(hashedPassword);

        // Envoyer un email à l'utilisateur
        String subject = "Création de votre compte chez ASE";
        String body = "Votre compte a été créé avec succès. Votre email professionnel est " + emailProfessionnel
                + ". Votre mot de passe est : " + DEFAULT_PASSWORD + ". \n Veuillez changer votre mot de passe dès que possible.";
        emailService.sendSimpleEmail(rh.getEmail(), subject, body);

        // Enregistrer l'utilisateur dans la base de données
        RH savedRH = rhRepository.save(rh);

        // Mapper Utilisateur vers RhDto et retourner le résultat
        return modelMapper.map(savedRH, RhDto.class);

    }
}
