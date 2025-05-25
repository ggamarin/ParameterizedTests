package tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

@DisplayName("Параметарезированные веб тесты")
public class GithubParameterizedWebTests {

    @BeforeAll
  static void setupEnvironment() {
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadStrategy = "eager";
        Configuration.browser = "chrome";
    }

        @BeforeEach
        void pageSetup(){
        open("https://github.com/");
    }

    @CsvSource(value = {
            "selenide, selenide/selenide",
            "junit5, junit-team/junit5",
            "allure, allure-framework/allure2"
    })
    @ParameterizedTest(name = "Для поискового запроса {0} в первой карточке должно быть название {1}")
    void githubSiteShouldDisplayCorrectSearchQueryTest(String searchQuery, String expectedName) {
        $("[placeholder='Search or jump to...']").click();
        $("#query-builder-test").setValue(searchQuery).pressEnter();
        $(".Box-sc-g0xbh4-0.JcuiZ h3 a").shouldHave(text(expectedName));
    }

    @ValueSource(strings = {
            "selenide", "junit", "allure"
    })
    @ParameterizedTest(name = "Для поискового запроса {0} должен отдавать не пустой список карточек")
    @Tag("SMOKE")
    void githubSearchResultShouldNotBeEmptyTest(String searchQuery) {
        $("[placeholder='Search or jump to...']").click();
        $("#query-builder-test").setValue(searchQuery).pressEnter();
        $$("[data-testid='search-sub-header']").shouldBe(sizeGreaterThan(0));
    }


    static Stream<Arguments> githubRepositoryWikiPageShouldDisplayCorrectLinksTest() {
        return Stream.of(
                Arguments.of("selenide", "selenide/selenide", "selenide / selenide",
                        List.of("Quick Start", "Selenide vs Selenium",
                                "Code snippets", "Custom conditions",
                                "Custom collection conditions",
                                "How Selenide creates WebDriver",
                                "Safari browser", "Build script",
                                "Soft assertions",
                                "Ports of Selenide",
                                "Selenide Roadmap",
                                "Lazy loading",
                                "Do not use getters in tests")),
                Arguments.of("junit5", "junit-team/junit5", "junit-team / junit5",
                        List.of("Upgrading to JUnit 5.11",
                                "Third-party Extensions",
                                "How-tos",
                                "Videos",
                                "Definition of Done",
                                "Core Principles",
                                "Crowdfunding Campaign",
                                "Glossary"))
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Во вкладке вики репозитория {0} должны отображаться ссылки {2} ")
    @Tag("REGRESS")
    void githubRepositoryWikiPageShouldDisplayCorrectLinksTest(String searchQuery, String expectedName,String expectedHeader, List<String> expectedLinks) {
        $("[placeholder='Search or jump to...']").click();
        $("#query-builder-test").setValue(searchQuery).pressEnter();
        $(".Box-sc-g0xbh4-0.JcuiZ h3 a").shouldHave(text(expectedName)).click();
        $("#repository-container-header").shouldHave(text(expectedHeader));
        $("#wiki-tab").shouldBe(visible).click();
        $$("#wiki-body ul li").shouldHave(texts(expectedLinks));
    }
}

