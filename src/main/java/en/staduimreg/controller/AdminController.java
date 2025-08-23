package en.staduimreg.controller;

import en.staduimreg.entity.*;
import en.staduimreg.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private FootballMatchService footballMatchService;

    @Autowired
    private StadiumService stadiumService;

    // Dashboard
    @GetMapping
    public String adminDashboard(Model model) {
        // Get statistics
        long totalUsers = userService.getTotalUsersCount();
        long totalBookings = bookingService.getTotalBookingsCount();
        BigDecimal totalRevenue = bookingService.getTotalRevenue();
        long totalMatches = footballMatchService.getAllMatches().size();

        // Get recent activity
        List<User> recentUsers = userService.findRecentUsers(5);
        List<Booking> recentBookings = bookingService.findRecentBookings(5);

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalMatches", totalMatches);
        model.addAttribute("recentUsers", recentUsers);
        model.addAttribute("recentBookings", recentBookings);

        return "admin/dashboard";
    }

    // User Management
    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/users/list";
    }

    @GetMapping("/users/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "admin/users/list"; // Changed from view to list since view template doesn't exist
    }

    @PostMapping("/users/{id}/role")
    public String updateUserRole(@PathVariable Long id, @RequestParam String role, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserRole(id, User.Role.valueOf(role));
            redirectAttributes.addFlashAttribute("success", "User role updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update user role");
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user");
        }
        return "redirect:/admin/users";
    }

    // Booking Management
    @GetMapping("/bookings")
    public String manageBookings(Model model) {
        List<Booking> bookings = bookingService.findAllBookings();
        model.addAttribute("bookings", bookings);
        return "admin/bookings/list";
    }

    @GetMapping("/bookings/{id}")
    public String viewBooking(@PathVariable Long id, Model model) {
        Booking booking = bookingService.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        model.addAttribute("booking", booking);
        return "admin/bookings/list"; // Changed from view to list since view template doesn't exist
    }

    @PostMapping("/bookings/{id}/status")
    public String updateBookingStatus(@PathVariable Long id, @RequestParam String status, RedirectAttributes redirectAttributes) {
        try {
            bookingService.updateBookingStatus(id, Booking.BookingStatus.valueOf(status));
            redirectAttributes.addFlashAttribute("success", "Booking status updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update booking status");
        }
        return "redirect:/admin/bookings";
    }

    // Match Management
    @GetMapping("/matches")
    public String manageMatches(Model model) {
        List<FootballMatch> matches = footballMatchService.getAllMatches();
        model.addAttribute("matches", matches);
        return "admin/matches/list";
    }

    @GetMapping("/matches/new")
    public String newMatch(Model model) {
        model.addAttribute("match", new FootballMatch());
        model.addAttribute("stadiums", stadiumService.getAllStadiums());
        return "admin/matches/form";
    }

    @PostMapping("/matches")
    public String createMatch(@Valid @ModelAttribute FootballMatch match, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("stadiums", stadiumService.getAllStadiums());
            return "admin/matches/form";
        }

        try {
            footballMatchService.save(match);
            redirectAttributes.addFlashAttribute("success", "Match created successfully");
            return "redirect:/admin/matches";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create match");
            model.addAttribute("stadiums", stadiumService.getAllStadiums());
            return "admin/matches/form";
        }
    }

    @GetMapping("/matches/{id}/edit")
    public String editMatch(@PathVariable Long id, Model model) {
        FootballMatch match = footballMatchService.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found"));
        model.addAttribute("match", match);
        model.addAttribute("stadiums", stadiumService.getAllStadiums());
        return "admin/matches/form";
    }

    @PostMapping("/matches/{id}")
    public String updateMatch(@PathVariable Long id, @Valid @ModelAttribute FootballMatch match, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("stadiums", stadiumService.getAllStadiums());
            return "admin/matches/form";
        }

        try {
            match.setId(id);
            footballMatchService.save(match);
            redirectAttributes.addFlashAttribute("success", "Match updated successfully");
            return "redirect:/admin/matches";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update match");
            model.addAttribute("stadiums", stadiumService.getAllStadiums());
            return "admin/matches/form";
        }
    }

    @PostMapping("/matches/{id}/delete")
    public String deleteMatch(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            footballMatchService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Match deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete match");
        }
        return "redirect:/admin/matches";
    }

    // Revenue Report
    @GetMapping("/revenue")
    public String revenueReport(Model model) {
        BigDecimal totalRevenue = bookingService.getTotalRevenue();
        List<FootballMatch> matches = footballMatchService.getAllMatches();

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("matches", matches);

        return "admin/revenue";
    }
}
