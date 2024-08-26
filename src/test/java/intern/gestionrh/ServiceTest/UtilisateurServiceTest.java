package intern.gestionrh.ServiceTest;

import intern.gestionrh.Entities.Departement;
import intern.gestionrh.Entities.Utilisateur;
import intern.gestionrh.Entities.Validation;
import intern.gestionrh.Repositories.DepartementRepository;
import intern.gestionrh.Repositories.UtilisateurRepository;
import intern.gestionrh.Repositories.ValidationRepository;
import intern.gestionrh.Services.Impl.EmailService;
import intern.gestionrh.Services.Impl.UtilisateurServiceImpl;
import intern.gestionrh.Services.Impl.ValidationServiceImpl;
import intern.gestionrh.dto.UtilisateurDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UtilisateurServiceTest {

    @Mock
   private ModelMapper modelMapper;
    @Mock
   private PasswordEncoder passwordEncoder;
    @Mock
    private UtilisateurRepository utilisateurRepository;


    @Mock
    private DepartementRepository departementRepository;

    @Mock
    ValidationRepository validationRepository;

    @InjectMocks
    private UtilisateurServiceImpl utilisateurService;

    @Mock
    private ValidationServiceImpl validationService;

    @Mock
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUtilisateur() {
        // Préparer les données de test
        UtilisateurDto utilisateurDto = new UtilisateurDto();
        utilisateurDto.setNomUser("John");
        utilisateurDto.setPrenomUser("jack");
        utilisateurDto.setEmail("john.doe@example.com");


        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUser("John");
        utilisateur.setPrenomUser("jack");
        utilisateur.setEmail("john.doe@example.com");


        when(modelMapper.map(utilisateurDto, Utilisateur.class)).thenReturn(utilisateur);
        when(modelMapper.map(utilisateur, UtilisateurDto.class)).thenReturn(utilisateurDto);

        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);

        // Appeler la méthode à tester
        UtilisateurDto result = utilisateurService.saveUtilisateur(utilisateurDto);

        // Vérifier les résultats
        assertNotNull(result);
        assertEquals("John", result.getNomUser());
        assertEquals("john.doe@example.com", result.getEmail());

        // Vérifier les interactions avec le mock
        verify(utilisateurRepository).save(any(Utilisateur.class));
    }







    @Test
    public void testUpdateUtilisateur_Success() {
        // Arrange
        Long utilisateurId = 1L;
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(utilisateurId);
        utilisateur.setNomUser("OldName");
        utilisateur.setPrenomUser("OldPrenom");
        utilisateur.setEmail("oldemail@example.com");

        UtilisateurDto utilisateurDto = new UtilisateurDto();
        utilisateurDto.setNomUser("NewName");
        utilisateurDto.setPrenomUser("NewPrenom");
        utilisateurDto.setEmail("newemail@example.com");
        utilisateurDto.setDepartementId(2L);

        Departement departement = new Departement();
        departement.setId(2L);
        departement.setNomDepartement("NewDepartement");

        when(utilisateurRepository.findById(utilisateurId)).thenReturn(Optional.of(utilisateur));
        when(departementRepository.findById(2L)).thenReturn(Optional.of(departement));
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);
        when(modelMapper.map(any(Utilisateur.class), eq(UtilisateurDto.class))).thenReturn(utilisateurDto);

        // Act
        UtilisateurDto updatedUtilisateurDto = utilisateurService.updateUtilisateur(utilisateurId, utilisateurDto);

        // Assert
        assertEquals("NewName", utilisateur.getNomUser());
        assertEquals("NewPrenom", utilisateur.getPrenomUser());
        assertEquals("newemail@example.com", utilisateur.getEmail());
        assertEquals(departement, utilisateur.getDepartement());
        assertEquals(utilisateurDto, updatedUtilisateurDto);

        // Verify repository interactions
        verify(utilisateurRepository, times(1)).findById(utilisateurId);
        verify(departementRepository, times(1)).findById(2L);
        verify(utilisateurRepository, times(1)).save(utilisateur);
    }


    @Test
    public void testGetUtilisateursByNomDepartement() {
        // Arrange
        String nomDepartement = "Informatique";

        Departement departement = new Departement();
        departement.setNomDepartement(nomDepartement);

        Utilisateur utilisateur1 = new Utilisateur();
        utilisateur1.setId(1L);
        utilisateur1.setNomUser("User1");
        utilisateur1.setDepartement(departement);

        Utilisateur utilisateur2 = new Utilisateur();
        utilisateur2.setId(2L);
        utilisateur2.setNomUser("User2");
        utilisateur2.setDepartement(departement);

        UtilisateurDto utilisateurDto1 = new UtilisateurDto();
        utilisateurDto1.setNomUser("User1");

        UtilisateurDto utilisateurDto2 = new UtilisateurDto();
        utilisateurDto2.setNomUser("User2");

        when(utilisateurRepository.findByDepartementNom(nomDepartement)).thenReturn(Arrays.asList(utilisateur1, utilisateur2));
        when(modelMapper.map(utilisateur1, UtilisateurDto.class)).thenReturn(utilisateurDto1);
        when(modelMapper.map(utilisateur2, UtilisateurDto.class)).thenReturn(utilisateurDto2);

        // Act
        List<UtilisateurDto> result = utilisateurService.getUtilisateursByNomDepartement(nomDepartement);

        // Assert
        assertEquals(2, ((List<?>) result).size());
        assertEquals("User1", result.get(0).getNomUser());
        assertEquals("User2", result.get(1).getNomUser());

        // Verify repository and mapper interactions
        verify(utilisateurRepository, times(1)).findByDepartementNom(nomDepartement);
        verify(modelMapper, times(1)).map(utilisateur1, UtilisateurDto.class);
        verify(modelMapper, times(1)).map(utilisateur2, UtilisateurDto.class);
    }

    @Test
    public void testLoadUserByUsername_foundUser() {
        // Given

        UtilisateurDto utilisateurDto = new UtilisateurDto();
        utilisateurDto.setNomUser("John");
        utilisateurDto.setPrenomUser("jack");
        utilisateurDto.setEmail("john.doe@example.com");


        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUser("John");
        utilisateur.setPrenomUser("jack");
        utilisateur.setEmail("john.doe@example.com");


        when(modelMapper.map(utilisateurDto, Utilisateur.class)).thenReturn(utilisateur);
        when(modelMapper.map(utilisateur, UtilisateurDto.class)).thenReturn(utilisateurDto);

        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);

        // Appeler la méthode à tester
        UtilisateurDto result = utilisateurService.saveUtilisateur(utilisateurDto);



        when(utilisateurRepository.findByEmail(utilisateur.getEmail())).thenReturn(Optional.of(utilisateur));


        Utilisateur resultt = utilisateurService.loadUserByUsername(utilisateur.getEmail());

        assertNotNull(resultt);

    }



}
