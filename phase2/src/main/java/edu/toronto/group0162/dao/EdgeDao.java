package edu.toronto.group0162.dao;
import edu.toronto.group0162.dao.BaseDao;
import edu.toronto.group0162.dao.CardDao;
import edu.toronto.group0162.entity.Edge;
import edu.toronto.group0162.entity.Node;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Set EdgeDao classes to connect postgreSQL database edge table
 *
 */
public class EdgeDao extends BaseDao<Integer, Node> {

    /**
     * Connect to database edge table
     *
     * @param connection
     */
    public EdgeDao(Connection connection) {
        super(connection);
    }

    /**
     * Gets all edges from database edge table
     *
     * @return List of edges
     */
    public List<Edge> getEdges() {
        try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM transit_system.edge")) {
            ResultSet result = ps.executeQuery();
            List s = new ArrayList<Node>();
            while (result.next()) {
                Edge edge = new Edge();
                edge.setEid(result.getInt("eid"));
                edge.setStart(result.getInt("start"));
                edge.setStop(result.getInt("stop"));
                edge.setEdge(result.getString("edge"));
                edge.setDistance(result.getDouble("distance"));
                edge.setDuration(result.getInt("duration"));
                s.add(edge);
            }
            return s;

        } catch (SQLException ex) {
            Logger.getLogger(EdgeDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
