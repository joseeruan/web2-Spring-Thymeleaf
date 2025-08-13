package jose.ruan.web2.dao.queries;

public class BatchQueries {
    public static final String FIND_ALL = "SELECT id, delivery_date, notes, inspection_agency_id, donor_agency_id FROM batch";
    public static final String FIND_BY_ID = "SELECT id, delivery_date, notes, inspection_agency_id, donor_agency_id FROM batch WHERE id = ?";
    public static final String INSERT = "INSERT INTO batch (delivery_date, notes, inspection_agency_id, donor_agency_id) VALUES (?, ?, ?, ?)";
    public static final String UPDATE = "UPDATE batch SET delivery_date = ?, notes = ?, inspection_agency_id = ?, donor_agency_id = ? WHERE id = ?";
    public static final String DELETE = "DELETE FROM batch WHERE id = ?";

    public static final String FIND_BY_INSPECTION_AGENCY = "SELECT id, delivery_date, notes, inspection_agency_id, donor_agency_id FROM batch WHERE inspection_agency_id = ?";
    public static final String FIND_BY_DONOR_AGENCY = "SELECT id, delivery_date, notes, inspection_agency_id, donor_agency_id FROM batch WHERE donor_agency_id = ?";
}