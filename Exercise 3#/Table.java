package cz.muni.fi.pb162.hw03;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Table class
 * @author Lukas Hajda
 */

public class Table {

    private List<String> header;
    private List<List<String>> body = new ArrayList<>();
    private File tableFile;

    /**
     *
     * @param file file which will be formed into table for better woring
     * @throws IOException IOException
     */
    public Table(File file) throws IOException {
        List<String> allLines = Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
        boolean findHeader = false;
        for (String line : allLines) {
            List<String> items = Arrays.asList(line.split(";"));
            if (!findHeader) {
                header = items;
                findHeader = true;
            }else {
                if (items.size() != header.size()) {
                    items = new ArrayList<>(items);
                    items.add(null);
                    items = Collections.unmodifiableList(items);
                }
                body.add(items);
            }
        }
        this.tableFile = file;
    }

    /**
     *
     * @param directory where files will be put
     * @param tableName name
     * @param header first columns of eveery table - the attributes.
     * @throws IOException IOException
     */

    public Table(File directory, String tableName, List<String> header) throws IOException {

        String path = directory.getPath() + "/" + tableName;

        this.tableFile = new File(path);
        this.header = header;

        tableFile.createNewFile();

        try(PrintWriter printWriter = new PrintWriter(tableFile)){
            StringBuilder result = new StringBuilder();

            for (Iterator<String> it = header.iterator(); it.hasNext();) {
                String column = it.next();
                result.append(column);
                if(it.hasNext()) {
                    result.append(";");
                }

            }

            result.append( "\n");
            printWriter.print(result);
        }
    }

    /**
     *
     * @return Number of columns/
     */

    public int columnsCount() {
        return header.size();
    }

    /**
     * @return header (attributes)
     */

    public List<String> getHeader(){
        return header;
    }

    /**
     *
     * @return Matrix of columns without header
     */

    public List<List<String>> getBody() {
        return body;
    }

    /**
     *
     * @param values add values into table
     */

    public void addValues(List<String> values) {
        body.add(values);
    }

    /**
     *
     * @return name of table.
     */

    public String getName() {
        return tableFile.getName();
    }

    /**
     *
     * @param columns in which columns to be put
     * @param values to be put in given columns
     * @throws IOException IOException
     */

    public void insertValues(List<String> columns, List<String> values) throws IOException {

        if (columns.size() == columnsCount()) {
            addValues(values);
            appendToFile(values);
            return;
        }

        List<String> result = new ArrayList<>();

        for (int i = 0; i < columnsCount(); i++) {
            result.add(null);
        }

        for (int i = 0; i < columnsCount(); i++) {
            String headColumn = header.get(i);
            for (int j = 0; j < columns.size(); j++) {
                String givenColumn = columns.get(j);
                if (headColumn.equals(givenColumn)) {
                    result.set(i, values.get(j));
                }
            }
        }

        addValues(result);
        appendToFile(result);


    }

    /**
     * Remove table / file.
     */

    public void remove() {
        tableFile.delete();
    }

    /**
     *
     * @param line line to be append into file.
     * @throws IOException
     */

    private void appendToFile(List<String> line) throws IOException {
        OutputStreamWriter outPutS = new OutputStreamWriter(new FileOutputStream(tableFile, true), "UTF-8");
        try(Writer writer = new BufferedWriter(outPutS)) {
            List<String> outputLine = line.stream().map(x -> x == null ? "" : x).collect(Collectors.toList());
            writer.append(String.join(";", outputLine));
            ((BufferedWriter) writer).newLine();
        } catch (IOException e) {
            throw e;
        }catch (Exception e) {
            throw new IOException();
        }

    }
}
