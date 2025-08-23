package en.staduimreg.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(max = 100)
    @Column(name = "home_team", nullable = false)
    private String homeTeam;

    @NotBlank
    @Size(max = 100)
    @Column(name = "away_team", nullable = false)
    private String awayTeam;

    @NotNull
    @Column(name = "match_date", nullable = false)
    private LocalDateTime matchDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id", nullable = false)
    private Stadium stadium;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status = MatchStatus.SCHEDULED;

    @Column(name = "home_score")
    private Integer homeScore;

    @Column(name = "away_score")
    private Integer awayScore;

    @Size(max = 500)
    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "ticket_price", precision = 10, scale = 2)
    private BigDecimal ticketPrice = BigDecimal.valueOf(50.00); // Default ticket price

    @Column(name = "available_seats")
    private Integer availableSeats;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Booking> bookings;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (availableSeats == null && stadium != null) {
            availableSeats = stadium.getTotalCapacity();
        }
    }

    public enum MatchStatus {
        SCHEDULED, ONGOING, FINISHED, CANCELLED, POSTPONED
    }

    // Constructors
    public FootballMatch() {}

    public FootballMatch(String homeTeam, String awayTeam, LocalDateTime matchDate, Stadium stadium) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchDate = matchDate;
        this.stadium = stadium;
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

    public MatchStatus getStatus() { return status; }
    public void setStatus(MatchStatus status) { this.status = status; }

    public Integer getHomeScore() { return homeScore; }
    public void setHomeScore(Integer homeScore) { this.homeScore = homeScore; }

    public Integer getAwayScore() { return awayScore; }
    public void setAwayScore(Integer awayScore) { this.awayScore = awayScore; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public BigDecimal getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(BigDecimal ticketPrice) { this.ticketPrice = ticketPrice; }

    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }

    public Set<Booking> getBookings() { return bookings; }
    public void setBookings(Set<Booking> bookings) { this.bookings = bookings; }

    public String getMatchTitle() {
        return homeTeam + " vs " + awayTeam;
    }

    public String getScoreDisplay() {
        if (homeScore != null && awayScore != null) {
            return homeScore + " - " + awayScore;
        }
        return "- : -";
    }
}
