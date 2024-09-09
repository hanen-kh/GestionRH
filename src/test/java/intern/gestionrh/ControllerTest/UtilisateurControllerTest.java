package intern.gestionrh.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import intern.gestionrh.Config.JwtService;
import intern.gestionrh.Controllers.UtilisateurController;
import intern.gestionrh.Entities.Departement;
import intern.gestionrh.Services.AdminService;
import intern.gestionrh.Services.Impl.AdminServiceImpl;
import intern.gestionrh.Services.Impl.UtilisateurServiceImpl;
import intern.gestionrh.Services.UtilisateurService;
import intern.gestionrh.dto.AdminDto;
import intern.gestionrh.dto.AuthentificationDto;
import intern.gestionrh.dto.UtilisateurDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)


@AutoConfigureMockMvc(addFilters = true)
public class UtilisateurControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UtilisateurServiceImpl utilisateurService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;
    @MockBean
    private AdminServiceImpl adminService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testDeleteAllUtilisateurs_AuthorizedAsAdmin() throws Exception {
        // Mock the service call to do nothing
        doNothing().when(utilisateurService).deleteAllUtilisateurs();

        // Perform the delete request and expect 204 No Content status
        mockMvc.perform(delete("/api/utilisateur/deleteAll"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testGetUtilisateursByNomDepartement_withAdminRole() throws Exception {
        // Given: Mocking the service layer
        String nomDepartement = "Finance";
        Departement d = new Departement();
        d.setNomDepartement(nomDepartement);
        d.setId(1L);

        List<UtilisateurDto> mockUsers = new ArrayList<>();
        UtilisateurDto u1 = new UtilisateurDto();
        u1.setNomUser("ala");
        u1.setPrenomUser("ali");
        u1.setDepartementId(1L);

        UtilisateurDto u2 = new UtilisateurDto();
        u2.setNomUser("alaa");
        u2.setPrenomUser("aali");
        u2.setDepartementId(1L);

        mockUsers.add(u1);
        mockUsers.add(u2);


        when(utilisateurService.getUtilisateursByNomDepartement(nomDepartement)).thenReturn(mockUsers);

        // When: Performing the GET request
        mockMvc.perform(get("/api/utilisateur/departement/{nomDepartement}", nomDepartement))
                .andExpect(status().isOk())  // Then: Expect a 200 OK status
                .andExpect(jsonPath("$", hasSize(2)))  // Expect two users in the response
                .andExpect(jsonPath("$[0].nomUser").value("ala"))
                .andExpect(jsonPath("$[1].nomUser").value("alaa"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testDeleteUtilisateurById_withAdminRole() throws Exception {
        Long userId = 1L;

        // Mock the service to do nothing when delete is called
        doNothing().when(utilisateurService).deleteUtilisateurById(userId);

        // Perform the DELETE request
        mockMvc.perform(delete("/api/utilisateur/{id}", userId))
                .andExpect(status().isNoContent());  // Expect a 204 No Content status

        // Verify that the service method was called with the correct user ID
        verify(utilisateurService).deleteUtilisateurById(userId);
    }


    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testUpdateUtilisateur_withAdminRole() throws Exception {


        UtilisateurDto utilisateurDto = new UtilisateurDto();
        utilisateurDto.setId(1L);
        utilisateurDto.setNomUser("ala");

        UtilisateurDto updatedUtilisateurDto = new UtilisateurDto();
        updatedUtilisateurDto.setId(1L);
        updatedUtilisateurDto.setNomUser("ala");

        when(utilisateurService.updateUtilisateur(1L, utilisateurDto)).thenReturn(updatedUtilisateurDto);

        // When: Performing the PUT request
        mockMvc.perform(put("/api/utilisateur/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(utilisateurDto)))
                .andExpect(status().isOk())  // Then: Expect a 200 OK status
                .andExpect(jsonPath("$.nomUser").value("ala"));
    }


    @Test
    public void testConnexion() throws Exception {

        String username = "user";
        Map<String, String> tokenMap = Map.of("token", "sample.jwt.token");
        when(jwtService.generate(anyString())).thenReturn(tokenMap);

        // When: Sending a POST request to /connexion
        mockMvc.perform(post("/api/utilisateur/connexion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\"}"))
                // Then: Expecting the response to be OK and contain the JWT token
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("sample.jwt.token"));
    }


    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testCreateAdmin_withAdminRole() throws Exception {

        AdminDto adminDto = new AdminDto();
        adminDto.setNomUser("John");
        adminDto.setPrenomUser("Doe");
        adminDto.setEmail("john.doe@systems.tn");


        when(adminService.saveAdmin(any(AdminDto.class))).thenReturn(adminDto);

        // When: Perform the POST request to /create/admin
        mockMvc.perform(post("/api/utilisateur/create/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(adminDto)))
                .andExpect(status().isCreated())  // Then: Expect a 201 Created status
                .andExpect(jsonPath("$.nomUser").value("John"))
                .andExpect(jsonPath("$.prenomUser").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@systems.tn"));
    }
    }

