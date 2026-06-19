@regression @admin
Feature: Jam Operasional Masjid — Admin Panel

  Sebagai admin
  Saya ingin mengelola jam operasional masjid
  Agar jadwal yang ditampilkan selalu akurat

  Background:
    Given admin sudah login
    And admin membuka halaman admin jadwal operasional

  @smoke
  Scenario: Admin melihat daftar jadwal operasional
    Then admin melihat tabel jadwal 7 hari
    And setiap baris memiliki tombol toggle dan tombol Edit

  @regression
  Scenario: Admin mengubah status hari menjadi tutup
    When admin mengklik toggle pada hari "Senin"
    Then jam operasional hari "Senin" berubah menjadi "Tutup"

  @regression
  Scenario: Admin mengubah status hari menjadi buka kembali
    Given hari "Minggu" sedang tutup
    When admin mengklik toggle pada hari "Minggu"
    Then jam operasional hari "Minggu" kembali menampilkan jam buka dan tutup

  @regression
  Scenario: Admin mengedit jam operasional
    When admin mengklik tombol Edit pada hari "Sabtu"
    Then form edit terbuka dengan input jam buka dan jam tutup
    When admin mengubah jam buka menjadi "05:00"
    And admin mengubah jam tutup menjadi "22:00"
    And admin mengklik tombol Simpan
    Then jam operasional hari "Sabtu" berubah menjadi "05.00 – 22.00"

  @regression
  Scenario: Admin membatalkan edit jam operasional
    When admin mengklik tombol Edit pada hari "Sabtu"
    And admin mengubah jam buka menjadi "99:99"
    And admin mengklik tombol Batal
    Then jam operasional hari "Sabtu" kembali ke nilai semula