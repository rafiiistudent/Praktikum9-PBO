package com.praktikum9;

/**
 * Contoh pembuatan thread dengan pewarisan kelas Thread.
 * Dalam konteks SiPinjamRuang, pekerja ini mensimulasikan pencetakan bukti
 * peminjaman tanpa mengakses database atau mencetak dokumen sungguhan.
 */
public class PekerjaCetak extends Thread {
    private final String namaPekerja;

    public PekerjaCetak(String namaPekerja) {
        this.namaPekerja = namaPekerja;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println(namaPekerja + " dihentikan.");
                return;
            }
            System.out.println(namaPekerja + " menyiapkan bukti peminjaman ke-" + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(namaPekerja + " terinterupsi.");
                return;
            }
        }
        System.out.println("=== " + namaPekerja + " SELESAI. ===");
    }
}
