Feature: Manajemen Berita dan Artikel (CRUD)
  Sebagai Admin, aku ingin mengelola konten Berita dan Artikel
  agar informasi yang ditampilkan selalu akurat dan terkini.

  # ============================================================
  #  CREATE
  # ============================================================

  Background:
    Given Admin berada pada halaman Berita dan Artikel dan masuk ke area Tambah Berita dan Artikel

  # TC01 – EP: Semua field valid → sukses publish
  Scenario: TC01 - Berhasil menambahkan berita dan artikel yang sesuai
    When Admin mengisi form judul
    And Admin mengunggah foto dengan format yang tepat
    And Admin menekan tombol Tambah Berita
    Then Admin berhasil menambahkan berita dan artikel ke List Berita dan Artikel

  # TC02 – EP: Kontrol unggah membatasi format file ke gambar saja
  Scenario: TC02 - Kontrol unggah gambar membatasi pemilihan hanya ke format gambar
    Then Kontrol unggah gambar dibatasi hanya untuk format gambar yang didukung

  # TC03 – EP: Salah satu field wajib kosong → sistem menampilkan error
  Scenario: TC03 - Gagal menambahkan berita karena salah satu field wajib tidak terisi
    When Admin hanya mengisi beberapa field yang disediakan
    And Admin tidak mengisi salah satu dari field yang disediakan
    Then Sistem mengeluarkan error untuk memasukkan field yang kosong tersebut

  # TC04 – EP: Semua field valid, simpan sebagai Draft → sukses draft
  Scenario: TC04 - Berhasil menyimpan berita dan artikel sebagai Draft
    When Admin mengisi form judul
    And Admin mengunggah foto dengan format yang tepat
    And Admin menekan tombol Simpan Draft
    Then Admin berhasil menambahkan menjadi Draft

  # ============================================================
  #  READ
  # ============================================================

  # TC05 – EP: Data tersedia → tabel tampil
  Scenario: TC05 - Berhasil melihat daftar Berita dan Artikel di tabel
    Given Admin berada di halaman List Berita dan Artikel
    Then Sistem menampilkan tabel berisi daftar berita dan artikel

  # TC06 – EP: Tombol Lihat → halaman detail/Lihat terbuka
  Scenario: TC06 - Berhasil membuka halaman Lihat pada berita dan artikel
    Given Admin berada di halaman List Berita dan Artikel
    When Admin menekan tombol Lihat pada baris pertama
    Then Sistem menampilkan halaman detail berita yang dipilih

  # ============================================================
  #  UPDATE
  # ============================================================

  # TC07 – EP: Edit konten valid → perubahan tersimpan
  Scenario: TC07 - Berhasil mengubah konten pada berita yang sudah ada
    Given Admin berada di halaman List Berita dan Artikel
    When Admin menekan tombol Edit pada baris pertama
    And Admin mengubah isi teks pada field konten
    And Admin menyimpan perubahan edit
    Then Sistem kembali ke List dan perubahan berhasil disimpan

  # TC08 – EP: Batal edit → data tidak berubah
  Scenario: TC08 - Berhasil membatalkan proses Edit berita
    Given Admin berada di halaman List Berita dan Artikel
    When Admin menekan tombol Edit pada baris pertama
    And Admin membatalkan proses edit
    Then Sistem kembali ke List dan data tidak mengalami perubahan

  # ============================================================
  #  DELETE
  # ============================================================

  # TC09 – EP: Konfirmasi hapus → data terhapus
  Scenario: TC09 - Berhasil menghapus data berita dan artikel
    Given Admin berada di halaman List Berita dan Artikel
    When Admin menekan tombol Hapus pada baris pertama
    And Admin mengonfirmasi penghapusan dengan menekan Ya
    Then Data berita tersebut hilang dari tabel daftar

  # TC10 – EP: Batal hapus → data tetap ada
  Scenario: TC10 - Membatalkan proses penghapusan berita
    Given Admin berada di halaman List Berita dan Artikel
    When Admin menekan tombol Hapus pada baris pertama
    And Admin memilih opsi Batal pada popup konfirmasi
    Then Data berita batal dihapus dan tetap ada di tabel
