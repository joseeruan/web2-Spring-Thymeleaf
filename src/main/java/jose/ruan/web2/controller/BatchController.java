package jose.ruan.web2.controller;

import jose.ruan.web2.dao.BatchDAO;
import jose.ruan.web2.dao.DonorAgencyDAO;
import jose.ruan.web2.dao.InspectionAgencyDAO;
import jose.ruan.web2.dao.ProductDAO;
import jose.ruan.web2.model.Batch;
import jose.ruan.web2.model.DonorAgency;
import jose.ruan.web2.model.InspectionAgency;
import jose.ruan.web2.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/batches")
public class BatchController {

    private final BatchDAO batchDAO;
    private final InspectionAgencyDAO inspectionAgencyDAO;
    private final DonorAgencyDAO donorAgencyDAO;
    private final ProductDAO productDAO;

    public BatchController(BatchDAO batchDAO, InspectionAgencyDAO inspectionAgencyDAO,
                           DonorAgencyDAO donorAgencyDAO, ProductDAO productDAO) {
        this.batchDAO = batchDAO;
        this.inspectionAgencyDAO = inspectionAgencyDAO;
        this.donorAgencyDAO = donorAgencyDAO;
        this.productDAO = productDAO;
    }

    @GetMapping
    public String list(@RequestParam(required = false) Integer inspectionAgencyId,
                       @RequestParam(required = false) Integer donorAgencyId,
                       Model model) {

        List<Batch> batches;

        if (inspectionAgencyId != null) {
            batches = batchDAO.findByInspectionAgencyId(inspectionAgencyId);
        } else if (donorAgencyId != null) {
            batches = batchDAO.findByDonorAgencyId(donorAgencyId);
        } else {
            batches = batchDAO.findAll();
        }

        model.addAttribute("batches", batches);
        model.addAttribute("inspectionAgencies", inspectionAgencyDAO.findAll());
        model.addAttribute("donorAgencies", donorAgencyDAO.findAll());

        model.addAttribute("inspectionAgencyIdFilter", inspectionAgencyId != null ? inspectionAgencyId.toString() : "");
        model.addAttribute("donorAgencyIdFilter", donorAgencyId != null ? donorAgencyId.toString() : "");

        return "batches/list";
    }


    @GetMapping("/new")
    public String createForm(Model model) {
        Batch batch = new Batch();
        batch.setInspectionAgency(new InspectionAgency());
        batch.setDonorAgency(new DonorAgency());
        model.addAttribute("batch", batch);
        model.addAttribute("inspectionAgencies", inspectionAgencyDAO.findAll());
        model.addAttribute("donorAgencies", donorAgencyDAO.findAll());
        model.addAttribute("products", productDAO.findAll());
        return "batches/form";
    }

    @PostMapping
    public String save(@ModelAttribute Batch batch,
                       @RequestParam(required = false) List<Integer> productCodes) {

        if (batch.getInspectionAgency() == null || batch.getInspectionAgency().getId() == null || batch.getInspectionAgency().getId() == 0) {
            batch.setInspectionAgency(inspectionAgencyDAO.findAll().get(0));
        } else {
            batch.setInspectionAgency(
                    inspectionAgencyDAO.findById(batch.getInspectionAgency().getId())
                            .orElseThrow(() -> new RuntimeException("InspectionAgency not found with ID " + batch.getInspectionAgency().getId()))
            );
        }

        if (batch.getDonorAgency() == null || batch.getDonorAgency().getId() == null || batch.getDonorAgency().getId() == 0) {
            batch.setDonorAgency(donorAgencyDAO.findAll().get(0));
        } else {
            DonorAgency donorAgency = donorAgencyDAO.findById(batch.getDonorAgency().getId())
                    .orElseThrow(() -> new RuntimeException("DonorAgency not found with ID " + batch.getDonorAgency().getId()));
            batch.setDonorAgency(donorAgency);
        }

        if (productCodes != null && !productCodes.isEmpty()) {
            batch.setProducts(productDAO.findByIds(productCodes));
        } else {
            batch.setProducts(List.of());
        }

        if (batch.getId() == null || batch.getId() == 0) {
            int generatedId = batchDAO.save(batch);
            batch.setId(generatedId);
        } else {
            batchDAO.update(batch);
        }

        return "redirect:/batches";
    }


    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        Batch batch = batchDAO.findById(id);

        System.out.println(batch.getId());
        if (batch.getInspectionAgency() == null) {
            batch.setInspectionAgency(inspectionAgencyDAO.findAll().get(0));
        }
        if (batch.getDonorAgency() == null) {
            batch.setDonorAgency(new DonorAgency());
        }
        if (batch.getProducts() == null) {
            batch.setProducts(List.of());
        }

        model.addAttribute("batch", batch);
        model.addAttribute("inspectionAgencies", inspectionAgencyDAO.findAll());
        model.addAttribute("donorAgencies", donorAgencyDAO.findAll());
        model.addAttribute("products", productDAO.findAll());

        return "batches/form";
    }



    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        batchDAO.delete(id);
        return "redirect:/batches";
    }
}
