# 🏠 Kosan-App

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![JavaFX](https://img.shields.io/badge/JavaFX-Enabled-green)](https://openjfx.io)
[![SQLite](https://img.shields.io/badge/Database-SQLite-lightgrey)](https://sqlite.org)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Aplikasi desktop berbasis JavaFX untuk mengelola keuangan kosan dengan antarmuka modern dan basis data SQLite.

---

## 🌟 Apa itu Kosan-App?

**Kosan-App** adalah aplikasi desktop berbasis JavaFX yang dirancang untuk mempermudah pengelolaan keuangan kosan. Dengan basis data SQLite yang ringan dan antarmuka yang ramah pengguna, aplikasi ini membantu pemilik kosan mengelola **penghuni**, **kamar**, **pembayaran**, dan **tagihan bersama** secara efisien.

---

## ✨ Fitur Utama

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

- JDK 17 atau yang lebih baru  
- Maven 3.6.3 atau yang lebih baru  
- SQLite (diintegrasikan via `sqlite-jdbc`, tidak perlu instalasi terpisah)  
- Git  
- Text editor (disarankan: IntelliJ IDEA atau VS Code)  

### 1. Clone Repository

```bash
git clone https://github.com/<username>/kosan-app.git
cd kosanku
```

> Ganti `<username>` dengan nama pengguna atau URL repository Anda.

### 2. Install Dependensi

Pastikan koneksi internet aktif, lalu jalankan:

```bash
mvn clean install
```

Ini akan mengunduh dependensi seperti `javafx-controls`, `javafx-fxml`, dan `sqlite-jdbc` sesuai `pom.xml`.

### 3. Konfigurasi Environment

Tidak diperlukan file `.env` karena SQLite menggunakan file lokal (`kosan.db`).  
Pastikan file database ada di direktori proyek atau dikonfigurasi di `DBUtil.java`.

### 4. Siapkan Database

Jalankan:

```bash
sqlite3 kosan.db < kosan.db.sql
```

Atau biarkan aplikasi membuat database secara otomatis jika kode di `DBUtil.java` telah diatur.

### 5. Jalankan Aplikasi

```bash
mvn javafx:run
```

Gunakan kredensial default:

- **Username**: `admin`  
- **Password**: `admin123`

---

## 🗂 Struktur Proyek

```
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
```

---

## 🔐 Autentikasi

- **Login Sederhana**: Autentikasi berbasis username dan password disimpan di tabel `users` pada database SQLite.  
- **Akses Admin**: Hak akses penuh untuk manajemen data.  
- **Keamanan**: Password disimpan dalam bentuk terenkripsi (*hashing* disarankan untuk versi selanjutnya).

---

## 🛠️ Teknologi yang Digunakan

- **Backend**: Java 17, JavaFX  
- **Database**: SQLite  
- **Frontend**: JavaFX (FXML + CSS)  
- **Build Tool**: Maven  
- **Driver Database**: `sqlite-jdbc`  

---

## 🧪 Testing

Belum tersedia pengujian otomatis.  
Untuk pengembangan selanjutnya, disarankan menggunakan **JUnit** untuk menguji logika pada controller dan utilitas.

Direktori pengujian yang disarankan:
```
src/test/java
```

---

## 📝 Catatan Penting

- Pastikan file `kosan.db.sql` dijalankan untuk membuat tabel `penghuni`, `kamar`, `pembayaran`, dll.  
- Jaga struktur tetap rapi, hindari file duplikat di direktori proyek.  
- Pertimbangkan penggunaan enkripsi untuk menyimpan kredensial.  
- Kontribusi sangat dihargai. Silakan buka **issue** atau **pull request**.  
- Pastikan modul JavaFX diatur di `module-info.java` untuk kompatibilitas JDK 17.

---

## 🎯 Rencana Pengembangan

- Ekspor laporan ke PDF/Excel  
- Fitur notifikasi untuk pengingat pembayaran  
- Pencarian & filter penghuni dan kamar  
- Autentikasi multi-user dengan JWT  

---

## 👨‍💻 Pengembang

Dikembangkan oleh **Kelompok 2 PBO 4D** untuk tugas Pemrograman Berorientasi Objek.

📧 Hubungi: [rizkianuari83@gmail.com](mailto:rizkianuari83@gmail.com)  
🌟 Beri bintang ⭐ di GitHub jika kamu suka proyek ini!

---

> Made with ❤️ for efficient boarding house management.
