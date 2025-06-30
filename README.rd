# Aplikasi Manajemen Keuangan Kosan

Selamat datang di **Aplikasi Manajemen Keuangan Kosan**, solusi modern dan efisien untuk mengelola keuangan kosan Anda! Aplikasi ini dirancang dengan antarmuka yang intuitif berbasis JavaFX dan menggunakan basis data SQLite untuk menyimpan data secara terstruktur. Dengan fitur-fitur canggih seperti pelacakan pembayaran, pengelolaan tagihan bersama, dan laporan keuangan, aplikasi ini membantu Anda mengelola kosan dengan mudah dan profesional.

## Fitur Utama
- **Manajemen Penghuni**: Tambah, edit, atau hapus data penghuni dengan detail seperti nama, kontak, dan peran.
- **Pengelolaan Kamar**: Kelola informasi kamar termasuk harga dan status ketersediaan.
- **Pencatatan Pembayaran**: Catat pembayaran bulanan penghuni dan pantau statusnya (pending/lunas).
- **Tagihan Bersama**: Kelola tagihan seperti listrik, air, dan WiFi dengan pembagian kontribusi penghuni.
- **Laporan Keuangan**: Buat laporan bulanan untuk melihat total pemasukan, pengeluaran, dan keuntungan bersih.
- **Log Aktivitas**: Pantau semua aktivitas sistem untuk keamanan dan transparansi.
- **Antarmuka Responsif**: Desain modern dengan dukungan untuk berbagai ukuran layar.

## Prasyarat
Sebelum menjalankan aplikasi, pastikan Anda memiliki perangkat lunak berikut:
- **JDK 17**: Versi Java Development Kit terbaru untuk kompatibilitas penuh.
- **Maven 3.6+**: Manajer build untuk mengelola dependensi dan kompilasi.
- **SQLite**: Diintegrasikan otomatis melalui library `sqlite-jdbc`, tidak perlu instalasi terpisah.

## Cara Menjalankan
Ikuti langkah-langkah berikut untuk memulai aplikasi:

1. **Clone Repository**:
   ```bash
   git clone <url-repo>
   cd kosan-app
   ```
   (Ganti `<url-repo>` dengan URL repository Anda.)

2. **Konfigurasi Proyek**:
   - Pastikan koneksi internet aktif untuk mengunduh dependensi Maven.
   - Verifikasi file `pom.xml` untuk memastikan dependensi seperti `javafx-controls`, `javafx-fxml`, dan `sqlite-jdbc` sudah terdaftar.

3. **Jalankan Aplikasi**:
   - Buka terminal di direktori proyek.
   - Eksekusi perintah berikut untuk membangun dan menjalankan:
     ```bash
     mvn clean install
     mvn javafx:run
     ```
   - Aplikasi akan terbuka dengan layar login. Gunakan kredensial admin default (username: "admin", pass "admin123") untuk mengakses dashboard.

4. **Inisialisasi Basis Data**:
   - File `kosan.db.sql` telah disediakan untuk membuat tabel dan memasukkan data awal.
   - Pastikan file ini dieksekusi melalui SQLite atau diintegrasikan dalam kode untuk pembuatan otomatis (lihat `DBUtil.java`).

## Struktur Proyek
- **`src/main/java/com.mykosan.controller`**: Berisi `DashboardController.java` untuk logika UI.
- **`src/main/resources`**: Menyimpan file FXML (`user.fxml`, `login.fxml`) dan CSS (`user.css`, `login.css`).
- **`kosan.db.sql`**: Skrip SQL untuk membuat dan mengisi basis data.

## Kontribusi
Kami menyambut kontribusi dari komunitas! Untuk berkontribusi:
1. Fork repository ini.
2. Buat branch baru untuk fitur atau perbaikan.
3. Kirim pull request dengan deskripsi perubahan.
4. Pastikan kode Anda sesuai dengan standar proyek (JavaFX, Maven, dan dokumentasi).

## Lisensi
Aplikasi ini dirilis di bawah [Lisensi MIT](LICENSE) – bebas digunakan dan dimodifikasi sesuai kebutuhan.

## Dukungan
Jika Anda mengalami masalah atau memiliki pertanyaan, silakan buka isu di repository atau hubungi kami melalui email: `rizkianuari83@gmail.com`.

---

**Versi Awal: 1.0.0**
*Dikembangkan dengan ❤️ oleh Kelompok 2 PBO 4D – Mulai petualangan manajemen kosan Anda sekarang!*