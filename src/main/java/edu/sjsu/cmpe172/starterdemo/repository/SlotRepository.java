package edu.sjsu.cmpe172.starterdemo.repository;

import edu.sjsu.cmpe172.starterdemo.model.TimeSlot;
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
public class SlotRepository {

    private final JdbcTemplate jdbc;

    public SlotRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<TimeSlot> ROW_MAPPER = (rs, rowNum) -> {
        TimeSlot s = new TimeSlot();
        s.setSlotId(rs.getLong("slot_id"));
        s.setStylistName(rs.getString("stylist_name"));
        s.setSlotDate(rs.getString("slot_date"));
        s.setStartTime(rs.getString("start_time"));
        s.setService(rs.getString("service"));
        s.setBlocked(rs.getBoolean("is_blocked"));
        return s;
    };

    public List<TimeSlot> findAll() {
        return jdbc.query("SELECT * FROM time_slots", ROW_MAPPER);
    }

    public List<TimeSlot> findAvailable() {
        return jdbc.query("SELECT * FROM time_slots WHERE is_blocked = FALSE", ROW_MAPPER);
    }

    public Optional<TimeSlot> findById(Long slotId) {
        List<TimeSlot> results = jdbc.query(
            "SELECT * FROM time_slots WHERE slot_id = ?", ROW_MAPPER, slotId
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public TimeSlot save(TimeSlot slot) {
        int updated = jdbc.update(
            "UPDATE time_slots SET stylist_name = ?, slot_date = ?, start_time = ?, service = ?, is_blocked = ? WHERE slot_id = ?",
            slot.getStylistName(), slot.getSlotDate(), slot.getStartTime(), slot.getService(), slot.isBlocked(), slot.getSlotId()
        );
        if (updated == 0) {
            jdbc.update(
                "INSERT INTO time_slots (slot_id, stylist_name, slot_date, start_time, service, is_blocked) VALUES (?, ?, ?, ?, ?, ?)",
                slot.getSlotId(), slot.getStylistName(), slot.getSlotDate(), slot.getStartTime(), slot.getService(), slot.isBlocked()
            );
        }
        return slot;
    }

    public TimeSlot insert(TimeSlot slot) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO time_slots (stylist_name, slot_date, start_time, service, is_blocked) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, slot.getStylistName());
            ps.setString(2, slot.getSlotDate());
            ps.setString(3, slot.getStartTime());
            ps.setString(4, slot.getService());
            ps.setBoolean(5, slot.isBlocked());
            return ps;
        }, keyHolder);
        slot.setSlotId(keyHolder.getKey().longValue());
        return slot;
    }

    public void deleteById(Long slotId) {
        jdbc.update("DELETE FROM time_slots WHERE slot_id = ?", slotId);
    }
}
