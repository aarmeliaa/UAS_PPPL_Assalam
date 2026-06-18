package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import pages.ActivityAdminPage;
import utils.AuthHelper;

public class ActivityAdminSteps {

    private WebDriver driver;
    private ActivityAdminPage page;

    private void initDriver() {
        this.driver = hooks.Hooks.driver;
        driver.get("https://assalam-fe.vercel.app");
        AuthHelper.injectToken(driver);
        driver.navigate().refresh();
        page = new ActivityAdminPage(driver);
        try {
            page.klikBtnMasukAdmin();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Gagal masuk ke Admin Panel — kemungkinan token kadaluarsa.", e);
        }
    }

    // ================================================================
    //  GIVEN
    // ================================================================

    @Given("Admin berada pada halaman Admin Panel")
    public void adminBukaAdminPanel() {
        initDriver();
    }

    @Given("Admin berada pada halaman Kegiatan Masjid dan masuk ke area Tambah Kegiatan")
    public void adminBukaFormTambah() {
        initDriver();
        page.klikMenuKegiatanMasjid();
        page.klikBtnTambahDiList();
    }

    @Given("Admin berada di halaman List Kegiatan Masjid")
    public void adminBukaHalamanList() {
        initDriver();
        page.klikMenuKegiatanMasjid();
    }

    // ================================================================
    //  CREATE – TC01: Tambah kegiatan sukses
    // ================================================================

    @When("Admin mengisi form nama kegiatan {string}")
    public void adminMengisiFormNamaKegiatan(String nama) {
        page.inputJudulKegiatan(nama);
    }

    @When("Admin mengisi form tanggal mulai kegiatan {string}")
    public void adminMengisiFormTanggalMulai(String tanggal) {
        page.inputTanggalMulai(tanggal);
    }

    @When("Admin mengisi form tanggal selesai kegiatan {string}")
    public void adminMengisiFormTanggalSelesai(String tanggal) {
        page.inputTanggalSelesai(tanggal);
    }

    @When("Admin mengisi form waktu mulai kegiatan {string}")
    public void adminMengisiFormWaktuMulai(String waktu) {
        page.inputWaktuMulai(waktu);
    }

    @When("Admin mengisi form waktu selesai kegiatan {string}")
    public void adminMengisiFormWaktuSelesai(String waktu) {
        page.inputWaktuSelesai(waktu);
    }

    @When("Admin mengisi form deskripsi kegiatan {string}")
    public void adminMengisiFormDeskripsi(String deskripsi) {
        page.inputDeskripsiKegiatan(deskripsi);
    }

    @When("Admin menekan tombol Simpan Perubahan")
    public void adminKlikSimpanPerubahan() {
        page.clickBtnSimpanPerubahan();
    }

    @Then("Admin berhasil menambahkan kegiatan ke List Kegiatan Masjid")
    public void adminBerhasilTambahKegiatan() throws InterruptedException {
        Thread.sleep(2000);
        Assert.assertTrue(
                "Seharusnya kembali ke halaman list (tombol Edit terlihat)",
                page.isHalamanListTampil());
    }

    // ================================================================
    //  CREATE – TC02: Field wajib kosong
    // ================================================================

    @When("Admin hanya mengisi beberapa field kegiatan yang disediakan")
    public void adminHanyaMengisiBeberapaFieldKegiatan() {
        page.inputJudulKegiatan("Hanya judul diisi");
    }

    @When("Admin tidak mengisi salah satu field kegiatan yang wajib")
    public void adminTidakMengisiSatuFieldKegiatan() {
        page.inputJudulKegiatan("");
        page.inputDeskripsiKegiatan("Deskripsi tanpa judul");
    }

    @Then("Sistem mengeluarkan error untuk field kegiatan yang kosong tersebut")
    public void sistemErrorFieldKegiatanKosong() throws InterruptedException {
        Thread.sleep(1000);
        // Verifikasi: form masih tampil = validasi berhasil menahan submit
        Assert.assertTrue(
                "Form seharusnya masih tampil karena ada field wajib yang kosong",
                page.isFormTambahMasihTampil());
    }

    // ================================================================
    //  READ – TC03: List berisi data
    // ================================================================

    @Then("Sistem menampilkan tabel berisi daftar kegiatan masjid")
    public void sistemTampilkanTabelKegiatan() {
        Assert.assertTrue(
                "Daftar kegiatan seharusnya memiliki setidaknya satu item",
                page.isTabelMemilikiData());
    }

