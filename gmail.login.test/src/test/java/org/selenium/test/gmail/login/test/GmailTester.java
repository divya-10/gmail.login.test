package org.selenium.test.gmail.login.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class GmailTester extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public GmailTester(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(GmailTester.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		String userName = "";
		String password = "";

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.println("Please Enter the gmail UserName :: ");
			userName = br.readLine();
			System.out.println("Please Enter the  password :: ");
			password = br.readLine();

			System.setProperty("webdriver.chrome.driver", "D:\\Tools\\chromedriver_win32\\chromedriver.exe");
			WebDriver driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.get("https://accounts.google.com/signin");

			// Getting the username by element Id
			WebElement userNameElement = driver.findElement(By.id("identifierId"));
			userNameElement.sendKeys(userName);

			// Getting the next button by element Id
			WebElement next = driver.findElement(By.id("identifierNext"));
			next.click();

			// implicitly waiting for 10 seconds to let google find my account
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			// Getting the Password by Element Name
			WebElement passwordElement = driver.findElement(By.name("password"));
			passwordElement.sendKeys(password);

			// Retrieve and click next button after entering password.
			// Element with className IdAqtf is blocking the next button so we are trying
			// wait until it is invisible.
			WebDriverWait wait = new WebDriverWait(driver, 3);
			boolean invisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("IdAqtf")));
			if (invisible) {
				WebElement login = driver.findElement(By.id("passwordNext"));
				login.click();
			}

			// Get googleAppMenu element and click
			WebElement googleAppMenu = driver.findElement(By.className("gb_C"));
			googleAppMenu.click();
			String parent_tab = driver.getWindowHandle();

			// get GmailApp element and click
			WebElement gmail = driver.findElement(By.id("gb23"));
			gmail.click();

			// Waiting until new tab is opened for Gmail.
			wait.until(ExpectedConditions.numberOfWindowsToBe(2));
			Set<String> s1 = driver.getWindowHandles();
			Iterator<String> i1 = s1.iterator();
			String currentURL = "";
			// Searching for gmail url from all tabs
			while (i1.hasNext()) {
				String child_tab = i1.next();
				if (!parent_tab.equalsIgnoreCase(child_tab)) {
					driver.switchTo().window(child_tab);
					new WebDriverWait(driver, 10).until(ExpectedConditions.urlContains("mail.google.com"));
					currentURL = driver.getCurrentUrl();
					break;
				}
			}

			String tabTitle = driver.getTitle();
			assertEquals("https://mail.google.com/mail/u/0/#inbox", currentURL);
			System.out.println(currentURL);
			assertTrue(tabTitle.contains("gmail") || tabTitle.contains("Gmail"));
			assertTrue(tabTitle.contains(userName));

			System.out.println(driver.getTitle());

			driver.quit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
