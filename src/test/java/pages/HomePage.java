package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── LOCATORS: PRIORITY 1 & 2 (HERO & WAKTU SHOLAT) ──────────────
    private final By headerMasjidTitle = By.xpath("//h1[contains(text(), 'Masjid As-Salam')]");
    private final By sectionWaktuSholatTitle = By.xpath("//*[contains(text(), 'Waktu Sholat Hari Ini')]");
    private final By clockRealtime = By.cssSelector("span.text-white.text-2xl.font-bold.tracking-widest");

    // ── LOCATORS: PRIORITY 3 (AUTHENTICATION) ───────────────────────
    private final By btnGoogleLogin = By.xpath("//button[contains(., 'Masuk dengan Google')]");

    // ── LOCATORS: PRIORITY 4 (KEGIATAN) ─────────────────────────────
    private final By btnLihatSemuaKegiatan = By.xpath("//button[contains(., 'Lihat Semua Kegiatan')]");

    // ── LOCATORS: PRIORITY 5 & 6 (BERITA & ARTIKEL) ─────────────────
    private final By sectionBeritaTitle = By.xpath("//h2[contains(text(), 'Kabar Terbaru dari Masjid')]");
    private final By beritaUtamaTitle = By.xpath("//div[contains(@class, 'Kabar Terbaru dari Masjid') or .//h2]//h3");
    private final By listKartuBerita = By.xpath("//div[contains(., 'Kabar Terbaru dari Masjid')]//div[contains(@class, 'rounded')]");
    private final By btnLihatSemuaBerita = By.xpath("//button[contains(., 'Lihat Semua Berita dan Artikel')]");

    // ── LOCATORS: PRIORITY 7 (INTEGRASI MAPS) ───────────────────────
    private final By btnGoogleMaps = By.xpath("//a[contains(text(), 'Buka di Google Maps')]");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── METHODS: PRIORITY 1 & 2 (HERO & WAKTU SHOLAT) ──────────────

    public boolean isHeaderMasjidDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(headerMasjidTitle)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isWaktuSholatDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(sectionWaktuSholatTitle)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement getClockElement() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(clockRealtime));
    }

    // ── METHODS: PRIORITY 3 (AUTHENTICATION) ───────────────────────

    public boolean isGoogleLoginBtnDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(btnGoogleLogin)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickGoogleLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(btnGoogleLogin)).click();
    }

    // ── METHODS: PRIORITY 4 (KEGIATAN) ─────────────────────────────

    public boolean isLihatSemuaKegiatanDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(btnLihatSemuaKegiatan)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickLihatSemuaKegiatan() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnLihatSemuaKegiatan));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btn);
        // Beri jeda sejenak agar animasi scroll selesai seperti di step awal
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        btn.click();
    }

    // ── METHODS: PRIORITY 5 & 6 (BERITA & ARTIKEL) ─────────────────

    public void scrollToSectionBerita() {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(sectionBeritaTitle));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    public boolean isHeaderBeritaDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(sectionBeritaTitle)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getJudulBeritaUtama() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(beritaUtamaTitle)).getText();
    }

    public int getJumlahBeritaTampil() {
        List<WebElement> elements = driver.findElements(listKartuBerita);
        return elements.size();
    }

    public void clickTombolLihatSemuaBerita() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnLihatSemuaBerita));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
        btn.click();
    }

    // ── METHODS: PRIORITY 7 (INTEGRASI MAPS) ───────────────────────

    public void scrollToMapsSection() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(btnGoogleMaps));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
    }

    public void clickBukaDiGoogleMaps() {
        wait.until(ExpectedConditions.elementToBeClickable(btnGoogleMaps)).click();
    }

    // ── GLOBAL UTILITIES ──────────────────────────────────────────────

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}