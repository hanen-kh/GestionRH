package intern.gestionrh.ServiceTest;

import intern.gestionrh.Entities.*;
import intern.gestionrh.Repositories.*;
import intern.gestionrh.Services.EmployeService;
import intern.gestionrh.Services.Impl.EmailService;
import intern.gestionrh.Services.Impl.EmployeServiceImpl;
import intern.gestionrh.dto.CongeDto;
import intern.gestionrh.dto.EmployeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest

public class EmployeServiceTest {
    @InjectMocks
    private EmployeServiceImpl employeService;
    @Mock
    EmployeRepository employeRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CongeRepository congeRepository;

    @Mock
    private UtilisateurRepository utilisateurRepo;

    @Mock
    RoleRepository roleRepository;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);


    }

@Test
    public void testSaveEmploye(){

        EmployeDto employeDto=new EmployeDto();
        employeDto.setNomUser("adem");
        employeDto.setPrenomUser("ali");
    employeDto.setEmail("khmileth@gmail.com");

    Role employeRole = new Role();
    employeRole.setLibelle(TypeRole.EMPLOYE);
    roleRepository.save(employeRole);


        Employe employe = new Employe();
        employe.setNomUser("adem");
        employe.setPrenomUser("ali");
        employe.setEmail("khmileth@gmail.com");

       when(modelMapper.map(employe,EmployeDto.class)).thenReturn(employeDto);
       when(modelMapper.map(employeDto,Employe.class)).thenReturn(employe);
       when(employeRepository.save(any(Employe.class))).thenReturn(employe);

    when(roleRepository.findByLibelle(TypeRole.EMPLOYE)).thenReturn(Optional.of(employeRole));

       EmployeDto e= employeService.saveEmploye(employeDto);
       assertNotNull(e);

}

}
