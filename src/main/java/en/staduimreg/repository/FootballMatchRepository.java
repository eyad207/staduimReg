package en.staduimreg.repository;

import en.staduimreg.entity.FootballMatch;
import en.staduimreg.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FootballMatchRepository extends JpaRepository<FootballMatch, Long> {
    List<FootballMatch> findByStadium(Stadium stadium);
    List<FootballMatch> findByMatchDateAfter(LocalDateTime date);
    List<FootballMatch> findByMatchDateBetween(LocalDateTime start, LocalDateTime end);
    List<FootballMatch> findByStatus(FootballMatch.MatchStatus status);

    @Query("SELECT fm FROM FootballMatch fm WHERE fm.availableSeats > 0 AND fm.matchDate > :currentDate ORDER BY fm.matchDate")
    List<FootballMatch> findAvailableMatches(@Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT fm FROM FootballMatch fm WHERE fm.availableSeats >= :requiredSeats AND fm.matchDate > :currentDate ORDER BY fm.matchDate")
    List<FootballMatch> findMatchesWithAvailableSeats(@Param("requiredSeats") int requiredSeats, @Param("currentDate") LocalDateTime currentDate);
}
