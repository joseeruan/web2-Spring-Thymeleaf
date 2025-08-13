package jose.ruan.web2.dao;

import jose.ruan.web2.model.DonorAgency;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import static jose.ruan.web2.dao.queries.DonorAgencyQueries.*;

import java.util.Optional;

@Repository
public class DonorAgencyDAO {

    private final JdbcTemplate jdbcTemplate;

    public DonorAgencyDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DonorAgency> findAll() {
        return jdbcTemplate.query(FIND_ALL, (rs, rowNum) -> new DonorAgency(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("phone"),
                rs.getString("working_hours"),
                rs.getString("description")
        ));
    }

    public Optional<DonorAgency> findById(int id) {
        try {
            DonorAgency donorAgency = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[]{id}, (rs, rowNum) -> new DonorAgency(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("working_hours"),
                    rs.getString("description")
            ));
            return Optional.ofNullable(donorAgency);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public int save(DonorAgency agency) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, agency.getName());
            ps.setString(2, agency.getAddress());
            ps.setString(3, agency.getPhone());
            ps.setString(4, agency.getWorkingHours());
            ps.setString(5, agency.getDescription());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new RuntimeException("Failed to retrieve generated key for DonorAgency");
        }
        return key.intValue();
    }

    public int update(DonorAgency agency) {
        return jdbcTemplate.update(UPDATE,
                agency.getName(),
                agency.getAddress(),
                agency.getPhone(),
                agency.getWorkingHours(),
                agency.getDescription(),
                agency.getId());
    }

    public int delete(int id) {
        return jdbcTemplate.update(DELETE, id);
    }
}
