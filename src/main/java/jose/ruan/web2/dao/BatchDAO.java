package jose.ruan.web2.dao;

import jose.ruan.web2.dao.queries.BatchProductQueries;
import jose.ruan.web2.dao.queries.BatchQueries;
import jose.ruan.web2.model.Batch;
import jose.ruan.web2.model.DonorAgency;
import jose.ruan.web2.model.InspectionAgency;
import jose.ruan.web2.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

@Repository
public class BatchDAO {

    private final JdbcTemplate jdbcTemplate;
    private final InspectionAgencyDAO inspectionAgencyDAO;
    private final DonorAgencyDAO donorAgencyDAO;

    public BatchDAO(JdbcTemplate jdbcTemplate, InspectionAgencyDAO inspectionAgencyDAO, DonorAgencyDAO donorAgencyDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.inspectionAgencyDAO = inspectionAgencyDAO;
        this.donorAgencyDAO = donorAgencyDAO;
    }

    public List<Batch> findAll() {
        return jdbcTemplate.query(BatchQueries.FIND_ALL, (rs, rowNum) -> mapBatch(rs));
    }

    public Batch findById(int id) {
        return jdbcTemplate.queryForObject(BatchQueries.FIND_BY_ID, new Object[]{id}, (rs, rowNum) -> mapBatch(rs));
    }

    private Batch mapBatch(java.sql.ResultSet rs) throws java.sql.SQLException {
        int inspectionAgencyId = rs.getInt("inspection_agency_id");
        int donorAgencyId = rs.getInt("donor_agency_id");

        InspectionAgency inspectionAgency = inspectionAgencyDAO.findById(inspectionAgencyId)
                .orElseThrow(() -> new RuntimeException("InspectionAgency not found with ID " + inspectionAgencyId));

        DonorAgency donorAgency = donorAgencyDAO.findById(donorAgencyId)
                .orElseThrow(() -> new RuntimeException("DonorAgency not found with ID " + donorAgencyId));

        List<Product> products = findProductsByBatchId(rs.getInt("id"));

        java.sql.Date sqlDate = rs.getDate("delivery_date");
        java.time.LocalDate deliveryDate = null;
        if (sqlDate != null) {
            deliveryDate = sqlDate.toLocalDate();
        }

        return new Batch(
                rs.getInt("id"),
                deliveryDate,
                rs.getString("notes"),
                inspectionAgency,
                donorAgency,
                products
        );
    }


    public int save(Batch batch) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(BatchQueries.INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf(batch.getDeliveryDate()));
            ps.setString(2, batch.getNotes());
            ps.setInt(3, batch.getInspectionAgency().getId());
            ps.setInt(4, batch.getDonorAgency().getId());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new RuntimeException("Failed to retrieve generated key for Batch");
        }

        int batchId = key.intValue();

        if (batch.getProducts() != null) {
            batch.getProducts().forEach(product ->
                    jdbcTemplate.update(BatchProductQueries.INSERT, batchId, product.getCode()));
        }

        return batchId;
    }

    public int update(Batch batch) {
        int rows = jdbcTemplate.update(BatchQueries.UPDATE,
                Date.valueOf(batch.getDeliveryDate()),
                batch.getNotes(),
                batch.getInspectionAgency().getId(),
                batch.getDonorAgency().getId(),
                batch.getId());

        jdbcTemplate.update(BatchProductQueries.DELETE_BY_BATCH, batch.getId());

        if (batch.getProducts() != null) {
            batch.getProducts().forEach(product ->
                    jdbcTemplate.update(BatchProductQueries.INSERT, batch.getId(), product.getCode()));
        }

        return rows;
    }


    public int delete(int id) {
        jdbcTemplate.update(BatchProductQueries.DELETE_BY_BATCH, id);
        return jdbcTemplate.update(BatchQueries.DELETE, id);
    }

    public List<Product> findProductsByBatchId(int batchId) {
        return jdbcTemplate.query(BatchProductQueries.FIND_PRODUCTS_BY_BATCH, new Object[]{batchId}, (rs, rowNum) ->
                new Product(
                        rs.getInt("code"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
    }

    public List<Batch> findByInspectionAgencyId(int inspectionAgencyId) {
        return jdbcTemplate.query(BatchQueries.FIND_BY_INSPECTION_AGENCY, new Object[]{inspectionAgencyId}, (rs, rowNum) -> mapBatch(rs));
    }

    public List<Batch> findByDonorAgencyId(int donorAgencyId) {
        return jdbcTemplate.query(BatchQueries.FIND_BY_DONOR_AGENCY, new Object[]{donorAgencyId}, (rs, rowNum) -> mapBatch(rs));
    }
}
