package edu.ucsc.extension.wtest.support;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import static java.lang.Integer.parseInt;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NewPostForm {
	
	private WebDriver driver;
	
	@FindBy(how = How.NAME, name = "post_title")
	private WebElement titleBox;
	
	@FindBy(how = How.ID, id = "publish")
	private WebElement publishButton;
	
	@FindBy(how = How.ID, using="mceu_4")
	private WebElement bulletButton;
	
	@FindBy(how = How.ID, id = "content_ifr")
	private WebElement contentFrame;
	
	@FindBy(how = How.ID, id = "postpost_iframe")
	private WebElement postPostFrame;
	
	@FindBy(how = How.ID , id = "tinymce")
	private WebElement contentBody;
			
	public NewPostForm(WebDriver driver) {
		this.driver = driver;
	}
	
	public int publish(String title, List<String> contentBullets) {		
		new WebDriverWait(driver, 10).until(ExpectedConditions.titleContains("Add New Post"));
		titleBox.sendKeys(title);
				
		//click the bullet list button.
		bulletButton.click();
		/* yvso debug. replaced by implicit wait
		new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.tagName("p")));
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.tagName("ul")));  //was ul

		//new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body[@id='tinymce']/ul/li")));
		//new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(bulletButton));
        //Util.wait(1);
         */

		//switch to the content box, which is an iframe. require special steps to activate
		driver.switchTo().defaultContent();
		driver.switchTo().frame(contentFrame);
		
		//now fill the content box with text
		for (int i=0; i<contentBullets.size(); i++){
			contentBody.sendKeys(contentBullets.get(i));
		}
		
		//now get out of the iframe, and back to the mother content
		driver.switchTo().defaultContent();
		publishButton.click();
		
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.titleContains("Edit Post"));
		//Tricky!!!!! my post is located on a separate iFrame, that why i couldn't find it!!!
		WebElement postPostFrame = driver.findElement(By.id("postpost-iframe"));
		driver.switchTo().frame(postPostFrame);

		WebElement myPost = driver.findElement(By.tagName("article"));
		String myPostStr = myPost.getAttribute("id");
		
		return Integer.parseInt(myPostStr.replaceAll("post-", ""));		
	}
}
