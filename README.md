# Assalam Testing

Proyek akhir mata kuliah **Praktikum Pengujian Perangkat Lunak** untuk website **Masjid As-Salam**.

| Environment | URL |
|------------|-----|
| Frontend (Vercel) | https://assalam-fe.vercel.app |
| Backend API (Railway) | https://assalam-be-production-341d.up.railway.app |
| API Docs (Swagger) | https://assalam-be-production-341d.up.railway.app/api/docs |

---

## Tech Stack

| Tool | Kegunaan |
|------|----------|
| **Java 17+** | Bahasa pemrograman |
| **Maven** | Build tool & dependency management |
| **Selenium WebDriver** | Otomatisasi browser (UI testing) |
| **Cucumber + Gherkin** | Behavior Driven Development (BDD) |
| **JUnit Platform Suite** | Test runner |
| **WebDriverManager** | Manajemen driver browser otomatis |
| **REST Assured** | API testing (langsung ke backend) |
| **AssertJ** | Assertions yang ekspresif |

---

## Struktur Proyek
```
src/
    └── test/
        ├── java/
        │   ├── runner/
        │   │   └── TestRunner.java            # Entry point Cucumber
        │   ├── hooks/
        │   │   └── Hooks.java                 # Setup/teardown WebDriver
        │   ├── pages/
        │   │   ├── HomePage.java
        │   │   ├── AdminPanelPage.java
        │   │   ├── NewsPage.java
        │   │   ├── ActivityPage.java
        │   │   └── OpHoursPage.java
        │   ├── stepdefinitions/
        │   │   ├── AuthSteps.java
        │   │   ├── NewsSteps.java
        │   │   ├── ActivitySteps.java
        │   │   ├── OperationalHourSteps.java
        │   │   └── HomepageSteps.java
        │   └── utils/
        │       └── AuthHelper.java            # Helper inject token Google
        └── resources/
            ├── features/
            │   ├── 01-authentication.feature
            │   ├── 02-news-management.feature
            │   ├── 03-activity-management.feature
            │   ├── 04-operational-hours.feature
            │   └── 05-homepage.feature
            └── config.properties              # URL, token, dll
```
---

## Fitur yang Diuji

| Fitur | Endpoint API | Role | Skenario Utama |
|-------|-------------|------|----------------|
| **Autentikasi** | `POST /api/auth/google` | Public | Login Google, akses admin, refresh token, logout, hapus akun |
| **Berita & Artikel** | `GET/POST/PUT/DELETE /api/news` | Admin | CRUD, validasi form, status publish/draft |
| **Kegiatan Masjid** | `GET/POST/PUT/DELETE /api/activities` | Admin + User | CRUD, join kegiatan, validasi waktu |
| **Jam Operasional** | `GET/POST/PUT/DELETE /api/operational-hours` | Admin | CRUD, toggle buka/tutup, edit jam |
| **Homepage** | `GET /api/home` | Public | Waktu sholat, jadwal hari ini, grid kegiatan, berita, lokasi map |

---

## User Flow

                ┌─────────────────────────┐
                │     HOMEPAGE (/)        │
                │  - Waktu Sholat         │
                │  - Jam Operasional      │
                │  - Grid Kegiatan        │
                │  - Berita & Artikel     │
                │  - Lokasi (Google Maps) │
                └──────┬──────────────────┘
                       │
          ┌────────────┴────────────┐
          ▼                         ▼
    ┌──────────────────┐     ┌──────────────────────┐
    │  TANPA LOGIN     │     │  LOGIN GOOGLE OAUTH  │
    │  - Lihat berita  │     │  - Ikut kegiatan     │
    │  - Lihat kegiatan│     │  - Sinkron kalender  │
    │  - "Masuk" btn   │     └──────────┬───────────┘
    └──────────────────┘                │
                                    ┌───┴──────────────────┐
                                    ▼                      ▼
                                ┌──────────────┐  ┌─────────────────┐
                                │  ROLE USER   │  │  ROLE ADMIN     │
                                │  - Lihat     │  │  - Panel Admin  │
                                │  - Ikut      │  │  - CRUD Berita  │
                                │  kegiatan    │  │  - CRUD Kegiatan│
                                └──────────────┘  │  - CRUD Jadwal  │
                                                  └─────────────────┘

---

## Metode Test Case

### Klasifikasi Berdasarkan Teknik

