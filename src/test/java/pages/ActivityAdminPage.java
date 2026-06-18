package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ActivityAdminPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait longWait;
    private final JavascriptExecutor jsExecutor;

    // ── LOCATORS: Navigasi ────────────────────────────────────────────
    private final By btnMasukAdmin       = By.xpath("//button[contains(., 'Admin') and .//img[@alt='Admin Icon']]");
    private final By menuKegiatanMasjid  = By.xpath("//button[.//span[normalize-space()='Kegiatan Masjid']]");

    // ── LOCATORS: Tombol Tambah ──────────────────────────────────────
    private final By btnTambahDiList = By.xpath(
            "//button[contains(@class, 'bg-green-500') and contains(text(), 'Tambah Kegiatan Masjid')]");

    // ── LOCATORS: Form Tambah / Edit Kegiatan ─────────────────────────
    private final By inputJudul = By.xpath(
            "//label[normalize-space()='Nama Kegiatan']/following-sibling::input[@type='text'] " +
                    "| //input[@placeholder='Masukkan nama kegiatan']");

    // Tanggal & Waktu: label ada di div yang sama (flex-col gap-1)
    // Struktur: div.flex-col > label + input  → pakai parent div sebagai anchor
    private final By inputTanggalMulai = By.xpath(
            "//label[normalize-space()='Tanggal Kegiatan Dimulai']" +
                    "/parent::div/input[@type='date']");
    private final By inputTanggalSelesai = By.xpath(
            "//label[normalize-space()='Tanggal Kegiatan Selesai']" +
                    "/parent::div/input[@type='date']");
    private final By inputWaktuMulai = By.xpath(
            "//label[normalize-space()='Waktu Kegiatan Dimulai']" +
                    "/parent::div/input[@type='time']");
    private final By inputWaktuSelesai = By.xpath(
            "//label[normalize-space()='Waktu Kegiatan Selesai']" +
                    "/parent::div/input[@type='time']");

    // Textarea deskripsi: form Edit tidak punya placeholder → pakai label
    private final By inputDeskripsi = By.xpath(
            "//label[normalize-space()='Deskripsi']/following-sibling::textarea " +
                    "| //textarea[@placeholder='Masukkan deskripsi kegiatan']");

    // ── LOCATORS: Tombol Form ─────────────────────────────────────────
    private final By btnSimpanPerubahan = By.xpath(
            "//button[contains(@class, 'bg-green-500') and contains(text(), 'Simpan Perubahan')]");
    // Dari inspect HTML: tombol Batal di form edit pakai class bg-white + border-gray-200
    // Gunakan contains text "Batal" saja, hindari filter class disabled (Tailwind utility bukan state class)
    private final By btnBatal = By.xpath(
            "//button[normalize-space(text())='Batal']");

    // ── LOCATORS: Tombol Edit / Hapus ────────────────────────────────
    private final By btnEditPertama  = By.xpath(
            "(//button[contains(@class, 'bg-[#BBF7D0]') and contains(text(), 'Edit')])[1]");
    private final By btnHapusPertama = By.xpath(
            "(//button[contains(@class, 'bg-white') and contains(@class, 'border-gray-300') and contains(text(), 'Hapus')])[1]");

    // ── LOCATORS: Konfirmasi Hapus ──────────────────────────────────
    private final By btnKonfirmasiHapus = By.xpath("//button[contains(.,'Ya, Hapus')]");
    private final By btnBatalHapus      = By.xpath("//button[normalize-space()='Batal' and contains(@class,'bg-gray')]");

    // ── LOCATORS: List Kegiatan ──────────────────────────────────────
    private final By itemListKegiatan = By.xpath(
            "//button[contains(@class, 'bg-[#BBF7D0]') and contains(text(), 'Edit')]");

    // ── LOCATORS: Toast ───────────────────────────────────────────────
    private final By toastContainer = By.xpath(
            "//div[contains(@class, 'fixed') and contains(@class, 'bottom-6')]");

    private static final Duration TIMEOUT      = Duration.ofSeconds(15);
    private static final Duration LONG_TIMEOUT = Duration.ofSeconds(30);

    public ActivityAdminPage(WebDriver driver) {
        this.driver     = driver;
        this.wait       = new WebDriverWait(driver, TIMEOUT);
        this.longWait   = new WebDriverWait(driver, LONG_TIMEOUT);
        this.jsExecutor = (JavascriptExecutor) driver;
    }

    // ═══════════════════════════════════════════════════════════════
    //  HELPERS
    // ═══════════════════════════════════════════════════════════════

    private WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    private WebElement waitVisible(By locator) {
        return longWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void scrollIntoView(WebElement el) {
        jsExecutor.executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private void setReactValue(WebElement el, String value) {
        // Step 1-3: clear via keyboard agar React state kosong dulu
        el.click();
        sleep(200);
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        sleep(100);
        el.sendKeys(Keys.DELETE);
        sleep(200);

        // Step 4: set nilai baru via React native setter
        jsExecutor.executeScript(
                "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(" +
                        "    window.HTMLInputElement.prototype, 'value').set;" +
                        "nativeInputValueSetter.call(arguments[0], arguments[1]);" +
                        "arguments[0].dispatchEvent(new Event('input',  { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                el, value
        );
        sleep(300);
    }

    private void setReactTextAreaValue(WebElement el, String value) {
        // Clear via keyboard dulu
        el.click();
        sleep(200);
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        sleep(100);
        el.sendKeys(Keys.DELETE);
        sleep(200);

        jsExecutor.executeScript(
                "var nativeSetter = Object.getOwnPropertyDescriptor(" +
                        "    window.HTMLTextAreaElement.prototype, 'value').set;" +
                        "nativeSetter.call(arguments[0], arguments[1]);" +
                        "arguments[0].dispatchEvent(new Event('input',  { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                el, value
        );
        sleep(300);
    }

    private void setReactDateOrTimeValue(WebElement el, String value) {
        // Coba clear via JS dulu (set ke kosong) agar React state reset
        jsExecutor.executeScript(
                "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(" +
                        "    window.HTMLInputElement.prototype, 'value').set;" +
                        "nativeInputValueSetter.call(arguments[0], '');" +
                        "arguments[0].dispatchEvent(new Event('input',  { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                el
        );
        sleep(200);

        // Set nilai baru
        jsExecutor.executeScript(
                "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(" +
                        "    window.HTMLInputElement.prototype, 'value').set;" +
                        "nativeInputValueSetter.call(arguments[0], arguments[1]);" +
                        "arguments[0].dispatchEvent(new Event('input',  { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                el, value
        );
        sleep(300);

        // Trigger blur agar React commit perubahan
        jsExecutor.executeScript("arguments[0].blur();", el);
        sleep(200);
    }

    private void waitForPageStable() { sleep(2000); }
    private void delay()             { sleep(500);  }

    // ═══════════════════════════════════════════════════════════════
    //  NAVIGASI
    // ═══════════════════════════════════════════════════════════════

    public void klikBtnMasukAdmin() {
        waitClickable(btnMasukAdmin).click();
        waitForPageStable();
    }

    public void klikMenuKegiatanMasjid() {
        waitClickable(menuKegiatanMasjid).click();
        waitForPageStable();
    }

    public void klikBtnTambahDiList() {
        WebElement el = waitClickable(btnTambahDiList);
        scrollIntoView(el);
        el.click();
        waitForPageStable();
    }

    // ═══════════════════════════════════════════════════════════════
    //  INPUT FORM  (semua pakai setReactValue agar React state sync)
    // ═══════════════════════════════════════════════════════════════

    public void inputJudulKegiatan(String judul) {
        WebElement el = waitVisible(inputJudul);
        scrollIntoView(el);
        delay();
        setReactValue(el, judul);
    }

    public void inputDeskripsiKegiatan(String deskripsi) {
        WebElement el = waitVisible(inputDeskripsi);
        scrollIntoView(el);
        delay();
        setReactTextAreaValue(el, deskripsi);
    }

    public void inputTanggalMulai(String tanggal) {
        WebElement el = waitVisible(inputTanggalMulai);
        scrollIntoView(el);
        delay();
        setReactDateOrTimeValue(el, tanggal);
    }

    public void inputTanggalSelesai(String tanggal) {
        WebElement el = waitVisible(inputTanggalSelesai);
        scrollIntoView(el);
        delay();
        setReactDateOrTimeValue(el, tanggal);
    }

    public void inputWaktuMulai(String waktu) {
        WebElement el = waitVisible(inputWaktuMulai);
        scrollIntoView(el);
        delay();
        setReactDateOrTimeValue(el, waktu);
    }

    public void inputWaktuSelesai(String waktu) {
        WebElement el = waitVisible(inputWaktuSelesai);
        scrollIntoView(el);
        delay();
        setReactDateOrTimeValue(el, waktu);
    }

    // ═══════════════════════════════════════════════════════════════
    //  TOMBOL FORM
    // ═══════════════════════════════════════════════════════════════

    public void clickBtnSimpanPerubahan() {
        WebElement el = waitClickable(btnSimpanPerubahan);
        scrollIntoView(el);
        delay();
        el.click();
        waitForPageStable();
    }

    public void clickBtnBatal() {
        WebElement el = waitClickable(btnBatal);
        scrollIntoView(el);
        delay();
        el.click();
        waitForPageStable();
    }

    // ═══════════════════════════════════════════════════════════════
    //  EDIT
    // ═══════════════════════════════════════════════════════════════

    public void klikEditDataPertama() {
        WebElement el = waitClickable(btnEditPertama);
        scrollIntoView(el);
        delay();
        el.click();
        waitForPageStable();
    }

    /**
     * @param index 0-based index dari StepDef
     */
    public void klikEditDataKe(int index) {
        By semuaEditBtn = By.xpath(
                "//button[contains(@class, 'bg-[#BBF7D0]') and contains(text(), 'Edit')]");

        longWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(semuaEditBtn, index));
        sleep(500);

        String xpath = "(//button[contains(@class, 'bg-[#BBF7D0]') and contains(text(), 'Edit')])[" + (index + 1) + "]";
        By locator = By.xpath(xpath);

        WebElement el = waitClickable(locator);
        scrollIntoView(el);
        sleep(800);

        el.click();
        sleep(2000);

        boolean formMuncul = !driver.findElements(inputJudul).isEmpty();

        if (!formMuncul) {
            try {
                el = waitClickable(locator);
                scrollIntoView(el);
                sleep(500);
                jsExecutor.executeScript("arguments[0].click();", el);
                sleep(3000);
            } catch (Exception ignored) {}
        }

        // Verifikasi final: form edit harus muncul
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputJudul));
    }

    // ═══════════════════════════════════════════════════════════════
    //  HAPUS
    // ═══════════════════════════════════════════════════════════════

    public void klikHapusDataPertama() {
        klikHapusDataKe(0);
    }

    /**
     * @param index 0-based index
     */
    public void klikHapusDataKe(int index) {
        By semuaHapusBtn = By.xpath(
                "//button[contains(@class,'border-gray-300') and normalize-space(text())='Hapus']");

        longWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(semuaHapusBtn, index));
        sleep(500);

        String xpath = "(//button[contains(@class,'border-gray-300') and normalize-space(text())='Hapus'])[" + (index + 1) + "]";
        WebElement el = waitClickable(By.xpath(xpath));
        scrollIntoView(el);
        sleep(500);
        el.click();
        delay();
    }

    public void konfirmasiHapus() {
        waitClickable(btnKonfirmasiHapus).click();
        waitForPageStable();
    }

    public void batalHapus() {
        waitClickable(btnBatalHapus).click();
        waitForPageStable();
    }

    // ═══════════════════════════════════════════════════════════════
    //  VERIFIKASI
    // ═══════════════════════════════════════════════════════════════

    public boolean isHalamanListTampil() {
        try {
            longWait.until(ExpectedConditions.presenceOfElementLocated(itemListKegiatan));
            return !driver.findElements(itemListKegiatan).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFormTambahMasihTampil() {
        // Cek input judul ATAU textarea deskripsi — salah satu cukup
        return !driver.findElements(inputJudul).isEmpty()
                || !driver.findElements(inputDeskripsi).isEmpty();
    }

    public boolean isTabelMemilikiData() {
        return isHalamanListTampil();
    }

    public String getToastMessage() {
        try {
            return waitVisible(toastContainer).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}