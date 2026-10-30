package com.praktikum9;

/** Penghitung bersama untuk memperagakan sinkronisasi. */
public class Penghitung {
    private int nilai = 0;

    // Hanya satu thread pada satu waktu yang boleh menambah nilai.
    public synchronized void tambah() {
        nilai++;
    }

    public synchronized int getNilai() {
        return nilai;
    }
}
