package jose.ruan.web2.dao;

import jose.ruan.web2.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import static jose.ruan.web2.dao.queries.ProductQueries.*;

@Repository
public class ProductDAO {

    private final JdbcTemplate jdbcTemplate;

    public ProductDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> findAll() {
        return jdbcTemplate.query(FIND_ALL, (rs, rowNum) -> new Product(
                rs.getInt("code"),
                rs.getString("name"),
                rs.getString("description")
        ));
    }

    public List<Product> findByIds(List<Integer> codes) {
        if (codes == null || codes.isEmpty()) {
            return Collections.emptyList();
        }

        String inSql = String.join(",", Collections.nCopies(codes.size(), "?"));

        String sql = "SELECT code, name, description FROM product WHERE code IN (" + inSql + ")";

        return jdbcTemplate.query(sql, codes.toArray(), (rs, rowNum) -> new Product(
                rs.getInt("code"),
                rs.getString("name"),
                rs.getString("description")
        ));
    }

    public Product findByCode(int code) {
        return jdbcTemplate.queryForObject(FIND_BY_CODE, new Object[]{code}, (rs, rowNum) -> new Product(
                rs.getInt("code"),
                rs.getString("name"),
                rs.getString("description")
        ));
    }

    public int save(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key == null) throw new RuntimeException("Erro ao obter ID do produto");
        return key.intValue();
    }


    public int update(Product product) {
        return jdbcTemplate.update(UPDATE, product.getName(), product.getDescription(), product.getCode());
    }

    public int delete(int code) {
        return jdbcTemplate.update(DELETE, code);
    }
}
