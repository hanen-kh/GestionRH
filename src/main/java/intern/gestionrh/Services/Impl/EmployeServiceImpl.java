package intern.gestionrh.Services.Impl;

import intern.gestionrh.Entities.*;
import intern.gestionrh.Repositories.CongeRepository;
import intern.gestionrh.Repositories.EmployeRepository;
import intern.gestionrh.Repositories.RoleRepository;
import intern.gestionrh.Repositories.UtilisateurRepository;
import intern.gestionrh.Services.EmployeService;
import intern.gestionrh.dto.CongeDto;
import intern.gestionrh.dto.EmployeDto;
import intern.gestionrh.dto.UtilisateurDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@AllArgsConstructor
@Service
public class EmployeServiceImpl  implements EmployeService {
    @Autowired
    private EmployeRepository employeRepo;

    @Autowired
    private RoleRepository roleRepository;

 @Autowired
   private UtilisateurServiceImpl utilisateurService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private UtilisateurRepository utilisateurRepo;
    @Autowired
    private CongeRepository congeRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;



    @Override
    public List<EmployeDto> findAllEmployes() {
        List<Utilisateur> employes = utilisateurRepo.findAll(); //  filtrer seulement les employes
        return employes.stream()
                .filter(utilisateur -> utilisateur instanceof Employe)
                .map(employe -> modelMapper.map(employe, EmployeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeDto findEmployeById(Long id) {
        Utilisateur employe = utilisateurRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé pour cet ID : " + id));
        if (!(employe instanceof Employe)) {
            throw new ResourceNotFoundException("Utilisateur trouvé n'est pas un employé");
        }
        return modelMapper.map(employe, EmployeDto.class);
    }




    @Override
    @Transactional
    public void deleteEmployeById(Long id) {
        Utilisateur employe = utilisateurRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé pour cet ID : " + id));
        if (!(employe instanceof Employe)) {
            throw new ResourceNotFoundException("Utilisateur trouvé n'est pas un employé");
        }
        utilisateurRepo.delete(employe);
    }

    @Override
    @Transactional
    public EmployeDto updateEmploye(Long id, EmployeDto employeDto) {
        Utilisateur employeExistant = utilisateurRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé pour cet ID : " + id));
        if (!(employeExistant instanceof Employe)) {
            throw new ResourceNotFoundException("Utilisateur trouvé n'est pas un employé");
        }

        Employe employe = (Employe) employeExistant;
        modelMapper.map(employeDto, employe);
        employe = utilisateurRepo.save(employe);

        return modelMapper.map(employe, EmployeDto.class);
    }


    @Override
    @Transactional
    public CongeDto demanderConge(Long employeId, CongeDto congeDto) {
        // Récupérer l'employé par ID
        Employe employe = (Employe) utilisateurRepo.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé pour cet ID : " + employeId));

        // Convertir CongeDto en entité Conge
        Conge conge = modelMapper.map(congeDto, Conge.class);
        conge.setEmploye(employe);

        // Définir le statut de congé à "EnAttente"
        conge.setStatutConge(StatutConge.EnAttente);

        conge = congeRepo.save(conge);
        employe.getConges().add(conge);
        employeRepo.save(employe);

        // Trouver le RH du même département
        Departement departement = employe.getDepartement();
        if (departement != null) {
            for (Utilisateur utilisateur : departement.getEmployesDepartement()) {
                if (utilisateur instanceof RH) {
                    RH rh = (RH) utilisateur;

                    // Construire le message de l'email
                    String subject = "Nouvelle demande de congé";
                    String body = "L'employé " + employe.getNomUser() + " " + employe.getPrenomUser() +
                            " a demandé un congé.\n\n" +
                            "Détails de la demande de congé :\n" +
                            "Date de début : " + conge.getDateDebut() + "\n" +
                            "Date de fin : " + conge.getDateFin() + "\n\n" +
                            "son solde de congés est : "+ employe.getSoldeConges() +
                            "Veuillez consulter les détails.";

                    // Envoyer un email au RH
                    emailService.sendSimpleEmail(rh.getEmail(), subject, body);

                    break; // Nous supposons qu'il n'y a qu'un seul RH par département
                }
            }
        }

        // Retourner le DTO du congé sauvegardé
        return modelMapper.map(conge, CongeDto.class);
    }

    @Override
    public List<CongeDto> consulterReponsesConges(Long employeId) {
        List<Conge> conges = congeRepo.findByEmployeId(employeId);
        return conges.stream()
                .map(conge -> modelMapper.map(conge, CongeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeDto> getEmployesByNomDepartement(String nomDepartement) {
        return utilisateurRepo.findByDepartementNom(nomDepartement).stream()
                .filter(utilisateur -> utilisateur instanceof Employe) // Filtre pour ne garder que les Employés
                .map(utilisateur -> modelMapper.map(utilisateur, EmployeDto.class)) // Convertit en EmployeDto
                .collect(Collectors.toList());
    }

@Override
    public List<EmployeDto> getHistoriqueEmployes(Long departementId, LocalDate dateEmbauche) {
        // Récupérer tous les employés du département
        List<Utilisateur> utilisateurs = utilisateurRepo.findByDepartementId(departementId);

        // Filtrer les employés par date d'embauche et les mapper vers EmployeDto
        return utilisateurs.stream()
                .filter(utilisateur -> utilisateur instanceof Employe) // Filtrer pour les employés
                .filter(employe -> employe.getDateEmbauche() != null )
                .map(employe -> modelMapper.map(employe, EmployeDto.class))
                .collect(Collectors.toList());
    }




    @Transactional
@Override
public EmployeDto saveEmploye(EmployeDto employeDto) {

    Optional<Role> employeRole = roleRepository.findByLibelle(TypeRole.EMPLOYE);
    if (employeRole.isPresent()) {
        employeDto.setRole(employeRole.get());
    } else {
        throw new RuntimeException("Role EMPLOYE n'existe pas");
    }
    String DEFAULT_PASSWORD = "AutonomousSystemsEngineering";
    String EMAIL_DOMAIN = "systems.tn";

    // Build professional email
    String emailProfessionnel = employeDto.getPrenomUser() + "." + employeDto.getNomUser() + "@" + EMAIL_DOMAIN;

    // Map EmployeDto to Employe
    Employe employe=new Employe();
     employe = modelMapper.map(employeDto, Employe.class);

    // Set default password and hash it
    employe.setMotDePasseUser(DEFAULT_PASSWORD);
    String hashedPassword = passwordEncoder.encode(employe.getMotDePasseUser());
    employe.setMotDePasseUser(hashedPassword);

    // Envoyer un email à l'utilisateur
    String subject = "Création de votre compte chez ASE";
    String body = "Votre compte a été créé avec succès. Votre email Professionnel est " + emailProfessionnel
            + ". Votre mot de passe est : "+ DEFAULT_PASSWORD +" . \n Veuillez changer votre mot de passe dès que possible.";
    emailService.sendSimpleEmail(employe.getEmail(), subject, body);



    employe.setMatricule(employeDto.getMatricule());
    employe.setSoldeConges(employeDto.getSoldeConges());

    // Save the employe entity

    employe = employeRepo.save(employe);

    return modelMapper.map(employe, EmployeDto.class);

}
}






