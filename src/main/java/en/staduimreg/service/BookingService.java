package en.staduimreg.service;

import en.staduimreg.entity.*;
import en.staduimreg.repository.BookingRepository;
import en.staduimreg.repository.SectionBookingRepository;
import en.staduimreg.repository.StadiumSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SectionBookingRepository sectionBookingRepository;
    private final StadiumSectionRepository stadiumSectionRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                         SectionBookingRepository sectionBookingRepository,
                         StadiumSectionRepository stadiumSectionRepository) {
        this.bookingRepository = bookingRepository;
        this.sectionBookingRepository = sectionBookingRepository;
        this.stadiumSectionRepository = stadiumSectionRepository;
    }

    public Booking createBooking(User user, FootballMatch match, Map<Long, Integer> sectionTickets) {
        // Calculate total amount and tickets
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalTickets = 0;

        for (Map.Entry<Long, Integer> entry : sectionTickets.entrySet()) {
            Long sectionId = entry.getKey();
            Integer tickets = entry.getValue();

            StadiumSection section = stadiumSectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Stadium section not found"));

            // Base price calculation (you might want to add a base price field to matches)
            BigDecimal basePrice = BigDecimal.valueOf(50.00); // Default base price
            BigDecimal sectionPrice = basePrice.multiply(BigDecimal.valueOf(section.getPriceMultiplier()));
            BigDecimal sectionTotal = sectionPrice.multiply(BigDecimal.valueOf(tickets));

            totalAmount = totalAmount.add(sectionTotal);
            totalTickets += tickets;
        }

        // Create main booking
        Booking booking = new Booking(user, match, totalAmount, totalTickets);
        booking.setStatus(Booking.BookingStatus.PENDING);
        booking = bookingRepository.save(booking);

        // Create section bookings
        for (Map.Entry<Long, Integer> entry : sectionTickets.entrySet()) {
            Long sectionId = entry.getKey();
            Integer tickets = entry.getValue();

            StadiumSection section = stadiumSectionRepository.findById(sectionId).orElseThrow();
            BigDecimal basePrice = BigDecimal.valueOf(50.00);
            BigDecimal sectionPrice = basePrice.multiply(BigDecimal.valueOf(section.getPriceMultiplier()));

            SectionBooking sectionBooking = new SectionBooking(booking, section, tickets, sectionPrice);
            sectionBookingRepository.save(sectionBooking);
        }

        return booking;
    }

    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> findBookingsByUser(User user) {
        return bookingRepository.findByUserOrderByBookingDateDesc(user);
    }

    public List<Booking> findBookingsByMatch(FootballMatch match) {
        return bookingRepository.findByMatchOrderByBookingDateDesc(match);
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking updateBookingStatus(Long bookingId, Booking.BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public long getTotalBookingsCount() {
        return bookingRepository.count();
    }

    public BigDecimal getTotalRevenue() {
        return bookingRepository.findAll().stream()
            .filter(booking -> booking.getStatus() == Booking.BookingStatus.PAID ||
                             booking.getStatus() == Booking.BookingStatus.CONFIRMED)
            .map(Booking::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Booking> findRecentBookings(int limit) {
        return bookingRepository.findTop10ByOrderByBookingDateDesc();
    }

    public BigDecimal calculateRevenueForMatch(FootballMatch match) {
        return bookingRepository.findByMatchOrderByBookingDateDesc(match).stream()
            .filter(booking -> booking.getStatus() == Booking.BookingStatus.PAID ||
                             booking.getStatus() == Booking.BookingStatus.CONFIRMED)
            .map(Booking::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getAvailableSeatsForSection(StadiumSection section, FootballMatch match) {
        Integer bookedSeats = sectionBookingRepository.sumTicketsByStadiumSectionAndMatch(section, match);
        return section.getTotalSeats() - (bookedSeats != null ? bookedSeats : 0);
    }

    public List<Booking> getUserBookings(User user) {
        return findBookingsByUser(user);
    }

    public void cancelBooking(Long bookingId) {
        Booking booking = findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    public Optional<Booking> findByConfirmationCode(String confirmationCode) {
        return bookingRepository.findByPaymentReference(confirmationCode);
    }

    public Booking createBookingWithSection(User user, FootballMatch match, StadiumSection section, Integer tickets) {
        Map<Long, Integer> sectionTickets = Map.of(section.getId(), tickets);
        return createBooking(user, match, sectionTickets);
    }
}
