<div align="center">
  <img src="https://img.icons8.com/color/48/000000/home.png" alt="KosanKu Logo" width="80"/>
  <h1>🏠 KosanKu</h1>
  <p>Aplikasi desktop berbasis JavaFX untuk mengelola keuangan kosan dengan antarmuka modern dan basis data SQLite.</p>
  
  <!-- Badges -->
  <p>
    <a href="https://www.java.com"><img src="https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=java" alt="Java Badge"/></a>
    <a href="https://openjfx.io"><img src="https://img.shields.io/badge/JavaFX-17-007396?style=flat-square&logo=java" alt="JavaFX Badge"/></a>
    <a href="https://www.sqlite.org"><img src="https://img.shields.io/badge/SQLite-3.42-003B57?style=flat-square&logo=sqlite" alt="SQLite Badge"/></a>
    <a href="https://maven.apache.org"><img src="https://img.shields.io/badge/Maven-3.6+-C71A36?style=flat-square&logo=apachemaven" alt="Maven Badge"/></a>
    <a href="https://opensource.org/licenses/MIT"><img src="https://img.shields.io/badge/License-MIT-blue?style=flat-square" alt="License Badge"/></a>
  </p>
</div>

---

## 🌟 Apa itu KosanKu?

**KosanKu** adalah aplikasi desktop berbasis **JavaFX** yang dirancang untuk mempermudah pengelolaan keuangan kosan. Dengan basis data **SQLite** yang ringan dan antarmuka yang ramah pengguna, aplikasi ini membantu pemilik kosan mengelola penghuni, kamar, pembayaran, dan tagihan bersama secara efisien. KosanKu menawarkan solusi modern untuk menjaga transparansi dan kemudahan dalam operasional kosan.

### ✨ Fitur Utama
- 🧑 **Manajemen Penghuni**: Tambah, edit, atau hapus data penghuni dengan informasi seperti nama, kontak, dan peran.
- 🏠 **Pengelolaan Kamar**: Kelola detail kamar, termasuk harga sewa dan status ketersediaan.
- 💸 **Pencatatan Pembayaran**: Catat dan lacak pembayaran bulanan (lunas/tertunda) dengan riwayat transaksi.
- 📑 **Tagihan Bersama**: Hitung dan bagi tagihan utilitas seperti listrik, air, dan Wi-Fi antar penghuni.
- 📊 **Laporan Keuangan**: Hasilkan laporan bulanan untuk pemasukan, pengeluaran, dan keuntungan bersih.
- 📜 **Log Aktivitas**: Rekam semua aktivitas sistem untuk transparansi dan keamanan.
- 🖥️ **Antarmuka Responsif**: Desain modern berbasis JavaFX yang mendukung berbagai ukuran layar.

---

## 🚀 Cara Menjalankan Proyek

### 📋 Prasyarat
- **JDK 17** atau yang lebih baru
- **Maven 3.6.3** atau yang lebih baru
- **SQLite** (diintegrasikan via `sqlite-jdbc`, tidak perlu instalasi terpisah)
- **Git**
- Text editor (IntelliJ IDEA atau VS Code direkomendasikan)

### 1. Clone Repository
```bash
git clone https://github.com/<username>/kosanku.git
cd kosanku
Ganti <username> dengan nama pengguna atau URL repository Anda.

2. Install Dependensi
Pastikan koneksi internet aktif, lalu jalankan:

bash

Ciutkan

Wrap

Lari

Salin
mvn clean install
Ini akan mengunduh dependensi seperti javafx-controls, javafx-fxml, dan sqlite-jdbc.

3. Konfigurasi Database
Impor file kosan.db.sql untuk membuat struktur tabel dan data awal:
bash

Ciutkan

Wrap

Lari

Salin
sqlite3 kosan.db < kosan.db.sql
Alternatifnya, aplikasi akan menginisialisasi database secara otomatis via DBUtil.java.
4. Jalankan Aplikasi
Jalankan aplikasi dengan:

bash

Ciutkan

Wrap

Lari

Salin
mvn javafx:run
Gunakan kredensial default:

Username: admin
Password: admin123
🗂 Struktur Proyek
plaintext

Ciutkan

Wrap

Salin
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
Login Sederhana: Berbasis username dan password, disimpan di tabel users dalam SQLite.
Akses Admin: Admin memiliki hak akses penuh untuk manajemen penghuni, kamar, dan laporan.
Keamanan: Password saat ini disimpan dalam format plain-text (disarankan menggunakan hashing seperti BCrypt di masa depan).
🛠️ Teknologi yang Digunakan
Backend: Java 17, JavaFX
Database: SQLite
Frontend: JavaFX (FXML + CSS)
Build Tool: Maven
Driver Database: sqlite-jdbc
🧪 Testing
Saat ini, proyek belum memiliki pengujian otomatis. Untuk masa depan, gunakan JUnit untuk menguji logika di controller dan util. Tambahkan test di src/test/java.

📝 Catatan Penting
Database: Pastikan kosan.db.sql dijalankan untuk membuat tabel seperti penghuni, kamar, dan pembayaran.
Kebersihan Kode: Hapus file duplikat untuk menjaga proyek tetap rapi.
Keamanan: Gunakan enkripsi password untuk meningkatkan keamanan.
Kontribusi: Buka issue atau pull request untuk perbaikan atau fitur baru.
🎯 Rencana Pengembangan
 Ekspor laporan ke PDF/Excel.
 Notifikasi pengingat pembayaran.
 Pencarian dan filter untuk penghuni dan kamar.
 Autentikasi berbasis JWT.
👨‍💻 Pengembang
Dikembangkan oleh Kelompok 2 PBO 4D untuk tugas Pemrograman Berorientasi Objek.

📧 Hubungi: rizkianuari83@gmail.com

🌟 Beri bintang di GitHub jika Anda menyukai proyek ini! 🌟

Made with ❤️ for efficient boarding house management!
