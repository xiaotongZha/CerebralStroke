package utils;
import java.io.IOException;

import java.nio.file.*;
import java.util.stream.Stream;

public class ClearUtils {
    public static void clearAllGenerateDir() throws IOException {
        clearEachDir(Paths.get(System.getProperty("user.dir"), "py", "sharedata"));
        clearEachDir(Paths.get(System.getProperty("user.dir"), "py", "processdata"));
        clearEachDir(Paths.get(System.getProperty("user.dir"), "py", "rawdata"));
    }
    public static void clearEachDir(Path dir) throws IOException{
        try (Stream<Path> paths = Files.walk(dir)) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.err.println("无法删除文件: " + path + " - " + e.getMessage());
                        }
                    });
        }
    }
}
