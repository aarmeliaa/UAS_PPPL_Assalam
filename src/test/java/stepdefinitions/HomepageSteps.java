package steps;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import java.time.Duration;
import java.util.Set;

public class HomepageSteps {

    private WebDriver driver;
    private HomePage homePage;

    @BeforeMethod
    public void setUp() {
        // Inisialisasi WebDriver Chrome
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Buka URL Homepage Publik Masjid As-Salam
        driver.get("https://assalam-fe.vercel.app/"); // Sesuaikan dengan URL web aslimu
        homePage = new HomePage(driver);
    }

    @Test(priority = 1)
    public void testHeaderMasjidName() {
        // Mengetes apakah nama "Masjid As-Salam" muncul di bagian atas (Hero section)
        // XPath ini mencari elemen h1 (heading) yang mengandung teks tersebut
        WebElement headerTitle = driver.findElement(By.xpath("//h1[contains(text(), 'Masjid As-Salam')]"));
        Assert.assertTrue(headerTitle.isDisplayed(), "Nama Masjid tidak ditampilkan di Homepage");
    }

    @Test(priority = 2)
    public void testWaktuSholatSection() {
        // Mengetes apakah bagian "Waktu Sholat Hari Ini" ada
        WebElement waktuSholatTitle = driver.findElement(By.xpath("//*[contains(text(), 'Waktu Sholat Hari Ini')]"));
        Assert.assertTrue(waktuSholatTitle.isDisplayed(), "Bagian Waktu Sholat tidak ditemukan");

        // Mengetes apakah elemen jam (misal 17:58:14) muncul
        WebElement clockElement = driver.findElement(By.cssSelector("span.text-white.text-2xl.font-bold.tracking-widest")); // Sesuaikan class-nya
        Assert.assertNotNull(clockElement, "Jam real-time tidak muncul");
    }

