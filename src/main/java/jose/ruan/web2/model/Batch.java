package jose.ruan.web2.model;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Batch {
    private Integer id;
    private LocalDate deliveryDate;
    private String notes;

    // Relationships
    private InspectionAgency inspectionAgency;
    private DonorAgency donorAgency;
    private List<Product> products;

    public Batch() {}

    public Batch(Integer id, LocalDate deliveryDate, String notes, InspectionAgency inspectionAgency, DonorAgency donorAgency, List<Product> products) {
        this.id = id;
        this.deliveryDate = deliveryDate;
        this.notes = notes;
        this.inspectionAgency = inspectionAgency;
        this.donorAgency = donorAgency;
        this.products = products;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDate getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public InspectionAgency getInspectionAgency() { return inspectionAgency; }
    public void setInspectionAgency(InspectionAgency inspectionAgency) { this.inspectionAgency = inspectionAgency; }

    public DonorAgency getDonorAgency() { return donorAgency; }
    public void setDonorAgency(DonorAgency donorAgency) { this.donorAgency = donorAgency; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}