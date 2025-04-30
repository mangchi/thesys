package com.thesys.titan.generator;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MysqlCodeGenerator {

    static final String URL = "jdbc:mysql://thesys.biz:33306/thesys?useSSL=false&serverTimezone=UTC";
    static final String USER = "thesys";
    static final String PASSWORD = "1q2w3e4r!";

    private static final String RESPONSE = "Response";
    private static final String REQUEST = "Request";
    private static final String LOCAL_DATA_TIME = "LocalDateTime";
    private static final String PUBLIC_STRING = "public class ";

    static final String TABLE_NAME = "user";
    static final String ENTITY_NAME = "User";
    static final String BASE_PACKAGE = "com.thesys.titan.user";
    private static final String QUOTE = "\"";

    private static boolean isSkippableField(String name) {
        return name.equalsIgnoreCase("created_at") ||
                name.equalsIgnoreCase("updated_at") ||
                name.equalsIgnoreCase("created_by") ||
                name.equalsIgnoreCase("updated_by");
    }

    private static String upperFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static void main(String[] args) throws Exception {
        List<ColumnInfo> columns = getTableColumns(URL, USER, PASSWORD, TABLE_NAME);

        generateJavaFile("entity", ENTITY_NAME, generateEntity(ENTITY_NAME, BASE_PACKAGE, columns));
        generateJavaFile("dto", ENTITY_NAME + REQUEST, generateDto(ENTITY_NAME + REQUEST, BASE_PACKAGE, columns));
        generateJavaFile("dto", ENTITY_NAME + RESPONSE, generateDto(ENTITY_NAME + RESPONSE, BASE_PACKAGE, columns));
    }

    static List<ColumnInfo> getTableColumns(String url, String user, String password, String table)
            throws SQLException {
        List<ColumnInfo> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getColumns(null, null, table, null)) {
                while (rs.next()) {
                    list.add(new ColumnInfo(
                            rs.getString("COLUMN_NAME"),
                            rs.getString("TYPE_NAME"),
                            rs.getString("REMARKS")));
                }
            }
        }
        return list;
    }

    static void generateJavaFile(String subDir, String className, String content) throws IOException {
        Path dir = Paths.get("generated", subDir);
        Files.createDirectories(dir);
        Path file = dir.resolve(className + ".java");
        Files.writeString(file, content);
        log.debug("Generated: " + file);
    }

    static String generateEntity(String className, String basePackage, List<ColumnInfo> columns) {
        StringBuilder sb = new StringBuilder();
        Set<String> imports = new HashSet<>(List.of(
                "jakarta.persistence.Column",
                "jakarta.persistence.GeneratedValue",
                "jakarta.persistence.GenerationType",
                "jakarta.persistence.Id",
                "jakarta.persistence.Entity",
                "jakarta.persistence.Table",
                "lombok.Builder",
                "lombok.Getter",
                "lombok.Setter",
                "lombok.AllArgsConstructor",
                "lombok.NoArgsConstructor",
                "com.thesys.titan.common.entity.BaseEntity"));
        if (columns.stream().anyMatch(c -> mapToJavaType(c.type()).equals(LOCAL_DATA_TIME))) {
            imports.add("java.time.LocalDateTime");
        }
        imports.add(basePackage + ".dto." + className + REQUEST);

        sb.append("package ").append(basePackage).append(".entity;\n\n");
        imports.forEach(i -> sb.append("import ").append(i).append(";\n"));

        sb.append("\n@Getter\n@Setter\n@Entity\n@NoArgsConstructor\n@AllArgsConstructor\n@Builder\n@Table(name = ")
                .append(QUOTE).append(TABLE_NAME).append(QUOTE).append(")\n");
        sb.append(PUBLIC_STRING).append(className).append(" extends BaseEntity").append(" {\n\n");

        for (ColumnInfo col : columns) {
            if (isSkippableField(col.name()))
                continue;

            if (col.name().equalsIgnoreCase("id")) {
                sb.append("    @Id\n    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
            }
            sb.append("    @Column(name = \"").append(col.name());
            if (col.type().equalsIgnoreCase("BIT")) {
                sb.append("\", columnDefinition = \"TINYINT(1)\")\n");
            } else {
                sb.append("\")\n");
            }

            sb.append("    private ").append(mapToJavaType(col.type())).append(" ").append(toCamelCase(col.name()))
                    .append(";\n\n");

        }

        // === of() 메서드 ===
        sb.append("    public static ").append(className).append(" of(").append(className)
                .append("Request request) {\n");
        sb.append("        return ").append(className).append(".builder()\n");
        for (ColumnInfo col : columns) {
            if (isSkippableField(col.name()))
                continue;
            sb.append("            .").append(toCamelCase(col.name())).append("(request.get")
                    .append(upperFirst(toCamelCase(col.name()))).append("())\n");
        }
        sb.append("            .build();\n");
        sb.append("    }\n\n");

        // === update() 메서드 ===
        sb.append("    public void update(").append(className).append("Request request) {\n");
        for (ColumnInfo col : columns) {
            if (isSkippableField(col.name()))
                continue;
            sb.append("        this.").append(toCamelCase(col.name())).append(" = request.get")
                    .append(upperFirst(toCamelCase(col.name()))).append("();\n");
        }
        sb.append("    }\n");

        sb.append("}\n");
        return sb.toString();
    }

    static String generateDto(String className, String basePackage, List<ColumnInfo> columns) {
        StringBuilder sb = new StringBuilder();
        Set<String> imports = new HashSet<>(List.of(
                "lombok.Getter",
                "lombok.Setter",
                "lombok.ToString",
                "io.swagger.v3.oas.annotations.media.Schema"));
        // if (columns.stream().anyMatch(c ->
        // mapToJavaType(c.type()).equals(LOCAL_DATA_TIME))) {
        // imports.add("java.time.LocalDateTime");
        // }
        if (className.contains(REQUEST)) {
            imports.add("com.thesys.titan.common.dto.CommonRequest");
            imports.add("jakarta.validation.constraints.NotNull");
            imports.add("jakarta.validation.constraints.NotBlank");
            imports.add("jakarta.validation.constraints.Pattern");
            imports.add("jakarta.validation.constraints.Size");
            imports.add("com.thesys.titan.common.valid.ValidGroups");
        }

        sb.append("package ").append(basePackage).append(".dto;\n\n");
        imports.forEach(i -> sb.append("import ").append(i).append(";\n"));
        sb.append("\n@Getter\n@Setter\n@ToString\n");
        sb.append(PUBLIC_STRING).append(className);
        if (className.contains(REQUEST)) {
            sb.append(" extends Common").append(REQUEST);
        }
        sb.append(" {\n\n");

        for (ColumnInfo col : columns) {

            log.debug("Column: " + col.name() + ", Type: " + col.type() + ", Comment: " + col.comment());
            if (isSkippableField(col.name()))
                continue;
            if (col.name().equalsIgnoreCase("id") && className.contains(REQUEST)) {
                sb.append(
                        "    @NotNull(groups = { ValidGroups.SelectValid.class, ValidGroups.UpdateValid.class, ValidGroups.DeleteValid.class }, message = \"{error.notNull}\")\n");
            }

            String camelName = toCamelCase(col.name());

            sb.append("    @Schema(description = \"").append(col.comment() != null ? col.comment() : "")
                    .append("\", example = \"").append(col.name()).append("\", nullable = true")
                    .append(")\n");

            sb.append("    private ").append(mapToJavaType(col.type()))
                    .append(" ").append(camelName).append(";\n\n");

        }

        sb.append("}\n");
        return sb.toString();
    }

    static String mapToJavaType(String sqlType) {
        return switch (sqlType.toUpperCase()) {
            case "BIGINT" -> "Long";
            case "INT", "INTEGER" -> "Integer";
            case "VARCHAR", "TEXT", "CHAR", "LONGTEXT" -> "String";
            case "BOOLEAN", "TINYINT(1)", "BIT" -> "Boolean";
            case "DATETIME", "TIMESTAMP" -> LOCAL_DATA_TIME;
            case "DATE" -> "LocalDate";
            default -> "String";
        };
    }

    static String toCamelCase(String name) {
        String[] parts = name.split("_");
        StringBuilder sb = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            sb.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1));
        }
        return sb.toString();
    }

    record ColumnInfo(String name, String type, String comment) {
    }
}
