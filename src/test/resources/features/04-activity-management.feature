Feature: Manajemen Kegiatan Masjid (Ikut Kegiatan)
  Sebagai User, aku ingin mengikuti kegiatan masjid
  agar dapat berpartisipasi dalam kegiatan komunitas.

  # ============================================================
  #  BACKGROUND: Dari Homepage ke Kegiatan Masjid
  # ============================================================

  Background:
    Given User berada pada halaman Kegiatan Masjid

  # ============================================================
  #  TEST CASE: Menekan Tombol Ikut Kegiatan
  # ============================================================

  @Follow @Positive
  Scenario: TC01 - Berhasil menekan tombol
    When User menekan tombol Ikut Kegiatan
    Then Tombol Ikut Kegiatan berhasil ditekan

  @Follow @Negative
  Scenario: TC02 - Gagal bergabung karena sudah terdaftar
    Given User sudah terdaftar
    When User menekan tombol Ikut Kegiatan
    Then Sistem menampilkan notifikasi "Kamu sudah terdaftar di kegiatan ini"

  @Follow @Positive
  Scenario: TC03 - Berhasil bergabung
    Given Terdapat kegiatan yang tersedia
    When User menekan tombol Ikut Kegiatan
    Then Sistem menampilkan notifikasi berhasil "Berhasil Bergabung"
    And User berhasil bergabung dengan kegiatan

  # ============================================================
  #  TEST CASE: Menekan Page Number
  # ============================================================

  @Pagination @Positive
  Scenario: TC04 - Berhasil pindah ke halaman 2S
    Given User berada di halaman Kegiatan Masjid langsung
    When User menekan page number 2
    Then User berada di halaman 2
    And Tombol Ikut Kegiatan tersedia

  @Pagination @Positive
  Scenario: TC05 - Tombol Ikut Kegiatan tersedia
    Given Terdapat kegiatan yang tersedia
    Then Tombol Ikut Kegiatan tersedia