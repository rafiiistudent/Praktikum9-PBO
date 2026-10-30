[README_Modul9_SiPinjamRuang.md](https://github.com/user-attachments/files/34191648/README_Modul9_SiPinjamRuang.md)
# Implementasi Modul 9 PBO pada Projek SiPinjamRuang

## Apa Saja yang Ditambahkan?

### 1. Thread dengan Pewarisan Kelas `Thread`
**File baru:**
- `PekerjaCetak.java`
- `MainThread.java`
Kedua file ini mensimulasikan dua pekerja yang menyiapkan bukti peminjaman secara bersamaan menggunakan metode `start()` dan `sleep()`.

### 2. Thread dengan Interface `Runnable`
**File baru:**
- `TugasDownload.java`
- `MainRunnable.java`
Kedua file ini mensimulasikan penyiapan laporan peminjaman menggunakan interface `Runnable`. Metode `join()` digunakan agar program utama menunggu kedua tugas selesai.

### 3. Sinkronisasi dan *Race Condition*
**File baru:**
- `Penghitung.java`
- `MainPenghitung.java`
Bagian ini menggunakan kata kunci `synchronized` agar dua thread dapat menambahkan nilai ke penghitung bersama tanpa kehilangan data akibat akses bersamaan.

### 4. Penerapan Langsung pada Aplikasi
**File baru:**
- `service/PersetujuanPeminjamanService.java`
**File yang diubah:**
- `ApprovalServlet.java`
- `dashboard-admin.jsp`
Pada proses persetujuan admin, digunakan `static synchronized` untuk mengatur pemeriksaan jadwal dan perubahan status peminjaman. Sehingga, saat dua pengajuan untuk ruangan dan waktu yang sama diproses, pengajuan yang bentrok dengan peminjaman yang sudah disetujui tidak akan ikut disetujui.
Penerapan ini menghubungkan materi thread, `Runnable`, siklus hidup thread, dan sinkronisasi pada Modul 9 dengan proses persetujuan peminjaman di SiPinjamRuang.

## Hasil Pengujian

| Pengujian | Hasil |
|---|---|
| Kompilasi seluruh 19 file Java | Berhasil |
| Pengujian penghitung bersama | Berhasil |
| Nilai akhir penghitung | **2000** |

Nilai akhir penghitung sebesar **2000** diperoleh dari dua thread yang masing-masing melakukan 1.000 penambahan.

## Cara Menerapkan ke NetBeans
1. Buat salinan cadangan folder projek SiPinjamRuang yang sedang digunakan.
2. Unduh dan ekstrak `SiPinjamRuang_Modul9_Patch.zip`.
3. Salin folder dan file hasil ekstrak ke folder projek SiPinjamRuang dengan mempertahankan struktur direktorinya. Timpa file lama apabila diminta untuk file yang memang diperbarui oleh patch.
4. Buka projek di NetBeans.
5. Jalankan file berikut satu per satu menggunakan **Run File**:
   - `MainThread.java`
   - `MainRunnable.java`
   - `MainPenghitung.java`
6. Jalankan **Clean and Build**, kemudian jalankan aplikasi web.

## Catatan
- Implementasi ini mempertahankan penerapan Modul 8 pada projek.
- Jangan menjalankan ulang `DummyDataGenerator.java` agar tidak menambah 1000 data lagi.
- Pastikan konfigurasi database dan server Tomcat di NetBeans sesuai dengan lingkungan projek yang digunakan.
