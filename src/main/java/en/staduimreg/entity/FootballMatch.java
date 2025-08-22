package en.staduimreg.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "football_matches")
public class FootballMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "home_team", nullable = false)
    private String homeTeam;

    @NotBlank
    @Column(name = "away_team", nullable = false)
    private String awayTeam;

    @NotNull
    @Column(name = "match_date", nullable = false)
    private LocalDateTime matchDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id", nullable = false)
    private Stadium stadium;

    @PositiveOrZero
    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @NotNull
    @Column(name = "ticket_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal ticketPrice;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status = MatchStatus.SCHEDULED;

    @OneToMany(mappedBy = "footballMatch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Booking> bookings;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (availableSeats == null && stadium != null) {
            availableSeats = stadium.getTotalCapacity();
        }
    }

    public enum MatchStatus {
        SCHEDULED, LIVE, FINISHED, CANCELLED
    }

    // Constructors
    public FootballMatch() {}

    public FootballMatch(String homeTeam, String awayTeam, LocalDateTime matchDate, Stadium stadium, BigDecimal ticketPrice) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchDate = matchDate;
        this.stadium = stadium;
        this.ticketPrice = ticketPrice;
        this.availableSeats = stadium.getTotalCapacity();
    }

    // Helper methods
    public boolean hasAvailableSeats(int requestedSeats) {
        return availableSeats >= requestedSeats;
    }

    public void reserveSeats(int seats) {
        if (hasAvailableSeats(seats)) {
            this.availableSeats -= seats;
        } else {
            throw new IllegalStateException("Not enough available seats");
        }
    }

    public void releaseSeats(int seats) {
        this.availableSeats += seats;
        if (this.availableSeats > stadium.getTotalCapacity()) {
            this.availableSeats = stadium.getTotalCapacity();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }

    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }

    public LocalDateTime getMatchDate() { return matchDate; }
    public void setMatchDate(LocalDateTime matchDate) { this.matchDate = matchDate; }

    public Stadium getStadium() { return stadium; }
    public void setStadium(Stadium stadium) { this.stadium = stadium; }

    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }

    public BigDecimal getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(BigDecimal ticketPrice) { this.ticketPrice = ticketPrice; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public MatchStatus getStatus() { return status; }
    public void setStatus(MatchStatus status) { this.status = status; }

    public Set<Booking> getBookings() { return bookings; }
    public void setBookings(Set<Booking> bookings) { this.bookings = bookings; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getMatchTitle() {
        return homeTeam + " vs " + awayTeam;
    }
}
