package edu.ucsc.extension.wtest.seleniumtests;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

//import edu.ucsc.extension.wtest.support.List;
import edu.ucsc.extension.wtest.support.*;
import edu.ucsc.extension.wtest.support.Dashboard;
import edu.ucsc.extension.wtest.support.LoginPage;
import edu.ucsc.extension.wtest.support.NewPostForm;
import edu.ucsc.extension.wtest.support.PostDashboard;

import java.util.List;  //yvso
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

public class Tests {
	
	private static final String username = "yvso";
	private static final String password = "yvonneso123";
	
	public static  int[] postIDArr = new int[100];
	
	private  LoginPage loginPage;
	private  Dashboard dashboard; 
	private  PostDashboard postdashboard;
	
	//make it global to make available to all methods
	private static WebDriver driver;
	
	public void testPostCreation() {
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		// You may want to add an implicit wait here
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		loginPage = PageFactory.initElements(driver, LoginPage.class);
		dashboard = loginPage.login(username, password);
		postdashboard = dashboard.showPostDashboard();
	}
	
	@Test
	public void mainTestFlow(){
		testPostCreation();
	
		//Test #2 --> Fix getPostCount to return the right number when no post is present
		int postCount = postdashboard.getPostCount();
		System.out.println("There are " + postCount + " posts.");
		
		//Test #1 --> Create a new post of text in bullet points, and return the post ID
		int postID = 0;
		NewPostForm newPostForm = postdashboard.openNewPostForm();
		new WebDriverWait(driver, 10).until(ExpectedConditions.titleContains("Add New Post"));

		List<String> postContent = new ArrayList<String>();
		postContent.add("You only live once, but if you do it right, once is enough\r");
		postContent.add("Life isn't about finding yourself. Life is about creating yourself\r");
		postContent.add("Life is like riding a bicycle. To keep your balance, you must keep moving");
		postID = newPostForm.publish("Great Quotes", postContent);
		System.out.println(postID);

		dashboard = Util.AboutPage2DashBoard(driver);
		new WebDriverWait(driver, 10).until(ExpectedConditions.titleContains("Dashboard"));
		postdashboard = dashboard.showPostDashboard();
		new WebDriverWait(driver, 10).until(ExpectedConditions.titleContains("Posts"));
		
		//postdashboard.GoPostDashboardPage(driver); //yvso debug
		
		//Test #3.1 --> Create a post and then delete it
		postdashboard.CreateAndDelete(driver);

		//Test #3.2 --> Create 3 posts and then delete them all one after the other
		postdashboard.Create3DeleteAll(driver);

		//Test 4 --> Delete All posts
		postdashboard.deleteAllPosts();
		
		//Test 5 --> Create, Edit and Delete a post
		postdashboard.CreateEditDelete(driver);

		//Test 6 ---> Upload 3 photos and Delete them
		postdashboard.UploadPhoto(driver);
	
		//Util.wait(5);
		driver.quit();
	}

}
