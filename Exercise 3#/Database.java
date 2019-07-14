package cz.muni.fi.pb162.hw03;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Database.
 * Logic check is here.
 * @author Lukas Hajda
 */

public class Database {

    private File directory;
    private List<Table> allTables;

    /**
     * Make tables of all txt files which are in directory.
     * @param directory given directory from args.
     * @throws IOException
     */

    public Database(File directory){
        this.directory = directory;
        List<Table> list = new ArrayList<>();

        for (File f : Objects.requireNonNull(directory.listFiles())) {
            if (!f.getName().startsWith(".")) {
                try {
                    list.add(new Table(f));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        this.allTables = list;
    }

    /**
     * Every statment will be executed.
     * @param statement Create/Insert/Drop/Select.
     * @throws SQLException SQLException
     * @throws IOException IOException
     */

    public void run(Statement statement) throws SQLException, IOException {

        if(statement instanceof Insert){
            insert((Insert) statement);
        }else if(statement instanceof Create) {
            create((Create) statement);
        }else if(statement instanceof Select) {
            select((Select) statement);
        }else if(statement instanceof Drop) {
            drop((Drop) statement);
        }
    }

    /**
     * Create table with given name and number of columns.
     * Also txt file will be created but in Table class.
     * Of course check there is no same table/file.
     * @param create Create class
     * @throws SQLException
     * @throws IOException
     */

    private void create(Create create) throws SQLException, IOException {
        for (Table table : allTables) {
            if (table.getName().equals(create.getTableName())) {
                throw new SQLException("Table " + create.getTableName() + " already exists");
            }
        }

        Table table = new Table(directory, create.getTableName(), create.getColumns());
        allTables.add(table);
    }

    /**
     * Insert into table
     * @param insert Insert class
     * @throws SQLException
     * @throws IOException
     */

    private void insert(Insert insert) throws SQLException, IOException {
        for (Table table : allTables) {
            if (table.getName().equals(insert.getTableName())) {
                table.insertValues(insert.getColumns(), insert.getValues());
                return;
            }
        }
        throw new SQLException("Table " + insert.getTableName() + " is missing");

    }

    /**
     * Print selected columns.
     * @param select Select class.
     * @throws SQLException
     */

    private void select(Select select) throws SQLException{
        for (Table table : allTables) {
            if (table.getName().equals(select.getTableName())) {
                selectPrint(table, select.getColumns());
                return;
            }
        }
        throw new SQLException("Table " + select.getTableName() + " is missing");

    }

    /**
     * Called from {@link #select(Select)}
     * @param table from this table columns will be printed.
     * @param columnsToPrint columns to print.
     */

    private void selectPrint(Table table, List<String> columnsToPrint) {

        // Every column has specific number
        // LIst indexes contains numbers of column to be print
        //Exp:
        // table
        // header: Name | Age | Adress | City
        // indexes: 0   |  1  |   2    |  3
        // Select Age, City
        // Indexes = {1,3}

        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < table.columnsCount(); i++) {
            String headerColumn = table.getHeader().get(i);
            for (String column : columnsToPrint) {
                if (headerColumn.equals(column)) {
                    indexes.add(i);
                }
            }
        }

        // Printing.
        for (List<String> row : table.getBody()) {
            StringBuilder toPrint = new StringBuilder();
            for (int i = 0; i < indexes.size(); i++) {
                if (row.get(indexes.get(i)) == null) {
                    if (i == indexes.size()-1) {
                        continue;
                    }
                    toPrint.append("" + ";");
                    continue;
                }
                if (i == indexes.size()-1) {
                    toPrint.append(row.get(indexes.get(i)));
                    continue;
                }
                toPrint.append(row.get(indexes.get(i))).append(";");
            }
            // Every values in table is this form "something"
            // So every "" has to deleted.
            System.out.println(toPrint.toString().replaceAll("\"", ""));
        }

    }

    /**
     * Delete given table / file.
     * @param drop Drop class.
     * @throws SQLException
     */

    private void drop(Drop drop) throws SQLException{
        // Iterator is used because in for loop we cant delete item from lis if we
        // iterate through this list

        for (Iterator<Table> it = allTables.iterator(); it.hasNext();) {
            Table table = it.next();
            if (table.getName().equals(drop.getTableName())) {
                it.remove();
                table.remove();
                return;
            }
        }
        throw new SQLException("Table " + drop.getTableName() + " is missing");
    }
}


