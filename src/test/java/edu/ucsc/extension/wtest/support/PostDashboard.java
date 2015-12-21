package edu.ucsc.extension.wtest.support;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.support.ui.Select;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import edu.ucsc.extension.wtest.support.Dashboard;
import edu.ucsc.extension.wtest.support.PostDashboard;

public class PostDashboard {
	
	private WebDriver driver;
	
	@FindBy(how = How.LINK_TEXT, linkText = "Add New")
	private WebElement newPostButton;
	
	public PostDashboard(WebDriver driver) {
		this.driver = driver;
		if(!driver.getCurrentUrl().endsWith("edit.php"))
			throw new IllegalStateException();
	}
	
	public void GoPostDashboardPage(WebDriver driver){
		this.driver = driver;
		driver.get("https://yvso.wordpress.com/wp-admin/edit.php");
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.titleContains("Posts"));
	}
	
	public NewPostForm openNewPostForm() {
		newPostButton.click();
		
		// Wait for the title to change
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.titleContains("Add New Post"));
		
		// Wait for few more elements
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.id("submitdiv")));
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.id("publish")));
		
		return PageFactory.initElements(driver, NewPostForm.class);
	}
	
	public int getPostCount() {
		
		List<WebElement> posts = driver.findElements(By.cssSelector("tbody#the-list tr"));	
		for (int i=0; i<posts.size(); i++){
			//System.out.printf("list items ---> %s",      posts.get(i).getText());
			if (posts.get(i).getText().contains("No posts found"))
				return(0);
		}
		return posts.size();
	}
	
	public PostDashboard deleteAllPosts(){
		//driver.findElement(By.linkText("Site Admin"));
		//(new WebDriverWait(driver, 10)).until(ExpectedConditions.titleContains("Posts"));

		//if(!driver.getCurrentUrl().endsWith("edit.php"))   yvso. url has ?id=xxx attached to the end after submitting to server
		if(!driver.getCurrentUrl().contains("edit.php"))
			throw new IllegalStateException();
		
		while (getPostCount() > 0){
			driver.findElement(By.id("cb-select-all-1")).click(); // select-all check box

			Select bulkSelDrpBox = new Select(driver.findElement(By.id("bulk-action-selector-top"))); // select move-to-trash
			bulkSelDrpBox.selectByVisibleText("Move to Trash");

			driver.findElement(By.id("doaction")).click(); // apply
			(new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Undo")));
		}
		return null;
	}
	
	public PostDashboard deletePostById(int postId) {
		//if(!driver.getCurrentUrl().endsWith("edit.php")) yvso
		if(!driver.getCurrentUrl().contains("edit.php"))
			throw new IllegalStateException();
		
		System.out.printf("Attempt to delete post %d\n", postId);

		// find the post first
		String postIdString = "";
		postIdString = "post-" + Integer.toString(postId);
		// System.out.printf("Attempt to delete post %s\n", postIdString);
		WebElement qBox;
		try {
			qBox = driver.findElement(By.id(postIdString));			
		} catch (Exception e) {
			System.out.println(postIdString + " not found... Quitting");
			return null;
		}

		//now hover to get "Trash" link to show
		Actions action = new Actions(driver);
		action.moveToElement(qBox).build().perform();
		//Util.wait(1); yvso debug. replaced by implicit wait

		//locate the Trash link
		String IdString="";
		String trashIdString="";
		IdString = Integer.toString(postId);
		trashIdString = "//tr[contains(@id,'" +IdString+ "')]/td/div/span/a[contains(@class,'submitdelete')]";
		System.out.println(trashIdString);
		
		//System.out.printf("--->>> %s %s   ",postIdString, trashIdString);
		WebElement trashLink = driver.findElement(By.xpath(trashIdString));		
		//System.out.println(trashLink.getAttribute("href"));		
		try {
			trashLink.click();
			System.out.printf("Post %d Deletion Successful.\n", postId);

		} catch (Exception e) {
			System.out.printf("Clicking on %d's Trash button unsuccessful.\n", postId);
			return null;
		}		
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Undo")));
		
		return null;
	}
	
	public PostDashboard Create3DeleteAll(WebDriver driver){
		//yvso debug - just how many frames are here?
		//Set<String> handles = driver.getWindowHandles();
		//System.out.printf("Number of Frames= %d", handles.size());
		int numPost = 3;
		int [] postIDArray = new int[100];
		
		NewPostForm newPostForm = openNewPostForm();
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.titleContains("Add New Post"));
		
		List<String> postContent = new ArrayList<String>();
		String[] quotes = new String[20];
		quotes[0] = ("Test 3.2 - Create 3 posts and then delete all of then (one after the other)");
		quotes[1] = ("Post 2 ...");
		quotes[2] = ("Post 3 ...");

		for (int i=0; i<numPost; i++){
			String title = "Post "+ (i+1);
			postContent.add(quotes[i]);
			postIDArray[i] = newPostForm.publish(title, postContent);

			(new WebDriverWait(driver, 10)).until(ExpectedConditions.titleContains("Edit Post"));
			driver.switchTo().defaultContent();
			
			if (i < numPost-1)
				driver.findElement(By.className("postpost-new")).click();
			
			postContent.clear();
		}		
		GoPostDashboardPage(driver);
		
		//now delete each one
		for (int j=0; j<numPost; j++)
			deletePostById(postIDArray[j]);

		return null; //stay in PostDashboard for the next test. no need to init
		//return PageFactory.initElements(driver, PostDashboard.class);
	}
	
	public PostDashboard CreateAndDelete(WebDriver driver){
		int postID = 0;

		List<String> postContent = new ArrayList<String>();
		postContent.add("Create a post and then delete it");

		NewPostForm newPostForm = openNewPostForm();
		new WebDriverWait(driver, 10).until(ExpectedConditions.titleContains("Add New Post"));

		postID = newPostForm.publish("Test 3.2", postContent);
		System.out.println(postID);
		
		//Now delete
		GoPostDashboardPage(driver);
		deletePostById(postID);
		
		return null;
		//url not ending with edit.php, but edit.php?id=xx, cause the constructor to crash
		//return PageFactory.initElements(driver, PostDashboard.class);
	}
	
	public void CreateEditDelete(WebDriver driver){
		int postId = 0;
        //=== Add a post
		List<String> postContent = new ArrayList<String>();
		postContent.add("Test 5. Create, Edit and Delete a post");

		NewPostForm newPostForm = openNewPostForm();
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.titleContains("Add New Post"));

		postId = newPostForm.publish("Test 5", postContent);
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.titleContains("Edit Post"));

        //=== Back to Post Page
		GoPostDashboardPage(driver); //this function already waits for the Posts title to come up

       	// find the post
		String postIdString = "";
		postIdString = "post-" + Integer.toString(postId);
		System.out.printf("Editing post %s\n", postIdString);
		
		//now hover to get "Edit" link to show
		WebElement qBox = driver.findElement(By.id(postIdString));
		Actions action = new Actions(driver);
		action.moveToElement(qBox).build().perform();
		//Util.wait(1); yvso debug. replaced by implicit wait

		//locate the Trash link
		String IdString="";
		String editIdString="";
		IdString = Integer.toString(postId);
		editIdString = "//tr[contains(@id,'" +IdString+ "')]/td/div/span/a[contains(@class,'editinline')]";
		
		//System.out.printf("--->>> %s %s   ",postIdString, editIdString);
		WebElement editLink = driver.findElement(By.xpath(editIdString));
        editLink.click();
		
		WebElement titleBox = driver.findElement(By.className("ptitle"));
		titleBox.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		titleBox.sendKeys(Keys.BACK_SPACE);
		titleBox.sendKeys("!!! All NEW Title !!!");
		
		//now click Update
		driver.findElement(By.cssSelector(".wp-core-ui .button-primary")).click();
		
		//Now delete
		//Get back to Post Dashboard to perform delete
		GoPostDashboardPage(driver);
		deletePostById(postId);
	}
	
	public void UploadPhoto(WebDriver driver){
		//Hover to get the Media button to show
		//WebElement mediaButton = driver.findElement(By.className("wp-menu-name"));
		WebElement mediaButton = driver.findElement(By.xpath("//*[text()='Media']"));			
		Actions action = new Actions(driver);
		action.moveToElement(mediaButton).build().perform();
		//Util.wait(1); yvso debug. replaced by implicit wait
		
		(driver.findElement(By.xpath("//li[@id='menu-media']/ul/li/a[@href='media-new.php']"))).click();
			
		WebElement bUploader = driver.findElement(By.xpath("//*[text()='browser uploader']"));			
		if (bUploader.isDisplayed()){
			bUploader.click();
		}
		
		//path too long. need to break it
		String[] photoFile = new String[50];
		photoFile[0] = "crazy_cat_lady";
		photoFile[1] = "grumpy2";
		photoFile[2] = "grumpy3";
		
		//System.out.printf("%s", System.getProperty("user.dir"));	
		String photoPath = System.getProperty("user.dir")+
				"\\src\\test\\java\\edu\\ucsc\\extension\\wtest\\support\\photos\\";
		
		//upload 3 photos
		int numPhoto = 3;
		WebElement freshPhoto[] = new WebElement[100];
		String photoXpath[] = new String[100];
		String photo;
		for (int i=0; i<numPhoto; i++){
			photo = photoPath + photoFile[i] + ".jpg";
			
			(driver.findElement(By.id("async-upload"))).sendKeys(photo);
			(driver.findElement(By.id("html-upload"))).click();

			photoXpath[i] = "//*[text()='" + photoFile[i] + "']";
			System.out.println(photoXpath[i]);
			freshPhoto[i] = driver.findElement(By.xpath(photoXpath[i]));
			try {
				(new WebDriverWait(driver, 10)).until(ExpectedConditions.textToBePresentInElement(freshPhoto[i], photoFile[i]));
			} catch (Exception e) {
				System.out.printf("Upload of photo %d UNSUCCESSFUL.\n", photoFile[i]);
			}
			if (i < (numPhoto-1)){
				(driver.findElement(By.linkText("Add New"))).click();
				new WebDriverWait(driver, 10).until(ExpectedConditions.titleContains("Upload New Media"));
			}
		}

		//delete 3 photos
		for (int j=0; j<numPhoto; j++){
			//Hover to get the Delete Permanently button to show
			Actions photoAction = new Actions(driver);
			freshPhoto[j] = driver.findElement(By.xpath(photoXpath[j]));
			photoAction.moveToElement(freshPhoto[j]).build().perform();
			//Util.wait(1); yvso debug. replaced by implicit wait
			(driver.findElement(By.partialLinkText("Delete Permanently"))).click();
			driver.switchTo().alert().accept();
		}
	} // closing UploadPhoto
} // closing PostDashboard
