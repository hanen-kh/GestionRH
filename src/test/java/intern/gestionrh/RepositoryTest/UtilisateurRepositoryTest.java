package intern.gestionrh.RepositoryTest;

import intern.gestionrh.Entities.Utilisateur;
import intern.gestionrh.Repositories.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@DataJpaTest
public class UtilisateurRepositoryTest {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private Utilisateur utilisateur1;
    private Utilisateur utilisateur2;

    @BeforeEach
    void setUp() {
        utilisateur1 = new Utilisateur();
        utilisateur1.setNomUser("Dali");
        utilisateur1.setPrenomUser("Jiji");
        utilisateur1.setEmail("Dali@gmail.com");

        utilisateur2 = new Utilisateur();
        utilisateur2.setNomUser("Sam");
        utilisateur2.setPrenomUser("Anna");
        utilisateur2.setEmail("sam@gmail.com");
    }

    @Test
    void testSaveUtilisateur() {
        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur1);
        assertNotNull(savedUtilisateur.getId());
    }

    @Test
    void testFindUtilisateurById() {
        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur1);
        Optional<Utilisateur> foundUtilisateur = utilisateurRepository.findById(savedUtilisateur.getId());
        assertTrue(foundUtilisateur.isPresent());
        assertEquals(savedUtilisateur.getNomUser(), foundUtilisateur.get().getNomUser());
    }

    @Test
    void testUpdateUtilisateur() {
        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur1);
        savedUtilisateur.setNomUser("mohammed");
        utilisateurRepository.save(savedUtilisateur);
        Optional<Utilisateur> updatedUtilisateur = utilisateurRepository.findById(savedUtilisateur.getId());
        assertTrue(updatedUtilisateur.isPresent());
        assertEquals("mohammed", updatedUtilisateur.get().getNomUser());
    }

    @Test
    void testDeleteUtilisateurById() {
        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur1);
        utilisateurRepository.deleteById(savedUtilisateur.getId());
        Optional<Utilisateur> foundUtilisateur = utilisateurRepository.findById(savedUtilisateur.getId());
        assertFalse(foundUtilisateur.isPresent());
    }

    @Test
    void testFindAllUtilisateurs() {
        utilisateurRepository.save(utilisateur1);
        utilisateurRepository.save(utilisateur2);
        Iterable<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        assertNotNull(utilisateurs);
        assertTrue(utilisateurs.iterator().hasNext());
    }
}
