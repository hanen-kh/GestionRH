package intern.gestionrh.Services;

import intern.gestionrh.dto.PrimeDto;

import java.util.List;

public interface PrimeService {


    List<PrimeDto> findAllPrimes(Long idUtilisateur);

    PrimeDto findPrimeById(Long id);
    PrimeDto savePrime(Long utilisateurId, PrimeDto primeDto);
    PrimeDto updatePrime(Long id, PrimeDto primeDto);
    void deletePrimeById(Long id);
    void deleteAllPrimes();}
