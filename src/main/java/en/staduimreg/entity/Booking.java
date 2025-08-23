package en.staduimreg.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private FootballMatch match;

    @Column(name = "booking_date")
    private LocalDateTime bookingDate;

    @NotNull
    @PositiveOrZero
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @NotNull
    @PositiveOrZero
    @Column(name = "total_tickets", nullable = false)
    private Integer totalTickets;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SectionBooking> sectionBookings;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "confirmation_code", unique = true)
    private String confirmationCode;

    @PrePersist
    protected void onCreate() {
        bookingDate = LocalDateTime.now();
        if (confirmationCode == null) {
            confirmationCode = generateConfirmationCode();
        }
    }

    private String generateConfirmationCode() {
        return "BK" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
    }

    public enum BookingStatus {
        PENDING, CONFIRMED, CANCELLED, PAID
    }

    // Constructors
    public Booking() {}

    public Booking(User user, FootballMatch match, BigDecimal totalAmount, Integer totalTickets) {
        this.user = user;
        this.match = match;
        this.totalAmount = totalAmount;
        this.totalTickets = totalTickets;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public FootballMatch getMatch() { return match; }
    public void setMatch(FootballMatch match) { this.match = match; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Integer getTotalTickets() { return totalTickets; }
    public void setTotalTickets(Integer totalTickets) { this.totalTickets = totalTickets; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public Set<SectionBooking> getSectionBookings() { return sectionBookings; }
    public void setSectionBookings(Set<SectionBooking> sectionBookings) { this.sectionBookings = sectionBookings; }

    public String getPaymentReference() { return paymentReference; }
    public void setPaymentReference(String paymentReference) { this.paymentReference = paymentReference; }

    public String getConfirmationCode() { return confirmationCode; }
    public void setConfirmationCode(String confirmationCode) { this.confirmationCode = confirmationCode; }
}
