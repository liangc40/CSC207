package dao;

import java.sql.Connection;

/** A BaseDao that connects the IDE with database. */
public abstract class BaseDao<PK, T> {

  /** A Connection that connects the database. */
  protected final Connection connection;

  /**
   * Construct the BaseDao.
   *
   * @param connection the Connection that connects the database.
   */
  public BaseDao(Connection connection) {
    this.connection = connection;
  }
}
