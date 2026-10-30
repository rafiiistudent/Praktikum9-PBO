package com.praktikum9;

/** Menjalankan dua tugas Runnable dan menunggu keduanya selesai dengan join(). */
public class MainRunnable {
    public static void main(String[] args) {
        TugasDownload tugas1 = new TugasDownload("Laporan Peminjaman Ruangan");
        TugasDownload tugas2 = new TugasDownload("Rekap Persetujuan Peminjaman");
        Thread thread1 = new Thread(tugas1, "Thread-Laporan-A");
        Thread thread2 = new Thread(tugas2, "Thread-Laporan-B");

        thread1.start();
        thread2.start();

        try {
            // join() membuat thread utama menunggu dua tugas selesai.
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Program utama terinterupsi saat menunggu laporan.");
            return;
        }

        System.out.println("Semua proses penyiapan laporan selesai.");
    }
}
