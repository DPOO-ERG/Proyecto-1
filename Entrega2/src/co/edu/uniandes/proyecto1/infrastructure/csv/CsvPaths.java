package co.edu.uniandes.proyecto1.infrastructure.csv;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class CsvPaths {
    private CsvPaths() { }

    public static Path resolve(String fileName) {
        Path cwd = Paths.get("").toAbsolutePath();
        // 1) Si existe Proyecto1/data bajo el cwd (caso: ejecutando desde raíz del repo)
        Path projectDataDir = cwd.resolve("Proyecto1").resolve("data");
        if (java.nio.file.Files.isDirectory(projectDataDir)) {
            ensureDir(projectDataDir);
            return projectDataDir.resolve(fileName);
        }
        // 2) Si existe data en el cwd (caso: ejecutando dentro de Proyecto1)
        Path localDataDir = cwd.resolve("data");
        if (java.nio.file.Files.isDirectory(localDataDir)) {
            ensureDir(localDataDir);
            return localDataDir.resolve(fileName);
        }
        // 3) Si existe data en el padre (compat con entornos previos)
        Path parent = cwd.getParent();
        if (parent != null) {
            Path parentDataDir = parent.resolve("data");
            if (java.nio.file.Files.isDirectory(parentDataDir)) {
                ensureDir(parentDataDir);
                return parentDataDir.resolve(fileName);
            }
        }
        // 4) Fallback: si existe dir Proyecto1, usar Proyecto1/data; si no, usar ./data
        if (java.nio.file.Files.isDirectory(cwd.resolve("Proyecto1"))) {
            ensureDir(projectDataDir);
            return projectDataDir.resolve(fileName);
        }
        ensureDir(localDataDir);
        return localDataDir.resolve(fileName);
    }

    private static boolean ensureDir(Path dir) {
        try {
            if (dir == null) return false;
            Files.createDirectories(dir);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}


