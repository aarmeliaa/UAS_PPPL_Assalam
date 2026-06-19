@regression
Feature: Jam Operasional Masjid

  Sebagai pengguna website Masjid As-Salam
  Saya ingin melihat dan mengelola jam operasional masjid
  Agar mengetahui kapan masjid buka dan tutup

  Background:
    Given pengguna membuka halaman utama

  @smoke
  Scenario: Menampilkan jadwal operasional 7 hari di homepage
    Then pengguna melihat section jadwal operasional
    And terdapat 7 baris hari (Senin sampai Minggu)
    And setiap baris menampilkan jam buka dan jam tutup

  @smoke
  Scenario: Hari ini ditandai dengan badge "HARI INI"
    Then baris hari ini memiliki badge "HARI INI"
    And baris hari ini memiliki background hijau muda

  @smoke
  Scenario: Status masjid (buka/tutup) tampil di atas jadwal
    Then terdapat badge status bertuliskan Masjid Saat Ini Buka atau Masjid Saat Ini Tutup
    And badge tersebut berwarna hijau jika buka, abu-abu jika tutup

  @regression
  Scenario: Hari yang tutup menampilkan teks "Tutup" berwarna merah
    Given jadwal hari "Minggu" adalah "Tutup"
    Then pada baris hari "Minggu" tampil teks Tutup dengan warna merah

  @regression
  Scenario Outline: Setiap hari menampilkan jam dalam format yang benar
    Given pengguna melihat jadwal hari "<hari>"
    Then jam buka dan jam tutup ditampilkan dalam format HH:MM

    Examples:
      | hari    |
      | Senin   |
      | Selasa  |
      | Rabu    |
      | Kamis   |
      | Jum'at  |
      | Sabtu   |
      | Minggu  |