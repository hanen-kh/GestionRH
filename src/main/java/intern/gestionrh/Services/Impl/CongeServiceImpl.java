package intern.gestionrh.Services.Impl;

import intern.gestionrh.Entities.*;
import intern.gestionrh.Repositories.CongeRepository;
import intern.gestionrh.Repositories.EmployeRepository;
import intern.gestionrh.Services.CongeService;
import intern.gestionrh.dto.CongeDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
@AllArgsConstructor
@Service
public class CongeServiceImpl implements CongeService {
    @Autowired
    private  CongeRepository congeRepo;

    @Autowired
    private EmployeRepository employeRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EmailService emailService;
    @Override
    public List<CongeDto> findCongesByEmployeId(Long idUEmploye) {
        List<Conge> conges = congeRepo.findByEmployeId(idUEmploye);
        return conges.stream()
                .map(conge -> modelMapper.map(conge, CongeDto.class))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public CongeDto updateConge(Long id, CongeDto congeDetails) {
        Conge conge = congeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("congé n'existe pas pour cet id " + id));


        modelMapper.map(congeDetails, conge);

        conge = congeRepo.save(conge);
        // Récupérer l'employé associé au congé
        Employe employe = conge.getEmploye();
        // Trouver le RH du même département
        Departement departement = employe.getDepartement();
        if (departement != null) {
            for (Utilisateur utilisateur : departement.getEmployesDepartement()) {
                if (utilisateur instanceof RH) {
                    RH rh = (RH) utilisateur;
                    // Construire le message de l'email
                    String subject = "Modification de la demande de congé - " + employe.getNomUser() + " " + employe.getPrenomUser();
                    String body = "Bonjour " + rh.getPrenomUser() + ",\n\n" +
                            "La demande de congé de l'employé " + employe.getNomUser() + " " + employe.getPrenomUser() +
                            " a été modifiée.\n\n" +
                            "Nouveaux détails de la demande :\n" +
                            "- Date de début : " + conge.getDateDebut() + "\n" +
                            "- Date de fin : " + conge.getDateFin() + "\n" +
                            "- Statut du congé : " + conge.getStatutConge() + "\n\n" +
                            "Veuillez consulter le système pour plus de détails et traiter les modifications si nécessaire.\n\n" +
                            "Merci,\nL'équipe RH";

                    // Envoyer un email au RH
                    emailService.sendSimpleEmail(rh.getEmail(), subject, body);
                }

    }}
        return modelMapper.map(conge, CongeDto.class);}


    @Override
    @Transactional
    public void reponseDemandeConge(Long congeId, StatutConge nouveauStatut) {
        Conge conge = congeRepo.findById(congeId)
                .orElseThrow(() -> new ResourceNotFoundException("Congé non trouvé pour cet ID : " + congeId));

        Employe employe = (Employe) conge.getEmploye();

        if (nouveauStatut == StatutConge.Valide) {
            long dureeConge = ChronoUnit.DAYS.between(conge.getDateDebut().toInstant(), conge.getDateFin().toInstant());

            if (employe.getSoldeConges() < dureeConge) {
                throw new IllegalStateException("Le solde de congés de l'employé est insuffisant pour valider ce congé.");
            }

            // Mise à jour du solde de congés de l'employé
            employe.setSoldeConges(employe.getSoldeConges() - dureeConge);
            employeRepo.save(employe);
        }

        // Mise à jour du statut du congé
        conge.setStatutConge(nouveauStatut);
        congeRepo.save(conge);
    }

}
