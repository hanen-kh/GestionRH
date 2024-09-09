package intern.gestionrh.ServiceTest;

import intern.gestionrh.Entities.*;
import intern.gestionrh.Repositories.CongeRepository;
import intern.gestionrh.Repositories.DepartementRepository;
import intern.gestionrh.Repositories.EmployeRepository;
import intern.gestionrh.Repositories.RhRepository;
import intern.gestionrh.Services.Impl.CongeServiceImpl;
import intern.gestionrh.Services.Impl.EmailService;
import intern.gestionrh.dto.CongeDto;
import intern.gestionrh.dto.EmployeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CongeServiceTest {
    @Mock
    private CongeRepository congeRepo;

    @Mock
    private EmployeRepository employeRepo;

    @Mock
    private DepartementRepository departementReo;

    @Mock
    private RhRepository rhRepo;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private CongeServiceImpl congeService;

    private RH rh;
    private Employe employe;
    private Departement departement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Créer un département
        departement = new Departement();
        departement.setId(1L);
        departement.setNomDepartement("Informatique");
        departementReo.save(departement);

        // Créer un RH
        rh = new RH();
        rh.setId(1L);
        rh.setEmail("rh@gmail.com");
        rh.setNomUser("Dali");
        rh.setPrenomUser("Jaan");
        rh.setDepartement(departement);
        rhRepo.save(rh);


        // Créer un employé
        employe = new Employe();
        employe.setId(3L);
        employe.setEmail("employe@gmail.com");
        employe.setNomUser("Mohammed");
        employe.setPrenomUser("ali");
        employe.setDepartement(departement);
        employe.setSoldeConges(10);
        employeRepo.save(employe);

        // Ajouter le RH et l'employé au département
        departement.getEmployesDepartement().add((Utilisateur) rh);
        departement.getEmployesDepartement().add((Utilisateur) employe);
    }

    @Test
    void testUpdateConge() {


        Conge conge = new Conge();
        conge.setId(1L);
        conge.setEmploye(employe);
        conge.setDateDebut(new Date());
        conge.setDateFin(new Date(System.currentTimeMillis() + 172800000)); // 2 jours plus tard

        CongeDto congeDto = new CongeDto();
        congeDto.setEmployeId(employe.getId());
        congeDto.setDateDebut(new Date());
        congeDto.setDateFin(new Date(System.currentTimeMillis() + 172800000)); // 2 jours plus tard

        // Configurer les mocks
        when(congeRepo.findById(anyLong())).thenReturn(Optional.of(conge));
        when(modelMapper.map(congeDto, Conge.class)).thenReturn(conge);
        when(congeRepo.save(conge)).thenReturn(conge);

        // Appeler la méthode
        CongeDto result = congeService.updateConge(1L, congeDto);

        // Vérifier
        assertNotNull(result);
        verify(emailService).sendSimpleEmail(eq("rh@gmail.com"), anyString(), anyString());
    }

    @Test
    void testReponseDemandeCongeSoldeInsuffisant() {
        // Préparer les objets
        Conge conge = new Conge();
        conge.setId(1L);
        conge.setDateDebut(new Date(System.currentTimeMillis() - 86400000)); // 1 jour
        conge.setDateFin(new Date(System.currentTimeMillis())); // Aujourd'hui
        conge.setStatutConge(StatutConge.EnAttente);


        employe.setSoldeConges(0L); // Solde insuffisant

        conge.setEmploye(employe);
        congeRepo.save(conge);

        // Configurer les mocks
        when(congeRepo.findById(anyLong())).thenReturn(Optional.of(conge));

        // Appeler la méthode et vérifier l'exception
        assertThrows(IllegalStateException.class, () -> {
            congeService.reponseDemandeConge(1L, StatutConge.Valide);
        });}


        @Test
        void testFindCongesByEmployeId() {


            // Création de quelques congés
            Conge conge1 = new Conge();
            conge1.setId(101L);
            conge1.setEmploye(employe);

            Conge conge2 = new Conge();
            conge2.setId(102L);
            conge2.setEmploye(employe);


            Set<Conge> congeSet = new HashSet<>();
            congeSet.add(conge1);
            congeSet.add(conge2);
            employe.setConges(congeSet);


            when(employeRepo.findById(1L)).thenReturn(Optional.of(employe));


            // Appeler la méthode à tester
            Set<Conge> congesDtoList = congeService.findCongesByEmployeId(1L);

            // Vérifications
            assertNotNull(congesDtoList);
            assertEquals(2, congesDtoList.size());

            verify(employeRepo).findById(1L);

        }





}
