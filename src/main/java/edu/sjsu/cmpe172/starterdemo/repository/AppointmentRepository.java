package edu.sjsu.cmpe172.starterdemo.repository;

import edu.sjsu.cmpe172.starterdemo.model.Appointment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentRepository {

    private final JdbcTemplate jdbc;

    public AppointmentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Appointment> ROW_MAPPER = (rs, rowNum) -> {
        Appointment a = new Appointment();
        a.setAppointmentId(rs.getLong("appointment_id"));
        a.setCustomerName(rs.getString("customer_name"));
        a.setCustomerEmail(rs.getString("customer_email"));
        a.setCustomerPhone(rs.getString("customer_phone"));
        a.setSlotId(rs.getLong("slot_id"));
        a.setStylistName(rs.getString("stylist_name"));
        a.setSlotDate(rs.getString("slot_date"));
        a.setStartTime(rs.getString("start_time"));
        a.setService(rs.getString("service"));
        return a;
    };

    public Appointment save(Appointment appointment) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO appointments (customer_name, customer_email, customer_phone, slot_id, stylist_name, slot_date, start_time, service) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, appointment.getCustomerName());
            ps.setString(2, appointment.getCustomerEmail());
            ps.setString(3, appointment.getCustomerPhone());
            ps.setLong(4, appointment.getSlotId());
            ps.setString(5, appointment.getStylistName());
            ps.setString(6, appointment.getSlotDate());
            ps.setString(7, appointment.getStartTime());
            ps.setString(8, appointment.getService());
            return ps;
        }, keyHolder);
        appointment.setAppointmentId(keyHolder.getKey().longValue());
        return appointment;
    }

    public Optional<Appointment> findById(Long appointmentId) {
        List<Appointment> results = jdbc.query(
            "SELECT * FROM appointments WHERE appointment_id = ?", ROW_MAPPER, appointmentId
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<Appointment> findAll() {
        return jdbc.query("SELECT * FROM appointments", ROW_MAPPER);
    }

    public void deleteById(Long appointmentId) {
        jdbc.update("DELETE FROM appointments WHERE appointment_id = ?", appointmentId);
    }
}
