package jose.ruan.web2.dao;

import jose.ruan.web2.model.InspectionAgency;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static jose.ruan.web2.dao.queries.InspectionAgencyQueries.*;

@Repository
public class InspectionAgencyDAO {

    private final JdbcTemplate jdbcTemplate;

    public InspectionAgencyDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<InspectionAgency> findAll() {
        return jdbcTemplate.query(FIND_ALL, (rs, rowNum) ->
                new InspectionAgency(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
    }

    public Optional<InspectionAgency> findById(int id) {
        try {
            InspectionAgency agency = jdbcTemplate.queryForObject(
                    FIND_BY_ID,
                    new Object[]{id},
                    (rs, rowNum) -> new InspectionAgency(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description")
                    ));
            return Optional.ofNullable(agency);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public InspectionAgency save(InspectionAgency agency) {
        if (agency.getId() == null) {
            return create(agency);
        } else {
            update(agency);
            return agency;
        }
    }

    private InspectionAgency create(InspectionAgency agency) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    INSERT,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, agency.getName());
            ps.setString(2, agency.getDescription());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new RuntimeException("Falha ao obter ID da InspectionAgency");
        }

        agency.setId(key.intValue());
        return agency;
    }

    public void update(InspectionAgency agency) {
        int affectedRows = jdbcTemplate.update(
                UPDATE,
                agency.getName(),
                agency.getDescription(),
                agency.getId());

        if (affectedRows == 0) {
            throw new RuntimeException("Nenhuma agência encontrada com o ID: " + agency.getId());
        }
    }

    public boolean delete(int id) {
        int affectedRows = jdbcTemplate.update(DELETE, id);
        return affectedRows > 0;
    }
}
