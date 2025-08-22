package en.staduimreg.repository;

import en.staduimreg.entity.SectionBooking;
import en.staduimreg.entity.FootballMatch;
import en.staduimreg.entity.StadiumSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SectionBookingRepository extends JpaRepository<SectionBooking, Long> {
    List<SectionBooking> findByFootballMatch(FootballMatch footballMatch);
    Optional<SectionBooking> findByFootballMatchAndStadiumSection(FootballMatch footballMatch, StadiumSection stadiumSection);

    @Query("SELECT sb FROM SectionBooking sb WHERE sb.footballMatch = :match ORDER BY sb.stadiumSection.sectionName")
    List<SectionBooking> findByFootballMatchOrderBySection(@Param("match") FootballMatch footballMatch);
}
