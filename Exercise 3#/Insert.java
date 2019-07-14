package cz.muni.fi.pb162.hw03;
import java.util.List;

/**
 * Insert class.
 * @author Lukas Hajda.
 */

public class Insert implements Statement {

    private String tableName;
    private List<String> columns;
    private List<String> values;

    /**
     * @param table in what table values will be put.
     * @param columns in which columns.
     * @param values what kind of values will be put.
     */

    public Insert(String table, List<String> columns, List<String> values) {
        this.tableName = table;
        this.columns = columns;
        this.values = values;
    }

    /**
     *
     * @return name of table where values will be put.
     */

    @Override
    public String getTableName() {
        return tableName;
    }

    /**
     *
     * @return trimed columns
     */

    public List<String> getColumns() {
        return Trim.removeWhiteSpaces(columns);
    }

    /**
     *
     * @return trimed values.
     */

    public List<String> getValues() {
        return Trim.removeWhiteSpaces(values);
    }
}
