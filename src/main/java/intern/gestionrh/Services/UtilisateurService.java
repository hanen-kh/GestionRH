package intern.gestionrh.Services;

import intern.gestionrh.Entities.TypeRole;
import intern.gestionrh.Entities.Utilisateur;
import intern.gestionrh.dto.UtilisateurDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

public interface UtilisateurService {




    UtilisateurDto saveUtilisateur(UtilisateurDto utilisateurDto);

    List<UtilisateurDto> getAllUtilisateurs();
    UtilisateurDto getUtilisateurById(Long id);
    List<UtilisateurDto> getUtilisateursByNomDepartement(String nomDepartement);
    void deleteUtilisateurById(Long id);
    UtilisateurDto updateUtilisateur(Long id, UtilisateurDto utilisateurDetails);
    void deleteAllUtilisateurs();

    Utilisateur loadUserByUsername(String username);
}
