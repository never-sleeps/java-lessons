package com.java.lessons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.java.lessons.MessageTemplateProviderImpl.DEFAULT_TEMPLATE;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс MessageBuilderImpl")
class MessageBuilderImplTest3_Annotations {

    public static final String DEFAULT_TEMPLATE_NAME = "defaultTemplate";
    public static final String DEFAULT_MESSAGE_TEXT = "defaultText";
    public static final String DEFAULT_SIGNATURE = "defaultSignature";

    @Mock
    private MessageTemplateProvider provider;

    @InjectMocks
    private MessageBuilderImpl messageBuilder;

    @DisplayName("должен создать верное сообщение для заданного шаблона, текста и подписи")
    @Test
    void shouldBuildCorrectMessageForGivenTemplateByTextAndSign() {
        Mockito.when(provider.getMessageTemplate(Mockito.any())).thenReturn(DEFAULT_TEMPLATE);
        String expectedMessage = String.format(DEFAULT_TEMPLATE, DEFAULT_MESSAGE_TEXT, DEFAULT_SIGNATURE);

        String actualMessage = messageBuilder.buildMessage(null, DEFAULT_MESSAGE_TEXT, DEFAULT_SIGNATURE);

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @DisplayName("должен единожды вызывать нужный метод зависимости при создании сообщения")
    @Test
    void shouldFireOnceExpectedDependencyMethodWhenBuildMessage() {
        Mockito.when(provider.getMessageTemplate(DEFAULT_TEMPLATE_NAME)).thenReturn(" ");
        messageBuilder.buildMessage(DEFAULT_TEMPLATE_NAME, null, null);
        Mockito.verify(provider, Mockito.times(1)).getMessageTemplate(DEFAULT_TEMPLATE_NAME);
    }

    @DisplayName("должен кидать нужное исключение, когда зависимость возвращает null вместо шаблона")
    @Test
    void shouldThrowExpectedExceptionWhenDependencyReturnNullInsteadOfTemplate() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> messageBuilder.buildMessage(null, null, null));
    }
}