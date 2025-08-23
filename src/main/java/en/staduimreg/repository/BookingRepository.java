package en.staduimreg.repository;

import en.staduimreg.entity.Booking;
import en.staduimreg.entity.FootballMatch;
import en.staduimreg.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserOrderByBookingDateDesc(User user);

    List<Booking> findByMatchOrderByBookingDateDesc(FootballMatch match);

    List<Booking> findByStatus(Booking.BookingStatus status);

    @Query("SELECT b FROM Booking b ORDER BY b.bookingDate DESC")
    List<Booking> findTop10ByOrderByBookingDateDesc();

    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.status IN ('PAID', 'CONFIRMED')")
    BigDecimal getTotalRevenue();

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = ?1")
    long countByStatus(Booking.BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.bookingDate BETWEEN ?1 AND ?2")
    List<Booking> findBookingsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.match = ?1 AND b.status IN ('PAID', 'CONFIRMED')")
    BigDecimal getRevenueForMatch(FootballMatch match);

    Optional<Booking> findByPaymentReference(String paymentReference);

    Optional<Booking> findByConfirmationCode(String confirmationCode);
}
