package en.staduimreg.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "football_match_id", nullable = false)
    private FootballMatch footballMatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_section_id")
    private StadiumSection stadiumSection;

    @Positive
    @Column(name = "number_of_seats", nullable = false)
    private Integer numberOfSeats;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.CONFIRMED;

    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;

    @Column(name = "confirmation_code", unique = true)
    private String confirmationCode;

    @PrePersist
    protected void onCreate() {
        bookingDate = LocalDateTime.now();
        generateConfirmationCode();
        calculateTotalPrice();
    }

    private void generateConfirmationCode() {
        this.confirmationCode = "FB" + System.currentTimeMillis() +
                               String.format("%03d", (int)(Math.random() * 1000));
    }

    private void calculateTotalPrice() {
        if (footballMatch != null && numberOfSeats != null) {
            BigDecimal basePrice = footballMatch.getTicketPrice();
            if (stadiumSection != null) {
                basePrice = basePrice.multiply(BigDecimal.valueOf(stadiumSection.getPriceMultiplier()));
            }
            this.totalPrice = basePrice.multiply(BigDecimal.valueOf(numberOfSeats));
        }
    }

    public enum BookingStatus {
        CONFIRMED, CANCELLED, PENDING
    }

    // Constructors
    public Booking() {}

    public Booking(User user, FootballMatch footballMatch, Integer numberOfSeats) {
        this.user = user;
        this.footballMatch = footballMatch;
        this.numberOfSeats = numberOfSeats;
    }

    public Booking(User user, FootballMatch footballMatch, StadiumSection stadiumSection, Integer numberOfSeats) {
        this.user = user;
        this.footballMatch = footballMatch;
        this.stadiumSection = stadiumSection;
        this.numberOfSeats = numberOfSeats;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public FootballMatch getFootballMatch() { return footballMatch; }
    public void setFootballMatch(FootballMatch footballMatch) { this.footballMatch = footballMatch; }

    public StadiumSection getStadiumSection() { return stadiumSection; }
    public void setStadiumSection(StadiumSection stadiumSection) {
        this.stadiumSection = stadiumSection;
        calculateTotalPrice();
    }

    public Integer getNumberOfSeats() { return numberOfSeats; }
    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
        calculateTotalPrice();
    }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public String getConfirmationCode() { return confirmationCode; }
    public void setConfirmationCode(String confirmationCode) { this.confirmationCode = confirmationCode; }
}
