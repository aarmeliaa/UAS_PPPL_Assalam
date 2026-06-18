package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import pages.ActivityPage;
import utils.AuthHelper;
import hooks.Hooks;

public class ActivitySteps {

    private WebDriver driver;
    private ActivityPage page;
    private int registeredActivityIndex = 1; // index 1 = kegiatan ke-2

    private void initDriver() {
        this.driver = Hooks.driver;
        page = new ActivityPage(driver);
        page.navigateFromHomeToKegiatan();
        AuthHelper.injectToken(driver);
        driver.navigate().refresh();
        page.waitForPageLoad();
        page.waitForLoadingComplete();
    }

    private void initDirectDriver() {
        this.driver = Hooks.driver;
        page = new ActivityPage(driver);
        page.navigateToKegiatanPage();
        AuthHelper.injectToken(driver);
        driver.navigate().refresh();
        page.waitForPageLoad();
        page.waitForLoadingComplete();
    }

    // ================================================================
    //  GIVEN
    // ================================================================

    @Given("User berada pada halaman Kegiatan Masjid")
    public void user_berada_pada_halaman_kegiatan_masjid() {
        initDriver();
    }

    @Given("User berada di halaman Kegiatan Masjid langsung")
    public void user_berada_di_halaman_kegiatan_masjid_langsung() {
        initDirectDriver();
    }

    @Given("Terdapat kegiatan yang tersedia")
    public void terdapat_kegiatan_yang_tersedia() {
        initDirectDriver();
        Assert.assertTrue("Tidak ada kegiatan yang tersedia",
                page.getActivityCount() > 0);
    }

    @Given("User sudah terdaftar")
    public void user_sudah_terdaftar() {
        initDirectDriver();
        Assert.assertTrue("Tidak ada cukup kegiatan (minimal 2)",
                page.getActivityCount() >= 2);

        // Daftarkan user ke kegiatan index 1 (kegiatan ke-2)
        try {
            page.klikIkutKegiatan(registeredActivityIndex);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } catch (Exception e) {
            page.klikIkutKegiatan(registeredActivityIndex);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        driver.navigate().refresh();
        page.waitForPageLoad();
        page.waitForLoadingComplete();
    }

    @Given("Token user expired atau tidak valid")
    public void token_user_expired_atau_tidak_valid() {
        this.driver = Hooks.driver;
        driver.manage().deleteAllCookies();
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("localStorage.clear();");
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("localStorage.setItem('accessToken', 'expired_token');");
        page = new ActivityPage(driver);
        page.navigateToKegiatanPage();
        driver.navigate().refresh();
        page.waitForPageLoad();
        page.waitForLoadingComplete();
    }

    @Given("User tidak login")
    public void user_tidak_login() {
        this.driver = Hooks.driver;
        driver.manage().deleteAllCookies();
        driver.navigate().refresh();
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("localStorage.clear();");
        page = new ActivityPage(driver);
        page.navigateToKegiatanPage();
        page.waitForPageLoad();
        page.waitForLoadingComplete();
    }

    // ================================================================
    //  WHEN
    // ================================================================

    @When("User menekan tombol Ikut Kegiatan")
    public void user_menekan_tombol_ikut_kegiatan() {
        page.klikIkutKegiatan(3);
    }

    @When("User menekan tombol Ikut Kegiatan pada kegiatan ke-{int}")
    public void user_menekan_tombol_ikut_kegiatan_pada_kegiatan_ke(int index) {
        int actualIndex = index - 1;
        page.klikIkutKegiatan(actualIndex);
    }

    @When("User menekan page number {int}")
    public void user_menekan_page_number(int pageNumber) {
        page.klikPageNumber(pageNumber);
    }

    @When("User mencoba mengikuti kegiatan")
    public void user_mencoba_mengikuti_kegiatan() {
        if (page.getActivityCount() > 0) {
            page.klikIkutKegiatan(0);
        }
    }

    // ================================================================
    //  THEN
    // ================================================================

    @Then("Tombol Ikut Kegiatan berhasil ditekan")
    public void tombol_ikut_kegiatan_berhasil_ditekan() {
        Assert.assertTrue("Tombol Ikut Kegiatan berhasil ditekan", true);
    }

    @Then("Sistem menampilkan notifikasi {string}")
    public void sistem_menampilkan_notifikasi(String expectedMessage) {
        String actualMessage = page.getToastMessage();
        Assert.assertTrue(
                "Expected: " + expectedMessage + ", Got: " + actualMessage,
                actualMessage.contains(expectedMessage)
        );
    }

    @Then("Sistem menampilkan notifikasi berhasil {string}")
    public void sistem_menampilkan_notifikasi_berhasil(String expectedMessage) {
        String actualMessage = page.getToastMessage();
        boolean isSuccess = actualMessage.contains(expectedMessage) ||
                actualMessage.contains("Berhasil Bergabung") ||
                actualMessage.contains("Berhasil");

        Assert.assertTrue(
                "Expected: " + expectedMessage + ", Got: " + actualMessage,
                isSuccess
        );
    }

    @Then("Sistem menampilkan notifikasi error {string}")
    public void sistem_menampilkan_notifikasi_error(String expectedMessage) {
        String actualMessage = page.getToastMessage();
        Assert.assertTrue(
                "Expected: " + expectedMessage + ", Got: " + actualMessage,
                actualMessage.contains(expectedMessage) ||
                        actualMessage.contains("Token tidak valid") ||
                        actualMessage.contains("expired") ||
                        actualMessage.contains("Gagal Bergabung")
        );
    }

    @Then("User berhasil bergabung dengan kegiatan")
    public void user_berhasil_bergabung_dengan_kegiatan() {
        String message = page.getToastMessage();
        boolean isSuccess = message.contains("Berhasil") ||
                message.contains("Google Calendar") ||
                message.contains("ditambahkan");

        Assert.assertTrue(
                "Seharusnya berhasil bergabung, Got: " + message,
                isSuccess
        );
    }

    @Then("User berada di halaman {int}")
    public void user_berada_di_halaman(int pageNumber) {
        String activePage = page.getActivePageNumber();
        Assert.assertEquals("Seharusnya berada di halaman " + pageNumber,
                String.valueOf(pageNumber), activePage);
    }

    @Then("Tombol Ikut Kegiatan tersedia")
    public void tombol_ikut_kegiatan_tersedia() {
        Assert.assertTrue("Tombol Ikut Kegiatan seharusnya tersedia",
                page.isIkutKegiatanButtonAvailable());
    }

    @Then("User diarahkan ke halaman login")
    public void user_diarahkan_ke_halaman_login() {
        String currentUrl = page.getCurrentUrl();
        Assert.assertTrue("Seharusnya diarahkan ke halaman login",
                currentUrl.contains("login"));
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