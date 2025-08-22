package en.staduimreg.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "stadium_sections")
public class StadiumSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id", nullable = false)
    private Stadium stadium;

    @NotBlank
    @Column(nullable = false)
    private String sectionName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SectionType sectionType;

    @Positive
    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @Column(name = "price_multiplier", nullable = false)
    private Double priceMultiplier = 1.0; // Multiplier for base ticket price

    // Visual positioning for diagram
    @PositiveOrZero
    @Column(name = "position_x")
    private Integer positionX; // X coordinate on stadium diagram

    @PositiveOrZero
    @Column(name = "position_y")
    private Integer positionY; // Y coordinate on stadium diagram

    @Column(name = "width")
    private Integer width = 100; // Width of section on diagram

    @Column(name = "height")
    private Integer height = 50; // Height of section on diagram

    @Column(name = "color")
    private String color = "#4CAF50"; // Default green color for available sections

    public enum SectionType {
        VIP, PREMIUM, STANDARD, ECONOMY, STANDING
    }

    // Constructors
    public StadiumSection() {}

    public StadiumSection(Stadium stadium, String sectionName, SectionType sectionType, Integer totalSeats) {
        this.stadium = stadium;
        this.sectionName = sectionName;
        this.sectionType = sectionType;
        this.totalSeats = totalSeats;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Stadium getStadium() { return stadium; }
    public void setStadium(Stadium stadium) { this.stadium = stadium; }

    public String getSectionName() { return sectionName; }
    public void setSectionName(String sectionName) { this.sectionName = sectionName; }

    public SectionType getSectionType() { return sectionType; }
    public void setSectionType(SectionType sectionType) { this.sectionType = sectionType; }

    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }

    public Double getPriceMultiplier() { return priceMultiplier; }
    public void setPriceMultiplier(Double priceMultiplier) { this.priceMultiplier = priceMultiplier; }

    public Integer getPositionX() { return positionX; }
    public void setPositionX(Integer positionX) { this.positionX = positionX; }

    public Integer getPositionY() { return positionY; }
    public void setPositionY(Integer positionY) { this.positionY = positionY; }

    public Integer getWidth() { return width; }
    public void setWidth(Integer width) { this.width = width; }

    public Integer getHeight() { return height; }
    public void setHeight(Integer height) { this.height = height; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
