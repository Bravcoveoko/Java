package cz.muni.fi.pb162.hw03;
import java.util.List;

/**
 * Class for Create statement.
 * @author Lukas Hajda
 */

public class Create implements Statement{

    private String tableName;
    private List<String> columns;

    /**
     *
     * @param table Table name.
     * @param columns How many attributes will table has.
     */

    public Create(String table, List<String> columns) {
        this.columns = columns;
        this.tableName = table;
    }

    /**
     * Trim class is called.
     * @return columns in much more prettier form.
     */

    public List<String> getColumns() {
        return Trim.removeWhiteSpaces(columns);
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
