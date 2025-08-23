package en.staduimreg.service;

import en.staduimreg.entity.FootballMatch;
import en.staduimreg.entity.Stadium;
import en.staduimreg.repository.FootballMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FootballMatchService {

    @Autowired
    private FootballMatchRepository footballMatchRepository;

    public List<FootballMatch> getAllMatches() {
        return footballMatchRepository.findAll();
    }

    public List<FootballMatch> getAvailableMatches() {
        return footballMatchRepository.findByMatchDateAfterAndStatus(LocalDateTime.now(), FootballMatch.MatchStatus.SCHEDULED);
    }

    public Optional<FootballMatch> findById(Long id) {
        return footballMatchRepository.findById(id);
    }

    public FootballMatch save(FootballMatch match) {
        return footballMatchRepository.save(match);
    }

    public void deleteById(Long id) {
        footballMatchRepository.deleteById(id);
    }

    public void deleteMatch(Long id) {
        footballMatchRepository.deleteById(id);
    }

    public List<FootballMatch> findByStadium(Stadium stadium) {
        return footballMatchRepository.findByStadium(stadium);
    }

    public List<FootballMatch> getUpcomingMatches() {
        return footballMatchRepository.findByMatchDateAfter(LocalDateTime.now());
    }

    public List<FootballMatch> getMatchesByStatus(FootballMatch.MatchStatus status) {
        return footballMatchRepository.findByStatus(status);
    }

    public List<FootballMatch> getMatchesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return footballMatchRepository.findByMatchDateBetween(startDate, endDate);
    }

    public long getTotalMatchesCount() {
        return footballMatchRepository.count();
    }
}
