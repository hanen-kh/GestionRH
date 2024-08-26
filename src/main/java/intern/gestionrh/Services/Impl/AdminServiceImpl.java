package intern.gestionrh.Services.Impl;

import intern.gestionrh.Entities.Administrateur;
import intern.gestionrh.Entities.Role;
import intern.gestionrh.Entities.TypeRole;
import intern.gestionrh.Repositories.AdminRepository;
import intern.gestionrh.Repositories.RoleRepository;
import intern.gestionrh.Services.AdminService;
import intern.gestionrh.dto.AdminDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

@Autowired
    private AdminRepository adminRepository;
@Autowired
private RoleRepository roleRepository;

@Autowired
EmailService emailService;

@Autowired
private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AdminDto saveAdmin(AdminDto adminDto) {

        Optional<Role> adminRole = roleRepository.findByLibelle(TypeRole.ADMIN);
        if (adminRole.isPresent()) {
            adminDto.setRole(adminRole.get());
        } else {
            throw new RuntimeException("Role ADMIN n'existe pas");
        }

        String DEFAULT_PASSWORD = "AdminSecurePassword";
        String EMAIL_DOMAIN = "systems.tn";

        // Construire l'email professionnel
        String emailProfessionnel = adminDto.getPrenomUser() + "." + adminDto.getNomUser() + "@" + EMAIL_DOMAIN;

        // Mappage de AdminDto à Utilisateur
        Administrateur admin=new Administrateur();
         admin = modelMapper.map(adminDto, Administrateur.class);

        // Définir un mot de passe par défaut et le hacher
        admin.setMotDePasseUser(DEFAULT_PASSWORD);
        String hashedPassword = passwordEncoder.encode(admin.getMotDePasseUser());
        admin.setMotDePasseUser(hashedPassword);

        // Envoi d'un email à l'administrateur
        String subject = "Création de votre compte Admin chez ASE";
        String body = "Votre compte administrateur a été créé avec succès. Votre email professionnel est " + emailProfessionnel
                + ". Votre mot de passe est : " + DEFAULT_PASSWORD + ". \n Veuillez changer votre mot de passe dès que possible.";
        emailService.sendSimpleEmail(admin.getEmail(), subject, body);

        // Enregistrer l'utilisateur en tant qu'admin
        Administrateur savedAdmin = adminRepository.save(admin);

        return modelMapper.map(savedAdmin, AdminDto.class);
    }
}
