package com.praktikum9;

/** Menguji dua thread yang mengubah satu penghitung bersama. */
public class MainPenghitung {
    public static void main(String[] args) throws InterruptedException {
        Penghitung penghitung = new Penghitung();

        Runnable tugasTambah = () -> {
            for (int i = 0; i < 1000; i++) {
                penghitung.tambah();
            }
        };

        Thread thread1 = new Thread(tugasTambah, "Penambah-A");
        Thread thread2 = new Thread(tugasTambah, "Penambah-B");
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        int hasil = penghitung.getNilai();
        System.out.println("Nilai akhir penghitung: " + hasil);
        if (hasil != 2000) {
            throw new AssertionError("Hasil tidak sesuai. Seharusnya 2000.");
        }
        System.out.println("Uji synchronized berhasil: 2000 penambahan tercatat.");
    }
}
