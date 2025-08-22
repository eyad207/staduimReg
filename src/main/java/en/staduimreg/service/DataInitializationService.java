package en.staduimreg.service;

import en.staduimreg.entity.FootballMatch;
import en.staduimreg.entity.Stadium;
import en.staduimreg.entity.StadiumSection;
import en.staduimreg.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private StadiumService stadiumService;

    @Autowired
    private FootballMatchService footballMatchService;

    @Autowired
    private UserService userService;

    @Autowired
    private StadiumSectionService stadiumSectionService;

    @Override
    public void run(String... args) throws Exception {
        try {
            initializeData();
        } catch (Exception e) {
            // Log the error but don't prevent application startup
            System.err.println("Warning: Could not initialize sample data: " + e.getMessage());
        }
    }

    @Transactional
    protected void initializeData() {
        // Create admin user if not exists
        try {
            if (!userService.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@stadium.com");
                admin.setPassword("admin123");
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setRole(User.Role.ADMIN);
                userService.registerUser(admin);
            }
        } catch (Exception e) {
            System.err.println("Could not create admin user: " + e.getMessage());
        }

        // Create test user if not exists
        try {
            if (!userService.existsByUsername("testuser")) {
                User testUser = new User();
                testUser.setUsername("testuser");
                testUser.setEmail("test@example.com");
                testUser.setPassword("password123");
                testUser.setFirstName("Test");
                testUser.setLastName("User");
                testUser.setPhoneNumber("+1234567890");
                userService.registerUser(testUser);
            }
        } catch (Exception e) {
            System.err.println("Could not create test user: " + e.getMessage());
        }

        // Initialize stadiums if none exist
        try {
            if (stadiumService.getAllStadiums().isEmpty()) {
                createSampleStadiums();
            }
        } catch (Exception e) {
            System.err.println("Could not create stadiums: " + e.getMessage());
        }

        // Initialize matches if none exist
        try {
            if (footballMatchService.getAllMatches().isEmpty()) {
                createSampleMatches();
            }
        } catch (Exception e) {
            System.err.println("Could not create matches: " + e.getMessage());
        }
    }

    private void createSampleStadiums() {
        Stadium stadium1 = new Stadium();
        stadium1.setName("Champions Arena");
        stadium1.setCity("New York");
        stadium1.setAddress("123 Sports Avenue, New York, NY");
        stadium1.setTotalCapacity(50000);
        stadium1.setDescription("Premier football stadium in the heart of New York");
        stadium1.setDiagramWidth(800);
        stadium1.setDiagramHeight(600);
        stadiumService.save(stadium1);
        createSectionsForStadium(stadium1);

        Stadium stadium2 = new Stadium();
        stadium2.setName("Victory Stadium");
        stadium2.setCity("Los Angeles");
        stadium2.setAddress("456 Victory Blvd, Los Angeles, CA");
        stadium2.setTotalCapacity(35000);
        stadium2.setDescription("Modern stadium with state-of-the-art facilities");
        stadium2.setDiagramWidth(800);
        stadium2.setDiagramHeight(600);
        stadiumService.save(stadium2);
        createSectionsForStadium(stadium2);

        Stadium stadium3 = new Stadium();
        stadium3.setName("Olympic Park");
        stadium3.setCity("Chicago");
        stadium3.setAddress("789 Olympic Drive, Chicago, IL");
        stadium3.setTotalCapacity(45000);
        stadium3.setDescription("Historic stadium hosting major football events");
        stadium3.setDiagramWidth(800);
        stadium3.setDiagramHeight(600);
        stadiumService.save(stadium3);
        createSectionsForStadium(stadium3);

        Stadium stadium4 = new Stadium();
        stadium4.setName("Metro Field");
        stadium4.setCity("Miami");
        stadium4.setAddress("321 Metro Street, Miami, FL");
        stadium4.setTotalCapacity(25000);
        stadium4.setDescription("Intimate stadium perfect for exciting matches");
        stadium4.setDiagramWidth(800);
        stadium4.setDiagramHeight(600);
        stadiumService.save(stadium4);
        createSectionsForStadium(stadium4);
    }

    private void createSectionsForStadium(Stadium stadium) {
        // Create a typical stadium layout with sections around the field

        // North sections (top of stadium)
        createSection(stadium, "North VIP", StadiumSection.SectionType.VIP, 2000, 2.5, 150, 50, 120, 60, "#9C27B0");
        createSection(stadium, "North Premium", StadiumSection.SectionType.PREMIUM, 3000, 1.8, 300, 50, 120, 60, "#3F51B5");
        createSection(stadium, "North Standard", StadiumSection.SectionType.STANDARD, 4000, 1.0, 450, 50, 120, 60, "#4CAF50");

        // South sections (bottom of stadium)
        createSection(stadium, "South VIP", StadiumSection.SectionType.VIP, 2000, 2.5, 150, 490, 120, 60, "#9C27B0");
        createSection(stadium, "South Premium", StadiumSection.SectionType.PREMIUM, 3000, 1.8, 300, 490, 120, 60, "#3F51B5");
        createSection(stadium, "South Standard", StadiumSection.SectionType.STANDARD, 4000, 1.0, 450, 490, 120, 60, "#4CAF50");

        // East sections (right side)
        createSection(stadium, "East VIP", StadiumSection.SectionType.VIP, 1500, 2.5, 650, 150, 80, 100, "#9C27B0");
        createSection(stadium, "East Premium", StadiumSection.SectionType.PREMIUM, 2500, 1.8, 650, 280, 80, 100, "#3F51B5");
        createSection(stadium, "East Standard", StadiumSection.SectionType.STANDARD, 3000, 1.0, 650, 410, 80, 100, "#4CAF50");

        // West sections (left side)
        createSection(stadium, "West VIP", StadiumSection.SectionType.VIP, 1500, 2.5, 50, 150, 80, 100, "#9C27B0");
        createSection(stadium, "West Premium", StadiumSection.SectionType.PREMIUM, 2500, 1.8, 50, 280, 80, 100, "#3F51B5");
        createSection(stadium, "West Standard", StadiumSection.SectionType.STANDARD, 3000, 1.0, 50, 410, 80, 100, "#4CAF50");

        // Corner sections
        createSection(stadium, "NE Corner", StadiumSection.SectionType.ECONOMY, 1000, 0.8, 600, 100, 80, 80, "#FF9800");
        createSection(stadium, "NW Corner", StadiumSection.SectionType.ECONOMY, 1000, 0.8, 120, 100, 80, 80, "#FF9800");
        createSection(stadium, "SE Corner", StadiumSection.SectionType.ECONOMY, 1000, 0.8, 600, 470, 80, 80, "#FF9800");
        createSection(stadium, "SW Corner", StadiumSection.SectionType.ECONOMY, 1000, 0.8, 120, 470, 80, 80, "#FF9800");
    }

    private void createSection(Stadium stadium, String name, StadiumSection.SectionType type,
                              int totalSeats, double priceMultiplier, int x, int y,
                              int width, int height, String color) {
        StadiumSection section = new StadiumSection();
        section.setStadium(stadium);
        section.setSectionName(name);
        section.setSectionType(type);
        section.setTotalSeats(totalSeats);
        section.setPriceMultiplier(priceMultiplier);
        section.setPositionX(x);
        section.setPositionY(y);
        section.setWidth(width);
        section.setHeight(height);
        section.setColor(color);
        stadiumSectionService.save(section);
    }

    private void createSampleMatches() {
        // Get stadiums for matches
        var stadiums = stadiumService.getAllStadiums();
        if (stadiums.isEmpty()) return;

        LocalDateTime now = LocalDateTime.now();

        // Match 1 - Next week
        FootballMatch match1 = new FootballMatch();
        match1.setHomeTeam("Real Madrid");
        match1.setAwayTeam("Barcelona");
        match1.setMatchDate(now.plusDays(7).withHour(15).withMinute(0).withSecond(0).withNano(0));
        match1.setStadium(stadiums.get(0));
        match1.setTicketPrice(new BigDecimal("85.00"));
        match1.setDescription("El Clasico - The ultimate football rivalry");
        match1.setStatus(FootballMatch.MatchStatus.SCHEDULED);
        footballMatchService.save(match1);

        // Match 2 - Next month
        FootballMatch match2 = new FootballMatch();
        match2.setHomeTeam("Manchester United");
        match2.setAwayTeam("Liverpool");
        match2.setMatchDate(now.plusDays(14).withHour(18).withMinute(30).withSecond(0).withNano(0));
        match2.setStadium(stadiums.get(1));
        match2.setTicketPrice(new BigDecimal("75.00"));
        match2.setDescription("Premier League classic between two giants");
        match2.setStatus(FootballMatch.MatchStatus.SCHEDULED);
        footballMatchService.save(match2);

        // Match 3 - In 3 weeks
        FootballMatch match3 = new FootballMatch();
        match3.setHomeTeam("Bayern Munich");
        match3.setAwayTeam("Borussia Dortmund");
        match3.setMatchDate(now.plusDays(21).withHour(16).withMinute(0).withSecond(0).withNano(0));
        match3.setStadium(stadiums.get(2));
        match3.setTicketPrice(new BigDecimal("65.00"));
        match3.setDescription("Der Klassiker - German football at its finest");
        match3.setStatus(FootballMatch.MatchStatus.SCHEDULED);
        footballMatchService.save(match3);

        // Match 4 - Next weekend
        FootballMatch match4 = new FootballMatch();
        match4.setHomeTeam("AC Milan");
        match4.setAwayTeam("Inter Milan");
        match4.setMatchDate(now.plusDays(10).withHour(20).withMinute(0).withSecond(0).withNano(0));
        match4.setStadium(stadiums.get(3));
        match4.setTicketPrice(new BigDecimal("55.00"));
        match4.setDescription("Derby della Madonnina - Milan city derby");
        match4.setStatus(FootballMatch.MatchStatus.SCHEDULED);
        footballMatchService.save(match4);

        // Match 5 - Limited seats
        FootballMatch match5 = new FootballMatch();
        match5.setHomeTeam("Chelsea");
        match5.setAwayTeam("Arsenal");
        match5.setMatchDate(now.plusDays(5).withHour(14).withMinute(0).withSecond(0).withNano(0));
        match5.setStadium(stadiums.get(0));
        match5.setTicketPrice(new BigDecimal("70.00"));
        match5.setDescription("London Derby - High intensity football");
        match5.setStatus(FootballMatch.MatchStatus.SCHEDULED);
        match5.setAvailableSeats(15); // Limited seats to test availability
        footballMatchService.save(match5);

        // Match 6 - Sold out
        FootballMatch match6 = new FootballMatch();
        match6.setHomeTeam("PSG");
        match6.setAwayTeam("Marseille");
        match6.setMatchDate(now.plusDays(12).withHour(21).withMinute(0).withSecond(0).withNano(0));
        match6.setStadium(stadiums.get(1));
        match6.setTicketPrice(new BigDecimal("80.00"));
        match6.setDescription("Le Classique - French football rivalry");
        match6.setStatus(FootballMatch.MatchStatus.SCHEDULED);
        match6.setAvailableSeats(0); // Sold out to test booking restrictions
        footballMatchService.save(match6);
    }
}
