package en.staduimreg.service;

import en.staduimreg.entity.Booking;
import en.staduimreg.entity.FootballMatch;
import en.staduimreg.entity.User;
import en.staduimreg.entity.StadiumSection;
import en.staduimreg.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FootballMatchService footballMatchService;

    @Autowired
    private StadiumSectionService stadiumSectionService;

    // Original booking method (without section selection)
    public Booking createBooking(User user, FootballMatch footballMatch, int numberOfSeats) {
        // Check if enough seats are available
        if (!footballMatch.hasAvailableSeats(numberOfSeats)) {
            throw new RuntimeException("Not enough available seats. Available: " + footballMatch.getAvailableSeats());
        }

        // Reserve seats in the match
        footballMatchService.reserveSeats(footballMatch.getId(), numberOfSeats);

        // Create and save booking
        Booking booking = new Booking(user, footballMatch, numberOfSeats);
        return bookingRepository.save(booking);
    }

    // New booking method with section selection
    public Booking createBookingWithSection(User user, FootballMatch footballMatch, StadiumSection stadiumSection, int numberOfSeats) {
        // Check if enough seats are available in the specific section
        if (!stadiumSectionService.canBookSeatsInSection(footballMatch, stadiumSection, numberOfSeats)) {
            int available = stadiumSectionService.getAvailableSeatsInSection(footballMatch, stadiumSection);
            throw new RuntimeException("Not enough available seats in " + stadiumSection.getSectionName() +
                                     ". Available: " + available);
        }

        // Reserve seats in the section
        stadiumSectionService.bookSeatsInSection(footballMatch, stadiumSection, numberOfSeats);

        // Also update the overall match seat count
        footballMatchService.reserveSeats(footballMatch.getId(), numberOfSeats);

        // Create and save booking with section information
        Booking booking = new Booking(user, footballMatch, stadiumSection, numberOfSeats);
        return bookingRepository.save(booking);
    }

    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }

        // Release seats back to the match
        footballMatchService.releaseSeats(booking.getFootballMatch().getId(), booking.getNumberOfSeats());

        // If booking has a section, release seats from that section too
        if (booking.getStadiumSection() != null) {
            stadiumSectionService.releaseSeatsInSection(
                booking.getFootballMatch(),
                booking.getStadiumSection(),
                booking.getNumberOfSeats()
            );
        }

        // Update booking status
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(User user) {
        return bookingRepository.findByUserOrderByBookingDateDesc(user);
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    public Optional<Booking> findByConfirmationCode(String confirmationCode) {
        return bookingRepository.findByConfirmationCode(confirmationCode);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByMatch(FootballMatch footballMatch) {
        return bookingRepository.findByFootballMatch(footballMatch);
    }

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }
}
