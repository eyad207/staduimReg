package en.staduimreg.service;

import en.staduimreg.entity.Stadium;
import en.staduimreg.repository.StadiumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StadiumService {

    @Autowired
    private StadiumRepository stadiumRepository;

    public List<Stadium> getAllStadiums() {
        return stadiumRepository.findAll();
    }

    public Optional<Stadium> findById(Long id) {
        return stadiumRepository.findById(id);
    }

    public Stadium save(Stadium stadium) {
        return stadiumRepository.save(stadium);
    }

    public List<Stadium> findByCity(String city) {
        return stadiumRepository.findByCity(city);
    }

    public List<Stadium> searchByName(String name) {
        return stadiumRepository.findByNameContainingIgnoreCase(name);
    }

    public void deleteStadium(Long id) {
        stadiumRepository.deleteById(id);
    }
}
