package en.staduimreg.service;

import en.staduimreg.entity.*;
import en.staduimreg.repository.StadiumSectionRepository;
import en.staduimreg.repository.SectionBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StadiumSectionService {

    @Autowired
    private StadiumSectionRepository stadiumSectionRepository;

    @Autowired
    private SectionBookingRepository sectionBookingRepository;

    public List<StadiumSection> getSectionsByStadium(Stadium stadium) {
        return stadiumSectionRepository.findByStadiumOrderByPositionYAscPositionXAsc(stadium);
    }

    public Optional<StadiumSection> findById(Long id) {
        return stadiumSectionRepository.findById(id);
    }

    public StadiumSection save(StadiumSection section) {
        return stadiumSectionRepository.save(section);
    }

    public List<StadiumSection> findByStadium(Stadium stadium) {
        return stadiumSectionRepository.findByStadium(stadium);
    }

    public List<StadiumSection> getAllSections() {
        return stadiumSectionRepository.findAll();
    }

    public void deleteSection(Long id) {
        stadiumSectionRepository.deleteById(id);
    }

    public int getAvailableSeatsForSection(StadiumSection section, FootballMatch match) {
        Integer bookedSeats = sectionBookingRepository.sumTicketsByStadiumSectionAndMatch(section, match);
        return section.getTotalSeats() - (bookedSeats != null ? bookedSeats : 0);
    }

    public List<SectionBooking> getSectionBookingsForMatch(FootballMatch match) {
        return sectionBookingRepository.findByMatch(match);
    }

    public int getAvailableSeatsInSection(FootballMatch match, StadiumSection section) {
        return getAvailableSeatsForSection(section, match);
    }

    public void initializeSectionBookingsForMatch(FootballMatch match) {
        // This method is simplified - section bookings are created on-demand when actual bookings are made
        // No need to pre-create them
    }
}
