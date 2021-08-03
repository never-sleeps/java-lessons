package test;

import ru.reflection.annotations.After;
import ru.reflection.annotations.Before;
import ru.reflection.annotations.Test;
import ru.reflection.asserts.Assert;

import java.util.HashMap;
import java.util.Map;

public class MapTest {

    private Map<Integer, String> testMap;

    @Before
    public void initMap() {
        testMap = new HashMap<>();
    }

    @After
    public void clearMap() {
        testMap.clear();
    }

    @Test
    public void shouldPutIntoMapSuccessfully() {
        // given
        String value = "a";

        // when
        testMap.put(0, value);

        // then
        Assert.assertTrue(testMap.size() == 1);
    }

    @Test
    public void shouldReturnExceptionForSize() {
        // given
        String value = "a";

        // when
        testMap.put(0, value);

        // then
        Assert.assertEquals(0, testMap.size());
    }


    @Test
    public void shouldReturnExceptionForKey() {
        // given
        int key = 0;
        String value = "a";

        // when
        testMap.put(key, value);

        // then
        Assert.assertFalse(testMap.containsKey(key));
    }

    @Test
    public void shouldReturnExceptionForValue() {
        // given
        String value = "a";

        // when
        testMap.put(0, value);

        // then
        Assert.assertTrue(testMap.get(0) == null);
    }
}