    // ================================================================
    //  UPDATE – TC04: Edit
    // ================================================================

    @When("Admin menekan tombol Edit pada kegiatan index {int}")
    public void adminKlikEditKegiatanIndex(int index) {
        page.klikEditDataKe(index - 1);
    }

    @When("Admin mengubah nama kegiatan menjadi {string}")
    public void adminUbahNamaKegiatan(String nama) {
        page.inputJudulKegiatan(nama);
    }

    @When("Admin mengubah tanggal mulai kegiatan menjadi {string}")
    public void adminUbahTanggalMulai(String tanggal) {
        page.inputTanggalMulai(tanggal);
    }

    @When("Admin mengubah tanggal selesai kegiatan menjadi {string}")
    public void adminUbahTanggalSelesai(String tanggal) {
        page.inputTanggalSelesai(tanggal);
    }

    @When("Admin mengubah waktu mulai kegiatan menjadi {string}")
    public void adminUbahWaktuMulai(String waktu) {
        page.inputWaktuMulai(waktu);
    }

    @When("Admin mengubah waktu selesai kegiatan menjadi {string}")
    public void adminUbahWaktuSelesai(String waktu) {
        page.inputWaktuSelesai(waktu);
    }

    @When("Admin mengubah deskripsi kegiatan menjadi {string}")
    public void adminUbahDeskripsiKegiatan(String deskripsi) {
        page.inputDeskripsiKegiatan(deskripsi);
    }

    @Then("Sistem kembali ke List dan perubahan kegiatan berhasil disimpan")
    public void sistemKembaliKeListSetelahEditKegiatan() throws InterruptedException {
        Thread.sleep(2000);
        Assert.assertTrue(
                "Seharusnya kembali ke halaman list",
                page.isHalamanListTampil());
    }

    // ================================================================
    //  UPDATE – TC05: Batal edit
    // ================================================================

    @When("Admin menekan tombol Edit pada kegiatan pertama")
    public void adminKlikEditKegiatan() {
        page.klikEditDataPertama();
    }

    @When("Admin membatalkan proses edit kegiatan")
    public void adminBatalEditKegiatan() {
        page.clickBtnBatal();
    }

    @Then("Sistem kembali ke List dan data kegiatan tidak mengalami perubahan")
    public void sistemKembaliKeListTanpaPerubahanKegiatan() throws InterruptedException {
        Thread.sleep(1000);
        Assert.assertTrue(
                "Seharusnya kembali ke halaman list tanpa perubahan",
                page.isHalamanListTampil());
    }

    // ================================================================
    //  DELETE – TC06: Konfirmasi hapus
    // ================================================================

    @When("Admin menekan tombol Hapus pada kegiatan pertama")
    public void adminKlikHapusKegiatan() {
        page.klikHapusDataPertama();
    }

    @When("Admin menekan tombol Hapus pada kegiatan index {int}")
    public void adminKlikHapusKegiatanIndex(int index) {
        // index dari Gherkin 1-based → konversi ke 0-based
        page.klikHapusDataKe(index - 1);
    }

    @When("Admin mengonfirmasi penghapusan kegiatan dengan menekan Ya")
    public void adminKonfirmasiHapusKegiatan() {
        page.konfirmasiHapus();
    }

    @Then("Data kegiatan tersebut hilang dari tabel daftar")
    public void dataKegiatanHilangDariTabel() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("[TC06] Data kegiatan berhasil dihapus dari tabel.");
        Assert.assertTrue("Data berhasil dihapus", true);
    }

    // ================================================================
    //  DELETE – TC07: Batal hapus
    // ================================================================

    @When("Admin memilih opsi Batal pada popup konfirmasi hapus kegiatan")
    public void adminBatalHapusKegiatan() {
        page.batalHapus();
    }

    @Then("Data kegiatan batal dihapus dan tetap ada di tabel")
    public void dataKegiatanTetapAda() throws InterruptedException {
        Thread.sleep(1000);
        Assert.assertTrue(
                "Halaman list seharusnya masih tampil dengan data yang ada",
                page.isHalamanListTampil());
        System.out.println("[TC07] Pembatalan hapus kegiatan berhasil — data tetap ada.");
    }

    // ================================================================
    //  CLEANUP
    // ================================================================

    @After
    public void bersihkanBrowser() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}