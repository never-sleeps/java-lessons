package ru.java.dataprocessor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.java.exception.FileProcessException;

class FileLoaderTest {

    @Test
    @DisplayName("должен успешно загружаться файл")
    void loadShouldReturnLoadFile() {
        // given
        var fileLoader = new FileLoader("inputData.json");

        // when
        var result = fileLoader.load();

        // then
        Assertions.assertThat(result.size()).isEqualTo(9);
    }

    @Test
    @DisplayName("должно выбрасываться исключение при попытке загрузки несуществующего файла ")
    void loadShouldThrowExceptionForNotExistsFile() {
        // given
        var fileLoader = new FileLoader("х.json");

        // when // then
        Assertions.assertThatExceptionOfType(FileProcessException.class)
                .isThrownBy(fileLoader::load);
    }
}