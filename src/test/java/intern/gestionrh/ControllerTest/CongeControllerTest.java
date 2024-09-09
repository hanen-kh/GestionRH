package intern.gestionrh.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import intern.gestionrh.Controllers.CongeController;
import intern.gestionrh.Entities.Conge;
import intern.gestionrh.Entities.StatutConge;
import intern.gestionrh.Entities.TypeConge;
import intern.gestionrh.Repositories.CongeRepository;
import intern.gestionrh.Services.CongeService;
import intern.gestionrh.dto.CongeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.modelmapper.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest

@ExtendWith(MockitoExtension.class)

@AutoConfigureMockMvc(addFilters = true)
public class CongeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    CongeRepository congeRepo;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private CongeService congeService;

    @InjectMocks
    CongeController congeController;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }




        @Test
        @WithMockUser(authorities= {"ROLE_RH", "ROLE_EMPLOYE"})
        void testFindCongesByEmployeId() throws Exception {
            // Préparer des données de test
            Set<Conge> conges = new HashSet<>();
            Conge conge1 = new Conge();
            conge1.setId(101L);
            Conge conge2 = new Conge();
            conge2.setId(102L);
            conges.add(conge1);
            conges.add(conge2);

            // Configurer le mock
            when(congeService.findCongesByEmployeId(anyLong())).thenReturn(conges);

            // Effectuer la requête et vérifier la réponse
            ResponseEntity<Set<Conge>> response = congeController.findCongesByEmployeId(1L);

            // Verifier
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(conges, response.getBody());
        }
    @Test
    @WithMockUser(authorities = "ROLE_EMPLOYE")
    public void testUpdateConge() {
        // Créer un congé existant
        Conge conge = new Conge();
        conge.setId(1L);
        conge.setDateDebut(new Date(System.currentTimeMillis() - 86400000)); // 1 jour
        conge.setDateFin(new Date(System.currentTimeMillis()));
        congeRepo.save(conge);

        // Créer un CongeDto avec les nouvelles données
        CongeDto congeDto = new CongeDto();
        congeDto.setDateDebut(new Date(System.currentTimeMillis() -2* 86400000));
        congeDto.setDateFin(new Date(System.currentTimeMillis()));

        // Simuler le comportement du service de congé
        when(congeService.updateConge(1L, congeDto)).thenReturn(congeDto);

        // Appeler la méthode à tester
        ResponseEntity<CongeDto> response = congeController.updateConge(1L, congeDto);

        // Vérifications
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());


    }
    @Test
    @WithMockUser(authorities = "ROLE_RH")

    void reponseDemandeConge() {
        // Given
        Long congeId = 1L;
        Conge conge = new Conge();
        conge.setId(congeId);
        StatutConge nouveauStatut = StatutConge.Valide;

        // When
        ResponseEntity<String> response = congeController.reponseDemandeConge(congeId, nouveauStatut);

        // Then
        verify(congeService).reponseDemandeConge(congeId, nouveauStatut);
        assertEquals(ResponseEntity.ok("Réponse à la demande de congé traitée avec succès."), response);
    }
    }








