Aplikasi Manajemen Keuangan Kosan
Selamat datang di Aplikasi Manajemen Keuangan Kosan, solusi modern untuk mengelola keuangan kosan Anda secara efisien dan profesional. Dibangun menggunakan JavaFX untuk antarmuka pengguna yang responsif dan SQLite untuk penyimpanan data yang ringan, aplikasi ini dirancang untuk mempermudah pengelolaan kosan dengan fitur-fitur canggih seperti pelacakan pembayaran, pengelolaan tagihan bersama, dan pembuatan laporan keuangan.
Fitur Utama

Manajemen Penghuni: Tambah, edit, atau hapus data penghuni, termasuk informasi seperti nama, nomor kontak, dan peran (penghuni/admin).
Pengelolaan Kamar: Kelola informasi kamar, termasuk harga sewa, status ketersediaan, dan detail lainnya.
Pencatatan Pembayaran: Catat pembayaran bulanan penghuni, lacak status pembayaran (lunas/tertunda), dan kelola riwayat transaksi.
Tagihan Bersama: Hitung dan bagi tagihan utilitas seperti listrik, air, dan Wi-Fi secara adil antar penghuni.
Laporan Keuangan: Hasilkan laporan bulanan yang merangkum pemasukan, pengeluaran, dan keuntungan bersih.
Log Aktivitas: Rekam semua aktivitas sistem untuk memastikan transparansi dan keamanan.
Antarmuka Intuitif: Desain responsif berbasis JavaFX yang mendukung berbagai ukuran layar dengan gaya modern.

Prasyarat
Untuk menjalankan aplikasi ini, pastikan Anda memiliki perangkat lunak berikut:

JDK 17 atau yang lebih baru: Diperlukan untuk menjalankan aplikasi JavaFX.
Maven 3.6.3 atau yang lebih baru: Untuk mengelola dependensi dan membangun proyek.
SQLite: Basis data diintegrasikan melalui library sqlite-jdbc, sehingga tidak perlu instalasi terpisah.

Instalasi dan Cara Menjalankan
Ikuti langkah-langkah berikut untuk mengatur dan menjalankan aplikasi:

Clone Repository:
git clone https://github.com/<username>/kosan-app.git
cd kosan-app

Ganti <username> dengan nama pengguna atau URL repository Anda.

Konfigurasi Proyek:

Pastikan koneksi internet aktif untuk mengunduh dependensi Maven.
Verifikasi file pom.xml untuk memastikan dependensi berikut terdaftar:<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>17</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>17</version>
    </dependency>
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.42.0</version>
    </dependency>
</dependencies>




Bangun dan Jalankan Aplikasi:

Buka terminal di direktori proyek.
Jalankan perintah berikut untuk membangun proyek:mvn clean install


Jalankan aplikasi dengan:mvn javafx:run


Aplikasi akan terbuka dengan layar login. Gunakan kredensial default:
Username: admin
Password: admin123




Inisialisasi Basis Data:

File kosan.db.sql berisi skrip SQL untuk membuat tabel dan data awal.
Jalankan skrip ini melalui SQLite CLI atau pastikan kode dalam DBUtil.java menginisialisasi basis data secara otomatis.
Contoh perintah untuk menjalankan skrip:sqlite3 kosan.db < kosan.db.sql





Struktur Proyek
Berikut adalah gambaran struktur direktori proyek:
kosan-app/
├── src/
│   ├── main/
│   │苗   ├── java/
│   │   ├── com/mykosan/controller/
│   │   │   └── DashboardController.java
│   │   ├── com/mykosan/model/
│   │   │   └── (Kelas model seperti Penghuni.java, Kamar.java, dll.)
│   │   └── com/mykosan/util/
│   │       └── DBUtil.java
│   ├── resources/
│   │   ├── fxml/
│   │   │   ├── login.fxml
│   │   │   └── user.fxml
│   │   ├── css/
│   │   │   ├── login.css
│   │   │   └── user.css
├── kosan.db.sql
├── pom.xml
└── README.md

Kontribusi
Kami mengundang kontribusi dari komunitas untuk meningkatkan aplikasi ini. Langkah-langkah untuk berkontribusi:

Fork repository ini.
Buat branch baru untuk fitur atau perbaikan bug:git checkout -b feature/nama-fitur


Lakukan perubahan dan uji kode Anda.
Kirim pull request dengan deskripsi perubahan yang jelas.
Pastikan kode sesuai dengan standar proyek: gunakan JavaFX, ikuti struktur Maven, dan sertakan dokumentasi.

Lisensi
Aplikasi ini dirilis di bawah Lisensi MIT. Anda bebas menggunakan, memodifikasi, dan mendistribusikan kode sesuai kebutuhan.
Dukungan
Jika Anda mengalami kendala atau memiliki pertanyaan, silakan:

Buka issue di repository GitHub.
Hubungi kami melalui email: rizkianuari83@gmail.com.

Rencana Pengembangan

Menambahkan fitur ekspor laporan ke PDF/Excel.
Mendukung autentikasi multi-pengguna dengan peran berbeda.
Mengintegrasikan notifikasi email untuk pengingat pembayaran.


Versi: 1.0.0Dikembangkan dengan penuh semangat oleh Kelompok 2 PBO 4D. Mulai kelola kosan Anda dengan lebih mudah sekarang!
