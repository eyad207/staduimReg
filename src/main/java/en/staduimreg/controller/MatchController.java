package en.staduimreg.controller;

import en.staduimreg.entity.Booking;
import en.staduimreg.entity.FootballMatch;
import en.staduimreg.entity.User;
import en.staduimreg.entity.StadiumSection;
import en.staduimreg.entity.SectionBooking;
import en.staduimreg.service.BookingService;
import en.staduimreg.service.FootballMatchService;
import en.staduimreg.service.UserService;
import en.staduimreg.service.StadiumSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/matches")
public class MatchController {

    @Autowired
    private FootballMatchService footballMatchService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private StadiumSectionService stadiumSectionService;

    @GetMapping
    public String listMatches(Model model) {
        List<FootballMatch> matches = footballMatchService.getAvailableMatches();
        model.addAttribute("matches", matches);
        return "matches/list";
    }

    @GetMapping("/view/{id}")
    public String viewMatch(@PathVariable Long id, Model model, Authentication auth) {
        FootballMatch match = footballMatchService.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        // Get stadium sections for the diagram
        List<StadiumSection> sections = stadiumSectionService.getSectionsByStadium(match.getStadium());
        List<SectionBooking> sectionBookings = stadiumSectionService.getSectionBookingsForMatch(match);

        // Initialize section bookings if they don't exist
        if (sectionBookings.isEmpty()) {
            stadiumSectionService.initializeSectionBookingsForMatch(match);
            sectionBookings = stadiumSectionService.getSectionBookingsForMatch(match);
        }

        model.addAttribute("match", match);
        model.addAttribute("sections", sections);
        model.addAttribute("sectionBookings", sectionBookings);
        model.addAttribute("isLoggedIn", auth != null && auth.isAuthenticated());

        return "matches/view";
    }

    // REST endpoint to get section availability data for AJAX calls
    @GetMapping("/api/{matchId}/sections")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getSectionAvailability(@PathVariable Long matchId) {
        FootballMatch match = footballMatchService.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        List<StadiumSection> sections = stadiumSectionService.getSectionsByStadium(match.getStadium());
        List<Map<String, Object>> sectionData = new java.util.ArrayList<>();

        for (StadiumSection section : sections) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", section.getId());
            data.put("name", section.getSectionName());
            data.put("type", section.getSectionType());
            data.put("totalSeats", section.getTotalSeats());
            data.put("availableSeats", stadiumSectionService.getAvailableSeatsInSection(match, section));
            data.put("priceMultiplier", section.getPriceMultiplier());
            data.put("positionX", section.getPositionX());
            data.put("positionY", section.getPositionY());
            data.put("width", section.getWidth());
            data.put("height", section.getHeight());
            data.put("color", section.getColor());
            sectionData.add(data);
        }

        return ResponseEntity.ok(sectionData);
    }

    @PostMapping("/book/{id}")
    public String bookMatch(@PathVariable Long id,
                           @RequestParam Integer numberOfSeats,
                           Authentication auth,
                           RedirectAttributes redirectAttributes) {

        if (auth == null || !auth.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "Please login to book seats");
            return "redirect:/login";
        }

        try {
            User user = userService.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            FootballMatch match = footballMatchService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Match not found"));

            if (numberOfSeats <= 0) {
                redirectAttributes.addFlashAttribute("error", "Number of seats must be greater than 0");
                return "redirect:/matches/view/" + id;
            }

            Booking booking = bookingService.createBooking(user, match, numberOfSeats);

            redirectAttributes.addFlashAttribute("success",
                "Booking successful! Confirmation code: " + booking.getConfirmationCode());

            return "redirect:/bookings";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/matches/view/" + id;
        }
    }

    @PostMapping("/book/{matchId}/section/{sectionId}")
    public String bookMatchWithSection(@PathVariable Long matchId,
                                     @PathVariable Long sectionId,
                                     @RequestParam Integer numberOfSeats,
                                     Authentication auth,
                                     RedirectAttributes redirectAttributes) {

        if (auth == null || !auth.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "Please login to book seats");
            return "redirect:/login";
        }

        try {
            User user = userService.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            FootballMatch match = footballMatchService.findById(matchId)
                    .orElseThrow(() -> new RuntimeException("Match not found"));

            StadiumSection section = stadiumSectionService.findById(sectionId)
                    .orElseThrow(() -> new RuntimeException("Stadium section not found"));

            if (numberOfSeats <= 0) {
                redirectAttributes.addFlashAttribute("error", "Number of seats must be greater than 0");
                return "redirect:/matches/view/" + matchId;
            }

            Booking booking = bookingService.createBookingWithSection(user, match, section, numberOfSeats);

            redirectAttributes.addFlashAttribute("success",
                "Booking successful in " + section.getSectionName() + "! Confirmation code: " + booking.getConfirmationCode());

            return "redirect:/bookings";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/matches/view/" + matchId;
        }
    }
}
