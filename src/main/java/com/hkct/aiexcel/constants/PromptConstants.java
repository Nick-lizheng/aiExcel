package com.hkct.aiexcel.constants;

public class PromptConstants {

    public static final String prompt = "任务指令：根据已解析成Markdown格式的Excel文件内容，编写Java代码以执行刚刚我描述的Excel操作，并生成新的Excel文档。\n\n" +
            "背景信息：\n" +
            "- 原始Excel文件路径: '" + PathConstants.ORIGINAL_EXCEL_PATH + "'\n" +
            "- 目标输出Excel文件路径: '" + PathConstants.OUTPUT_EXCEL_PATH + "'\n\n" +
            "具体要求：\n" +
            "- 读取位于'" + PathConstants.ORIGINAL_EXCEL_PATH + "'的Excel文件。\n" +
            "- 解析并理解提供的Markdown文件中的数据和操作说明。\n" +
            "- 根据Markdown文件中描述的操作，在原始Excel文件上执行相应处理。\n" +
            "- 将处理后的结果保存为一个新的Excel文件，存储于'" + PathConstants.OUTPUT_EXCEL_PATH + "'。\n\n" +
            "注意事项：\n" +
            "1. Java代码需要能够准确地反映Markdown文件中定义的所有操作步骤。\n" +
            "2. 确保最终生成的Excel文件符合Markdown文件中指定的所有要求。\n" +
            "3. 如果在实现过程中遇到任何问题或不确定的地方，请明确指出并提供可能的解决方案或建议。\n\n" +
            "请基于上述要求生成详细的Java代码示例。如果还有其他特定需求或者偏好（例如使用特定库来处理Excel），请通过设置以下变量告知：\n" +
            "- `${preferred_library_for_excel_handling}` (例如Apache POI, JXL等)\n" +
            "- `${additional_requirements}` (如果有额外的需求或限制条件)";


}
