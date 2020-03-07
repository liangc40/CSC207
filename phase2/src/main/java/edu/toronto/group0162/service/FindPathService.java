package edu.toronto.group0162.service;

import edu.toronto.group0162.dao.EdgeDao;
import edu.toronto.group0162.dao.NodeDao;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class FindPathService {

    //algorithm build 2D ArrayList
    //algorithm get shortest distance and duration

    @Getter
    private final EdgeDao edgeDao;
    private final NodeDao nodeDao;
}
