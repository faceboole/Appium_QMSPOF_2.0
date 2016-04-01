package pages;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import io.appium.java_client.android.AndroidDriver;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Step;

public class ListingPage extends BasePage {
	private String filterResultSearchSemuaMobilID="com.app.tokobagus.betterb:id/categoryBottomText";
	private String filterButtonID="com.app.tokobagus.betterb:id/filter";
	private String sortButton="com.app.tokobagus.betterb:id/sort";
	private String filterTerbaru="Terbaru";
	private String filterTermurah="Termurah";
	private String filterTermahal="Termahal";
	private String filterText="Filter";
	private String brandTextTitleMobilID="com.app.tokobagus.betterb:id/title";
	private String priceTextMobilID="com.app.tokobagus.betterb:id/price";
	private String actionBarID="com.app.tokobagus.betterb:id/action_bar";
	private String topListingID="com.app.tokobagus.betterb:id/imageview_toplisting";
	
	
	public ListingPage(WebDriver driver) {
		super(driver);
	}
	
	@Step("Verify Filter on Mobil Bottom Result Page")
	public void getFilterOnMobilBottomResultPage(){
		waitForVisibilityOf(getIdLocator(filterResultSearchSemuaMobilID));
	}
	
	@Step("Verify openPage{0}")
	public void verifyOpenedPageSemuaMobilLink(String verifyText){
		System.out.println("Verify openPageSemuaMobilLink");
		Assert.assertEquals(verifyText,driver.findElement(getIdLocator(filterResultSearchSemuaMobilID)).getText());
	}
	
	@Step("Go To Filter Page")
	public FilterPage clickFilter(){
		clickElement(getIdLocator(filterButtonID));
		return new FilterPage(driver);
	}
	
	@Step("Go To Sort Button")
	private void clickSortPage(){
		clickElement(getIdLocator(sortButton));
	}
	
	@Step("Choose Harga Termurah")
	public void chooseHargaTermurah(){
		clickSortPage();
		clickElement(getTextLocator(filterTermurah));
	}
	
	@Step("Choose Harga Termahal")
	public void chooseHargaTermahal(){
		clickSortPage();
		clickElement(getTextLocator(filterTermahal));
	}
	
	@Step("Choose Harga Terbaru")
	public void chooseHargaTerbaru(){
		clickSortPage();
		clickElement(getTextLocator(filterTerbaru));
	}
	
	@Step("Click Action Bar")
	public void clickActionBar(){
		clickElement(getIdLocator(actionBarID));
	}
	
	
	@Step("Iterate Value from Result Search = {0}")
	public void verifyResultFilterByKeyword(String keyword) throws Exception{
		System.out.println("--Iterate value from Result Search Mobil keyword");
		Boolean status=true;
		waitForVisibilityOf(getIdLocator(brandTextTitleMobilID));
		 List<WebElement> elements = driver.findElements(getIdLocator(brandTextTitleMobilID));
		 Iterator<WebElement> program = elements.iterator();
		 while (program.hasNext()) {
		        String values = program.next().getText();
		        System.out.println("Search for keyword : "+keyword.toUpperCase()+
		        		           " from Apps Value : "+values.toUpperCase());
		        if (!values.toUpperCase().contains(keyword.toUpperCase())){
		        	status=false;
		        }
		    }
		 if (!status){
			 getAttachment("iterateMobilKeywordFilter.png");
			 Assert.fail("Please check the result from the attachment : iterateMobilKeywordFilter.png");
		 }
		 else{
			 Assert.assertTrue(status);
		 }
	}
	
