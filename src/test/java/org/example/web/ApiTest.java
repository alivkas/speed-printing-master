package org.example.web;

import org.junit.Assert;
import org.junit.Test;

/**
 * Тесты для класса Api
 */
public class ApiTest {

    /**
     * Тестировать получение данных из api
     */
    @Test
    public void getApiTest() {
        Api api = new Api();
        String text = api.getApi();
        Assert.assertNotEquals(null, text);
    }
}
