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

    public List<SectionBooking> getSectionBookingsForMatch(FootballMatch match) {
        return sectionBookingRepository.findByFootballMatchOrderBySection(match);
    }

    public SectionBooking getOrCreateSectionBooking(FootballMatch match, StadiumSection section) {
        return sectionBookingRepository.findByFootballMatchAndStadiumSection(match, section)
                .orElseGet(() -> {
                    SectionBooking sectionBooking = new SectionBooking(match, section);
                    return sectionBookingRepository.save(sectionBooking);
                });
    }

    public boolean canBookSeatsInSection(FootballMatch match, StadiumSection section, int requestedSeats) {
        SectionBooking sectionBooking = getOrCreateSectionBooking(match, section);
        return sectionBooking.hasAvailableSeats(requestedSeats);
    }

    public void bookSeatsInSection(FootballMatch match, StadiumSection section, int seats) {
        SectionBooking sectionBooking = getOrCreateSectionBooking(match, section);
        sectionBooking.bookSeats(seats);
        sectionBookingRepository.save(sectionBooking);
    }

    public void releaseSeatsInSection(FootballMatch match, StadiumSection section, int seats) {
        Optional<SectionBooking> sectionBookingOpt =
            sectionBookingRepository.findByFootballMatchAndStadiumSection(match, section);

        if (sectionBookingOpt.isPresent()) {
            SectionBooking sectionBooking = sectionBookingOpt.get();
            sectionBooking.releaseSeats(seats);
            sectionBookingRepository.save(sectionBooking);
        }
    }

    public int getAvailableSeatsInSection(FootballMatch match, StadiumSection section) {
        SectionBooking sectionBooking = getOrCreateSectionBooking(match, section);
        return sectionBooking.getAvailableSeats();
    }

    public void initializeSectionBookingsForMatch(FootballMatch match) {
        List<StadiumSection> sections = getSectionsByStadium(match.getStadium());
        for (StadiumSection section : sections) {
            getOrCreateSectionBooking(match, section);
        }
    }
}
