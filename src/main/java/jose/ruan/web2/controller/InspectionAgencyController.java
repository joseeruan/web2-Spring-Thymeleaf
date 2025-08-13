package jose.ruan.web2.controller;

import jose.ruan.web2.dao.InspectionAgencyDAO;
import jose.ruan.web2.model.InspectionAgency;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/inspection-agencies")
public class InspectionAgencyController {

    private final InspectionAgencyDAO inspectionAgencyDAO;

    public InspectionAgencyController(InspectionAgencyDAO inspectionAgencyDAO) {
        this.inspectionAgencyDAO = inspectionAgencyDAO;
    }

    @GetMapping
    public String list(Model model) {
        List<InspectionAgency> agencies = inspectionAgencyDAO.findAll();
        model.addAttribute("agencies", agencies);
        return "inspection-agencies/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("agency", new InspectionAgency());
        return "inspection-agencies/form";
    }

    @PostMapping
    public String save(@ModelAttribute InspectionAgency agency) {
        inspectionAgencyDAO.save(agency);
        return "redirect:/inspection-agencies";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        Optional<InspectionAgency> agencyOpt = inspectionAgencyDAO.findById(id);
        if (agencyOpt.isEmpty()) {
            return "redirect:/inspection-agencies";
        }
        model.addAttribute("agency", agencyOpt.get());
        return "inspection-agencies/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        inspectionAgencyDAO.delete(id);
        return "redirect:/inspection-agencies";
    }
}
