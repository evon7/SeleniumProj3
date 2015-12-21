Code Organization
=================

Tests.java
----------
public void testPostCreation() {

public void mainTestFlow() – the main flow containing these tests ( in order):

* Test #2 --> Fix getPostCount to return the right number when no post is present
* Test #1 --> Create a new post of text in bullet points, and return the post ID
* Test #3.1 --> Create a post and then delete it
* Test #3.2 --> Create 3 posts and then delete them all one after the other
* Test 4 --> Delete All posts
* Test 5 --> Create, Edit and Delete a post
* Test 6 ---> Upload 3 photos and Delete them


PostDashboard.java
------------------
	public PostDashboard(WebDriver driver) {
	public NewPostForm openNewPostForm() {

	public void GoPostDashboardPage(WebDriver driver){  ---> to bring the screen back to the Post Dashboard page.
                                                             Made it a method because we do this often

	public int getPostCount() {                         ---> Test 2
	public PostDashboard deletePostById(int postId) {   ---> Used by Test 2, 3.1 and 5

    Tests:
    ------
	public PostDashboard deleteAllPosts(){                      ---> Test 4
	public PostDashboard Create3DeleteAll(WebDriver driver){    ---> Test 3.2
	public PostDashboard CreateAndDelete(WebDriver driver){     ---> Test 3.1
	public void CreateEditDelete(WebDriver driver){             ---> Test 5
	public void UploadPhoto(WebDriver driver){                  ---> Test 6

NewPostForm.java
----------------
	public int publish(String title, List<String> contentBullets) {

Dashboard.java
--------------
	public Dashboard(WebDriver driver) {
	public PostDashboard showPostDashboard() {

Util.java
---------
	public static void wait(int seconds) {
	public static Dashboard AboutPage2DashBoard(WebDriver driver){	---> go from the About Page to the Dash Board
	

What is working well?
=====================
Everything works well.

What still needs more work?
===========================
None. Everything runss smoothly.

What was challenging?
=====================
- it took me a while to figure out how to get the bullet points to appear reliably in the text box

- Working with iFrame
    - While I was on the Edit Post page, I tried to locate my post to retrieve the Post ID. I
      could not locate it at first, because it was hiding in an iFrame. It took me a while to 
      figure out 

