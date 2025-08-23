package en.staduimreg.repository;

import en.staduimreg.entity.Stadium;
import en.staduimreg.entity.StadiumSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StadiumSectionRepository extends JpaRepository<StadiumSection, Long> {

    List<StadiumSection> findByStadium(Stadium stadium);

    List<StadiumSection> findByStadiumOrderBySectionName(Stadium stadium);

    List<StadiumSection> findBySectionType(StadiumSection.SectionType sectionType);

    Optional<StadiumSection> findByStadiumAndSectionName(Stadium stadium, String sectionName);

    @Query("SELECT s FROM StadiumSection s WHERE s.stadium = ?1 AND s.totalSeats >= ?2")
    List<StadiumSection> findByStadiumAndMinSeats(Stadium stadium, Integer minSeats);

    @Query("SELECT SUM(s.totalSeats) FROM StadiumSection s WHERE s.stadium = ?1")
    Integer getTotalCapacityForStadium(Stadium stadium);

    List<StadiumSection> findByStadiumOrderByPositionYAscPositionXAsc(Stadium stadium);
}
