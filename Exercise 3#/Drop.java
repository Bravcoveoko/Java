package cz.muni.fi.pb162.hw03;

/**
 * Drop class
 * @author Lukas Hajda
 */
public class Drop implements Statement{

    private String tableName;

    /**
     *
     * @param table table name
     */
    public Drop(String table) {
        this.tableName = table;
    }

    /**
     *
     * @return name of table wich will be deleted.
     */

    @Override
    public String getTableName() {
        return tableName;
    }
}
