# Implementasi Modul 9 pada SiPinjamRuang

## Materi yang diterapkan

1. **Extends Thread**: `com.praktikum9.PekerjaCetak` dan `MainThread` mensimulasikan dua pekerja yang menyiapkan bukti peminjaman secara bersamaan.
2. **Implements Runnable**: `TugasDownload` dan `MainRunnable` mensimulasikan dua tugas penyiapan laporan. `MainRunnable` menggunakan `start()` dan `join()`.
3. **sleep()**: dipakai pada simulasi agar jeda proses thread dapat diamati.
4. **join()**: dipakai agar program utama menunggu thread selesai.
5. **synchronized / race condition**: `Penghitung.tambah()` adalah contoh penghitung bersama yang aman dipakai dua thread.
6. **Integrasi pada aplikasi**: `PersetujuanPeminjamanService.setujui()` dan `tolak()` memakai `static synchronized`. Proses pemeriksaan bentrok jadwal dan perubahan status menjadi satu bagian kritis. Dua request persetujuan dalam satu instance aplikasi Tomcat akan diproses bergantian. Jika ada pengajuan lain yang sudah Approved pada ruangan, tanggal, dan rentang waktu yang sama, persetujuan berikutnya ditolak.

## File baru

- `src/java/com/praktikum9/PekerjaCetak.java`
- `src/java/com/praktikum9/MainThread.java`
- `src/java/com/praktikum9/TugasDownload.java`
- `src/java/com/praktikum9/MainRunnable.java`
- `src/java/com/praktikum9/Penghitung.java`
- `src/java/com/praktikum9/MainPenghitung.java`
- `src/java/service/PersetujuanPeminjamanService.java`

## File yang diubah

- `src/java/servlet/ApprovalServlet.java`: persetujuan dan penolakan diteruskan ke service.
- `web/dashboard-admin.jsp`: menampilkan pesan jika persetujuan ditolak karena jadwal bentrok.

## Cara menguji di NetBeans

1. Buka projek SiPinjamRuang.
2. Jalankan `com.praktikum9.MainThread` untuk melihat dua thread berjalan bergantian.
3. Jalankan `com.praktikum9.MainRunnable` untuk melihat `Runnable`, `sleep()`, dan `join()`.
4. Jalankan `com.praktikum9.MainPenghitung`. Hasil akhir yang diharapkan adalah **2000**.
5. Jalankan aplikasi web, masuk sebagai admin, lalu coba setujui dua pengajuan untuk ruangan, tanggal, dan waktu yang saling bertabrakan. Pengajuan pertama yang berhasil disetujui akan tetap Approved; pengajuan yang bentrok akan ditolak dan dashboard menampilkan pesan.
6. Lakukan **Clean and Build**, lalu jalankan projek.

## Catatan penting

- Sinkronisasi Java ini melindungi request yang berjalan di **satu proses JVM / satu instance Tomcat**. Jika aplikasi dikembangkan menjadi beberapa server, perlindungan bentrok juga perlu diterapkan dengan constraint/locking transaksi di database.
- Thread pada tiga kelas `Main...` merupakan program demonstrasi yang dijalankan manual dari NetBeans. Aplikasi web tidak membuat thread mentah sendiri pada setiap request; penanganan persetujuan di web memakai `synchronized` agar pengelolaan thread tetap dilakukan oleh container Servlet.
- Pengujian compile mandiri untuk kelas demo dan service dilakukan terpisah. Pengujian menyeluruh terhadap database dan deploy Tomcat memerlukan database lokal projek.
