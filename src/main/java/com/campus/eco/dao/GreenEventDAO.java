package com.campus.eco.dao;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.GreenEvent;
import com.campus.eco.model.EventParticipant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GreenEventDAO {

    public boolean insert(GreenEvent e) throws SQLException {
        String sql = "INSERT INTO Green_Events (event_name, event_type, event_date, location, description, green_points) VALUES (?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getEventName());
            ps.setString(2, e.getEventType());
            ps.setDate(3, Date.valueOf(e.getEventDate()));
            ps.setString(4, e.getLocation());
            ps.setString(5, e.getDescription());
            ps.setInt(6, e.getGreenPoints());
            int r = ps.executeUpdate();
            if (r > 0) {
                try (ResultSet g = ps.getGeneratedKeys()) { if (g.next()) e.setEventId(g.getInt(1)); }
            }
            return r > 0;
        }
    }

    public List<GreenEvent> findAll() throws SQLException {
        List<GreenEvent> list = new ArrayList<>();
        String sql = "SELECT * FROM Green_Events ORDER BY event_date DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapEvent(rs));
        }
        return list;
    }

    public boolean delete(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Green_Events WHERE event_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean addParticipant(EventParticipant p) throws SQLException {
        String sql = "INSERT INTO Event_Participants (event_id, dept_id, participant_name, attended, points_awarded) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, p.getEventId());
            if (p.getDeptId() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, p.getDeptId());
            ps.setString(3, p.getParticipantName());
            ps.setInt(4, p.isAttended() ? 1 : 0);
            ps.setInt(5, p.getPointsAwarded());
            return ps.executeUpdate() > 0;
        }
    }

    public List<EventParticipant> participants(int eventId) throws SQLException {
        List<EventParticipant> list = new ArrayList<>();
        String sql = "SELECT * FROM Event_Participants WHERE event_id=? ORDER BY participant_name";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapPart(rs));
            }
        }
        return list;
    }

    private GreenEvent mapEvent(ResultSet rs) throws SQLException {
        GreenEvent e = new GreenEvent();
        e.setEventId(rs.getInt("event_id"));
        e.setEventName(rs.getString("event_name"));
        e.setEventType(rs.getString("event_type"));
        Date d = rs.getDate("event_date");
        if (d != null) e.setEventDate(d.toLocalDate());
        e.setLocation(rs.getString("location"));
        e.setDescription(rs.getString("description"));
        e.setGreenPoints(rs.getInt("green_points"));
        return e;
    }

    private EventParticipant mapPart(ResultSet rs) throws SQLException {
        EventParticipant p = new EventParticipant();
        p.setParticipantId(rs.getInt("participant_id"));
        p.setEventId(rs.getInt("event_id"));
        int d = rs.getInt("dept_id");
        p.setDeptId(rs.wasNull() ? null : d);
        p.setParticipantName(rs.getString("participant_name"));
        p.setAttended(rs.getInt("attended") == 1);
        p.setPointsAwarded(rs.getInt("points_awarded"));
        return p;
    }
}
