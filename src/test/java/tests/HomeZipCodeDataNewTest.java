package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import pages.TestData;
import pages.home.HomeZipCodeDataNewPage;
import utils.ProjectConstants;
import utils.TestUtils;

import java.util.List;

public class HomeZipCodeDataNewTest extends BaseTest {

    @Test
    public void testListOfStatesIsCorrectAndSorted() {
        final List<String> expectedStatesNames = ProjectConstants.EXPECTED_STATES_NAMES;
        final int expectedStatesAmount = 51;

        HomeZipCodeDataNewPage homeZipCodeDataNewPage = openBaseURL()
                .clickMarketplaceMenu()
                .switchToMarketplaceWindow()
                .clickWeatherDataByStateMenu()
                .clickDropdownSelectStateButton();

        List<String> actualStatesNames = homeZipCodeDataNewPage.getStatesNames();
        List<String> sortedExpectedStates = TestUtils.getSortedList(expectedStatesNames);

        Assert.assertEquals(actualStatesNames.size(), expectedStatesAmount);
        Assert.assertEquals(actualStatesNames, sortedExpectedStates);
    }

    @Test
    public void testListOfYearsIsCorrect() {
        final List<String> expectedYears = List.of("2018", "2019");
        final int expectedAmountOfYears = 2;

        List<String> actualYears = openBaseURL()
                .clickMarketplaceMenu()
                .switchToMarketplaceWindow()
                .clickWeatherDataByStateMenu()
                .clickDropdownSelectYearButton()
                .getYearsNames();

        Assert.assertEquals(actualYears.size(), expectedAmountOfYears);
        Assert.assertEquals(actualYears, expectedYears);
    }

    @Test
    public void testPriceForStateIsCorrect() {
        final String expectedPriceForVirginia = "1300 $";
        final String stateName = "Virginia";

        String actualPriceForVirginia = openBaseURL()
                .clickMarketplaceMenu()
                .switchToMarketplaceWindow()
                .clickWeatherDataByStateMenu()
                .clickDropdownSelectStateButton()
                .clickState(stateName)
                .getVirginiaTotalPrice();

        Assert.assertEquals(actualPriceForVirginia, expectedPriceForVirginia);
    }

    @Test
    public void testWeatherParametersAreDisplayed() {
        final List<String> expectedWeatherParameters = ProjectConstants.EXPECTED_WEATHER_PARAMETERS;

        List<String> actualWeatherParameters = openBaseURL()
                .clickMarketplaceMenu()
                .switchToMarketplaceWindow()
                .clickWeatherDataByStateMenu()
                .getWeatherParameters();

        Assert.assertEquals(actualWeatherParameters, expectedWeatherParameters);
    }

    @Test
    public void testPlaceOrderPopUpWindowIsDisplayed() {
        final String expectedPopUpHeader = "Order details";
        final String state = "Florida";
        final String year = "2019";
        final String expectedValue = "current";

        HomeZipCodeDataNewPage homeZipCodeDataNewPage = openBaseURL()
                .clickMarketplaceMenu()
                .switchToMarketplaceWindow()
                .clickWeatherDataByStateMenu()
                .clickDropdownSelectStateButton()
                .clickState(state)
                .clickDropdownSelectYearButton()
                .clickYear(year)
                .clickPlaceOrder()
                .waitUntilPlaceOrderPopUpIsVisible();

        String actualPopUpHeader = homeZipCodeDataNewPage.getPlaceOrderPopUpHeader();

        Assert.assertTrue(homeZipCodeDataNewPage.isOrderPopUpDisplayed());
        Assert.assertEquals(actualPopUpHeader, expectedPopUpHeader);
        Assert.assertEquals(homeZipCodeDataNewPage.getAttributeTopPopUpWindowOne(), expectedValue);
    }

