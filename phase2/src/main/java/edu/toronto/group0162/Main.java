package edu.toronto.group0162;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;

import com.sun.tools.javah.Gen;
import edu.toronto.group0162.dao.*;
import edu.toronto.group0162.entity.*;
import edu.toronto.group0162.service.ArchiveService;
import edu.toronto.group0162.service.Graph;
import lombok.extern.slf4j.Slf4j;
import edu.toronto.group0162.view.Window;

/**
 * Run the main program and connect postgreSQL databse to backend and frontend
 */

@Slf4j
public class Main {

  public static void main(String[] args) throws ClassNotFoundException, SQLException {
    Class.forName("org.postgresql.Driver"); //读取数据库驱动
    Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "chenliang", "");
    //create GUI
    final Window window = new Window(connection);
    //show GUI
    window.setVisible(true);

  }
}
