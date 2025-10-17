package com.muyu.xuaiagent.tools;

import org.springframework.ai.tool.annotation.Tool;

/**
 * @author xu
 * @version 1.0
 * @CreateTime: 2025-10-17
 * 终止工具(让自主规划的智能体合理的停止)
 */
public class TerminateTool {

    @Tool(description = """
            Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task.
            "When you have finished all the tasks, call this tool to end the work.
            """)
    public String doTerminate() {
        return "任务结束";
    }
}
