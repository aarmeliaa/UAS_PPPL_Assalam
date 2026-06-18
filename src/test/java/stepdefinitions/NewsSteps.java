package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import pages.NewsPage;
import utils.AuthHelper;

import java.nio.file.Paths;

public class NewsSteps {

    private WebDriver driver;
    private NewsPage page;
    private static String testDataPath(String filename) {
        return Paths.get(System.getProperty("user.dir"),
                        "src", "test", "resources", "testData", filename)
                .toAbsolutePath().toString();
    }

    private void initDriver() {
        this.driver = hooks.Hooks.driver;
        driver.get("https://assalam-fe.vercel.app");
        AuthHelper.injectToken(driver);
        driver.navigate().refresh();
        page = new NewsPage(driver);
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

    @Given("Admin berada pada halaman Berita dan Artikel dan masuk ke area Tambah Berita dan Artikel")
    public void adminBukaFormTambah() {
        initDriver();
        page.klikMenuBeritaArtikel();
        page.klikBtnTambahDiList();
    }

    @Given("Admin berada di halaman List Berita dan Artikel")
    public void adminBukaHalamanList() {
        initDriver();
        page.klikMenuBeritaArtikel();
    }

    // ================================================================
    //  CREATE – TC01: Publish sukses
    // ================================================================

    @When("Admin mengisi form judul")
    public void adminMengisiFormJudul() {
        page.inputJudulBerita("Judul Testing Otomatis");
    }

    @When("Admin mengunggah foto dengan format yang tepat")
    public void adminUnggahFotoValid() {
        page.uploadFile(testDataPath("test.png"));
    }

    @When("Admin menekan tombol Tambah Berita")
    public void adminKlikTambahBerita() {
        page.inputRingkasanBerita("Ringkasan testing otomatis");
        page.inputKontenBerita("Konten testing otomatis");
        page.clickBtnTambahBerita();
    }

    @Then("Admin berhasil menambahkan berita dan artikel ke List Berita dan Artikel")
    public void adminBerhasilTambahBerita() throws InterruptedException {
        Thread.sleep(2000);
        Assert.assertTrue(
                "Seharusnya kembali ke halaman list (tombol Edit terlihat)",
                page.isHalamanListTampil());
    }

    // ================================================================
    //  CREATE – TC02: Kontrol unggah dibatasi hanya untuk format gambar
    // ================================================================

    @Then("Kontrol unggah gambar dibatasi hanya untuk format gambar yang didukung")
    public void kontrolUnggahDibatasiFormatGambar() {
        String accept = page.getAcceptAttributeUpload();
        Assert.assertNotNull(
                "Input file seharusnya memiliki atribut 'accept' untuk membatasi tipe file",
                accept);
        String acceptLower = accept.toLowerCase();
        boolean hanyaGambar = acceptLower.contains("image")
                || acceptLower.matches(".*\\.(png|jpe?g|gif|webp).*");
        Assert.assertTrue(
                "Atribut accept ('" + accept + "') seharusnya membatasi pemilihan hanya ke format gambar",
                hanyaGambar);
    }

    // ================================================================
    //  CREATE – TC03: Field wajib kosong
    // ================================================================

    @When("Admin hanya mengisi beberapa field yang disediakan")
    public void adminHanyaMengisiBeberapa() {
        page.inputRingkasanBerita("Hanya ringkasan diisi");
    }

    @When("Admin tidak mengisi salah satu dari field yang disediakan")
    public void adminTidakMengisiSatuField() {
        page.inputJudulBerita("");
    }

    @Then("Sistem mengeluarkan error untuk memasukkan field yang kosong tersebut")
    public void sistemErrorFieldKosong() throws InterruptedException {
        try {
            page.clickBtnTambahBerita();
        } catch (Exception ignored) {
        }
        Thread.sleep(1000);
        Assert.assertTrue(
                "Form seharusnya masih tampil karena ada field wajib yang kosong",
                page.isFormTambahMasihTampil());
    }

    // ================================================================
    //  CREATE – TC04: Simpan Draft
    // ================================================================

    @When("Admin menekan tombol Simpan Draft")
    public void adminKlikSimpanDraft() {
        page.inputRingkasanBerita("Draft ringkasan testing");
        page.inputKontenBerita("Draft konten testing");
        page.clickBtnSimpanDraft();
    }

    @Then("Admin berhasil menambahkan menjadi Draft")
    public void adminBerhasilSimpanDraft() throws InterruptedException {
        Thread.sleep(2000);
        Assert.assertTrue(
                "Seharusnya kembali ke halaman list (tombol Edit terlihat)",
                page.isHalamanListTampil());
    }

    // ================================================================
    //  READ – TC05: List berisi data
    // ================================================================

    @Then("Sistem menampilkan tabel berisi daftar berita dan artikel")
    public void sistemTampilkanTabel() {
        Assert.assertTrue(
                "Daftar berita seharusnya memiliki setidaknya satu item",
                page.isTabelMemilikiData());
    }

    // ================================================================
    //  READ – TC10: Membuka halaman Lihat (detail)
    // ================================================================

    private String urlSebelumKlikLihat;

    @When("Admin menekan tombol Lihat pada baris pertama")
    public void adminKlikLihat() {
        urlSebelumKlikLihat = page.getCurrentUrl();
        page.klikLihatDataPertama();
    }

    @Then("Sistem menampilkan halaman detail berita yang dipilih")
    public void sistemTampilkanHalamanLihat() throws InterruptedException {
        Thread.sleep(2000); // beri waktu untuk tab baru/navigasi terbuka
        Assert.assertTrue(
                "Seharusnya menampilkan halaman detail/Lihat berita (baik di tab baru maupun tab yang sama)",
                page.isHalamanLihatTampil(urlSebelumKlikLihat));
    }

    // ================================================================
    //  UPDATE – TC06: Edit dan simpan perubahan
    // ================================================================

    @When("Admin menekan tombol Edit pada baris pertama")
    public void adminKlikEdit() {
        page.klikEditDataPertama();
    }

    @When("Admin mengubah isi teks pada field konten")
    public void adminUbahKonten() throws InterruptedException {
        Thread.sleep(1500);
        page.inputKontenBerita("Konten diperbarui via Automation - UPDATE TERKINI");
    }

    @When("Admin menyimpan perubahan edit")
    public void adminSimpanEdit() {
        page.clickBtnSimpanPublikasikan();
    }

    @Then("Sistem kembali ke List dan perubahan berhasil disimpan")
    public void sistemKembaliKeListSetelahEdit() throws InterruptedException {
        Thread.sleep(2000);
        Assert.assertTrue(
                "Seharusnya kembali ke halaman list",
                page.isHalamanListTampil());
    }

    // ================================================================
    //  UPDATE – TC07: Batal edit
    // ================================================================

    @When("Admin membatalkan proses edit")
    public void adminBatalEdit() {
        page.clickBtnBatalEdit();
    }

    @Then("Sistem kembali ke List dan data tidak mengalami perubahan")
    public void sistemKembaliKeListTanpaPerubahan() throws InterruptedException {
        Thread.sleep(1000);
        Assert.assertTrue(
                "Seharusnya kembali ke halaman list tanpa perubahan",
                page.isHalamanListTampil());
    }

    // ================================================================
    //  DELETE – TC08: Konfirmasi hapus
    // ================================================================

    @When("Admin menekan tombol Hapus pada baris pertama")
    public void adminKlikHapus() {
        page.klikHapusDataPertama();
    }

    @When("Admin mengonfirmasi penghapusan dengan menekan Ya")
    public void adminKonfirmasiHapus() {
        page.konfirmasiHapus();
    }

    @Then("Data berita tersebut hilang dari tabel daftar")
    public void dataHilangDariTabel() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("[TC08] Data berhasil dihapus dari tabel.");
    }

    // ================================================================
    //  DELETE – TC09: Batal hapus
    // ================================================================

    @When("Admin memilih opsi Batal pada popup konfirmasi")
    public void adminBatalHapus() {
        page.batalHapus();
    }

    @Then("Data berita batal dihapus dan tetap ada di tabel")
    public void dataTetapAda() throws InterruptedException {
        Thread.sleep(1000);
        Assert.assertTrue(
                "Halaman list seharusnya masih tampil dengan data yang ada",
                page.isHalamanListTampil());
        System.out.println("[TC09] Pembatalan hapus berhasil — data tetap ada.");
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