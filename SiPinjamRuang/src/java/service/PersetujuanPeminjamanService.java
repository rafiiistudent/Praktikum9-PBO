package service;

import config.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

/**
 * Mengatur persetujuan peminjaman secara tersinkronisasi.
 * synchronized mencegah dua request pada satu instance aplikasi Tomcat
 * menyetujui jadwal ruangan yang saling bertabrakan pada saat bersamaan.
 */
public final class PersetujuanPeminjamanService {
    public static final String APPROVED = "APPROVED";
    public static final String REJECTED = "REJECTED";
    public static final String CONFLICT = "CONFLICT";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String NOT_PENDING = "NOT_PENDING";

    private PersetujuanPeminjamanService() {
        // Utility service; tidak perlu dibuat menjadi objek.
    }

    public static synchronized String setujui(int idPeminjaman) throws SQLException {
        if (idPeminjaman <= 0) {
            return NOT_FOUND;
        }

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) {
                throw new SQLException("Koneksi database gagal.");
            }

            boolean autoCommitAwal = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                String idRuangan;
                java.sql.Date tanggal;
                Time jamMulai;
                Time jamSelesai;
                String status;

                String sqlDetail = "SELECT id_ruangan, tanggal, jam_mulai, jam_selesai, status "
                        + "FROM PEMINJAMAN_RUANGAN WHERE id_peminjaman = ? FOR UPDATE";
                try (PreparedStatement ps = conn.prepareStatement(sqlDetail)) {
                    ps.setInt(1, idPeminjaman);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return NOT_FOUND;
                        }
                        idRuangan = rs.getString("id_ruangan");
                        tanggal = rs.getDate("tanggal");
                        jamMulai = rs.getTime("jam_mulai");
                        jamSelesai = rs.getTime("jam_selesai");
                        status = rs.getString("status");
                    }
                }

                if (!"Pending".equalsIgnoreCase(status)) {
                    conn.rollback();
                    return NOT_PENDING;
                }

                String sqlBentrok = "SELECT id_peminjaman FROM PEMINJAMAN_RUANGAN "
                        + "WHERE id_ruangan = ? AND tanggal = ? AND status = 'Approved' "
                        + "AND id_peminjaman <> ? "
                        + "AND (? < jam_selesai AND ? > jam_mulai) LIMIT 1";
                try (PreparedStatement ps = conn.prepareStatement(sqlBentrok)) {
                    ps.setString(1, idRuangan);
                    ps.setDate(2, tanggal);
                    ps.setInt(3, idPeminjaman);
                    ps.setTime(4, jamMulai);
                    ps.setTime(5, jamSelesai);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            conn.rollback();
                            return CONFLICT;
                        }
                    }
                }

                String sqlUpdate = "UPDATE PEMINJAMAN_RUANGAN SET status = 'Approved' "
                        + "WHERE id_peminjaman = ? AND status = 'Pending'";
                try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                    ps.setInt(1, idPeminjaman);
                    if (ps.executeUpdate() != 1) {
                        conn.rollback();
                        return NOT_PENDING;
                    }
                }

                conn.commit();
                return APPROVED;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(autoCommitAwal);
            }
        }
    }

    public static synchronized String tolak(int idPeminjaman) throws SQLException {
        if (idPeminjaman <= 0) {
            return NOT_FOUND;
        }

        String sql = "UPDATE PEMINJAMAN_RUANGAN SET status = 'Rejected' "
                + "WHERE id_peminjaman = ? AND status = 'Pending'";
        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) {
                throw new SQLException("Koneksi database gagal.");
            }
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idPeminjaman);
                return ps.executeUpdate() == 1 ? REJECTED : NOT_PENDING;
            }
        }
    }
}
