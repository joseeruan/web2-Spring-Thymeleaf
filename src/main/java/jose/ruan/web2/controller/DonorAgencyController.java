package jose.ruan.web2.controller;

import jose.ruan.web2.dao.DonorAgencyDAO;
import jose.ruan.web2.model.DonorAgency;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/donor-agencies")
public class DonorAgencyController {

    private final DonorAgencyDAO donorAgencyDAO;

    public DonorAgencyController(DonorAgencyDAO donorAgencyDAO) {
        this.donorAgencyDAO = donorAgencyDAO;
    }

    @GetMapping
    public String list(Model model) {
        List<DonorAgency> donors = donorAgencyDAO.findAll();
        model.addAttribute("donors", donors);
        return "donor-agencies/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("donor", new DonorAgency());
        return "donor-agencies/form";
    }

    @PostMapping
    public String save(@ModelAttribute DonorAgency donor) {
        if (donor.getId() == null) {
            donorAgencyDAO.save(donor);
        } else {
            donorAgencyDAO.update(donor);
        }

        return "redirect:/donor-agencies";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        DonorAgency donor = donorAgencyDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("DonorAgency not found with ID " + id));
        model.addAttribute("donor", donor);
        return "donor-agencies/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        donorAgencyDAO.delete(id);
        return "redirect:/donor-agencies";
    }
}

