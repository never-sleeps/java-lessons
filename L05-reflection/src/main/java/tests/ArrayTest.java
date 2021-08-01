package tests;

import ru.reflection.annotations.Before;
import ru.reflection.annotations.Test;
import ru.reflection.asserts.Assert;

import java.util.ArrayList;
import java.util.List;

public class ArrayTest {

    private List<Integer> list;

    @Before
    public void initMap() {
        list = new ArrayList<>();
    }

    @Test
    public void shouldPutIntoListSuccessfully() {
        // given
        int value = 0;

        // when
        list.add(value);

        // then
        Assert.assertTrue(list.size() == 1);
    }
}
