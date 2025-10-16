package com.muyu.xuaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author xu
 * @version 1.0
 * @CreateTime: 2025-10-16
 */
@SpringBootTest
class WebSearchToolTest {


    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Test
    void searchWeb() {
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        String query = "菜鸟教程 https://www.runoob.com/";
        String result = webSearchTool.searchWeb(query);
        Assertions.assertNotNull(result);
    }
}