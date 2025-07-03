
  
  🏠 KosanKu
  Aplikasi desktop berbasis JavaFX untuk mengelola keuangan kosan dengan antarmuka modern dan basis data SQLite.
  
  
  
    
    
    
    
    
  



🌟 Apa itu KosanKu?
KosanKu adalah aplikasi desktop berbasis JavaFX yang dirancang untuk mempermudah pengelolaan keuangan kosan. Dengan basis data SQLite yang ringan dan antarmuka yang ramah pengguna, aplikasi ini membantu pemilik kosan mengelola penghuni, kamar, pembayaran, dan tagihan bersama secara efisien. KosanKu menawarkan solusi modern untuk menjaga transparansi dan kemudahan dalam operasional kosan.
✨ Fitur Utama

🧑 Manajemen Penghuni: Tambah, edit, atau hapus data penghuni dengan informasi seperti nama, kontak, dan peran.
🏠 Pengelolaan Kamar: Kelola detail kamar, termasuk harga sewa dan status ketersediaan.
💸 Pencatatan Pembayaran: Catat dan lacak pembayaran bulanan (lunas/tertunda) dengan riwayat transaksi.
📑 Tagihan Bersama: Hitung dan bagi tagihan utilitas seperti listrik, air, dan Wi-Fi antar penghuni.
📊 Laporan Keuangan: Hasilkan laporan bulanan untuk pemasukan, pengeluaran, dan keuntungan bersih.
📜 Log Aktivitas: Rekam semua aktivitas sistem untuk transparansi dan keamanan.
🖥️ Antarmuka Responsif: Desain modern berbasis JavaFX yang mendukung berbagai ukuran layar.


🚀 Cara Menjalankan Proyek
📋 Prasyarat

JDK 17 atau yang lebih baru
Maven 3.6.3 atau yang lebih baru
SQLite (diintegrasikan via sqlite-jdbc, tidak perlu instalasi terpisah)
Git
Text editor (IntelliJ IDEA atau VS Code direkomendasikan)

1. Clone Repository
git clone https://github.com/<username>/kosanku.git
cd kosanku

Ganti <username> dengan nama pengguna atau URL repository Anda.
2. Install Dependensi
Pastikan koneksi internet aktif, lalu jalankan:
mvn clean install

Ini akan mengunduh dependensi yang diperlukan, seperti javafx-controls, javafx-fxml, dan sqlite-jdbc, sesuai dengan pom.xml.
3. Konfigurasi Environment
Tidak diperlukan file .env karena SQLite menggunakan file lokal (kosan.db). Pastikan file database ada di direktori proyek atau dikonfigurasi di DBUtil.java.
4. Siapkan Database

Impor file kosan.db.sql untuk membuat struktur tabel dan data awal:sqlite3 kosan.db < kosan.db.sql


Alternatifnya, aplikasi akan secara otomatis menginisialisasi database jika kode di DBUtil.java telah diatur.

5. Jalankan Aplikasi
Jalankan aplikasi dengan:
mvn javafx:run

Aplikasi akan terbuka dengan layar login. Gunakan kredensial default:

Username: admin
Password: admin123

Buka aplikasi melalui antarmuka desktop yang muncul.

🗂 Struktur Proyek
kosanku/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/mykosan/controller/
│   │   │   │   └── DashboardController.java
│   │   │   ├── com/mykosan/model/
│   │   │   │   └── (Penghuni.java, Kamar.java, dll.)
│   │   │   └── com/mykosan/util/
│   │   │       └── DBUtil.java
│   │   └── resources/
│   │       ├── fxml/
│   │       │   ├── login.fxml
│   │       │   └── user.fxml
│   │       └── css/
│   │           ├── login.css
│   │           └── user.css
├── kosan.db.sql          # Skrip SQL untuk database
├── pom.xml               # File konfigurasi Maven
└── README.md             # Dokumentasi proyek


🔐 Autentikasi

Login Sederhana: Sistem autentikasi berbasis username dan password disimpan di tabel users dalam database SQLite.
Akses Admin: Admin memiliki hak akses penuh untuk manajemen penghuni, kamar, dan laporan.
Keamanan: Password disimpan dalam format terenkripsi (disarankan menggunakan hashing di masa depan).


🛠️ Teknologi yang Digunakan

Backend: Java 17, JavaFX
Database: SQLite
Frontend: JavaFX (FXML + CSS)
Build Tool: Maven
Driver Database: sqlite-jdbc


🧪 Testing
Saat ini, proyek belum memiliki pengujian otomatis. Untuk pengembangan di masa depan, kami merekomendasikan penggunaan JUnit untuk menguji logika di controller dan util. Contoh pengujian dapat ditambahkan di direktori src/test/java.

📝 Catatan Penting

Database: Pastikan file kosan.db.sql dijalankan untuk membuat tabel seperti penghuni, kamar, dan pembayaran.
Kebersihan Kode: Pastikan tidak ada file duplikat di direktori proyek untuk menjaga struktur tetap rapi.
Keamanan: Pertimbangkan untuk mengenkripsi kredensial admin di database untuk keamanan lebih lanjut.
Kontribusi: Silakan buka issue atau pull request untuk fitur baru atau perbaikan bug.
JavaFX: Pastikan modul JavaFX diatur dengan benar di module-info.java untuk JDK 17.


🎯 Rencana Pengembangan

 Integrasi ekspor laporan ke format PDF/Excel.
 Fitur notifikasi untuk pengingat pembayaran.
 Opsi pencarian dan filter untuk penghuni dan kamar.
 Dukungan multi-pengguna dengan autentikasi berbasis token (JWT).


👨‍💻 Pengembang
Dikembangkan oleh Kelompok 2 PBO 4D untuk tugas Pemrograman Berorientasi Objek.📧 Hubungi kami melalui rizkianuari83@gmail.com untuk pertanyaan, laporan bug, atau kontribusi.


  🌟 Beri bintang di GitHub jika Anda menyukai proyek ini! 🌟
  Made with ❤️ for efficient boarding house management!


