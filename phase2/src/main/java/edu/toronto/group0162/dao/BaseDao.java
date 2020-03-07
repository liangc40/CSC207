package edu.toronto.group0162.dao;
import java.sql.Connection;

/**
 * Set Dao classes to connect postgreSQL database
 *
 * @param <PK> type 1
 * @param <T> type 2
 */
public abstract class BaseDao<PK, T> {

    protected final Connection connection;

    /**
     * Connect to database
     * @param connection
     */
    public BaseDao(Connection connection) {
        this.connection = connection;
    }

//    public abstract T get(PK pk);
//
//    public abstract void delete(PK pk);
//
//    public abstract T save(T bean);

    // public abstract T update(T bean);
}
