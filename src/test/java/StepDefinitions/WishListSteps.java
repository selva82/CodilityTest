package StepDefinitions;

import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class WishListSteps {
	
	WebDriver driver =null;
	WebDriverWait wait = null;
	double leastPrice=0;
	String leastPriceProdName="";
	String leastPriceEleId="";
	
	@Given("open browser")
	public void open_browser() {
		System.out.println("Inside Open Browser Step :::");
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver/chromedriver.exe");
		
		driver = new ChromeDriver();
	
	}
	
	@Given("I want to add four random items to my cart")
	public void i_want_to_add_four_random_items_to_my_cart() {

		System.out.println("Adding the Products !!!");
	    
	    driver.navigate().to("https://cms.demo.katalon.com/");
	    wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	    
	    //Get list of web-elements with tagName  - a
	    List<WebElement> allLinks = driver.findElements(By.tagName("a"));
	    List<Integer> itemsIdsList = new ArrayList<Integer>() ;
	    
	    //Traversing through the list and printing its text along with link address
	    for(WebElement link:allLinks){
	    	
	    	String hrefVal=link.getAttribute("href");
	    	
	    	if(hrefVal.contains("add-to-cart")) {
	    		String itemId = hrefVal.split("=")[1];
	    		itemsIdsList.add(Integer.parseInt(itemId));
	    	}
	    		
	    }
	    
	    Set<Integer> randItemList = getRandomElement(itemsIdsList, 4);
	    System.out.println("Item Id List Size :::"+randItemList.size());
	    
	    for(Integer itemId:randItemList)
	    {
	    	System.out.println("itemId :::"+itemId);
	    	wait = new WebDriverWait(driver, Duration.ofSeconds(60));
	    	String hrefItemXpath="//a[@href='?add-to-cart="+itemId+"']";
	    	System.out.println("hrefItemXpath :::"+hrefItemXpath);
	    	driver.findElement(By.xpath(hrefItemXpath)).click();
	    	wait = new WebDriverWait(driver, Duration.ofSeconds(90));
	    }
	    
	}

	@When("I view my cart")
	public void i_view_my_cart() {
		System.out.println("View the cart !!!");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='https://cms.demo.katalon.com/cart/']"))).click();
		driver.findElement(By.xpath("//a[@href='https://cms.demo.katalon.com/cart/']")).click();
		wait = new WebDriverWait(driver, Duration.ofSeconds(50));
		
		String heading = driver.findElement(By.xpath("//h1[@class='page-title']")).getText();
		assertTrue("Item Added to Cart !!!", heading.equalsIgnoreCase("Cart"));
	}

	@Then("I find total four items in my cart")
	public void i_find_total_four_items_in_my_cart() {
		System.out.println("Find the Four Selected Products !!!");
		assertTrue("Four Items Found in the Cart", getCartItems()==4);
	}

	
	@When("I search for lowest price item")
	public void i_search_for_lowest_price_item() {
		System.out.println("Searching the Lowest Price !!!");
		
		List<WebElement> rows=driver.findElements(By.xpath("//*[@class=\"woocommerce-cart-form\"]/table/tbody/tr/td/span"));
		Set<Double> rowValList = new HashSet<Double>();

		for(WebElement ele:rows)
		{
			String itemVal=ele.getText().substring(1);
			rowValList.add(Double.parseDouble(itemVal));
		}
		
		leastPrice=Collections.min(rowValList);
		System.out.println("Least Price Found ::"+leastPrice);
		assertTrue("Lowest Price Item Found !!!", String.valueOf(leastPrice)!=null);
		
	}

	@When("I am able to remove the lowest price item from my cart")
	public void i_am_able_to_remove_the_lowest_price_item_from_my_cart() {
		System.out.println("Adding the lowest price Items !!!");
		
		List<WebElement> rowItems=driver.findElements(By.xpath("//*[@class=\"woocommerce-cart-form\"]/table/tbody/tr"));
		
		for(WebElement ele:rowItems)
		{
			String eleVal=ele.getText();
			if(eleVal.contains(String.valueOf(leastPrice))) {
				WebElement rowVals=ele.findElement(By.tagName("a"));
				String hrefXpath="//a[@href=\""+rowVals.getAttribute("href")+"\"]";
				driver.findElement(By.xpath(hrefXpath)).click();
			}
		}
	}

	@Then("I am able to verify three items in my cart")
	public void i_am_able_to_verify_three_items_in_my_cart() {
		System.out.println("Verify Items in the cart !!!");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='https://cms.demo.katalon.com/cart/']"))).click();
		driver.findElement(By.xpath("//a[@href='https://cms.demo.katalon.com/cart/']")).click();
		
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		assertTrue("Four Items Found in the Cart", getCartItems()==3);
	}
	
	private int getCartItems() {
		List<WebElement> rows=driver.findElements(By.xpath("//tr[@class=\"woocommerce-cart-form__cart-item cart_item\"]"));
		return rows.size();
	}
	
	private Set<Integer> getRandomElement(List<Integer> list, int totalItems)
    {
        Random rand = new Random();
        
        // create a temporary list for storing
        // selected element
        Set<Integer> tempList = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
 
            // take a random index between 0 to size
            // of given List
            int randomIndex = rand.nextInt(list.size());
 
            // add element in temporary list
            tempList.add(list.get(randomIndex));
            if(tempList.size() == totalItems)
        		break;
        }
        return tempList;
    }
}
