package intern.gestionrh.Services;

import intern.gestionrh.Entities.Validation;

import java.util.Map;

public interface ValidationService {


    void codeActivation(String username);





    void demandeDeNouveauMotDePasse(Map<String, String> parametres);

    void modifierMotDePasse(Map<String, String> parametres);
}
