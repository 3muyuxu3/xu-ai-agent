package com.muyu.xuaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xu
 * @version 1.0
 * @CreateTime: 2025-10-16
 */
@SpringBootTest
public class ResourceDownloadToolTest {

    @Test
    public void testDownloadResource() {
        ResourceDownloadTool tool = new ResourceDownloadTool();
        String url = "https://tse3-mm.cn.bing.net/th/id/OIP-C.Afq8_E_Klz-tqfmp4D7SogHaE7?o=7&cb=12rm=3&rs=1&pid=ImgDetMain&o=7&rm=3";
        String fileName = "cat.png";
        String result = tool.downloadResource(url, fileName);
        assertNotNull(result);
    }
}
