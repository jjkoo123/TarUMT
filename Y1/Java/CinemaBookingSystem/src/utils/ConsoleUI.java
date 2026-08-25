package utils;


import java.util.List;

public final class ConsoleUI {
    private static final String BORDER_LINE = "════════════════════════════════════════════════════════════";
    private static final String BOX_TOP     = "╔══════════════════════════════════════════════════════════╗";
    private static final String BOX_BOTTOM  = "╚══════════════════════════════════════════════════════════╝";
    private static final String BOX_MIDDLE  = "╠══════════════════════════════════════════════════════════╣";
    private static final int BOX_WIDTH      = 58;
    private static final boolean ANSI_ENABLED = isAnsiEnabled();

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private ConsoleUI() {
    }

    public static void headerArt(String type) {
        System.out.println();
        String art = "";
        switch (type.toLowerCase()) {
            case "main":
                art = colorize("   __  __    _    ___ _   _ \n", ANSI_CYAN) +
                        colorize("  |  \\/  |  / \\  |_ _| \\ | |\n", ANSI_CYAN) +
                        colorize("  | |\\/| | / _ \\  | ||  \\| |\n", ANSI_CYAN) +
                        colorize("  | |  | |/ ___ \\ | || |\\  |\n", ANSI_CYAN) +
                        colorize("  |_|  |_/_/   \\_\\___|_| \\_|\n", ANSI_CYAN);
                break;
            case "admin":
                art = colorize("      _    ____  __  __ ___ _   _ \n", ANSI_YELLOW) +
                        colorize("     / \\  |  _ \\|  \\/  |_ _| \\ | |\n", ANSI_YELLOW) +
                        colorize("    / _ \\ | |_) | |\\/| || ||  \\| |\n", ANSI_YELLOW) +
                        colorize("   / ___ \\|  __/| |  | || || |\\  |\n", ANSI_YELLOW) +
                        colorize("  /_/   \\_\\_|   |_|  |_|___|_| \\_|\n", ANSI_YELLOW);
                break;
            case "customer":
                art = colorize("   _   _ ____  _____ ____  \n", ANSI_GREEN) +
                        colorize("  | | | / ___|| ____|  _ \\ \n", ANSI_GREEN) +
                        colorize("  | | | \\___ \\|  _| | |_) |\n", ANSI_GREEN) +
                        colorize("  | |_| |___) | |___|  _ < \n", ANSI_GREEN) +
                        colorize("   \\___/|____/|_____|_| \\_\\\n", ANSI_GREEN);
                break;
            case "movie":
                art = colorize("   __  __  ___  _     ___ _____ \n", ANSI_CYAN) +
                        colorize("  |  \\/  |/ _ \\| |   |_ _| ____|\n", ANSI_CYAN) +
                        colorize("  | |\\/| | | | | |    | ||  _|  \n", ANSI_CYAN) +
                        colorize("  | |  | | |_| | |___ | || |___ \n", ANSI_CYAN) +
                        colorize("  |_|  |_|\\___/|_____|___|_____|\n", ANSI_CYAN);
                break;
            case "ticket":
                art = colorize("   _____ ___ ____ _  _______ _____ \n", ANSI_YELLOW) +
                        colorize("  |_   _|_ _/ ___| |/ / ____|_   _|\n", ANSI_YELLOW) +
                        colorize("    | |  | | |   | ' /|  _|   | |  \n", ANSI_YELLOW) +
                        colorize("    | |  | | |___| . \\| |___  | |  \n", ANSI_YELLOW) +
                        colorize("    |_| |___\\____|_|\\_\\_____| |_|  \n", ANSI_YELLOW);
                break;
            case "payment":
                art = colorize("   ____   _ __   ____  __ _____ _   _ _____ \n", ANSI_GREEN) +
                        colorize("  |  _ \\ / \\\\ \\ / /  \\/  | ____| \\ | |_   _|\n", ANSI_GREEN) +
                        colorize("  | |_) / _ \\\\ V /| |\\/| |  _| |  \\| | | |  \n", ANSI_GREEN) +
                        colorize("  |  __/ ___ \\| | | |  | | |___| |\\  | | |  \n", ANSI_GREEN) +
                        colorize("  |_| /_/   \\_\\_| |_|  |_|_____|_| \\_| |_|  \n", ANSI_GREEN);
                break;
            case "time":
                art = colorize("   _____ ___ __  __ _____ \n", ANSI_CYAN) +
                        colorize("  |_   _|_ _|  \\/  | ____|\n", ANSI_CYAN) +
                        colorize("    | |  | || |\\/| |  _|  \n", ANSI_CYAN) +
                        colorize("    | |  | || |  | | |___ \n", ANSI_CYAN) +
                        colorize("    |_| |___|_|  |_|_____|\n", ANSI_CYAN);
                break;
        }
        System.out.print(art);
    }

    public static void pageTitle(String title) {
        System.out.println();
        System.out.println(colorize(BORDER_LINE, ANSI_PURPLE));
        System.out.println(colorize(center(" " + title + " "), ANSI_YELLOW));
        System.out.println(colorize(BORDER_LINE, ANSI_PURPLE));
    }

