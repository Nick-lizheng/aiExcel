package com.hkct.aiexcel.constants;

import java.io.File;

public class PathConstants {

    public static final String PATH = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "hackathon" + File.separator + "javaFile" + File.separator;


    //for Excel source path
//    public static final String ORIGINAL_EXCEL_PATH = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "hackathon" + File.separator + "excelSource" + File.separator + "latest_10min_wind.xlsx";
    public static final String ORIGINAL_EXCEL_PATH = "./excel_file/latest_10min_wind.xlsx";

    //for Excel output path
//    public static final String OUTPUT_EXCEL_PATH = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "hackathon" + File.separator + "excelSource" + File.separator + "output.xlsx";
    public static final String OUTPUT_EXCEL_PATH = "./excel_file/output.xlsx";



    //api for /case/submit
    public static final String IMPORT_EXCEL = "/case/submit";

    public static final String SELECT_EXCEL = "/case/list";
    public static final String STATUS = "/case/status";







}
