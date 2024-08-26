package intern.gestionrh.ServiceTest;

import intern.gestionrh.Entities.RH;
import intern.gestionrh.Entities.Role;
import intern.gestionrh.Entities.TypeRole;
import intern.gestionrh.Entities.Utilisateur;
import intern.gestionrh.Repositories.RhRepository;
import intern.gestionrh.Repositories.RoleRepository;
import intern.gestionrh.Services.Impl.EmailService;
import intern.gestionrh.Services.Impl.RhServiceImpl;
import intern.gestionrh.dto.RhDto;
import intern.gestionrh.dto.UtilisateurDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RhServiceTest {
    @InjectMocks
    private RhServiceImpl rhService;

    @Mock
    private RhRepository rhRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testSaveRH() {
        // Arrange
        RhDto rhDto = new RhDto();
        rhDto.setNomUser("John");
        rhDto.setPrenomUser("jack");
        rhDto.setEmail("john.doe@example.com");


        RH rh = new RH();
        rh.setNomUser("John");
        rh.setPrenomUser("jack");
        rh.setEmail("john.doe@example.com");
        Role rhRole = new Role();
        rhRole.setLibelle(TypeRole.RH);
        roleRepository.save(rhRole);


        when(modelMapper.map(rhDto, RH.class)).thenReturn(rh);
        when(modelMapper.map(rh, RhDto.class)).thenReturn(rhDto);

        when(rhRepository.save(any(RH.class))).thenReturn(rh);

        when(roleRepository.findByLibelle(TypeRole.RH)).thenReturn(Optional.of(rhRole));

        RhDto resultat=rhService.saveRH(rhDto);

    }
}
