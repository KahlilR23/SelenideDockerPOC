package dockerselenidepoc;

import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.junit5.ScreenShooterExtension;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static com.codeborne.selenide.Condition.*;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SpringBootApplication
public class basicTest {

@Container
    public static BrowserWebDriverContainer<?> webDriverContainer =
      new BrowserWebDriverContainer<>()
        .withCapabilities(new ChromeOptions()
           .addArguments("--no-sandbox")
           .addArguments("--disable-dev-shm-usage"));

@RegisterExtension
    public static ScreenShooterExtension screenShooterExtension =
        new ScreenShooterExtension().to("target/selenide");

@LocalServerPort
    private Integer port;
    
@Test
    public void searchFor() {

    Configuration.timeout = 4000;
    Configuration.baseUrl = "http://172.17.0.3:" + port;
    
        RemoteWebDriver remoteWebDriver = webDriverContainer.getWebDriver();
    WebDriverRunner.setWebDriver(remoteWebDriver);

        open("https://duckduckgo.com");
        //open(relativeOrAbsoluteURL "/book-store");
    $(By.name("q")).val("selenide").pressEnter();
    $$(".results .result").shouldHave(sizeGreaterThan(1));
    $(".results .result").shouldBe(visible).shouldHave(
        text("Selenide: concise UI tests in Java"),
        text("selenide.org"));
        System.out.println("End of basicTest******************************************");
  }
    
}
