Feature: Manajemen Kegiatan Masjid (Admin CRUD)
  Sebagai Admin, aku ingin mengelola kegiatan masjid
  agar informasi kegiatan selalu akurat dan terkini.

  # ============================================================
  #  CREATE
  # ============================================================

  Background:
    Given Admin berada pada halaman Admin Panel

  # TC01 – EP: Semua field valid
  Scenario: TC01 - Berhasil menambahkan kegiatan baru
    Given Admin berada pada halaman Kegiatan Masjid dan masuk ke area Tambah Kegiatan
    When Admin mengisi form nama kegiatan "test pppl"
    And Admin mengisi form tanggal mulai kegiatan "2026-06-19"
    And Admin mengisi form tanggal selesai kegiatan "2026-06-20"
    And Admin mengisi form waktu mulai kegiatan "04:35"
    And Admin mengisi form waktu selesai kegiatan "20:00"
    And Admin mengisi form deskripsi kegiatan "testing matkul menambahkan pppl 2026"
    And Admin menekan tombol Simpan Perubahan
    Then Admin berhasil menambahkan kegiatan ke List Kegiatan Masjid

  # TC02 – EP: Field wajib kosong
  Scenario: TC02 - Gagal menambahkan kegiatan karena field wajib kosong
    Given Admin berada pada halaman Kegiatan Masjid dan masuk ke area Tambah Kegiatan
    When Admin hanya mengisi beberapa field kegiatan yang disediakan
    And Admin tidak mengisi salah satu field kegiatan yang wajib
    And Admin menekan tombol Simpan Perubahan
    Then Sistem mengeluarkan error untuk field kegiatan yang kosong tersebut

  # ============================================================
  #  READ
  # ============================================================

  # TC03 – EP: Data tersedia
  Scenario: TC03 - Berhasil melihat daftar Kegiatan Masjid di tabel
    Given Admin berada di halaman List Kegiatan Masjid
    Then Sistem menampilkan tabel berisi daftar kegiatan masjid

  # ============================================================
  #  UPDATE
  # ============================================================

  # TC04 – EP: Edit valid
  Scenario: TC04 - Berhasil mengubah kegiatan
    Given Admin berada di halaman List Kegiatan Masjid
    When Admin menekan tombol Edit pada kegiatan index 2
    And Admin mengubah nama kegiatan menjadi "test pppl"
    And Admin mengubah tanggal mulai kegiatan menjadi "2026-06-21"
    And Admin mengubah tanggal selesai kegiatan menjadi "2026-06-22"
    And Admin mengubah waktu mulai kegiatan menjadi "08:00"
    And Admin mengubah waktu selesai kegiatan menjadi "20:00"
    And Admin mengubah deskripsi kegiatan menjadi "testing pppl edit kegiatan masjid 2026"
    And Admin menekan tombol Simpan Perubahan
    Then Sistem kembali ke List dan perubahan kegiatan berhasil disimpan

  # TC05 – EP: Batal edit
  Scenario: TC05 - Berhasil membatalkan proses Edit kegiatan
    Given Admin berada di halaman List Kegiatan Masjid
    When Admin menekan tombol Edit pada kegiatan pertama
    And Admin membatalkan proses edit kegiatan
    Then Sistem kembali ke List dan data kegiatan tidak mengalami perubahan

  # ============================================================
  #  DELETE
  # ============================================================

  # TC06 – EP: Konfirmasi hapus
  Scenario: TC06 - Berhasil menghapus
    Given Admin berada di halaman List Kegiatan Masjid
    When Admin menekan tombol Hapus pada kegiatan index 3
    And Admin mengonfirmasi penghapusan kegiatan dengan menekan Ya
    Then Data kegiatan tersebut hilang dari tabel daftar

  # TC07 – EP: Batal hapus
  Scenario: TC07 - Membatalkan proses penghapusan kegiatan
    Given Admin berada di halaman List Kegiatan Masjid
    When Admin menekan tombol Hapus pada kegiatan index 3
    And Admin memilih opsi Batal pada popup konfirmasi hapus kegiatan
    Then Data kegiatan batal dihapus dan tetap ada di tabel