    public static void menuBox(String title, String[] options) {
        System.out.println();
        System.out.println(colorize(BOX_TOP, ANSI_CYAN));
        printBoxLine("");
        printBoxLine(centerText(title, BOX_WIDTH));
        printBoxLine("");
        System.out.println(colorize(BOX_MIDDLE, ANSI_CYAN));
        for (String option : options) {
            printBoxLine(option);
        }
        System.out.println(colorize(BOX_BOTTOM, ANSI_CYAN));
    }

    public static void divider() {
        System.out.println(colorize(BOX_MIDDLE, ANSI_CYAN));
    }

    public static void spacer() {
        System.out.println();
    }

    public static void showLoading(String message) {
        System.out.print(ANSI_CYAN + message + " [" + ANSI_RESET);
        for (int i = 0; i < 20; i++) {
            System.out.print(ANSI_GREEN + "=" + ANSI_RESET);
            try {
                Thread.sleep(100); // 100ms per tick
            } catch (InterruptedException e) {
                // Ignore
            }
        }
        System.out.println(ANSI_CYAN + "] Done!" + ANSI_RESET);
    }

    public static void sectionTitle(String title) {
        System.out.println();
        System.out.println(colorize(" ─── " + title + " ───", ANSI_PURPLE));
    }

    public static void option(int index, String label) {
        System.out.println("[" + index + "] " + label);
    }

    public static void listItem(int index, String label) {
        System.out.println(index + ". " + label);
    }

    public static void prompt(String message) {
        System.out.print(colorize("> " + message, ANSI_CYAN));
    }

    public static void info(String message) {
        System.out.println(colorize("[INFO] " + message, ANSI_CYAN));
    }

    public static void success(String message) {
        System.out.println(colorize("[OK] " + message, ANSI_GREEN));
    }

    public static void warning(String message) {
        System.out.println(colorize("[WARN] " + message, ANSI_YELLOW));
    }

    public static void error(String message) {
        System.out.println(colorize("[ERROR] " + message, ANSI_RED));
    }

    public static void keyValue(String key, String value) {
        System.out.println("- " + key + ": " + value);
    }

    public static void keyValueAligned(String key, String value) {
        System.out.printf("  %-12s : %s%n", key, value);
    }

    public static void tableBox(String title, String[] headers, List<String[]> rows) {
        int[] widths = computeColumnWidths(headers, rows);
        String border = tableBorder(widths);

        System.out.println();
        System.out.println(border);
        System.out.println(tableRow(new String[] { centerText(title, border.length() - 4) },
                new int[] { border.length() - 4 }));
        System.out.println(border);
        System.out.println(tableRow(headers, widths));
        System.out.println(border);
        for (String[] row : rows) {
            System.out.println(tableRow(row, widths));
        }
        System.out.println(border);
    }

    private static void printBoxLine(String content) {
        String safeContent = content == null ? "" : content;
        if (safeContent.length() > BOX_WIDTH) {
            safeContent = safeContent.substring(0, BOX_WIDTH);
        }
        System.out.printf("%s%-" + BOX_WIDTH + "s%s%n", colorize("║", ANSI_CYAN), safeContent, colorize("║", ANSI_CYAN));
    }

    private static String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }

        int leftPad = (width - text.length()) / 2;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < leftPad; i++) {
            builder.append(' ');
        }
        builder.append(text);
        return builder.toString();
    }

    private static int[] computeColumnWidths(String[] headers, List<String[]> rows) {
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }

        for (String[] row : rows) {
            for (int i = 0; i < widths.length && i < row.length; i++) {
                if (row[i] != null && row[i].length() > widths[i]) {
                    widths[i] = row[i].length();
                }
            }
        }
        return widths;
    }

    private static String tableBorder(int[] widths) {
        StringBuilder b = new StringBuilder();
        b.append('+');
        for (int width : widths) {
            for (int i = 0; i < width + 2; i++) {
                b.append('-');
            }
            b.append('+');
        }
        return b.toString();
    }

    private static String tableRow(String[] values, int[] widths) {
        StringBuilder b = new StringBuilder();
        b.append('|');
        for (int i = 0; i < widths.length; i++) {
            String value = i < values.length && values[i] != null ? values[i] : "";
            b.append(' ');
            b.append(padRight(value, widths[i]));
            b.append(' ');
            b.append('|');
        }
        return b.toString();
    }

    private static String padRight(String value, int width) {
        if (value.length() >= width) {
            return value;
        }
        StringBuilder b = new StringBuilder(value);
        while (b.length() < width) {
            b.append(' ');
        }
        return b.toString();
    }

    private static boolean isAnsiEnabled() {
        String term = System.getenv("TERM");
        return term != null && !"dumb".equalsIgnoreCase(term);
    }

    private static String colorize(String text, String color) {
        if (!ANSI_ENABLED) {
            return text;
        }
        return color + text + ANSI_RESET;
    }

    private static String center(String text) {
        int width = BORDER_LINE.length();
        if (text.length() >= width) {
            return text;
        }

        int leftPad = (width - text.length()) / 2;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < leftPad; i++) {
            builder.append(' ');
        }
        builder.append(text);
        return builder.toString();
    }
}