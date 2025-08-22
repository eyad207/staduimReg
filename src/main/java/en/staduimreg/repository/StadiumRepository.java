package en.staduimreg.repository;

import en.staduimreg.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Long> {
    List<Stadium> findByCity(String city);
    List<Stadium> findByNameContainingIgnoreCase(String name);
}
