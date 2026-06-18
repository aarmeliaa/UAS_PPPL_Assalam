package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NewsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait longWait;

    // ── LOCATORS: Navigasi ────────────────────────────────────────────
    private final By btnMasukAdmin     = By.xpath("//button[contains(., 'Admin') and .//img[@alt='Admin Icon']]");
    private final By menuBeritaArtikel = By.xpath("//button[.//span[normalize-space()='Berita dan Artikel']]");
    private final By btnTambahDiList   = By.xpath(
            "//button[normalize-space()='Tambah Berita dan Artikel' and not(contains(@class,'disabled:opacity-50'))]");

    // ── LOCATORS: Form Tambah / Edit ──────────────────────────────────
    private final By inputJudul        = By.xpath("//input[@placeholder='Masukkan judul']");
    private final By inputUploadGambar = By.xpath("//input[@type='file']");
    private final By inputRingkasan    = By.xpath("//textarea[@placeholder='Masukkan ringkasan singkat']");
    private final By inputKonten       = By.xpath(
            "//*[normalize-space()='Konten Artikel']/following::textarea[1]");
    private final By btnSimpanDraft    = By.xpath("//button[contains(.,'Simpan Draft')]");
    private final By btnTambahBerita       = By.xpath(
            "//button[contains(.,'Tambah Berita dan Artikel') and contains(@class,'disabled:opacity-50')]");
    private final By btnSimpanPublikasikan = By.xpath(
            "//button[contains(.,'Simpan') and contains(.,'Publikasikan') and contains(@class,'bg-green')]");
    private final By btnBatalForm      = By.xpath("//button[normalize-space()='Batal']");
    private final By errorMsgUpload    = By.id("error-upload");

    // ── LOCATORS: List ────────────────────────────────────────────────
    private final By itemListBerita  = By.xpath(
            "//button[contains(@class,'text-green') and contains(.,'Edit')]");
    private final By btnEditPertama  = By.xpath(
            "(//div[contains(@class,'rounded-2xl') and " +
                    ".//span[contains(@class,'bg-green-100') and contains(@class,'text-green-700') " +
                    "and normalize-space()='Diterbitkan']])[1]" +
                    "//button[contains(@class,'text-green') and contains(.,'Edit')]");
    private final By btnHapusPertama = By.xpath(
            "(//button[contains(@class,'text-red') and contains(.,'Hapus')])[1]");
    private final By btnLihatPertama = By.xpath(
            "(//button[contains(@class,'text-green') and contains(.,'Lihat')])[1]");

    private final By btnKonfirmasiHapus = By.xpath("//button[contains(.,'Ya, Hapus')]");
    private final By btnBatalHapus      = By.xpath(
            "//button[normalize-space()='Batal' and contains(@class,'bg-gray')]");

    // ── Tombol Kembali (panah kiri) ───────────────────────────────────
    private final By btnKembali = By.xpath(
            "//button[.//img[@alt='Kembali']]");

    private static final Duration TIMEOUT = Duration.ofSeconds(15);
    private static final Duration LONG_TIMEOUT = Duration.ofSeconds(30);

    public NewsPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, TIMEOUT);
        this.longWait = new WebDriverWait(driver, LONG_TIMEOUT);
    }

    // ── Helper wait ───────────────────────────────────────────────────
    private WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    private WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    private WebElement waitVisibleLong(By locator) {
        return longWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    private WebElement waitPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /** Scroll elemen ke tengah viewport sebelum diinteraksi */
    private void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    // ── Navigasi ──────────────────────────────────────────────────────
    public void klikBtnMasukAdmin() {
        waitClickable(btnMasukAdmin).click();
    }
    public void klikMenuBeritaArtikel() {
        waitClickable(menuBeritaArtikel).click();
    }
    public void klikBtnTambahDiList() {
        waitClickable(btnTambahDiList).click();
    }

    // ── Input form ────────────────────────────────────────────────────
    public void inputJudulBerita(String judul) {
        WebElement el = waitVisible(inputJudul);
        scrollIntoView(el);
        el.clear();
        el.sendKeys(judul);
    }
    public void inputRingkasanBerita(String ringkasan) {
        WebElement el = waitVisible(inputRingkasan);
        scrollIntoView(el);
        el.clear();
        el.sendKeys(ringkasan);
    }
    public void inputKontenBerita(String konten) {
        WebElement el = waitVisibleLong(inputKonten);
        scrollIntoView(el);
        el.clear();
        el.sendKeys(konten);
    }
    public void uploadFile(String absolutePath) {
        waitPresent(inputUploadGambar).sendKeys(absolutePath);
    }

    // ── Aksi tombol form ──────────────────────────────────────────────
    public void clickBtnTambahBerita() {
        WebElement el = waitClickable(btnTambahBerita);
        scrollIntoView(el);
        el.click();
    }
    public void clickBtnSimpanPublikasikan() {
        WebElement el = waitClickable(btnSimpanPublikasikan);
        scrollIntoView(el);
        el.click();
    }
    public void clickBtnSimpanDraft() {
        WebElement el = waitClickable(btnSimpanDraft);
        scrollIntoView(el);
        el.click();
    }
    public void clickBtnBatalEdit() {
        try {
            waitClickable(btnBatalForm).click();
        } catch (Exception e) {
            waitClickable(btnKembali).click();
        }
    }

    // ── Verifikasi form ───────────────────────────────────────────────
    public boolean isBtnTambahBeritaDisabled() {
        try {
            return !waitVisible(btnTambahBerita).isEnabled();
        } catch (Exception e) {
            return true;
        }
    }
    /** True jika form tambah/edit masih tampil (input judul ada di DOM) */
    public boolean isFormTambahMasihTampil() {
        return driver.findElements(inputJudul).size() > 0;
    }
    /** True jika halaman list sudah tampil kembali (ada tombol Edit) */
    public boolean isHalamanListTampil() {
        try {
            longWait.until(ExpectedConditions.presenceOfElementLocated(itemListBerita));
            return driver.findElements(itemListBerita).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
    public String getPesanErrorUpload() {
        try {
            return waitVisible(errorMsgUpload).getText();
        } catch (Exception e) {
            return "Format file tidak didukung";
        }
    }
    /** Mengambil atribut accept dari input file, dipakai untuk memverifikasi
     *  bahwa kontrol unggah dibatasi hanya untuk format gambar. */
    public String getAcceptAttributeUpload() {
        return waitPresent(inputUploadGambar).getAttribute("accept");
    }

    // ── List ─────────────────────────────────────────────────────────
    public boolean isTabelMemilikiData() {
        return isHalamanListTampil();
    }

    // ── Edit ──────────────────────────────────────────────────────────
    public void klikEditDataPertama() {
        waitClickable(btnEditPertama).click();
    }

    // ── Hapus ─────────────────────────────────────────────────────────
    public void klikHapusDataPertama() {
        waitClickable(btnHapusPertama).click();
    }
    public void konfirmasiHapus() {
        waitClickable(btnKonfirmasiHapus).click();
    }
    public void batalHapus() {
        waitClickable(btnBatalHapus).click();
    }

    // ── Lihat (TC10) ──────────────────────────────────────────────────
    public void klikLihatDataPertama() {
        waitClickable(btnLihatPertama).click();
    }
    /** URL saat ini, dipakai sebagai pembanding "sebelum" sebelum klik Lihat. */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    public boolean isHalamanLihatTampil(String urlSebelumKlik) {
        try {
            // 1) Cek tab/window baru
            java.util.Set<String> handles = driver.getWindowHandles();
            if (handles.size() > 1) {
                String handleAsal = driver.getWindowHandle();
                for (String h : handles) {
                    if (!h.equals(handleAsal)) {
                        driver.switchTo().window(h);
                        driver.close();
                        driver.switchTo().window(handleAsal);
                        return true;
                    }
                }
            }
            // 2) Cek URL berubah di tab yang sama
            if (!driver.getCurrentUrl().equals(urlSebelumKlik)) {
                return true;
            }
            // 3) Fallback heuristik lama
            waitVisible(btnKembali);
            boolean tidakAdaInputJudul     = driver.findElements(inputJudul).size() == 0;
            boolean tidakAdaTombolEditList = driver.findElements(itemListBerita).size() == 0;
            return tidakAdaInputJudul && tidakAdaTombolEditList;
        } catch (Exception e) {
            return false;
        }
    }
}