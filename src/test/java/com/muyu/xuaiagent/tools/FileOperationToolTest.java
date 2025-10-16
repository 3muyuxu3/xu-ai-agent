package com.muyu.xuaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xu
 * @version 1.0
 * @CreateTime: 2025-10-16
 */

public class FileOperationToolTest {

    /**
     * 读文件
     */
    @Test
    public void testReadFile() {
        FileOperationTool tool = new FileOperationTool();
        String fileName = "创建文件.txt";
        String result = tool.readFile(fileName);
        assertNotNull(result);
    }

    /**
     * 写文件
     */
    @Test
    public void testWriteFile() {
        FileOperationTool tool = new FileOperationTool();
        String fileName = "创建文件.txt";
        String content = "创建测试文件用于AI超级智能体项目工具调用功能的测试";
        String result = tool.writeFile(fileName, content);
        assertNotNull(result);
    }
}
