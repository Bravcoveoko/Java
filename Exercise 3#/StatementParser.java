package cz.muni.fi.pb162.hw03;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser.
 * @author Lukas Hajda
 */

public class StatementParser {

    // Regular expression
    // Create regex - Create Table <tableName> (column1, column2, ...)
    private static final String CREATE_REGEX = "^\\s*[Cc][Rr][Ee][Aa][Tt][Ee]\\s+" +
            "[Tt][Aa][Bb][Ll][Ee]\\s+([\\p{L}0-9]+)\\s+" +
            "\\((\\s*[\\p{L}0-9]+\\s*(?:\\s*,\\s*[\\p{L}0-9]+)*\\s*)\\)\\s*;\\s*$";
    // Drop regex - Drop Table <tableName>
    private static final String DROP_REGEX = "^\\s*[Dd][Rr][Oo][Pp]\\s+[Tt][Aa][Bb][Ll][Ee]\\s+" +
            "([\\p{L}0-9]+)\\s*;\\s*$";
    // Select regex - Select (column1, column2,..) From <tableName>
    private static final String SELECT_REGEX = "^\\s*[Ss][Ee][Ll][Ee][Cc][Tt]\\s+(\\s*[\\p{L}0-9]+\\s*" +
            "(?:\\s*,\\s*[\\p{L}0-9]+)*\\s*)\\s+[Ff][Rr][Oo][Mm]\\s+([\\p{L}0-9]+)\\s*;\\s*$";
    // Insert regex - Insert Into <tableName> Values ("value1", "value2",...)
    private static final String INSERT_REGEX = "^\\s*[Ii][Nn][Ss][Ee][Rr][Tt]\\s+[Ii][Nn][Tt][Oo]\\s+" +
            "([\\p{L}0-9]+)\\s+\\((\\s*[\\p{L}0-9]+\\s*(?:\\s*,\\s*[\\p{L}0-9]+)*\\s*)\\)\\s+" +
            "[Vv][Aa][Ll][Uu][Ee][Ss]\\s+\\((\\s*\"[^\";]+\"\\s*(?:\\s*,\\s*\"[^\";]+\")*\\s*)\\)\\s*;\\s*$";


    /**
     *
     * @param inp file full of statments to be printed
     * @param chSet charset - use UTF-8
     * @return List of statements which are SYNTAX valid.
     * @throws IOException IOException
     * @throws SynException SynException
     */

    public static List<Statement> parse(File inp, Charset chSet) throws IOException, SynException {
        List<String> parsedLines = getParsedLines(inp, chSet);
        List<Statement> result = new ArrayList<>();

        if (!checkSemicolon(parsedLines)) throw new SynException();

            parsedLines = makeItPrettier(parsedLines);
            for (String line : parsedLines) {
                List<String> groups = getGroups(INSERT_REGEX, line);
                if (groups != null) {
                    result.add(new Insert(groups.get(0), groupsFst(groups), groupsSnd(groups)));
                    continue;
                }
                groups = getGroups(SELECT_REGEX, line);
                if (groups != null) {
                    result.add(new Select(Arrays.asList(groups.get(0).split(",")), groups.get(1)));
                    continue;
                }
                groups = getGroups(DROP_REGEX, line);
                if (groups != null) {
                    result.add(new Drop(groups.get(0)));
                    continue;
                }
                groups = getGroups(CREATE_REGEX, line);
                if (groups != null) {
                    result.add(new Create(groups.get(0), Arrays.asList(groups.get(1).split(","))));
                    continue;
                }
                throw new SynException();
            }
        return Collections.unmodifiableList(result);
    }

    private static List<String> groupsFst(List<String> gr) {
        return Arrays.asList(gr.get(1).split(","));
    }

    private static List<String> groupsSnd(List<String> gr) {
        return Arrays.asList(gr.get(2).split(","));
    }


    /**
     *
     * @param input file to be parsed
     * @param charset utf-8
     * @return list of statements spliced by semicolon
     * @throws IOException
     */

    private static List<String> getParsedLines(File input, Charset charset) throws IOException {
        List<String> parsedString = new ArrayList<>();
        List<String> result = new ArrayList<>();

        String concatLine = "";

        // Split every line by semicolon by keep semicolon.
        for (String line :  Files.readAllLines(input.toPath(), charset)) {
            parsedString.addAll(Arrays.asList(line.split("(?<=;)")));
        }

        // EXP:
        // DroP \n\n\n\n\n\n\n\n tabLE <tableName>; is also valid
        // So method concat every line until line contains ;
        for(String line : parsedString) {
            if(!line.contains(";")) {
                concatLine = concatLine.concat(line);
                continue;
            }
            concatLine = concatLine.concat(line);
            result.add(concatLine);
            concatLine = "";
        }

        result.add(concatLine);

        List<String> toRemove = new ArrayList<>();

        // remove lines with only whitespaces.
        for (String line : result) {
            if (line.matches("^\\s*$")) toRemove.add(line);
        }

        result.removeAll(toRemove);


        return Collections.unmodifiableList(result);
    }

    /**
     * Make every line much more prettier.
     * Replace long sequence of whitespaces with only one whitespace.
     * @param allLines list
     * @return pretty list
     */

    private static List<String> makeItPrettier(List<String> allLines) {
        List<String> result = new ArrayList<>();

        for (String line : allLines) {
            if (line.equals("^\\s*$")) continue;
            result.add(line.replaceAll("\\s+", " "));
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Check if every statement has semicolon at the end of line.
     * @param allLines parsed list
     * @return true if every line has semiclon, false otherwise
     */

    private static boolean checkSemicolon(List<String> allLines) {
        for (String line : allLines) {
            line = line.trim();
            if (line.length() == 0) continue;
            if (line.charAt(line.length() - 1) != ';') {
                return false;
            }
        }
        return true;
    }

    /**
     * On every parsed line is applied regex.
     * If all fail the SynException is thrown
     * @param regex regular expression to be used
     * @param line line from parsed lines
     * @return list of arguments that need specific statment
     * Create needs - table name, columns
     * Drop needs - table name
     * Insert needs - table name, columns, values
     * Select needs - table name, columns
     */

    private static List<String> getGroups(String regex, String line) {

        Pattern checkRegex = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = checkRegex.matcher(line);

        if (!matcher.find()){
            return null;
        }

        List<String> arguments = new ArrayList<>();

        for (int i = 1; i <= matcher.groupCount(); i++) {
            arguments.add(matcher.group(i));
        }

        return Collections.unmodifiableList(arguments);

    }
    /******************************************************************/
}

