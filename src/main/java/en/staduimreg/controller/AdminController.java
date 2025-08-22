package en.staduimreg.controller;

import en.staduimreg.entity.FootballMatch;
import en.staduimreg.entity.Stadium;
import en.staduimreg.service.FootballMatchService;
import en.staduimreg.service.StadiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private FootballMatchService footballMatchService;

    @Autowired
    private StadiumService stadiumService;

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("matchCount", footballMatchService.getAllMatches().size());
        model.addAttribute("stadiumCount", stadiumService.getAllStadiums().size());
        return "admin/dashboard";
    }

    // Stadium Management
    @GetMapping("/stadiums")
    public String listStadiums(Model model) {
        List<Stadium> stadiums = stadiumService.getAllStadiums();
        model.addAttribute("stadiums", stadiums);
        return "admin/stadiums/list";
    }

    @GetMapping("/stadiums/new")
    public String newStadium(Model model) {
        model.addAttribute("stadium", new Stadium());
        return "admin/stadiums/form";
    }

    @PostMapping("/stadiums")
    public String createStadium(@Valid @ModelAttribute Stadium stadium,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/stadiums/form";
        }

        stadiumService.save(stadium);
        redirectAttributes.addFlashAttribute("success", "Stadium created successfully");
        return "redirect:/admin/stadiums";
    }

    // Match Management
    @GetMapping("/matches")
    public String listMatches(Model model) {
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
    public String createMatch(@Valid @ModelAttribute FootballMatch match,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("stadiums", stadiumService.getAllStadiums());
            return "admin/matches/form";
        }

        footballMatchService.save(match);
        redirectAttributes.addFlashAttribute("success", "Match created successfully");
        return "redirect:/admin/matches";
    }

    @PostMapping("/matches/delete/{id}")
    public String deleteMatch(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            footballMatchService.deleteMatch(id);
            redirectAttributes.addFlashAttribute("success", "Match deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete match with existing bookings");
        }
        return "redirect:/admin/matches";
    }
}
