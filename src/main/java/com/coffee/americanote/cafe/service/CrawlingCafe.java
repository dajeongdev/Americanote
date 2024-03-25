package com.coffee.americanote.cafe.service;

import com.coffee.americanote.cafe.domain.entity.Cafe;
import com.coffee.americanote.cafe.repository.CafeRepository;
import com.coffee.americanote.cafe.service.AddressToCoordinate;
import com.coffee.americanote.coffee.domain.entity.Coffee;
import com.coffee.americanote.coffee.domain.entity.CoffeeFlavour;
import com.coffee.americanote.coffee.repository.CoffeeFlavourRepository;
import com.coffee.americanote.coffee.repository.CoffeeRepository;
import com.coffee.americanote.global.Degree;
import com.coffee.americanote.global.Flavour;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingCafe {

    private final AddressToCoordinate addressToCoordinate;
    private final CafeRepository cafeRepository;
    private final CoffeeRepository coffeeRepository;
    private final CoffeeFlavourRepository flavourRepository;

    private WebDriver driver;
    private static final String LOCATION = "연남동";
    private static final String KEYWORD = "카페";
    private static final String URL = "https://map.naver.com/v5/";

    private static final List<String> MENU_NAMES = List.of("아메리카노", "커피", "americano", "coffee");

    // 매장 정보들을 저장할 해시맵
    Map<String, ArrayList<String>> coffeeInfoms = new HashMap<>();

    public void process() throws InterruptedException {
        // 크롬 드라이버 세팅 (드라이버 설치 경로 입력)
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver123version/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        // 브라우저 선택
        driver = new ChromeDriver(options);

        getDataList();

        // 탭 닫기
        driver.close();
        // 브라우저 닫기
        driver.quit();
    }

    // 데이터 가져오기
    private void getDataList() throws InterruptedException {
        // 브라우저에서 url로 이동한다.
        driver.get(URL);
        // 브라우저에서 로딩될 때까지 잠시 기다린다.
        Thread.sleep(2000);

        // 네이버 지도 검색창에 원하는 단어 입력 후 엔터
        // 연남동카페 검색
        WebElement inputSearch = driver.findElement(By.className("input_search"));
        inputSearch.sendKeys(LOCATION + " " + KEYWORD);
        Thread.sleep(1500);
        inputSearch.sendKeys(Keys.ENTER);
        Thread.sleep(1500);

        // 데이터가 iframe 안에 있는 경우 (HTML 안 HTML) 이동
        driver.switchTo().frame("searchIframe");

        while (true) {
            // 다음 페이지로 가는 버튼
            WebElement nextButton = driver.findElement(By.xpath("//span[contains(text(), '다음페이지')]/.."));

            // 원하는 요소를 찾기
            WebElement scrollBox = driver.findElement(By.id("_pcmap_list_scroll_container"));

            // 한 페이지 전부 스크롤
            while (true) {
                long lastHeight = (long) ((JavascriptExecutor) driver).executeScript(
                        "return arguments[0].scrollHeight;", scrollBox);
                // 스크롤을 가장 아래로 내림
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTo(0, arguments[0].scrollHeight);",
                        scrollBox);
                Thread.sleep(2000);
                long newHeight = (long) ((JavascriptExecutor) driver).executeScript("return arguments[0].scrollHeight;",
                        scrollBox);
                // 스크롤이 더이상 내려가지 않을 때 반복문 종료
                if (newHeight == lastHeight) {
                    break;
                }
            }

            // 사이트에서 전체 매장을 찾은 뒤 코드를 읽는다.
            List<WebElement> elements = driver.findElements(By.xpath("//*[@id='_pcmap_list_scroll_container']//li"));

            for (WebElement e : elements) {
                WebElement finalElement = e.findElement(By.xpath(".//a/div/div/span"));
                // 매장명을 키값으로 해시맵 생성
                coffeeInfoms.put(finalElement.getText(), new ArrayList<>());
            }

            // 매장을 하나씩 클릭하고 주소를 읽는다.
            for (WebElement e : elements) {
                // 약간 스크롤
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView(true);", e);

                //e.click(); // 이거클릭하니까 사진 탭으로 가버림
                // 가게 이름 클릭
                e.findElement(By.xpath(".//a/div/div/span")).click();
                String key = e.findElement(By.xpath(".//a/div/div/span")).getText();


                Thread.sleep(2000);
                driver.switchTo().parentFrame(); // 부모 프레임으로 이동
                driver.switchTo().frame(driver.findElement(By.id("entryIframe"))); // 옆 iframe으로 이동
                Thread.sleep(2000);

                // 주소 가져오기
                js.executeScript("window.scrollBy(0, 200);"); // 세로로 50px 만큼 스크롤
                String address = driver.findElement(By.cssSelector("span.LDgIH")).getText();
                Thread.sleep(2000);

                try {
                    // 텍스트를 포함하는 요소를 찾기 위한 XPath
                    driver.findElement(By.xpath("//div[@class='flicking-camera']//*[contains(text(), '메뉴')]")).click();
                } catch (Exception ex) {
                    try {
                        // 바에서 보통 3번째가 '메뉴'
                        driver.findElement(By.xpath("//div[@class='flicking-camera']/*[3]")).click();
                    } catch (Exception ex1) {
                        // 메뉴 클릭 못하면 해시맵에서 삭제
                        coffeeInfoms.remove(key);
                        continue;
                    }
                }

                Thread.sleep(2000);

                // 메뉴 정보 가져오기
                String[] menuInfo = getMenuInfo(driver);
                // 5000~6000 이런건 처리 불가
                menuInfo[1] = menuInfo[1].replaceAll("[^0-9]", "");

                // 메뉴 가격이 있으면 저장
                if (!menuInfo[1].equals("")) {
                    // 주소 -> 좌표
                    String[] coordinate = addressToCoordinate.addressToCoordinate(address);

                    js.executeScript("window.scrollTo(0, 0)");
                    WebElement profile = null;
                    try {
                        // 대표 사진
                        profile = driver.findElement(By.cssSelector("div#ibu_1.K0PDV._div"));
                    } catch (Exception exception) {
                        // 대표 사진 위치에 동영상이 있다면 옆 사진
                        profile = driver.findElement(By.cssSelector("div#ibu_2.K0PDV._div"));
                    }
                    String style = profile.getAttribute("style");
                    String imageUrl = extractImageUrl(style); // 대표 사진

                    // db에 카페 정보 저장
                    Cafe cafeEntity = Cafe.builder()
                            .name(key)
                            .address(address)
                            .latitude(Double.parseDouble(coordinate[0]))
                            .longitude(Double.parseDouble(coordinate[1]))
                            .imageUrl(imageUrl).build();
                    Cafe savedCafe = cafeRepository.save(cafeEntity);
                    // db에 커피 정보 저장
                    Coffee coffeeEntity = Coffee.builder()
                            .cafe(savedCafe)
                            .name(menuInfo[0])
                            .intensity(getRandomDegree())
                            .acidity(getRandomDegree())
                            .price(Integer.parseInt(menuInfo[1])).build();
                    Coffee savedCoffee = coffeeRepository.save(coffeeEntity);
                    // db에 커피 향 정보 저장
                    List<Flavour> randomFlavours = getRandomFlavours();
                    for (Flavour randomFlavour : randomFlavours) {
                        CoffeeFlavour flavourEntity = CoffeeFlavour.builder()
                                .coffee(savedCoffee)
                                .flavour(randomFlavour).build();
                        flavourRepository.save(flavourEntity);
                    }

                    coffeeInfoms.remove(key);
                } else {
                    // 메뉴 없으면 해시맵에서 삭제
                    coffeeInfoms.remove(key);
                }

                driver.switchTo().parentFrame(); // 부모 프레임으로 이동
                driver.switchTo().frame("searchIframe"); // 원래 iframe으로 이동
            }

            if (nextButton.getAttribute("aria-disabled").equals("true")) {
                // 다음 페이지로 못가면 break;
                break;
            }

            // 다음 페이지로 이동
            nextButton.click();
            Thread.sleep(2000);
        }
    }

    public static String[] getMenuInfo(WebDriver driver) {
        try {
            // 더보기 클릭
            WebElement moreSee = driver.findElement(By.className("fvwqf"));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", moreSee);
            moreSee.click();
        } catch (Exception ignored) {

        }
        try {
            // 메뉴 정보가 있는 요소를 찾아서 가져오기
            List<WebElement> menuElements = driver.findElements(By.xpath("//div[@class='yQlqY']/span[@class='lPzHi']"));
            // 변동 가격이랑 123~124 이런 가격도 나오도록
            List<WebElement> priceElements = driver.findElements(By.xpath("//div[@class='GXS1X']"));

            String[] menuInfo = {"", ""};

            // 각 메뉴 요소에서 메뉴 이름과 가격을 가져오기
            for (int i = 0; i < menuElements.size(); i++) {
                String menuName = menuElements.get(i).getText().toLowerCase(); // 소문자로 변형 -> 비교 위해서
                String price = priceElements.get(i).getText();

                if (MENU_NAMES.contains(menuName)) {
                    // 메뉴 이름과 가격을 문자열로 추가
                    menuInfo[0] = menuName;
                    menuInfo[1] = price;
                    break;
                }
            }
            // 메뉴 정보 반환
            return menuInfo;
        } catch (Exception e) {
            log.info(e.getMessage());
            return new String[]{"", ""};
        }
    }

    public static String extractImageUrl(String styleAttribute) {
        Pattern pattern = Pattern.compile("url\\(\"(.*?)\"\\)");
        Matcher matcher = pattern.matcher(styleAttribute);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    public static Degree getRandomDegree() {
        Degree[] degrees = Degree.values();
        Random random = new Random();
        int randomIndex = random.nextInt(degrees.length);
        return degrees[randomIndex];
    }

    public static List<Flavour> getRandomFlavours() {
        Random random = new Random();
        List<Flavour> allFlavours = List.of(Flavour.values());
        int chooseNum = random.nextInt(3) + 1;

        List<Flavour> chooseFlavours = new ArrayList<>();
        for (int i = 0; i < chooseNum; i++) {
            Flavour randomFlavour;
            do {
                randomFlavour = allFlavours.get(random.nextInt(allFlavours.size()));
            } while (chooseFlavours.contains(randomFlavour));
            chooseFlavours.add(randomFlavour);
        }
        return chooseFlavours;
    }
}
