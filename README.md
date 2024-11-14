# Hackathon Project --AiExcel

## Repository URL
https://github.com/Nick-lizheng/aiExcel
https://github.com/Nick-lizheng/aiExcel_FE

## Introduction
This project leverages the Tongyi Qianwen large model to generate code for manipulating Excel files based on user instructions and returns the required Excel file to the user. The project is built using Java, Spring Boot, and Maven, and it follows a modular structure to ensure maintainability and scalability.

## Modular Structure and Primary Functionalities

### Modular Structure
- **Controller**: Handles HTTP requests and responses.
  - `StatusController.java`: Manages endpoints for updating the status of Excel templates.
  - `genCodeController.java`: Manages endpoints for uploading Excel files and generating code.
- **Service**: Contains business logic for code generation and Excel processing.
  - `StatusService.java`: Interface defining the service methods.
  - `StatusServiceImpl.java`: Implementation of the service methods.
  - `CodeGenerationService.java`: Interface defining the service methods.
  - `CodeGenerationServiceImpl.java`: Implementation of the service methods.
- **Utils**: Utility classes for various tasks.
  - `CommonOssUtils.java`: Provides helper methods for saving Java code to OSS.
  - `JavaToClassFile.java`: Compiles Java source files to class files.
- **Entity**: Represents database entities.
  - `ExcelRecord.java`: Entity class for the `excel_record` table.
- **Mapper**: Interfaces for database operations.
  - `ExcelRecordMapper.java`: Mapper interface for CRUD operations on the `excel_record` table.
- **Model**: Contains request and response models.
  - `SubmitRespones.java`: Model class for the response of the code generation process.

### Primary Functionalities
- **Excel to Markdown Conversion**: Converts uploaded Excel files to Markdown format using the aliyun DocMind API.
- **Code Generation**: According user prompt to generates Java code from the Markdown content using the Qwen-plus API.
- **Java Code Compilation**: Compiles the dynamic generated Java code into class files.
- **Excel Modification**: Executes the compiled Java code to modify the Excel files.
- **Apply Java to Other Excel Template**: Executes the compiled Java code to modify other similar Excel files.
- **Database Operations**: Stores records of processed Excel files in a database and OSS driver.

## API Endpoints

### `api/case/submit`
- **Specification**: Users submit an Excel template and instructions on how to manipulate the Excel file.
- **Method**: POST
- **Request Body**:
  - `multipart/form-data`
    - `file`: File needed to upload
    - `userId`: "string"
    - `instruction`: "string"
- **Response Body**:
  - `template_id`: "string"
  - `message`: "string"

### `api/case/status`
- **Specification**: Users preview the Excel file and decide to keep or discard it, marking the correct backend code template in the database or deleting it.
- **Method**: POST
- **Request Body**:
  - `Json`
    - `template ID`: "string"
    - `status`: "obsolete" \ "store"
- **Response Body**:
  - `status`: "ok"

### `api/case/generate`
- **Specification**: Users use an existing template to parse data.
- **Method**: POST
- **Request Body**:
  - `multipart/form-data`
    - `file`: File needed to upload
    - `template_id`: "string"
- **Response Body**:
  - `binary Data`
  - `template_id`: "string"

### `api/case/list`
- **Specification**: Users view the list of code that can correctly parse Excel files.
- **Method**: POST
- **Request Body**:
  - `Json`
    - `status`: "1"
- **Response Body**:
  - `Json`
    - `data`: [{"templateUd": "xxx"}, {"templateUd": "xxx"}]
