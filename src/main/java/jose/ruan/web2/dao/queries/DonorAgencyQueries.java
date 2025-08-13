package jose.ruan.web2.dao.queries;

public class DonorAgencyQueries {
    public static final String FIND_ALL = "SELECT id, name, address, phone, working_hours, description FROM donor_agency";
    public static final String FIND_BY_ID = "SELECT id, name, address, phone, working_hours, description FROM donor_agency WHERE id = ?";
    public static final String INSERT = "INSERT INTO donor_agency (name, address, phone, working_hours, description) VALUES (?, ?, ?, ?, ?)";
    public static final String UPDATE = "UPDATE donor_agency SET name = ?, address = ?, phone = ?, working_hours = ?, description = ? WHERE id = ?";
    public static final String DELETE = "DELETE FROM donor_agency WHERE id = ?";
}
