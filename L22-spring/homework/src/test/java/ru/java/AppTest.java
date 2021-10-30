package ru.java;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.java.appcontainer.AppComponentsContainerImpl;
import ru.java.config.AppConfig;
import ru.java.config.AppConfig2;
import ru.java.services.*;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    @DisplayName("Из контекста тремя способами (по интерфейсу, по реализации, по id) должен корректно доставаться компонент с проставленными полями")
    @ParameterizedTest(name = "Достаем по: {0}")
    @CsvSource(value = {
            "GameProcessor, ru.java.services.GameProcessor",
            "GameProcessorImpl, ru.java.services.GameProcessor",
            "gameProcessor, ru.java.services.GameProcessor",

            "IOService, ru.java.services.IOService",
            "IOServiceImpl, ru.java.services.IOService",
            "ioService, ru.java.services.IOService",

            "PlayerService, ru.java.services.PlayerService",
            "PlayerServiceImpl, ru.java.services.PlayerService",
            "playerService, ru.java.services.PlayerService",

            "EquationPreparer, ru.java.services.EquationPreparer",
            "EquationPreparerImpl, ru.java.services.EquationPreparer",
            "equationPreparer, ru.java.services.EquationPreparer"
    })
    void shouldExtractFromContextCorrectComponentWithNotNullFields(String classNameOrBeanId, Class<?> rootClass) throws Exception {
        // инициализация контекста
        var ctx = new AppComponentsContainerImpl(AppConfig.class);

        assertThat(classNameOrBeanId).isNotEmpty();
        Object component = null;

        // если первая буква заглавная, то это класс или интерфейс (тогда получаем компонент по классу)
        // если первая буква строчная, то это просто идентификатор (тогда получаем компонент по идентификатору)
        if (classNameOrBeanId.charAt(0) == classNameOrBeanId.toUpperCase().charAt(0)) {
            Class<?> gameProcessorClass = Class.forName("ru.java.services." + classNameOrBeanId);
            assertThat(rootClass).isAssignableFrom(gameProcessorClass);

            component = ctx.getAppComponent(gameProcessorClass);
        } else {
            component = ctx.getAppComponent(classNameOrBeanId);
        }
        assertThat(component).isNotNull();
        assertThat(rootClass).isAssignableFrom(component.getClass());

        // проходим по полям класса и проверяем, что что все его зависимости не null
        // и что каждая из них - какой-либо из указанных в isInstanceOfAny классов
        for (var field: component.getClass().getFields()){
            var fieldValue = field.get(component);
            assertThat(fieldValue)
                    .isNotNull()
                    .isInstanceOfAny(IOService.class, PlayerService.class, EquationPreparer.class);
        }

    }
}