package servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import service.PersetujuanPeminjamanService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ApprovalServlet", urlPatterns = {"/ApprovalServlet"})
public class ApprovalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String idInput = request.getParameter("id");

        try {
            int idPeminjaman = Integer.parseInt(idInput);
            if ("approve".equals(action)) {
                String hasil = PersetujuanPeminjamanService.setujui(idPeminjaman);
                if (PersetujuanPeminjamanService.APPROVED.equals(hasil)) {
                    response.sendRedirect("dashboard-admin.jsp?status=approved");
                } else if (PersetujuanPeminjamanService.CONFLICT.equals(hasil)) {
                    response.sendRedirect("dashboard-admin.jsp?status=conflict");
                } else {
                    response.sendRedirect("dashboard-admin.jsp?status=error");
                }
            } else if ("reject".equals(action)) {
                String hasil = PersetujuanPeminjamanService.tolak(idPeminjaman);
                if (PersetujuanPeminjamanService.REJECTED.equals(hasil)) {
                    response.sendRedirect("dashboard-admin.jsp?status=rejected");
                } else {
                    response.sendRedirect("dashboard-admin.jsp?status=error");
                }
            } else {
                response.sendRedirect("dashboard-admin.jsp?status=error");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("dashboard-admin.jsp?status=error");
        } catch (Exception e) {
            String pesan = URLEncoder.encode("Terjadi kesalahan saat memproses persetujuan.", StandardCharsets.UTF_8);
            response.sendRedirect("dashboard-admin.jsp?status=error&message=" + pesan);
        }
    }
}
