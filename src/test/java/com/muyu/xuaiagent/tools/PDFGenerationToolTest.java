package com.muyu.xuaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xu
 * @version 1.0
 * @CreateTime: 2025-10-16
 */
public class PDFGenerationToolTest {

    @Test
    public void testGeneratePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "创建pdf文件测试.pdf";
        String content = "创建pdf文件测试用于AI拆机智能体项目的工具测试 https://gitee.com/czxspaces";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}
