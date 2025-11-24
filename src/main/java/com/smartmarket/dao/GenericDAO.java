package com.smartmarket.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public abstract class GenericDAO {

    protected LocalDateTime getLocalDateTime(ResultSet rs, String columnLabel) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnLabel);
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    protected Date getDate(ResultSet rs, String columnLabel) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnLabel);
        return timestamp != null ? Date.from(timestamp.toInstant().atZone(ZoneId.systemDefault()).toInstant()) : null;
    }
}