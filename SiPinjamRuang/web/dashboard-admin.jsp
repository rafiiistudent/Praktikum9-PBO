<%@page import="java.sql.*, config.Koneksi"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String adminSession = (String) session.getAttribute("admin");
    if (adminSession == null) {
        response.sendRedirect("login-admin.jsp?status=error&message=" + java.net.URLEncoder.encode("Silakan login sebagai Admin terlebih dahulu!", "UTF-8"));
        return;
    }
%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Admin - SipinjamRuang</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    
    <!-- Google Font: Inter -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <style>
        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
            background-color: #f8fafc;
            color: #1e293b;
        }

        .app-navbar {
            background-color: #ffffff;
            border-bottom: 1px solid #e2e8f0;
        }

        .brand-logo {
            font-weight: 700;
            color: #0f172a;
            font-size: 1.15rem;
            letter-spacing: -0.02em;
        }

        .content-card {
            background: #ffffff;
            border: 1px solid #e2e8f0;
            border-radius: 12px;
            overflow: hidden;
        }

        .card-header-title {
            padding: 14px 20px;
            font-size: 0.85rem;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 0.05em;
            color: #475569;
            background-color: #f8fafc;
            border-bottom: 1px solid #e2e8f0;
        }
    </style>
</head>
<body>

    <header class="app-navbar py-3 sticky-top">
        <div class="container d-flex align-items-center justify-content-between">
            <a class="brand-logo text-decoration-none d-flex align-items-center gap-2" href="index.jsp">
                <i class="bi bi-shield-lock-fill text-primary"></i>
                <span>SipinjamRuang</span>
                <span class="badge bg-light text-secondary border fw-normal" style="font-size: 0.7rem;">Panel Admin</span>
            </a>
            <div class="d-flex align-items-center gap-3">
                <span class="small text-secondary fw-medium">Admin: <strong><%= adminSession %></strong></span>
                <a href="login-admin.jsp" class="btn btn-outline-secondary btn-sm fw-semibold">
                    Logout
                </a>
            </div>
        </div>
    </header>

    <main class="container my-5" style="max-width: 1080px;">
        <div class="mb-4">
            <h1 class="h4 fw-bold text-slate-900 mb-1" style="letter-spacing: -0.02em;">Persetujuan Peminjaman Ruangan</h1>
            <p class="text-muted small mb-0">Kelola dan tentukan persetujuan pengajuan penggunaan ruangan oleh mahasiswa PSDKU.</p>
        </div>

        <% 
            String status = request.getParameter("status");
            if ("approved".equals(status)) {
        %>
            <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm" role="alert">
                <i class="bi bi-check-circle-fill me-2"></i> Pengajuan peminjaman berhasil disetujui (Approved).
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% } else if ("rejected".equals(status)) { %>
            <div class="alert alert-warning alert-dismissible fade show border-0 shadow-sm" role="alert">
                <i class="bi bi-x-circle-fill me-2"></i> Pengajuan peminjaman telah ditolak (Rejected).
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% } else if ("conflict".equals(status)) { %>
            <div class="alert alert-danger alert-dismissible fade show border-0 shadow-sm" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i> Pengajuan tidak dapat disetujui karena jadwal ruangan bertabrakan dengan peminjaman lain yang sudah disetujui.
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% } %>

        <div class="content-card shadow-sm">
            <div class="card-header-title d-flex justify-content-between align-items-center">
                <span>Daftar Pengajuan Masuk</span>
                <span class="badge bg-light text-secondary border fw-normal">Real-time DB</span>
            </div>
            <div class="p-0">
                <div class="table-responsive">
                    <table class="table align-middle mb-0 text-center" style="font-size: 0.88rem;">
                        <thead>
                            <tr class="text-secondary small text-uppercase" style="border-bottom: 1px solid #e2e8f0;">
                                <th class="py-3">Pemohon</th>
                                <th class="py-3">Ruangan & Kegiatan</th>
                                <th class="py-3">Waktu</th>
                                <th class="py-3">Status</th>
                                <th class="py-3">Aksi Persetujuan</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                Connection conn = null;
                                Statement stmt = null;
                                ResultSet rs = null;
                                try {
                                    conn = Koneksi.getConnection();
                                    if (conn != null) {
                                        stmt = conn.createStatement();
                                        String sql = "SELECT p.*, r.nama_ruangan, m.nim, m.nama FROM PEMINJAMAN_RUANGAN p " +
                                                     "JOIN RUANGAN r ON p.id_ruangan = r.id_ruangan " +
                                                     "JOIN MAHASISWA m ON p.id_mahasiswa = m.id_mahasiswa " +
                                                     "ORDER BY p.id_peminjaman DESC";
                                        rs = stmt.executeQuery(sql);
                                        boolean adaData = false;
                                        while(rs.next()) {
                                            adaData = true;
                                            int id = rs.getInt("id_peminjaman");
                                            String st = rs.getString("status");
                                            String butuhKomp = rs.getString("butuh_komputer");
                                            String statusTampil = "Approved".equals(st) ? "Disetujui" : ("Rejected".equals(st) ? "Ditolak" : "Pending");
                                            String badgeClass = "Approved".equals(st) ? "bg-success-subtle text-success border border-success-subtle" : ("Rejected".equals(st) ? "bg-danger-subtle text-danger border border-danger-subtle" : "bg-warning-subtle text-warning-emphasis border border-warning-subtle");

                                            String jMulai = rs.getTime("jam_mulai").toString().substring(0, 5).replace(":", ".");
                                            String jSelesai = rs.getTime("jam_selesai").toString().substring(0, 5).replace(":", ".");
                            %>
                                            <tr>
                                                <td>
                                                    <div class="fw-medium text-slate-800"><%= rs.getString("nama") %></div>
                                                    <small class="text-muted"><%= rs.getString("nim") %></small>
                                                </td>
                                                <td>
                                                    <div class="fw-semibold text-primary"><%= rs.getString("nama_ruangan") %></div>
                                                    <small class="text-secondary"><%= rs.getString("keperluan") %> (Komputer: <%= "Ya".equals(butuhKomp) ? "Ya" : "Tidak" %>)</small>
                                                </td>
                                                <td>
                                                    <span class="d-block text-secondary" style="font-size: 0.8rem;"><%= rs.getDate("tanggal") %></span>
                                                    <small class="text-muted"><%= jMulai %> - <%= jSelesai %> WIB</small>
                                                </td>
                                                <td><span class="badge <%= badgeClass %> px-2 py-1"><%= statusTampil %></span></td>
                                                <td>
                                                    <% if("Pending".equals(st)) { %>
                                                        <div class="d-flex justify-content-center gap-1">
                                                            <a href="ApprovalServlet?action=approve&id=<%= id %>" class="btn btn-success btn-sm py-1 px-2 fw-semibold" style="font-size: 0.75rem;">
                                                                Setujui
                                                            </a>
                                                            <a href="ApprovalServlet?action=reject&id=<%= id %>" class="btn btn-outline-danger btn-sm py-1 px-2 fw-semibold" style="font-size: 0.75rem;">
                                                                Tolak
                                                            </a>
                                                        </div>
                                                    <% } else { %>
                                                        <span class="text-muted small">Selesai</span>
                                                    <% } %>
                                                </td>
                                            </tr>
                            <%
                                        }
                                        if(!adaData) {
                                            out.print("<tr><td colspan='5' class='py-4 text-muted small'>Belum ada pengajuan masuk.</td></tr>");
                                        }
                                    }
                                } catch(Exception e) {
                                    out.print("<tr><td colspan='5' class='text-danger py-4'>Error: " + e.getMessage() + "</td></tr>");
                                } finally {
                                    if(rs != null) try { rs.close(); } catch(Exception e){}
                                    if(stmt != null) try { stmt.close(); } catch(Exception e){}
                                    if(conn != null) try { conn.close(); } catch(Exception e){}
                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/bootstrap.bundle.min.js"></script>
</body>
</html>