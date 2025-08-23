package en.staduimreg.repository;

import en.staduimreg.entity.FootballMatch;
import en.staduimreg.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FootballMatchRepository extends JpaRepository<FootballMatch, Long> {

    List<FootballMatch> findByStadium(Stadium stadium);

    List<FootballMatch> findByStatus(FootballMatch.MatchStatus status);

    List<FootballMatch> findByMatchDateAfter(LocalDateTime dateTime);

    List<FootballMatch> findByMatchDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<FootballMatch> findByMatchDateAfterAndStatus(LocalDateTime dateTime, FootballMatch.MatchStatus status);

    @Query("SELECT m FROM FootballMatch m WHERE m.homeTeam LIKE %?1% OR m.awayTeam LIKE %?1%")
    List<FootballMatch> searchByTeamName(String teamName);
}
