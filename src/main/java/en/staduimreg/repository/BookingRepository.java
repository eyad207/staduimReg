package en.staduimreg.repository;

import en.staduimreg.entity.Booking;
import en.staduimreg.entity.User;
import en.staduimreg.entity.FootballMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByFootballMatch(FootballMatch footballMatch);
    List<Booking> findByStatus(Booking.BookingStatus status);
    Optional<Booking> findByConfirmationCode(String confirmationCode);
    List<Booking> findByUserOrderByBookingDateDesc(User user);
}
