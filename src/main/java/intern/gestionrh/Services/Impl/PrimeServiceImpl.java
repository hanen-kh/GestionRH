package intern.gestionrh.Services.Impl;

import intern.gestionrh.Entities.Prime;
import intern.gestionrh.Entities.Utilisateur;
import intern.gestionrh.Repositories.PrimeRepository;
import intern.gestionrh.Repositories.UtilisateurRepository;
import intern.gestionrh.Services.PrimeService;
import intern.gestionrh.dto.PrimeDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrimeServiceImpl implements PrimeService {
    @Autowired
   private PrimeRepository primeRepo;

    @Autowired
    private UtilisateurRepository utilisateurRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<PrimeDto> findAllPrimes(Long idUtilisateur) {
        List<Prime> primes = primeRepo.findByUtilisateurId(idUtilisateur);

        return primes.stream()
                .map(prime -> modelMapper.map(prime, PrimeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PrimeDto findPrimeById(Long id) {
        Prime prime = primeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prime n'existe pas pour cet id :: " + id));
        return modelMapper.map(prime, PrimeDto.class);
    }

    @Override
    @Transactional
    public PrimeDto savePrime(Long utilisateurId, PrimeDto primeDto) {
        Utilisateur utilisateur = utilisateurRepo.findById(utilisateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé pour cet ID : " + utilisateurId));

        Prime prime = modelMapper.map(primeDto, Prime.class);
        prime.setUtilisateur(utilisateur);
        utilisateur.addPrime(prime);
        utilisateurRepo.save(utilisateur);

        prime = primeRepo.save(prime);
        return modelMapper.map(prime, PrimeDto.class);
    }

    @Override
    public void deletePrimeById(Long id) {
        if (!primeRepo.existsById(id)) {
            throw new ResourceNotFoundException("Prime n'existe pas pour cet id :: " + id);
        }
        primeRepo.deleteById(id);
    }

    @Override
    public PrimeDto updatePrime(Long id, PrimeDto primeDetails) {
        Prime prime = primeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prime n'existe pas pour cet id:: " + id));

        // Mise à jour des informations de la prime
        modelMapper.map(primeDetails, prime);
        Utilisateur utilisateur = utilisateurRepo.findById(primeDetails.getUtilisateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé pour cet ID : " + primeDetails.getUtilisateurId()));
        prime.setUtilisateur(utilisateur);

        prime = primeRepo.save(prime);
        return modelMapper.map(prime, PrimeDto.class);
    }

    @Override
    public void deleteAllPrimes() {
        primeRepo.deleteAll();
    }




}
