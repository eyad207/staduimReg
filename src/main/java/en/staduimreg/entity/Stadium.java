package en.staduimreg.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.Set;

@Entity
@Table(name = "stadiums")
public class Stadium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String city;

    @NotBlank
    @Column(nullable = false)
    private String address;

    @Positive
    @Column(name = "total_capacity", nullable = false)
    private Integer totalCapacity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "stadium", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FootballMatch> matches;

    // Stadium diagram dimensions
    @Column(name = "diagram_width")
    private Integer diagramWidth = 800; // Default diagram width in pixels

    @Column(name = "diagram_height")
    private Integer diagramHeight = 600; // Default diagram height in pixels

    // Constructors
    public Stadium() {}

    public Stadium(String name, String city, String address, Integer totalCapacity) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.totalCapacity = totalCapacity;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getTotalCapacity() { return totalCapacity; }
    public void setTotalCapacity(Integer totalCapacity) { this.totalCapacity = totalCapacity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<FootballMatch> getMatches() { return matches; }
    public void setMatches(Set<FootballMatch> matches) { this.matches = matches; }

    public Integer getDiagramWidth() { return diagramWidth; }
    public void setDiagramWidth(Integer diagramWidth) { this.diagramWidth = diagramWidth; }

    public Integer getDiagramHeight() { return diagramHeight; }
    public void setDiagramHeight(Integer diagramHeight) { this.diagramHeight = diagramHeight; }
}
