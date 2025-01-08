/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import util.DatabaseConnection; // Import kelas DatabaseConnection
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final String TEST_DB_URL = "jdbc:mysql://localhost:3306/karyawan_db";
    private static final String TEST_DB_USER = "root";
    private static final String TEST_DB_PASSWORD = "";

    public static void main(String[] args) {
        // Pengujian koneksi database
        if (testDatabaseConnection(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD)) {
            System.out.println("Koneksi ke database berhasil!");
        } else {
            System.out.println("Koneksi ke database gagal! Periksa konfigurasi database Anda.");
            return; // Keluar jika koneksi gagal
        }

        // Menu utama aplikasi
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Menu Data Karyawan ===");
            System.out.println("1. Tampilkan Data Karyawan");
            System.out.println("2. Tambah Data Karyawan");
            System.out.println("3. Keluar");
            System.out.print("Pilih menu: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine(); // membersihkan buffer

            switch (pilihan) {
                case 1:
                    tampilkanDataKaryawan();
                    break;
                case 2:
                    tambahDataKaryawan(scanner);
                    break;
                case 3:
                    System.out.println("Keluar dari aplikasi...");
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private static boolean testDatabaseConnection(String url, String user, String password) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            return true; // Koneksi berhasil
        } catch (SQLException e) {
            System.out.println("Gagal terhubung ke database: " + e.getMessage());
            return false; // Koneksi gagal
        }
    }

    private static void tampilkanDataKaryawan() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM karyawan";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n=== Data Karyawan ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Nama: " + rs.getString("nama") +
                        ", Jabatan: " + rs.getString("jabatan") +
                        ", Gaji: " + rs.getDouble("gaji"));
            }
        } catch (SQLException e) {
            System.out.println("Gagal menampilkan data: " + e.getMessage());
        }
    }

    private static void tambahDataKaryawan(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Masukkan nama: ");
            String nama = scanner.nextLine();
            System.out.print("Masukkan jabatan: ");
            String jabatan = scanner.nextLine();
            System.out.print("Masukkan gaji: ");
            double gaji = scanner.nextDouble();

            String query = "INSERT INTO karyawan (nama, jabatan, gaji) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nama);
            stmt.setString(2, jabatan);
            stmt.setDouble(3, gaji);
            stmt.executeUpdate();

            System.out.println("Data karyawan berhasil ditambahkan!");
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan data: " + e.getMessage());
        }
    }
}
