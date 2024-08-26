package intern.gestionrh.RepositoryTest;

import intern.gestionrh.Entities.Departement;
import intern.gestionrh.Entities.Utilisateur;
import intern.gestionrh.Repositories.DepartementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@DataJpaTest


public class DepartementRepositoryTest {

    @Autowired
    private DepartementRepository departementRepo;


    private Departement departement1;
    private Departement departement2;


    private Utilisateur utilisateur1;
    private Utilisateur utilisateur2;

    @BeforeEach
    void setUp() {
        // Créer deux départements
        departement1 = new Departement();
        departement1.setNomDepartement("Informatique");

        departement2 = new Departement();
        departement2.setNomDepartement("Ressources Humaines");
    }
    @Test
    void testSaveDepartement() {
        Departement savedDepartement1 = departementRepo.save(departement1);
        Long id1 = savedDepartement1.getId();
        assertNotNull(id1);
        assertEquals("Informatique", savedDepartement1.getNomDepartement());
        Departement savedDepartement2 = departementRepo.save(departement2);
        Long id2 = savedDepartement1.getId();
        assertNotNull(id2);
    }

    @Test
    void testFindAllDepartements() {
        // Sauvegarder deux départements
        departementRepo.save(departement1);
        departementRepo.save(departement2);

        // Récupérer tous les départements
        List<Departement> departements = departementRepo.findAll();

        // Vérifier que deux départements sont récupérés
        assertEquals(2, departements.size());
    }

    @Test
    void testFindDepartementById() {
        Departement savedDepartement = departementRepo.save(departement1);

        // Récupérer le département par ID
        Optional<Departement> foundDepartement = departementRepo.findById(savedDepartement.getId());

        // Vérifier que le département est trouvé
        assertTrue(foundDepartement.isPresent());
        assertEquals("Informatique", foundDepartement.get().getNomDepartement());
    }
    @Test
    void testUpdateDepartement() {

        Departement savedDepartement = departementRepo.save(departement1);

        // Mettre à jour le nom du département
        savedDepartement.setNomDepartement("IT");
        Departement updatedDepartement = departementRepo.save(savedDepartement);

        // Vérifier que le nom du département est mis à jour
        assertEquals("IT", updatedDepartement.getNomDepartement());
    }

    @Test
    void testDeleteDepartement() {

        Departement savedDepartement = departementRepo.save(departement1);

        // Supprimer le département par ID
        departementRepo.deleteById(savedDepartement.getId());

        // Vérifier que le département n'existe plus
        Optional<Departement> foundDepartement = departementRepo.findById(savedDepartement.getId());
        assertFalse(foundDepartement.isPresent(), "Le département devrait être supprimé");
    }
}