    @Test
    public void testOrderDetailsParametersAreCorrect() {
        final String expectedState = "Florida";
        final String expectedYear = "2019";
        final String expectedPrice = "1300 $";
        final String expectedFileFormat = "CSV";
        final String expectedUnits = "Standard (Kelvin, hPa, meter/sec)";

        List<String> expectedData = List.of(
                expectedState,
                expectedYear,
                ProjectConstants.EXPECTED_WEATHER_PARAMETERS_AS_STRING,
                expectedFileFormat,
                expectedUnits,
                expectedPrice);

        HomeZipCodeDataNewPage homeZipCodeDataNewPage = openBaseURL()
                .clickMarketplaceMenu()
                .switchToMarketplaceWindow()
                .clickWeatherDataByStateMenu()
                .clickDropdownSelectStateButton()
                .clickState(expectedState)
                .clickDropdownSelectYearButton()
                .clickYear(expectedYear)
                .clickPlaceOrder()
                .waitUntilPlaceOrderPopUpIsVisible();

        Assert.assertEquals(homeZipCodeDataNewPage.getOrderParametersTexts(), expectedData);
    }

    @Test
    public void testBillingDetailsWhenFillingIndividualForms() {
        final String expectedBillingAddressHeader = "Billing address";
        final String expectedBillingDetailsHeader = "Billing details";
        final String expectedValue = "current";
        final String state = "Florida";
        final String year = "2019";
        final String firstName = ProjectConstants.FIRST_NAME;
        final String lastName = ProjectConstants.LAST_NAME;
        final String phoneNumber = ProjectConstants.PHONE_NUMBER;
        final String email = ProjectConstants.EMAIL;

        HomeZipCodeDataNewPage homeZipCodeDataNewPage = openBaseURL()
                .clickMarketplaceMenu()
                .switchToMarketplaceWindow()
                .clickWeatherDataByStateMenu()
                .clickDropdownSelectStateButton()
                .clickState(state)
                .clickDropdownSelectYearButton()
                .clickYear(year)
                .clickPlaceOrder()
                .waitUntilPlaceOrderPopUpIsVisible()
                .clickNextButton()
                .waitUntilPlaceOrderPopUpIsVisible();

        Assert.assertEquals(homeZipCodeDataNewPage.getAttributeTopPopUpWindowTwo(), expectedValue);
        Assert.assertEquals(homeZipCodeDataNewPage.getBillingDetailsHeader(), expectedBillingDetailsHeader);

        homeZipCodeDataNewPage.inputFirstName(firstName)
                .inputLastName(lastName)
                .inputPhone(phoneNumber)
                .inputEmail(email)
                .clickNextButton();

        Assert.assertEquals(homeZipCodeDataNewPage.getAttributeTopPopUpWindowThree(), expectedValue);
        Assert.assertEquals(homeZipCodeDataNewPage.getBillingAddressHeader(), expectedBillingAddressHeader);
    }

    @Test(dataProvider = "BillingDetails", dataProviderClass = TestData.class)
    public void testErrorMessage_WhenFirstNameIsEmpty_InBillingDetails(
            String scenario, String state, String year, String firstName, String lastName, String phoneNumber, String email,
             String expectedMessage) {

        HomeZipCodeDataNewPage homeZipCodeDataNewPage = openBaseURL()
                .clickMarketplaceMenu()
                .switchToMarketplaceWindow()
                .clickWeatherDataByStateMenu()
                .clickDropdownSelectStateButton()
                .clickState(state)
                .clickDropdownSelectYearButton()
                .clickYear(year)
                .clickPlaceOrder()
                .waitUntilPlaceOrderPopUpIsVisible()
                .clickNextButton()
                .waitUntilPlaceOrderPopUpIsVisible()
                .inputFirstName(firstName)
                .inputLastName(lastName)
                .inputPhone(phoneNumber)
                .inputEmail(email)
                .clickNextButton();

        String actualErrorMessageText = homeZipCodeDataNewPage.getErrorMessageText();
        Reporter.log("In case of " + scenario + " User should see a notice " + homeZipCodeDataNewPage.getDescriptionText(), true);

        Assert.assertEquals(actualErrorMessageText, expectedMessage);
        Assert.assertFalse(homeZipCodeDataNewPage.isNextButtonSubmitting());
    }
}