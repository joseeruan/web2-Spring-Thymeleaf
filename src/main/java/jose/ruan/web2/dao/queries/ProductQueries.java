package jose.ruan.web2.dao.queries;

public class ProductQueries {
    public static final String FIND_ALL = "SELECT code, name, description FROM product";
    public static final String FIND_BY_CODE = "SELECT code, name, description FROM product WHERE code = ?";
    public static final String INSERT = "INSERT INTO product (name, description) VALUES (?, ?)";
    public static final String UPDATE = "UPDATE product SET name = ?, description = ? WHERE code = ?";
    public static final String DELETE = "DELETE FROM product WHERE code = ?";
}
