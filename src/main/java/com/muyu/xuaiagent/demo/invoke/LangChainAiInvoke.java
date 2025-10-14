package com.muyu.xuaiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

/**
 * @author xu
 * @version 1.0
 * @CreateTime: 2025-10-14
 */
public class LangChainAiInvoke {
    public static void main(String[] args) {

        ChatLanguageModel qwenModel = QwenChatModel.builder()
                .apiKey(TestApiKey.API_KEY)
                .modelName("qwen3-max")
                .build();
        String answer = qwenModel.chat("你好，我是一名Java开发者");
        System.out.println(answer);
    }
}
