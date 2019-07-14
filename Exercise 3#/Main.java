package cz.muni.fi.pb162.hw03;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author Lukas Hajda
 */
public class Main {

    /**
     * @param args Arguments
     */
    private static Charset charset = Charset.forName("UTF-8");

    /**
     *
     * @param args given arguments.
     * @throws IOException IOException
     */
    public static void main(String[] args) throws IOException {
        // Check arguments
        if (args.length != 2) {
            System.out.println("usage: java -jar database.jar <path/to/statements_file.txt> <path/to/tables_folder>");
            return;
        }
        // Check if both parameters exist
        String inPath = args[0];
        String databasePath = args[1];

        File input = getFile(inPath);
        File databaseDirectory = getFile(databasePath);

        // If in directory are some txt file(tables) they will be parsed.
        Database database = new Database(databaseDirectory);

        // This List will contain all (only syntax valid, no logic valid) lines.
        // If there si only one syntax invalid line SynException is thrown.
        List<Statement> statements;
        try {
            statements = StatementParser.parse(input, charset);
        } catch (SynException e) {
            return;
        }


        // Now we know that every line is syntqx valid.
        // Program will run every statement. Logic check now comes where can be
        // SQLException thrown.
        for(Statement statement: statements){
            try {
                database.run(statement);
            } catch (SQLException e) {
                return;
            }
        }
    }

    /**
     * Create new file, otherwise throws IOException
     * @param path from args argument
     * @return file if success
     * @throws IOException
     */

    private static File getFile(String path)  throws IOException {
        File file = new File(path);
        if (!file.exists() ) {
            throw new IOException();
        }
        return file;
    }
}
