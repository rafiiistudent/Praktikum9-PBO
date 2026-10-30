package com.praktikum9;

/** Menjalankan dua pekerja cetak secara bersamaan. */
public class MainThread {
    public static void main(String[] args) {
        System.out.println("=== DEMO THREAD SIPINJAMRUANG ===");
        PekerjaCetak pekerja1 = new PekerjaCetak("Thread-A");
        PekerjaCetak pekerja2 = new PekerjaCetak("Thread-B");

        // start() menjalankan run() pada thread terpisah.
        pekerja1.start();
        pekerja2.start();

        System.out.println("Program utama tetap berjalan setelah memulai kedua thread.");
    }
}
