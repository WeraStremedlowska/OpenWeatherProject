package tests.api;

import api.ApiHelpers;
import api.CaptureNetworkTraffic;
import api.model.Current;
import base.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import pages.MainPage;
import tests.retrytest.Retry;
import utils.DateTimeUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class APIMainTest extends BaseTest {
    final static String DATA_URL = "https://openweathermap.org/data/2.5/";
    final static String PARIS_URL_ONECALL = DATA_URL
            + "onecall?lat=48.8534&lon=2.3488&units=metric&appid=439d4b804bc8187953eb36d2a8c26a02";
    final static String PARIS_URL_WEATHER = DATA_URL + "weather?id=2988507&appid=439d4b804bc8187953eb36d2a8c26a02";
    static HttpResponse<String> response;
    static String city;
    static String country;
    static long weatherTemp;
    static long weatherFeelsLike;
    static String weatherDescription;
    static List<String> weatherDescriptionList = new ArrayList<>();

    @Test
    public void test_API_CNTRequest_OpenBaseURL() {
        List<String> requests = new CaptureNetworkTraffic()
                .setUpDevTool(getDriver())
                .captureHttpRequestsContain("weather");

        openBaseURL();

        Assert.assertNotNull(requests);
        for (int i = 0; i < requests.size(); i += 2) {
            Assert.assertEquals(requests.get(i), "GET");
        }
        for (int i = 1; i < requests.size(); i += 2) {
            Assert.assertTrue(requests.get(i).contains("openweathermap.org/"));
        }
    }

    @Test
    public void test_API_CNTResponse_OpenBaseURL() {
        List<String> responses = new CaptureNetworkTraffic()
                .setUpDevTool(getDriver())
                .captureHttpResponsesContain("weather");

        openBaseURL();

        Assert.assertNotNull(responses);
        for (int i = 0; i < responses.size(); i += 4) {
            Assert.assertEquals(responses.get(i), "200");
        }
        for (int i = 1; i < responses.size(); i += 4) {
            Assert.assertEquals(responses.get(i), "OK");
        }
        for (int i = 2; i < responses.size(); i += 4) {
            Assert.assertTrue(responses.get(i).contains("openweathermap.org/"));
        }

        Assert.assertTrue(Double.parseDouble(responses.get(3).substring(10, 14)) <= 3);
    }

    @Test
    public void test_API_CNTRequests_WhenSearchingCityCountry() {
        List<String> requestsSearchButton = new CaptureNetworkTraffic()
                .setUpDevTool(getDriver())
                .captureHttpRequestsContain("weather");

        MainPage mainPage = openBaseURL()
                .clickSearchCityField()
                .inputSearchCriteria("Paris")
                .clickSearchButton();

        Assert.assertNotNull(requestsSearchButton);
        Assert.assertEquals(requestsSearchButton.get(requestsSearchButton.size() - 2), "GET");
        Assert.assertTrue(requestsSearchButton.get(requestsSearchButton.size() - 1)
                .contains("openweathermap.org/data/2.5/find?q=Paris"));

        mainPage.clickParisInDropDownList();

        Assert.assertNotNull(requestsSearchButton);
        Assert.assertEquals(requestsSearchButton.get(requestsSearchButton.size() - 2), "GET");
        Assert.assertTrue(requestsSearchButton.get(requestsSearchButton.size() - 1)
                .contains("openweathermap.org/data/2.5/onecall?lat=48.8534&lon=2.3488"));
    }

    @Test(retryAnalyzer = Retry.class)
    public void test_API_HttpRequestResponse_WhenSearchingCityCountry() {
        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(PARIS_URL_WEATHER))
                    .GET()
                    .build();

            response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

        } catch (URISyntaxException | InterruptedException | IOException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.body());
        Assert.assertEquals(response.statusCode(), 200);

        Reporter.log("Response: " + response.body(), true);

        final JSONObject obj = new JSONObject(response.body());
        final JSONArray weather = obj.getJSONArray("weather");
        for (int i = 0; i < weather.length(); i+=2) {
            JSONObject item = weather.getJSONObject(i);
            if (item.keySet().contains("description")) {
                weatherDescription = (String) item.get("description");
                break;
            }
        }

        city = obj.getString("name");
        country = obj.getJSONObject("sys").getString("country");
        weatherTemp = Math.round(obj.getJSONObject("main").getDouble("temp"));

        String expectedCityCountry = city.concat(", ").concat(country);
        String expectedCurrentTemp = String.valueOf(weatherTemp).concat("°C");

        List<String> actualUIWeatherCondition = Arrays.asList(
                openBaseURL()
                        .clickSearchCityField()
                        .inputSearchCriteria("Paris")
                        .clickSearchButton()
                        .clickParisInDropDownList()
                        .waitForCityCountryNameChanged("London, GB")
                        .getCurrentWeatherText()
                        .toString()
                        .split("\n")
        );

        Reporter.log(String.valueOf(actualUIWeatherCondition), true);

        Assert.assertEquals(actualUIWeatherCondition.get(1), expectedCityCountry);
        Assert.assertEquals(actualUIWeatherCondition.get(2), expectedCurrentTemp);

    }

    @Test
    public void test_API_RAResponse_WhenSearchingCityCountry() {
        Current obj = given()
                .when()
                .contentType(ContentType.JSON)
                .get(PARIS_URL_ONECALL)
                .then().log().ifError()
                .extract().body().jsonPath().getObject("current", Current.class);

        weatherTemp = Math.round(obj.getTemp());
        weatherFeelsLike = Math.round(obj.getFeels_like());

        MainPage mainPage = openBaseURL()
                .clickSearchCityField()
                .inputSearchCriteria("Paris")
                .clickSearchButton()
                .clickParisInDropDownList()
                .waitForCityCountryNameChanged("London, GB");

        String actualCurrentTemp = mainPage.getCurrentTempAndUnit();
        String actualFeelsLike = mainPage.getFeelsLike();

        Assert.assertEquals(actualCurrentTemp, String.valueOf(weatherTemp).concat("°C"));
        Assert.assertEquals(actualFeelsLike, "Feels like " + weatherFeelsLike + "°C.");
    }

    @Test
    public void test_API_RAResponse_8DayForecastWeatherDescription() {
        final Response response = given()
                .when()
                .contentType(ContentType.JSON)
                .get(PARIS_URL_ONECALL)
                .then()
                .log().ifError()
                .extract().response();

        final List<JSONObject> daily = response.jsonPath().get("daily");

        for (int i = 0; i < daily.size(); i++) {
            weatherDescriptionList.add(
                    response.jsonPath()
                            .get(String.format("daily[%d].weather[0].description", i))
            );
        }

        MainPage mainPage = openBaseURL();

         List<String> weatherDescriptionFromUI = mainPage
                .clickSearchCityField()
                .inputSearchCriteria("Paris")
                .clickSearchButton()
                .clickParisInDropDownList()
                .waitForCityCountryNameChanged("London, GB")
                .getListWeatherDescriptionText();

        Reporter.log(String.valueOf(weatherDescriptionFromUI), true);

        if (!daily.isEmpty() && !weatherDescriptionFromUI.isEmpty() && daily.size() == weatherDescriptionFromUI.size()) {
            Assert.assertEquals(weatherDescriptionFromUI, weatherDescriptionList);
        }
    }

    @Test
    public void test_API_HttpResponse_UIEightDaysForecastCalendarOnCurrentDateFrom() {
        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(PARIS_URL_ONECALL))
                    .GET()
                    .build();

            response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

        } catch (URISyntaxException | InterruptedException | IOException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.body());
        Assert.assertEquals(response.statusCode(), 200);

        Reporter.log("Response: " + response.body(), true);

        final JSONObject obj = new JSONObject(response.body());

        final String[] dowMonDate = ApiHelpers.getEightDaysForecastCalendar(obj).trim().split(" ");
        final String monNum = dowMonDate[1];
        final int date = Integer.parseInt(dowMonDate[2]);

        final String expectedApiResult =
                DateTimeUtils
                        .getEightDaysFromDate(monNum, date, Year.now()
                                .getValue());

        MainPage mainPage = openBaseURL();

         List<String> uiResult = mainPage
                .clickSearchCityField()
                .inputSearchCriteria("Paris")
                .clickSearchButton()
                .clickParisInDropDownList()
                .waitForCityCountryNameChanged("London, GB")
                .getListOfEightDaysDataText();

        String actualUiResult = ApiHelpers.getFormattedResult(uiResult);

        Reporter.log(actualUiResult, true);

        if (!actualUiResult.isEmpty() && !actualUiResult.isBlank()) {
            Assert.assertEquals(actualUiResult, expectedApiResult);
        }
    }

    @Test
    public void testAllLinksAreNotBroken() {
        final List<String> allLinks = openBaseURL().getAllLinks();

        List<String> brokenLinks = new ArrayList<>();
        List<String> workingLinks = new ArrayList<>();

        for (String link : allLinks) {
            try {
                URL url = new URL(link);

                HttpURLConnection httpURLConnect = (HttpURLConnection) url.openConnection();
                httpURLConnect.setConnectTimeout(500);
                httpURLConnect.connect();

                if (httpURLConnect.getResponseCode() >= 400) {
                    brokenLinks.add(link);
                    Reporter.log("Broken links " + link + httpURLConnect.getResponseMessage(), true);
                } else {
                    workingLinks.add(link);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Assert.assertEquals(workingLinks.size() + brokenLinks.size(), allLinks.size());
        Reporter.log("Working links " + workingLinks.size(), true);
        Reporter.log("Broken links " + brokenLinks.size(), true);
    }
}
