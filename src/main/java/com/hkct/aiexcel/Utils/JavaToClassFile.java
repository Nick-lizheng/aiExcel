package com.hkct.aiexcel.Utils;

import javax.tools.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JavaToClassFile {

    public static void compileToClassFile(String sourceFilePath) {
        File sourceFile = new File(sourceFilePath);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        List<File> list = Arrays.asList(
                new File("./jar/poi-5.2.3.jar"),
                new File("./jar/poi-ooxml-5.2.3.jar"));
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));

        String collect = list.stream().map(f -> f.getAbsolutePath()).collect(Collectors.joining(";"));
        System.out.println(collect);
        Iterable<String> options = Arrays.asList(
                "-d", "./gen_src_code", // designate the output path
                "-classpath", collect
        );

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
        boolean success = task.call();

        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            System.out.println(diagnostic.getMessage(null));
        }

    }
}
