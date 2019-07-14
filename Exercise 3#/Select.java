package cz.muni.fi.pb162.hw03;
import java.util.List;

/**
 * Select class.
 * @author Lukas Hajda
 */

public class Select implements Statement {

    private List<String> columms;
    private String tableName;

    /**
     * @param columns columns to be printed.
     * @param table from which table.
     */

    public Select(List<String> columns, String table) {
        this.columms = columns;
        this.tableName = table;
    }

    /**
     *
     * @return trimed columns.
     */

    public List<String> getColumns() {
        return Trim.removeWhiteSpaces(columms);
    }

    /**
     *
     * @return table name.
     */

    @Override
    public String getTableName() {
        return tableName;
    }
}

