package game;

import io.github.classgraph.ClassGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private static String CLASS_PATH;

    public static void main(String[] args) throws Exception {
//        new GameServer().start();

        StringBuilder builder = new StringBuilder();
        for (URI uri : new ClassGraph().getClasspathURIs()) {
            builder.append(uri.toURL().getFile()).append(File.pathSeparator);
        }
        CLASS_PATH = builder.toString().replace("%20", " ");

        File file = new File("D:\\github\\SimpleServer\\GameServer\\src\\main\\java\\script\\common\\cmd\\CmdScript.java");
        compileToOutDir(file);
    }

    public static void compileToOutDir(File file) throws IOException {
        final DiagnosticCollector<? super JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
        final JavaCompiler SYSTEM_COMPILER = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fileManager = SYSTEM_COMPILER.getStandardFileManager(diagnosticCollector, null, null)) {
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Collections.singleton(file));
            List<String> options = Arrays.asList("-encoding", "UTF-8", "-cp", CLASS_PATH, "-d", "bin");
            JavaCompiler.CompilationTask task = SYSTEM_COMPILER.getTask(null, fileManager, diagnosticCollector, options, null, compilationUnits);
            boolean success = task.call();

            if (!success) {
                logger.error(diagnosticCollector.getDiagnostics().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(System.lineSeparator())));
            }
            logger.info("编译" + (success ? "成功":"失败"));
        }
    }

}
