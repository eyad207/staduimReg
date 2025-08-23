package en.staduimreg.repository;

import en.staduimreg.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Long> {
    Optional<Stadium> findByName(String name);

    List<Stadium> findByCity(String city);

    @Query("SELECT s FROM Stadium s WHERE s.name LIKE %?1% OR s.city LIKE %?1% OR s.address LIKE %?1%")
    List<Stadium> searchStadiums(String searchTerm);

    @Query("SELECT s FROM Stadium s WHERE s.totalCapacity >= ?1")
    List<Stadium> findByMinCapacity(Integer minCapacity);

    boolean existsByName(String name);
}
