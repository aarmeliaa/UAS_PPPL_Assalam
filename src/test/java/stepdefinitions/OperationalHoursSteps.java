package stepdefinitions;

import hooks.Hooks;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.assertj.core.api.Assertions.assertThat;
import pages.OpHoursPage;

public class OperationalHoursSteps {
    private final OpHoursPage opHoursPage = new OpHoursPage(Hooks.driver);

    // ─── Background / Given ───

    @Given("pengguna membuka halaman utama")
    public void penggunaMembukaHalamanUtama() {
        opHoursPage.openHomepage();
    }

    @Given("admin sudah login")
    public void adminSudahLogin() {
        // Token di-inject di Hooks.java @Before
    }

    @Given("admin membuka halaman admin jadwal operasional")
    public void adminMembukaAdminJadwalOperasional() {
        opHoursPage.openAdminOpHoursPage();
    }

    @Given("jadwal hari {string} adalah {string}")
    public void jadwalHariAdalahTutup(String day, String status) {
        opHoursPage.openAdminOpHoursPage();
        if (status.equals("Tutup") && !opHoursPage.isToggleInactive(day)) {
            opHoursPage.clickToggle(day);
        } else if (status.equals("Buka") && !opHoursPage.isToggleActive(day)) {
            opHoursPage.clickToggle(day);
        }
        opHoursPage.openHomepage();
    }

    @Given("hari {string} sedang tutup")
    public void hariSedangTutup(String day) {
        opHoursPage.openAdminOpHoursPage();
        if (!opHoursPage.isToggleInactive(day)) {
            opHoursPage.clickToggle(day);
        }
    }

    // ─── Homepage steps ───

    @Then("pengguna melihat section jadwal operasional")
    public void melihatSectionJadwal() {
        assertThat(opHoursPage.isScheduleSectionDisplayed())
                .as("Section jadwal operasional harus muncul").isTrue();
    }

    @Then("terdapat 7 baris hari \\(Senin sampai Minggu)")
    public void terdapatTujuhBaris() {
        assertThat(opHoursPage.getDayRowCount())
                .as("Jumlah baris hari harus 7").isEqualTo(7);
    }

    @Then("setiap baris menampilkan jam buka dan jam tutup")
    public void setiapBarisMenampilkanJam() {
        String[] days = {"Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu", "Minggu"};
        for (String day : days) {
            String timeText = opHoursPage.getDayTimeText(day);
            assertThat(timeText.contains("–") || timeText.equals("Tutup"))
                    .as("Hari " + day + " harus menampilkan jam atau 'Tutup', ditemukan: " + timeText)
                    .isTrue();
        }
    }

    @Then("baris hari ini memiliki badge {string}")
    public void barisHariIniMemilikiBadge(String badge) {
        assertThat(opHoursPage.isTodayBadgeDisplayed())
                .as("Badge '" + badge + "' harus muncul").isTrue();
    }

    @Then("baris hari ini memiliki background hijau muda")
    public void barisHariIniBackgroundHijau() {
        String today = getTodayIndonesianName();
        assertThat(opHoursPage.isDayHighlightedGreen(today))
                .as("Baris hari " + today + " harus background hijau").isTrue();
    }

    @Then("terdapat badge status bertuliskan Masjid Saat Ini Buka atau Masjid Saat Ini Tutup")
    public void badgeStatusTampil() {
        String text = opHoursPage.getStatusBadgeText();
        assertThat(text.contains("Masjid Saat Ini Buka") || text.contains("Masjid Saat Ini Tutup"))
                .as("Badge status harus bertuliskan 'Masjid Saat Ini Buka' atau 'Masjid Saat Ini Tutup', ditemukan: " + text)
                .isTrue();
    }

    @Then("badge tersebut berwarna hijau jika buka, abu-abu jika tutup")
    public void badgeColorCheck() {
        String text = opHoursPage.getStatusBadgeText();
        String color = opHoursPage.getStatusBadgeColor();
        if (text.contains("Buka")) {
            assertThat(color).as("Badge harus hijau saat buka").isEqualTo("green");
        } else {
            assertThat(color).as("Badge harus abu-abu saat tutup").isEqualTo("gray");
        }
    }

    @Then("pada baris hari {string} tampil teks Tutup dengan warna merah")
    public void barisTutupWarnaMerah(String day) {
        assertThat(opHoursPage.isDayClosed(day))
                .as("Hari " + day + " harus menampilkan 'Tutup'").isTrue();
    }

