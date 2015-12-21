package edu.ucsc.extension.wtest.support;

import java.io.File;

import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;


import org.openqa.selenium.support.ui.WebDriverWait;


import edu.ucsc.extension.wtest.support.*;
import edu.ucsc.extension.wtest.support.Dashboard;
import edu.ucsc.extension.wtest.support.LoginPage;
import edu.ucsc.extension.wtest.support.NewPostForm;
import edu.ucsc.extension.wtest.support.PostDashboard;

public class Util {
	public static void wait(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		}catch(Exception e) {
			//ignore
		}
	}
	
	public static void takeScreenshot(WebDriver driver, String destFileName) {
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		File destFile = new File(destFileName);
		try {
			FileUtils.copyFile(srcFile, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Dashboard AboutPage2DashBoard(WebDriver driver){
		// Bring the driver back to post post page
		WebElement siteAdminLbl = driver.findElement(By.linkText("Site Admin"));
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(siteAdminLbl));
		siteAdminLbl.click();
		
		return PageFactory.initElements(driver, Dashboard.class);
	}

}
