package jose.ruan.web2.dao.queries;

public class BatchProductQueries {
    public static final String INSERT = "INSERT INTO batch_product (batch_id, product_code) VALUES (?, ?)";
    public static final String DELETE_BY_BATCH = "DELETE FROM batch_product WHERE batch_id = ?";
    public static final String FIND_PRODUCTS_BY_BATCH =
            "SELECT p.code, p.name, p.description FROM product p " +
                    "JOIN batch_product bp ON p.code = bp.product_code WHERE bp.batch_id = ?";
}
