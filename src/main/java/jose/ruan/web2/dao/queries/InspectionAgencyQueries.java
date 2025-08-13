package jose.ruan.web2.dao.queries;

public class InspectionAgencyQueries {
    public static final String FIND_ALL = "SELECT id, name, description FROM inspection_agency";
    public static final String FIND_BY_ID = "SELECT id, name, description FROM inspection_agency WHERE id = ?";
    public static final String INSERT = "INSERT INTO inspection_agency (name, description) VALUES (?, ?)";
    public static final String UPDATE = "UPDATE inspection_agency SET name = ?, description = ? WHERE id = ?";
    public static final String DELETE = "DELETE FROM inspection_agency WHERE id = ?";
}