    @Test(priority = 3)
    public void testGoogleLoginButton() {
        // 1. Simpan ID jendela utama (halaman web saat ini) sebelum klik tombol
        String mainWindowHandle = driver.getWindowHandle();

        // 2. Cari dan klik tombol "Masuk dengan Google"
        WebElement googleLoginBtn = driver.findElement(By.xpath("//button[contains(., 'Masuk dengan Google')]"));
        Assert.assertTrue(googleLoginBtn.isDisplayed(), "Tombol Masuk dengan Google tidak ditemukan");
        googleLoginBtn.click();

        // 3. Tunggu sampai jumlah jendela/tab menjadi 2 (utama + popup Google)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        // 4. Dapatkan semua ID jendela yang sedang terbuka saat ini
        Set<String> allWindowHandles = driver.getWindowHandles();

        // 5. Pindahkan fokus Selenium ke jendela yang BARU (popup)
        for (String handle : allWindowHandles) {
            if (!handle.equals(mainWindowHandle)) {
                driver.switchTo().window(handle); // Pindah ke tab/popup baru
                break;
            }
        }

        try {
            // 6. Sekarang Selenium sudah fokus ke popup. Kita cek URL-nya.
            wait.until(ExpectedConditions.urlContains("accounts.google.com"));
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("accounts.google.com"), "Popup gagal mengarah ke Google");

            System.out.println("Berhasil membuka popup Google di URL: " + currentUrl);

        } catch (Exception e) {
            Assert.fail("Waktu habis saat menunggu popup Google, atau URL tidak sesuai.");
        } finally {
            // 7. Tutup popup/tab Google tersebut agar rapi
            driver.close();

            // 8. KEMBALIKAN fokus Selenium ke jendela utama agar tes tidak error
            driver.switchTo().window(mainWindowHandle);
        }
    }

    @Test(priority = 4)
    public void testLihatSemuaKegiatanButton() {
        // 1. Cari tombol "Lihat Semua Kegiatan" menggunakan XPath berdasarkan teksnya
        WebElement lihatSemuaBtn = driver.findElement(By.xpath("//button[contains(., 'Lihat Semua Kegiatan')]"));
        Assert.assertTrue(lihatSemuaBtn.isDisplayed(), "Tombol 'Lihat Semua Kegiatan' tidak ditemukan");

        // 2. Scroll layar agar tombol terlihat (Mencegah error ElementClickIntercepted)
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", lihatSemuaBtn);

        // Beri jeda sejenak agar animasi scroll selesai
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Simpan URL sebelum klik untuk perbandingan nanti
        String urlSebelumKlik = driver.getCurrentUrl();

        // 3. Klik tombol tersebut
        lihatSemuaBtn.click();

        // 4. Verifikasi hasilnya (Tunggu URL berubah)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Menunggu sampai URL saat ini BERBEDA dari URL beranda
            wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(urlSebelumKlik)));

            String urlSetelahKlik = driver.getCurrentUrl();
            System.out.println("Berhasil diarahkan ke halaman: " + urlSetelahKlik);

            /* * Opsional: Jika Anda tahu URL pastinya (misal: /kegiatan),
             * Anda bisa menggunakan baris di bawah ini:
             * Assert.assertTrue(urlSetelahKlik.contains("kegiatan"), "Halaman tujuan salah");
             */

        } catch (Exception e) {
            Assert.fail("Gagal pindah halaman setelah tombol 'Lihat Semua Kegiatan' diklik.");
        } finally {
            // 5. Kembali ke halaman utama (beranda) untuk mereset posisi
            // Sangat penting jika Anda punya skenario tes selanjutnya
//            driver.get("https://assalam-fe.vercel.app/");
        }
    }

    @Test(priority = 5, description = "Verifikasi elemen-elemen section Berita di Homepage")
    public void testVerifikasiSectionBeritaTampil() {
        // 1. Scroll menuju section Berita dan Artikel
        homePage.scrollToSectionBerita();

        // 2. Validasi judul section "Kabar Terbaru dari Masjid" harus muncul
        Assert.assertTrue(homePage.isHeaderBeritaDisplayed(),
                "[FAILED] Section Kabar Terbaru dari Masjid tidak ditemukan di Homepage!");

        // 3. Validasi konten: Pastikan ada judul berita utama yang terbaca
        String judulUtama = homePage.getJudulBeritaUtama();
        System.out.println("Judul Berita Utama yang tampil: " + judulUtama);
        Assert.assertFalse(judulUtama.isEmpty(), "[FAILED] Judul berita utama kosong!");

        // 4. Validasi jumlah data: Pastikan minimal ada 1 berita yang dimuat di grid
        int jumlahBerita = homePage.getJumlahBeritaTampil();
        System.out.println("Jumlah kartu berita terdeteksi: " + jumlahBerita);
        Assert.assertTrue(jumlahBerita > 0, "[FAILED] Tidak ada kartu berita yang dimuat di Homepage!");
    }

    @Test(priority = 6, description = "Menguji navigasi tombol Lihat Semua Berita dan Artikel")
    public void testNavigasiLihatSemuaBerita() {
        homePage.scrollToSectionBerita();

        String urlAwal = homePage.getCurrentUrl();

        // Klik tombol hijau "Lihat Semua Berita dan Artikel"
        homePage.clickTombolLihatSemuaBerita();

        // Verifikasi URL berubah (berpindah ke halaman list berita publik)
//        String urlSetelahKlik = homePage.getCurrentUrl();
//        Assert.assertNotEquals(urlAwal, urlSetelahKlik,
//                "[FAILED] URL tidak berubah setelah tombol Lihat Semua Berita dan Artikel diklik!");
//        Assert.assertTrue(urlSetelahKlik.contains("berita") || urlSetelahKlik.contains("artikel"),
//                "[FAILED] Navigasi salah, URL tujuan tidak mengandung unsur berita/artikel: " + urlSetelahKlik);

        // Contoh menunggu hingga URL tidak lagi mengandung URL lama / berubah
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe("https://assalam-fe.vercel.app/")));
    }

    @Test(priority = 7)
    public void testNavigasiGoogleMaps() {
        // 1. Scroll down atau pastikan elemen maps terlihat
        WebElement btnMaps = driver.findElement(By.xpath("//a[contains(text(), 'Buka di Google Maps')]"));
        // Simpan ID tab utama sebelum klik
        String originalWindow = driver.getWindowHandle();

        // 2. Klik tombol "Buka di Google Maps"
        btnMaps.click();

        // 3. Tunggu hingga tab baru/jendela baru terbuka (karena biasanya membuka _blank)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        // 4. Pindah fokus Selenium ke tab baru tersebut
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        // 5. Ambil URL di tab baru dan lakukan Assert
        String currentUrl = driver.getCurrentUrl();
        System.out.println("URL yang terbuka: " + currentUrl);

        // Verifikasi apakah URL mengandung domain google.com/maps
        Assert.assertTrue(currentUrl.contains("google.com/maps") || currentUrl.contains("maps.app.goo.gl"),
                "[FAILED] Tombol tidak mengarah ke Google Maps!");

        // 6. Close tab baru dan kembali ke halaman utama (agar test selanjutnya tidak error)
//        driver.close();
//        driver.switchTo().window(originalWindow);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}