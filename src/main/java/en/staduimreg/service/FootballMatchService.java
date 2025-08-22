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
        return footballMatchRepository.findAvailableMatches(LocalDateTime.now());
    }

    public List<FootballMatch> getMatchesWithAvailableSeats(int requiredSeats) {
        return footballMatchRepository.findMatchesWithAvailableSeats(requiredSeats, LocalDateTime.now());
    }

    public Optional<FootballMatch> findById(Long id) {
        return footballMatchRepository.findById(id);
    }

    public FootballMatch save(FootballMatch match) {
        return footballMatchRepository.save(match);
    }

    public List<FootballMatch> findByStadium(Stadium stadium) {
        return footballMatchRepository.findByStadium(stadium);
    }

    public List<FootballMatch> getUpcomingMatches() {
        return footballMatchRepository.findByMatchDateAfter(LocalDateTime.now());
    }

    public boolean hasAvailableSeats(Long matchId, int requestedSeats) {
        Optional<FootballMatch> match = findById(matchId);
        return match.isPresent() && match.get().hasAvailableSeats(requestedSeats);
    }

    public void reserveSeats(Long matchId, int seats) {
        FootballMatch match = footballMatchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        if (!match.hasAvailableSeats(seats)) {
            throw new RuntimeException("Not enough available seats");
        }

        match.reserveSeats(seats);
        footballMatchRepository.save(match);
    }

    public void releaseSeats(Long matchId, int seats) {
        FootballMatch match = footballMatchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        match.releaseSeats(seats);
        footballMatchRepository.save(match);
    }

    public void deleteMatch(Long id) {
        footballMatchRepository.deleteById(id);
    }
}