| Teknik | Contoh Penerapan |
|--------|-----------------|
| **Equivalence Partitioning** | Judul berita: 3-200 karakter (valid), <3 karakter (invalid), >200 karakter (invalid) |
| **Boundary Value Analysis** | Batas bawah (3 karakter), batas atas (200 karakter), BVA+1 (201) |
| **Positive Testing** | Isi form dengan data valid → submit berhasil |
| **Negative Testing** | End time < start time → error. Judul kosong → error |
| **State Transition** | Status berita: DRAFT → PUBLISHED → DRAFT |
| **Decision Table** | Kombinasi role (USER/ADMIN) × aksi (create/edit/delete) × hasil (sukses/403) |

### Klasifikasi Berdasarkan Level

| Level | Tools | Deskripsi          |
|-------|-------|--------------------|
| **UI / E2E Test** | Selenium + Cucumber | Test dari browser  |

---

## Cara Menjalankan Test

### Prasyarat
- Java 17+
- Chrome browser (versi terbaru)
- Maven (`mvn` tersedia di PATH)
- Access token Google (untuk test auth — lihat bagian Google OAuth)

### Install dependencies
```bash
mvn clean install -DskipTests
```
### Jalankan Semua Test (Full Regression)
```bash
mvn test
```
### Jalankan Smoke Test 
Tag skenario dengan `@smoke` di feature file:

@smoke 

Scenario: 
- Halaman homepage menampilkan waktu sholat 
- Given pengguna membuka halaman utama
-  Then waktu sholat Subuh harus terlihat

Jalankan:
```bash
mvn test -Dcucumber.filter.tags="@smoke"
```
### Jalankan Regression Test
Tag semua skenario dengan `@regression`:
```bash
mvn test -Dcucumber.filter.tags="@regression"
```
### Jalankan End-to-End (E2E) Test

E2E mensimulasikan flow lengkap dari awal sampai akhir:
```bash
mvn test -Dcucumber.filter.tags="@e2e"
```
Contoh flow E2E:

@e2e

Scenario: 
- Admin membuat berita lalu user melihatnya
-  Given admin sudah login
-  When admin membuat berita baru dengan judul "Kajian Ramadhan"
-  And admin mempublikasikan berita tersebut
-  When pengguna membuka halaman utama
- Then berita "Kajian Ramadhan" muncul di daftar berita terbaru

### Jalankan Feature Tertentu
```bash
mvn test -Dcucumber.features="src/test/resources/features/02-news-management.feature"
```
### Jalankan Berdasarkan Tag Lain
| Tag           | Kegunaan                             |
|---------------|--------------------------------------|
| `@smoke`      | Skenario kritis, jalankan cepat      |
| `@regression` | Semua skenario                       |
| `@e2e`        | Flow end-to-end lintas fitur         |
| `@positive`   | Hanya positive test case             |
| `@negative`   | Hanya negative test case             |
| `@auth`       | Hanya test autentikasi               |
| `@news`       | Hanya test berita                    |
| `@activity`   | Hanya test kegiatan                  |
| `@wip`        | Work in progress (sedang dikerjakan) |

---

## Hasil Test
### Format Laporan
Cucumber secara bawaan menghasilkan laporan dalam beberapa format:

| Format                                       | Hasil Output                            | Cara Melihat                            | 
|----------------------------------------------|-----------------------------------------|-----------------------------------------|
| HTML	                                        | target/cucumber-reports/cucumber.html	  | Buka di browser                         |
| JSON | 	target/cucumber-reports/cucumber.json	 | Bisa di-import ke CI/CD atau tools lain |               
| XML (JUnit) | target/cucumber-reports/cucumber.xml	| Integrasi dengan Jenkins, GitLab CI     |

### Contoh Output HTML
Setelah `mvn test` selesai, buka file:

`target/cucumber-reports/cucumber.html` → klik kanan → Open in Browser

Di laporan HTML akan terlihat:
- ✅ Total skenario: pass/fail
- ✅ Duration setiap step
- ✅ Screenshot otomatis saat test fail
- ✅ Timeline eksekusi

----

## Tipe Testing dalam Proyek Ini
| Tipe | Deskripsi                                                       | Contoh                                  | 
|------|-----------------------------------------------------------------|-----------------------------------------|
| Functional Testing | Memastikan fitur bekerja sesuai spesifikasi	                    | CRUD berita, login, join kegiatan       |
| UI Testing | Memastikan elemen tampil dan responsif	                         | Grid kegiatan muncul, tombol berfungsi  |
|  Validation Testing | Validasi input form	                                            | End time harus > start time             |
| Security Testing | Role-based access control	                                      | User biasa tidak bisa akses admin panel |
| Integration Testing | Antar modul berfungsi bersama	| Frontend sukses fetch data dari API     |
| E2E Testing| Flow lengkap dari user login sampai selesai	| Admin bikin berita → user lihat berita  |
----