	@Step("Iterate Value from Result Search by Price from {0} to {1}")
	public void verifyResultFilterByPriceRange(int fromHarga, int toHarga) throws Exception{
		System.out.println("--Iterate value from search mobil price");
	    int tempFromHarga=fromHarga;
	    int tempToHarga=toHarga;
	    if (fromHarga>toHarga){
	    	tempFromHarga=toHarga;
	    	tempToHarga=fromHarga;
	    	System.out.println("From dan To Tertukar");
	    }
	    fromHarga=tempFromHarga;
	    toHarga=tempToHarga;
		Boolean status=true;
		waitForVisibilityOf(getIdLocator(priceTextMobilID));
		 List<WebElement> elements = driver.findElements(getIdLocator(priceTextMobilID));
		 Iterator<WebElement> program = elements.iterator();
		 while (program.hasNext()) {
		        String values = program.next().getText();
		        int nilai = Integer.parseInt(values.replace("Rp ", "").replace(".", ""));
		        System.out.println("Range Harga "+String.valueOf(nilai)+
		        		           " From : "+String.valueOf(fromHarga)+" , To Harga : "+String.valueOf(toHarga));
		     if((nilai<fromHarga || nilai>toHarga)&&fromHarga!=0&&toHarga!=0){
				 getAttachment("iterateMobilPriceFilter.png");
				 Assert.fail("Please check the result from the attachment : iterateMobilPriceFilter.png");
		     }
		     
		 }
	}
	
	
	@Step("{method} {0}")
	public void verifySort(String keyword) throws Exception{
		 System.out.println("verifySort "+keyword);
		 waitForVisibilityOf(getIdLocator(priceTextMobilID));
		 List<WebElement> topListing = driver.findElements(getIdLocator(topListingID));
		 List<WebElement> elements = driver.findElements(getIdLocator(priceTextMobilID));
		 Iterator<WebElement> program = elements.iterator();
		 int normalAdsArraySize = elements.size()-topListing.size();
		 int[] hargaArray = new int[elements.size()];
		 int[] sortArray = new int[elements.size()];
		 int[] topArray = new int[topListing.size()];
		 int[] tempArray = new int[normalAdsArraySize];
		 int index=0;
		 while (program.hasNext()) {
		        String values = program.next().getText();
		        int nilai = Integer.parseInt(values.replace("Rp ", "").replace(".", ""));
		        hargaArray[index]=nilai;
		        index++;
		 }
		 
		 //Sort Array on TopListing First
		 topArray=Arrays.copyOfRange(hargaArray, 0, topListing.size());
		 tempArray=Arrays.copyOfRange(hargaArray, topListing.size(), elements.size());
		 
		 //copy Array
		 //sortArray=Arrays.copyOf(hargaArray, elements.size());
		 if (keyword.equalsIgnoreCase("Termurah")){
			 //Arrays.sort(sortArray);
			 Arrays.sort(topArray);
			 Arrays.sort(tempArray);
			 
		 }else if(keyword.equalsIgnoreCase("Termahal")){
			 topArray=sortDesc(topArray);
			 tempArray=sortDesc(tempArray);
		 }
		 
		 //merged Array from Top Listing ads with Normal Ads
		 System.arraycopy(topArray, 0, sortArray, 0, topListing.size());
		 System.arraycopy(tempArray, 0, sortArray, topListing.size(),normalAdsArraySize);
		
		 //Debug Array
		 System.out.println("-->topArray  : "+Arrays.toString(topArray));
		 System.out.println("-->tempArray : "+Arrays.toString(tempArray));
		 System.out.println("-->Array Get From Elements: "+ Arrays.toString(hargaArray));
		 System.out.println("-->Sort Array Now Become  : " + Arrays.toString(sortArray));
         
		 //if equals return true
		 if (Arrays.equals(hargaArray, sortArray)){
			 Assert.assertTrue(true);
		 }else{
			 getAttachment("verifySort"+keyword+".png");
			 Assert.fail("Please check the result from the attachment : verifySort"+keyword+".png"); 
		 }
		 getAttachment("verifySort"+keyword+".png");
		 getTextAttachment("-->Array Get From Elements: "+Arrays.toString(hargaArray)+"\n"+
                           "-->Sort Array Now Become  : " + Arrays.toString(sortArray));
	}

}