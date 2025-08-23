package en.staduimreg.repository;

import en.staduimreg.entity.Booking;
import en.staduimreg.entity.FootballMatch;
import en.staduimreg.entity.SectionBooking;
import en.staduimreg.entity.StadiumSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionBookingRepository extends JpaRepository<SectionBooking, Long> {

    List<SectionBooking> findByBooking(Booking booking);

    List<SectionBooking> findByStadiumSection(StadiumSection stadiumSection);

    @Query("SELECT COALESCE(SUM(sb.ticketsBooked), 0) FROM SectionBooking sb WHERE sb.stadiumSection = ?1 AND sb.booking.match = ?2 AND sb.booking.status IN ('PENDING', 'CONFIRMED', 'PAID')")
    Integer sumTicketsByStadiumSectionAndMatch(StadiumSection stadiumSection, FootballMatch match);

    @Query("SELECT sb FROM SectionBooking sb WHERE sb.booking.match = ?1")
    List<SectionBooking> findByMatch(FootballMatch match);

    @Query("SELECT sb FROM SectionBooking sb WHERE sb.stadiumSection = ?1 AND sb.booking.match = ?2")
    List<SectionBooking> findByStadiumSectionAndMatch(StadiumSection stadiumSection, FootballMatch match);
}
