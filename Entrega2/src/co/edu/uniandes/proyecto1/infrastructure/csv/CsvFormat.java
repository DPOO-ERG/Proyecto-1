package co.edu.uniandes.proyecto1.infrastructure.csv;

import java.util.ArrayList;
import java.util.List;

public final class CsvFormat {
    private CsvFormat() { }

    public static String escape(String value) {
        if (value == null) return "";
        boolean needsQuote = value.contains(",") || value.contains("\n") || value.contains("\"");
        String v = value.replace("\"", "\"\"");
        return needsQuote ? "\"" + v + "\"" : v;
    }

    public static String join(String... values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(escape(values[i]));
        }
        return sb.toString();
    }

    public static List<String> split(String line) {
        List<String> out = new ArrayList<>();
        if (line == null || line.isEmpty()) return out;
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        current.append('"');
                        i++; // skip escaped quote
                    } else {
                        inQuotes = false;
                    }
                } else {
                    current.append(c);
                }
            } else {
                if (c == ',') {
                    out.add(current.toString());
                    current.setLength(0);
                } else if (c == '"') {
                    inQuotes = true;
                } else {
                    current.append(c);
                }
            }
        }
        out.add(current.toString());
        return out;
    }
}


