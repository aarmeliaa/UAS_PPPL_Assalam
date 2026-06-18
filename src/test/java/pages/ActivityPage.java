package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ActivityPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait longWait;

    // ── LOCATORS: Navigasi ────────────────────────────────────────────
    // Cari span dengan teks KEGIATAN MASJID (bisa diklik)
    private final By linkKegiatanMasjid = By.xpath("//span[text()='KEGIATAN MASJID']");
    // Alternatif: cari span yang mengandung teks KEGIATAN MASJID
    private final By linkKegiatanMasjid2 = By.xpath("//span[contains(text(), 'KEGIATAN MASJID')]");
    // Alternatif: cari elemen apapun dengan teks KEGIATAN MASJID
    private final By linkKegiatanMasjid3 = By.xpath("//*[text()='KEGIATAN MASJID']");

    // ── LOCATORS: Tombol Ikut Kegiatan ────────────────────────────────
    private final By btnIkutKegiatan = By.xpath("//button[contains(text(), 'Ikut Kegiatan')]");

    // ── LOCATORS: Pagination ──────────────────────────────────────────
    private final By btnPageNumber = By.xpath("//div[contains(@class, 'flex') and contains(@class, 'justify-center')]//button");
    private final By btnPageActive = By.xpath("//button[contains(@class, 'bg-green-500')]");

    // ── LOCATORS: Toast / Notifikasi ──────────────────────────────────
    private final By toastContainer = By.xpath("//div[contains(@class, 'fixed') and contains(@class, 'bottom-6')]");
    private final By toastTitle = By.xpath("//div[contains(@class, 'fixed') and contains(@class, 'bottom-6')]//p[contains(@class, 'font-bold')]");
    private final By toastSubtitle = By.xpath("//div[contains(@class, 'fixed') and contains(@class, 'bottom-6')]//p[contains(@class, 'text-xs')]");

    // ── LOCATORS: Loading ─────────────────────────────────────────────
    private final By loadingIndicator = By.xpath("//div[contains(@class, 'animate-spin')]");

    // ── LOCATORS: Card Kegiatan ──────────────────────────────────────
    private final By activityCards = By.xpath("//div[contains(@class, 'border') and contains(@class, 'rounded-xl') and contains(@class, 'shadow-sm')]");
    private final By activityTitle = By.xpath("//div[contains(@class, 'border') and contains(@class, 'rounded-xl')]//h3");

    private static final Duration TIMEOUT = Duration.ofSeconds(15);
    private static final Duration LONG_TIMEOUT = Duration.ofSeconds(30);

    public ActivityPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
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

    private List<WebElement> waitPresenceAll(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    /** Scroll elemen ke tengah viewport sebelum diinteraksi */
    private void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    // ── Navigasi ──────────────────────────────────────────────────────
    public void klikLinkKegiatanMasjid() {
        WebElement link = null;

        // Coba cari span dengan teks KEGIATAN MASJID
        try {
            link = waitClickable(linkKegiatanMasjid);
        } catch (Exception e1) {
            try {
                link = waitClickable(linkKegiatanMasjid2);
            } catch (Exception e2) {
                try {
                    link = waitClickable(linkKegiatanMasjid3);
                } catch (Exception e3) {
                    List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), 'KEGIATAN MASJID')]"));
                    for (WebElement el : elements) {
                        if (el.isDisplayed() && el.isEnabled()) {
                            link = el;
                            break;
                        }
                    }
                }
            }
        }

        if (link == null) {
            throw new RuntimeException("Tidak dapat menemukan elemen KEGIATAN MASJID!");
        }

        wait.until(ExpectedConditions.elementToBeClickable(link));
        // Scroll ke elemen agar terlihat
        scrollIntoView(link);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        link.click();
    }

    public void navigateToKegiatanPage() {
        driver.get("https://assalam-fe.vercel.app/user/kegiatan-masjid");
        waitForPageLoad();
    }

    public void navigateFromHomeToKegiatan() {
        driver.get("https://assalam-fe.vercel.app");
        waitForPageLoad();

        // Tunggu sebentar agar halaman benar-benar loading
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        klikLinkKegiatanMasjid();
        waitForPageLoad();
        waitForLoadingComplete();
    }

    public void waitForPageLoad() {
        wait.until(driver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void waitForLoadingComplete() {
        try {
            List<WebElement> loading = driver.findElements(loadingIndicator);
            if (!loading.isEmpty()) {
                wait.until(ExpectedConditions.invisibilityOf(loading.get(0)));
            }
        } catch (Exception e) {
            // Loading sudah hilang
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ── Tombol Ikut Kegiatan ──────────────────────────────────────────
    public void klikIkutKegiatan(int index) {
        List<WebElement> buttons = waitPresenceAll(btnIkutKegiatan);

        if (buttons.isEmpty()) {
            throw new RuntimeException("Tidak ada tombol Ikut Kegiatan yang ditemukan!");
        }
        if (index >= buttons.size()) {
            throw new RuntimeException("Index " + index + " melebihi jumlah tombol (" + buttons.size() + ")");
        }

        WebElement button = buttons.get(index);
        scrollIntoView(button);
        wait.until(ExpectedConditions.elementToBeClickable(button));
        button.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isIkutKegiatanButtonAvailable() {
        return driver.findElements(btnIkutKegiatan).size() > 0;
    }

    public String getIkutKegiatanButtonText(int index) {
        List<WebElement> buttons = driver.findElements(btnIkutKegiatan);
        if (buttons.isEmpty() || index >= buttons.size()) {
            return "";
        }
        return buttons.get(index).getText().trim();
    }

    // ── Pagination ────────────────────────────────────────────────────
    public void klikPageNumber(int pageNumber) {
        String xpath = "//button[text()='" + pageNumber + "']";
        By locator = By.xpath(xpath);
        WebElement button = waitClickable(locator);
        button.click();
        waitForPageLoad();
        waitForLoadingComplete();
    }

    public void klikNextPage() {
        WebElement button = waitClickable(By.xpath("//button[text()='›']"));
        button.click();
        waitForPageLoad();
        waitForLoadingComplete();
    }

    public void klikPreviousPage() {
        WebElement button = waitClickable(By.xpath("//button[text()='‹']"));
        button.click();
        waitForPageLoad();
        waitForLoadingComplete();
    }

    public String getActivePageNumber() {
        try {
            WebElement activeButton = waitVisible(btnPageActive);
            return activeButton.getText().trim();
        } catch (Exception e) {
            return "1";
        }
    }

    public boolean isPaginationVisible() {
        return driver.findElements(btnPageNumber).size() > 0;
    }

    // ── Toast / Notifikasi ────────────────────────────────────────────
    public boolean isToastVisible() {
        return driver.findElements(toastContainer).size() > 0;
    }

    public String getToastTitle() {
        return waitVisible(toastTitle).getText();
    }

    public String getToastSubtitle() {
        return waitVisible(toastSubtitle).getText();
    }

    public String getToastMessage() {
        return getToastTitle() + " - " + getToastSubtitle();
    }

    // ── Informasi Kegiatan ────────────────────────────────────────────
    public int getActivityCount() {
        return driver.findElements(activityCards).size();
    }

    public String getActivityTitle(int index) {
        List<WebElement> titles = driver.findElements(activityTitle);
        if (titles.isEmpty() || index >= titles.size()) {
            return "Tidak ada judul";
        }
        return titles.get(index).getText().trim();
    }

    // ── Getter URL ────────────────────────────────────────────────────
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}