package en.staduimreg.controller;

import en.staduimreg.entity.Booking;
import en.staduimreg.entity.User;
import en.staduimreg.service.BookingService;
import en.staduimreg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String myBookings(Model model, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Booking> bookings = bookingService.getUserBookings(user);
        model.addAttribute("bookings", bookings);

        return "bookings/list";
    }

    @PostMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id,
                               Authentication auth,
                               RedirectAttributes redirectAttributes) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            User user = userService.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Booking booking = bookingService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            // Check if the booking belongs to the current user
            if (!booking.getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "You can only cancel your own bookings");
                return "redirect:/bookings";
            }

            bookingService.cancelBooking(id);
            redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully");

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/bookings";
    }

    @GetMapping("/confirmation/{code}")
    public String viewBookingByConfirmation(@PathVariable String code, Model model) {
        Booking booking = bookingService.findByConfirmationCode(code)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        model.addAttribute("booking", booking);
        return "bookings/confirmation";
    }
}
