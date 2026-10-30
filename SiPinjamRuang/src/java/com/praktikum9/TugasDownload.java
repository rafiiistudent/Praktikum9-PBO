package com.praktikum9;

/** Contoh tugas yang dijalankan melalui interface Runnable. */
public class TugasDownload implements Runnable {
    private final String namaLaporan;

    public TugasDownload(String namaLaporan) {
        this.namaLaporan = namaLaporan;
    }

    @Override
    public void run() {
        System.out.println("Mulai menyiapkan laporan: " + namaLaporan);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Penyiapan laporan terinterupsi: " + namaLaporan);
            return;
        }
        System.out.println("Laporan siap: " + namaLaporan);
    }
}
