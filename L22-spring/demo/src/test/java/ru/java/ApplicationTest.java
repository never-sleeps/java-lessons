package ru.java;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.java.services.*;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest {

    @DisplayName("Из контекста тремя способами (по интерфейсу, по реализации, по id) должен корректно доставаться компонент с проставленными полями")
    @ParameterizedTest(name = "Достаем по: {0}")
    @CsvSource(value = {
            "GameService, ru.java.services.GameService",
            "GameServiceImpl, ru.java.services.GameService",
            "gameService, ru.java.services.GameService",

            "IOService, ru.java.services.IOService",
            "IOServiceImpl, ru.java.services.IOService",
            "ioService, ru.java.services.IOService",

            "PlayerService, ru.java.services.PlayerService",
            "PlayerServiceImpl, ru.java.services.PlayerService",
            "playerService, ru.java.services.PlayerService",

            "EquationPreparerService, ru.java.services.EquationPreparerService",
            "EquationPreparerServiceImpl, ru.java.services.EquationPreparerService",
            "equationPreparerService, ru.java.services.EquationPreparerService"
    })
    void shouldExtractFromContextCorrectComponentWithNotNullFields(String classNameOrBeanId, Class<?> rootClass) throws Exception {
        var context = new ClassPathXmlApplicationContext("/spring-context.xml");

        assertThat(classNameOrBeanId).isNotEmpty();
        Object component = null;

        // если первая буква заглавная, то это класс или интерфейс
        if (classNameOrBeanId.charAt(0) == classNameOrBeanId.toUpperCase().charAt(0)) {
            Class<?> gameProcessorClass = Class.forName("ru.java.services." + classNameOrBeanId); // получаем класс
            assertThat(rootClass).isAssignableFrom(gameProcessorClass);

            component = context.getBean(gameProcessorClass); // получаем бин по классу
        } else {
            // если первая буква строчная, то это просто идентификатор,
            // тогда компонент можно достать по этой строке-идентификатору
            component = context.getBean(classNameOrBeanId);
        }

        // проверяем, что компонент не null
        assertThat(component).isNotNull();
        // проверяем, что он действительно представляет из себя тот класс, который мы передавали
        assertThat(rootClass).isAssignableFrom(component.getClass());

        // проходим по полям класса и проверяем, что что все его зависимости не null
        // и что каждая из них - что-нибудь из указанных в isInstanceOfAny классов
        for (var field: component.getClass().getFields()){
            var fieldValue = field.get(component);
            assertThat(fieldValue).isNotNull().isInstanceOfAny(IOService.class, PlayerService.class, EquationPreparerService.class);
        }
    }
}