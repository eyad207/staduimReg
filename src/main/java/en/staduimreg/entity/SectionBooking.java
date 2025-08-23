package en.staduimreg.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Entity
@Table(name = "section_bookings")
public class SectionBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_section_id", nullable = false)
    private StadiumSection stadiumSection;

    @NotNull
    @PositiveOrZero
    @Column(name = "tickets_booked", nullable = false)
    private Integer ticketsBooked;

    @NotNull
    @PositiveOrZero
    @Column(name = "price_per_ticket", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerTicket;

    @NotNull
    @PositiveOrZero
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    // Constructors
    public SectionBooking() {}

    public SectionBooking(Booking booking, StadiumSection stadiumSection, Integer ticketsBooked, BigDecimal pricePerTicket) {
        this.booking = booking;
        this.stadiumSection = stadiumSection;
        this.ticketsBooked = ticketsBooked;
        this.pricePerTicket = pricePerTicket;
        this.totalPrice = pricePerTicket.multiply(BigDecimal.valueOf(ticketsBooked));
    }

    // Getters and Setters - keeping only the essential ones used in the codebase
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    // Adding getters to fix "assigned but never accessed" warnings
    public StadiumSection getStadiumSection() { return stadiumSection; }
    public Integer getTicketsBooked() { return ticketsBooked; }
    public BigDecimal getPricePerTicket() { return pricePerTicket; }
    public BigDecimal getTotalPrice() { return totalPrice; }
}
