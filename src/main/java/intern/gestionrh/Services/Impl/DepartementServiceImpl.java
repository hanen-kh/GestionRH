package intern.gestionrh.Services.Impl;

import intern.gestionrh.Entities.Departement;
import intern.gestionrh.Repositories.DepartementRepository;
import intern.gestionrh.Services.DepartementService;
import intern.gestionrh.dto.DepartementDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartementServiceImpl implements DepartementService {
    @Autowired
    DepartementRepository departementRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public DepartementDto createDepartement(DepartementDto departementDto) {
        Departement departement = modelMapper.map(departementDto, Departement.class);
        departement = departementRepo.save(departement);
        return modelMapper.map(departement, DepartementDto.class);
    }

    @Override
    public DepartementDto getDepartementById(Long id) {
        Departement departement = departementRepo.findById(id).orElse(null);
        return modelMapper.map(departement, DepartementDto.class);
    }

    @Override
    public List<DepartementDto> getAllDepartements() {
        List<Departement> departements = departementRepo.findAll();
        return departements.stream()
                .map(departement -> modelMapper.map(departement, DepartementDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DepartementDto updateDepartement(Long id, DepartementDto departementDto) {
        Departement departementExistant = departementRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Département non trouvé"));

        modelMapper.map(departementDto, departementExistant);
        Departement departementMisAJour = departementRepo.save(departementExistant);
        return modelMapper.map(departementMisAJour, DepartementDto.class);
    }

    @Override
    public void deleteDepartementById(Long id) {
        if (!departementRepo.existsById(id)) {
            throw new ResourceNotFoundException("Département non trouvé pour cet ID : " + id);
        }
        departementRepo.deleteById(id);
    }
}
