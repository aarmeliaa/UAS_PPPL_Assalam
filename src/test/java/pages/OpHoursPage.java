package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OpHoursPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public OpHoursPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ──────── HOMEPAGE ────────

    public void openHomepage() {
        driver.get("https://assalam-fe.vercel.app/");
    }

    public boolean isScheduleSectionDisplayed() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h2[text()='Jam Buka Masjid']"))).isDisplayed();
    }

    public int getDayRowCount() {
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//span[text()='Senin']")));
        List<WebElement> rows = driver.findElements(
                By.xpath("//div[contains(@class,'flex') and contains(@class,'justify-between') and contains(@class,'px-5') and contains(@class,'py-4')]"));
        return rows.size();
    }

    public String getDayTimeText(String day) {
        String xpath = String.format(
                "//span[contains(., \"%s\")]/ancestor::div[contains(@class,'flex') and contains(@class,'justify-between')]/span[contains(@class,'text-green') or contains(@class,'text-red')]",
                day);
        WebElement timeSpan = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        return timeSpan.getText().trim();
    }

    public boolean isTodayBadgeDisplayed() {
        return !wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//*[text()='HARI INI']"))).isEmpty();
    }

    public boolean isDayHighlightedGreen(String day) {
        String xpath = String.format(
                "//span[contains(., \"%s\")]/ancestor::div[contains(@class,'flex') and contains(@class,'justify-between')]",
                day);
        WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        String classAttr = row.getAttribute("class");
        return classAttr != null && classAttr.contains("bg-green-50");
    }

    public String getStatusBadgeText() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'Masjid Saat Ini')]"))).getText();
    }

    public boolean isDayClosed(String day) {
        String xpath = String.format(
                "//span[contains(., \"%s\")]/ancestor::div[contains(@class,'flex') and contains(@class,'justify-between')]//span[text()='Tutup']",
                day);
        return !wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath))).isEmpty();
    }

    public String getStatusBadgeColor() {
        WebElement dot = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'Masjid Saat Ini')]/span[contains(@class,'rounded-full')]")));
        String classAttr = dot.getAttribute("class");
        if (classAttr != null && classAttr.contains("bg-green-500")) return "green";
        return "gray";
    }

    // ──────── ADMIN PAGE ────────

    public void openAdminOpHoursPage() {
        driver.get("https://assalam-fe.vercel.app/admin/jadwal-operasional");
    }

    public boolean isAdminPageDisplayed() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h2[text()='Jadwal Mingguan']"))).isDisplayed();
    }

    public void clickToggle(String day) {
        String xpath = String.format(
                "//div[normalize-space()=\"%s\"]/following-sibling::div[contains(@class,'mr-6')]//button", day);
        WebElement toggle = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        String wasClass = toggle.getAttribute("class");
        toggle.click();
        if (wasClass != null && wasClass.contains("bg-[#16A34A]")) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                    "//div[normalize-space()=\"" + day + "\"]/following-sibling::div[contains(@class,'mr-6')]//button[contains(@class,'bg-gray-400')]")));
        } else {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                    "//div[normalize-space()=\"" + day + "\"]/following-sibling::div[contains(@class,'mr-6')]//button[contains(@class,'bg-[#16A34A]')]")));
        }
    }

    public boolean isToggleActive(String day) {
        String xpath = String.format(
                "//div[normalize-space()=\"%s\"]/following-sibling::div[contains(@class,'mr-6')]//button", day);
        WebElement toggle = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        String classAttr = toggle.getAttribute("class");
        return classAttr != null && classAttr.contains("bg-[#16A34A]");
    }

    public boolean isToggleInactive(String day) {
        String xpath = String.format(
                "//div[normalize-space()=\"%s\"]/following-sibling::div[contains(@class,'mr-6')]//button", day);
        WebElement toggle = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        String classAttr = toggle.getAttribute("class");
        return classAttr != null && classAttr.contains("bg-gray-400");
    }

    public void clickEdit(String day) {
        String xpath = String.format(
                "//div[normalize-space()=\"%s\"]/following-sibling::button[text()='Edit']", day);
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        btn.click();
    }

    public boolean isEditFormVisible(String day) {
        String xpath = String.format(
                "//div[normalize-space()=\"%s\"]/ancestor::div[contains(@class,'border-b')]//input[@type='time']", day);
        return !driver.findElements(By.xpath(xpath)).isEmpty();
    }

    public void setOpenTime(String day, String time) {
        String xpath = String.format(
                "//div[normalize-space()=\"%s\"]/ancestor::div[contains(@class,'border-b')]//input[@type='time']", day);
        List<WebElement> inputs = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath)));
        inputs.get(0).clear();
        inputs.get(0).sendKeys(time);
    }

    public void setCloseTime(String day, String time) {
        String xpath = String.format(
                "//div[normalize-space()=\"%s\"]/ancestor::div[contains(@class,'border-b')]//input[@type='time']", day);
        List<WebElement> inputs = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath)));
        inputs.get(1).clear();
        inputs.get(1).sendKeys(time);
    }

    public void clickSave(String day) {
        String xpath = String.format(
                "//div[normalize-space()=\"%s\"]/ancestor::div[contains(@class,'border-b')]//button[text()='Simpan']", day);
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        btn.click();
    }

    public void clickCancel(String day) {
        String xpath = String.format(
                "//div[normalize-space()=\"%s\"]/ancestor::div[contains(@class,'border-b')]//button[text()='Batal']", day);
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        btn.click();
    }

    public String getAdminDayTimeText(String day) {
        String xpath = String.format(
                "//div[normalize-space()=\"%s\"]/following-sibling::div[contains(@class,'flex-1')]//span", day);
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath))).getText().trim();
    }

    public void waitForSaveComplete(String day) {
        String xpath = String.format(
                "//div[normalize-space()=\"%s\"]/ancestor::div[contains(@class,'border-b')]//button[text()='Simpan']", day);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
    }
}