    @Then("jam buka dan jam tutup ditampilkan dalam format HH:MM")
    public void jamFormatHHMM() {
        String[] days = {"Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu", "Minggu"};
        for (String day : days) {
            String text = opHoursPage.getDayTimeText(day);
            if (!text.equals("Tutup") && text.contains("–")) {
                String[] parts = text.split("–");
                assertThat(parts[0].trim()).as("Jam buka hari " + day + " harus format HH:MM")
                        .matches("\\d{2}:\\d{2}");
                assertThat(parts[1].trim()).as("Jam tutup hari " + day + " harus format HH:MM")
                        .matches("\\d{2}:\\d{2}");
            }
        }
    }

    @Given("pengguna melihat jadwal hari {string}")
    public void melihatJadwalHari(String day) {
        assertThat(opHoursPage.getDayTimeText(day))
                .as("Jadwal hari " + day + " harus ditemukan").isNotNull();
    }

    // ─── Admin steps ───

    @Then("admin melihat tabel jadwal 7 hari")
    public void adminMelihatTabelJadwal() {
        assertThat(opHoursPage.isAdminPageDisplayed())
                .as("Halaman admin jadwal operasional harus tampil").isTrue();
    }

    @Then("setiap baris memiliki tombol toggle dan tombol Edit")
    public void setiapBarisMemilikiToggleDanEdit() {
        String[] days = {"Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu", "Minggu"};
        for (String day : days) {
            boolean hasToggle = opHoursPage.isToggleActive(day) || opHoursPage.isToggleInactive(day);
            assertThat(hasToggle).as("Hari " + day + " harus memiliki toggle").isTrue();
        }
    }

    @When("admin mengklik toggle pada hari {string}")
    public void adminKlikToggle(String day) {
        opHoursPage.clickToggle(day);
    }

    @Then("jam operasional hari {string} berubah menjadi Tutup")
    public void jamBerubahTutup(String day) {
        assertThat(opHoursPage.isToggleInactive(day))
                .as("Toggle hari " + day + " harus tidak aktif").isTrue();
        assertThat(opHoursPage.getAdminDayTimeText(day).trim())
                .as("Teks harus berubah jadi 'Tutup'").isEqualTo("Tutup");
    }

    @Then("jam operasional hari {string} kembali menampilkan jam buka dan tutup")
    public void jamKembaliNormal(String day) {
        assertThat(opHoursPage.isToggleActive(day))
                .as("Toggle hari " + day + " harus aktif").isTrue();
        assertThat(opHoursPage.getAdminDayTimeText(day)).contains("–");
    }

    @When("admin mengklik tombol Edit pada hari {string}")
    public void adminKlikEdit(String day) {
        opHoursPage.clickEdit(day);
    }

    @Then("form edit terbuka dengan input jam buka dan jam tutup")
    public void formEditTerbuka() {
        assertThat(opHoursPage.isEditFormVisible("Sabtu"))
                .as("Form edit harus muncul").isTrue();
    }

    @When("admin mengubah jam buka menjadi {string}")
    public void adminUbahJamBuka(String time) {
        opHoursPage.setOpenTime("Sabtu", time);
    }

    @When("admin mengubah jam tutup menjadi {string}")
    public void adminUbahJamTutup(String time) {
        opHoursPage.setCloseTime("Sabtu", time);
    }

    @When("admin mengklik tombol Simpan")
    public void adminKlikSimpan() {
        opHoursPage.clickSave("Sabtu");
        opHoursPage.waitForSaveComplete("Sabtu");
    }

    @Then("jam operasional hari {string} berubah menjadi {string}")
    public void jamBerubah(String day, String expectedTime) {
        assertThat(opHoursPage.getAdminDayTimeText(day).trim())
                .as("Jam operasional hari " + day + " tidak sesuai")
                .isEqualTo(expectedTime);
    }

    @When("admin mengklik tombol Batal")
    public void adminKlikBatal() {
        opHoursPage.clickCancel("Sabtu");
    }

    @Then("jam operasional hari {string} kembali ke nilai semula")
    public void jamKembaliKeNilaiSemula(String day) {
        assertThat(opHoursPage.isEditFormVisible(day))
                .as("Form edit harus tertutup").isFalse();
    }

    @When("admin mengubah jam buka hari {string} menjadi {string}")
    public void adminUbahJamBukaHari(String day, String time) {
        opHoursPage.clickEdit(day);
        opHoursPage.setOpenTime(day, time);
    }

    @Then("jam buka hari {string} menampilkan {string}")
    public void jamBukaMenampilkan(String day, String expectedTime) {
        String actual = opHoursPage.getAdminDayTimeText(day).trim();
        assertThat(actual).as("Jam buka hari " + day).contains(expectedTime);
    }

    // ─── Helper ───

    private String getTodayIndonesianName() {
        String[] days = {"Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu"};
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1;
        return days[dayOfWeek];
    }
}
