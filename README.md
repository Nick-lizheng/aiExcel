# Hackathon Project

## Repository URL
https://github.com/Nick-lizheng/aiExcel

## Introduction
This project is a Spring Boot application designed to process Excel files, convert them to Markdown, generate Java code, compile it, and execute it to modify the Excel files. The project leverages various libraries and tools such as Javassist, Apache POI, and MyBatis-Plus.

## Modular Structure and Primary Functionalities

### Modular Structure
- **Controller**: Handles HTTP requests and responses.
    - `genCodeController.java`: Manages endpoints for uploading Excel files and generating code.
- **Service**: Contains business logic for code generation and Excel processing.
    - `CodeGenerationService.java`: Interface defining the service methods.
    - `CodeGenerationServiceImpl.java`: Implementation of the service methods.
- **Utils**: Utility classes for various tasks.
    - `JavaToClassFile.java`: Compiles Java source files to class files.
- **Entity**: Represents database entities.
    - `ExcelRecord.java`: Entity class for the `excel_record` table.
- **Mapper**: Interfaces for database operations.
    - `ExcelRecordMapper.java`: Mapper interface for CRUD operations on the `excel_record` table.
- **Model**: Contains request and response models.
    - `SubmitRespones.java`: Model class for the response of the code generation process.

### Primary Functionalities
- **Excel to Markdown Conversion**: Converts uploaded Excel files to Markdown format.
- **Code Generation**: Generates Java code from the Markdown content.
- **Java Code Compilation**: Compiles the generated Java code into class files.
- **Excel Modification**: Executes the compiled Java code to modify the Excel files.
- **Database Operations**: Stores records of processed Excel files in a database.

This modular structure ensures a clean separation of concerns, making the codebase easier to maintain and extend.