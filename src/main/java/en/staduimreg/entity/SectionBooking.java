package en.staduimreg.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "section_bookings")
public class SectionBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "football_match_id", nullable = false)
    private FootballMatch footballMatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_section_id", nullable = false)
    private StadiumSection stadiumSection;

    @PositiveOrZero
    @Column(name = "booked_seats", nullable = false)
    private Integer bookedSeats = 0;

    // Constructors
    public SectionBooking() {}

    public SectionBooking(FootballMatch footballMatch, StadiumSection stadiumSection) {
        this.footballMatch = footballMatch;
        this.stadiumSection = stadiumSection;
        this.bookedSeats = 0;
    }

    // Helper methods
    public Integer getAvailableSeats() {
        return stadiumSection.getTotalSeats() - bookedSeats;
    }

    public boolean hasAvailableSeats(int requestedSeats) {
        return getAvailableSeats() >= requestedSeats;
    }

    public void bookSeats(int seats) {
        if (!hasAvailableSeats(seats)) {
            throw new IllegalStateException("Not enough available seats in section " + stadiumSection.getSectionName());
        }
        this.bookedSeats += seats;
    }

    public void releaseSeats(int seats) {
        this.bookedSeats = Math.max(0, this.bookedSeats - seats);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public FootballMatch getFootballMatch() { return footballMatch; }
    public void setFootballMatch(FootballMatch footballMatch) { this.footballMatch = footballMatch; }

    public StadiumSection getStadiumSection() { return stadiumSection; }
    public void setStadiumSection(StadiumSection stadiumSection) { this.stadiumSection = stadiumSection; }

    public Integer getBookedSeats() { return bookedSeats; }
    public void setBookedSeats(Integer bookedSeats) { this.bookedSeats = bookedSeats; }
}
