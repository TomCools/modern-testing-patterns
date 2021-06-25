package be.tomcools.advancedtestingpatterns.testcontainers.additional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Testcontainers
@SpringBootTest
class WebdriverTestContainers {

    @Test
    @Disabled
    void exampleTestForSeleniumContainer() {
        final RemoteWebDriver driver = chrome.getWebDriver();
        driver.get("https://www.meetup.com/JUG-Bonn/events/277560762/");

        final String titleText = driver.findElement(By.className("pageHead-headline")).getText();

        assertThat(titleText).contains("Advanced Testing Patterns mit Tom Cools");
    }

    @Container
    BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>();
            //Record with = .withRecordingMode(RECORD_ALL, target, VncRecordingContainer.VncRecordingFormat.MP4);


}
