Feature: Fungsionalitas Halaman Beranda Publik
  Sebagai Pengguna, aku ingin menjelajahi halaman beranda publik Masjid As-Salam
  agar mendapatkan informasi terkini mengenai masjid dan kegiatannya.

  # ============================================================
  #  HERO SECTION & WAKTU SHOLAT (READ)
  # ============================================================

  Background:
    Given Pengguna membuka halaman beranda "https://assalam-fe.vercel.app/"

  # TC01 – EP: Tampilan Judul Utama → Berhasil memuat nama masjid
  Scenario: TC01 - Verifikasi Tampilan Nama Masjid di Hero Section
    Then Pengguna harus melihat teks judul "Masjid As-Salam" ditampilkan di layar

  # TC02 – EP: Tampilan Komponen Sholat → Berhasil menampilkan jadwal dan jam real-time
  Scenario: TC02 - Verifikasi Komponen Waktu Sholat dan Jam Real-time
    Then Pengguna harus melihat bagian "Waktu Sholat Hari Ini"
    And Pengguna memastikan jam digital real-time aktif muncul di layar

  # ============================================================
  #  AUTENTIKASI (AUTHENTICATION)
  # ============================================================

  # TC03 – EP: Klik Google Login → Sukses membuka jendela popup Google
  Scenario: TC03 - Menguji Proses Autentikasi Menggunakan Google Login
    When Pengguna mengklik tombol "Masuk dengan Google"
    Then Jendela popup autentikasi Google harus terbuka
    And Sistem memastikan URL tujuan popup tersebut mengarah ke "accounts.google.com"

  # ============================================================
  #  NAVIGASI & REDIRECTION (READ/NAVIGATE)
  # ============================================================

  # TC04 – EP: Klik Lihat Semua Kegiatan → URL berubah/pindah halaman
  Scenario: TC04 - Verifikasi Navigasi Tombol Lihat Semua Kegiatan
    When Pengguna menggulir layar ke tombol "Lihat Semua Kegiatan"
    And Pengguna mengklik tombol "Lihat Semua Kegiatan"
    Then Halaman web harus berpindah rute dari halaman beranda utama

  # TC05 – EP: Konten Berita Tersedia → Section dan grid berita tampil valid
  Scenario: TC05 - Validasi Tampilan Grid Berita dan Artikel Utama
    When Pengguna menggulir layar ke bagian section berita
    Then Judul section "Kabar Terbaru dari Masjid" harus ditampilkan
    And Sistem memastikan judul berita utama tidak kosong
    And Sistem memastikan jumlah kartu berita yang tampil minimal berjumlah 1

  # TC06 – EP: Klik Lihat Semua Berita → URL berubah/pindah halaman
  Scenario: TC06 - Menguji Navigasi Tombol Lihat Semua Berita dan Artikel
    When Pengguna menggulir layar ke bagian section berita
    And Pengguna mengklik tombol "Lihat Semua Berita dan Artikel"
    Then Halaman web harus berpindah rute dari halaman beranda utama

  # ============================================================
  #  INTEGRASI PIHAK KETIGA (THIRD-PARTY INTEGRATION)
  # ============================================================

  # TC07 – EP: Klik Buka di Google Maps → Sukses membuka tab baru menuju URL Google Maps
  Scenario: TC07 - Verifikasi Integrasi Tautan Peta Lokasi ke Google Maps
    When Pengguna menggulir layar ke komponen peta lokasi
    And Pengguna mengklik tautan "Buka di Google Maps"
    Then Sistem mendeteksi tab atau jendela baru telah terbuka
    And Sistem memastikan URL tujuan pada tab baru tersebut mengarah ke halaman Google Maps