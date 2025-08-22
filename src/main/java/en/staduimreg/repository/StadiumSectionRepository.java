package en.staduimreg.repository;

import en.staduimreg.entity.StadiumSection;
import en.staduimreg.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StadiumSectionRepository extends JpaRepository<StadiumSection, Long> {
    List<StadiumSection> findByStadium(Stadium stadium);
    List<StadiumSection> findByStadiumOrderByPositionYAscPositionXAsc(Stadium stadium);
}
