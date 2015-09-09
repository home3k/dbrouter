/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */

package com.haoyayi.dbrouter.core.monitor;

import com.haoyayi.dbrouter.core.datasource.VirtualDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Basic Heartbeat monitor.
 * 1. kick the bad connection.
 * 2. resume the bad connection.
 *
 * @author home3k (sunkai@51haoyayi.com)
 */
public class HeartbeatMonitor extends Thread implements InitializingBean {

    private Logger LOG = LoggerFactory.getLogger(HeartbeatMonitor.class);

    private int interval = 5;

    private String checkAvailableSql = "select 1";

    private VirtualDataSource virtualDataSource;

    private Map<String, DataSource> disconnectDataSources = new HashMap<String, DataSource>();

    public HeartbeatMonitor() {
        this.setDaemon(true);
        this.setName("DbRouter-Monitor-Thread");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.start();
    }

    @Override
    public void start() {
        while (true) {
            resumeDataSource();
            kickDataSource();
            try {
                Thread.sleep(interval * 1000);
            } catch (InterruptedException e) {
                LOG.warn("Check Master InterruptedException", e);
            }
        }

    }

    private void kickDataSource() {
        Set<String> masterKeys = new HashSet<>(virtualDataSource.getMasters().keySet());
        for (String name : masterKeys) {
            if (!isDataSourceAvailable(virtualDataSource.getMasters().get(name))) {
                virtualDataSource.removeMaster(name);
                this.disconnectDataSources.put(name, virtualDataSource.getMasters().get(name));
            }
        }
        Set<String> slaveKeys = new HashSet<>(virtualDataSource.getSlaves().keySet());
        for (String name : slaveKeys) {
            if (!isDataSourceAvailable(virtualDataSource.getSlaves().get(name))) {
                virtualDataSource.removeSlave(name);
                this.disconnectDataSources.put(name, virtualDataSource.getSlaves().get(name));
            }
        }
    }


    private void resumeDataSource() {
        if (disconnectDataSources.isEmpty()) {
            return;
        } else {
            Set<String> disconnectDataSourceKeys = new HashSet<>(disconnectDataSources.keySet());
            for (String name : disconnectDataSourceKeys) {
                if (isDataSourceAvailable(disconnectDataSources.get(name))) {
                    if (virtualDataSource.getMasters().containsKey(name)) {
                        virtualDataSource.addMaster(name, disconnectDataSources.get(name));
                        disconnectDataSources.remove(name);
                    } else if (virtualDataSource.getSlaves().containsKey(name)) {
                        virtualDataSource.addSlave(name, disconnectDataSources.get(name));
                        disconnectDataSources.remove(name);
                    } else {
                        LOG.error("DataSource Key invalid, with {}", name);
                    }

                }
            }
        }
    }

    private boolean isDataSourceAvailable(DataSource dataSource) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            boolean success = stmt.execute(checkAvailableSql);
            stmt.close();
            return success;
        } catch (SQLException e) {
            LOG.error("Check dataSource available exception", e);
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOG.error("Close Connection Exception", e);
                }
            }
        }
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public VirtualDataSource getVirtualDataSource() {
        return virtualDataSource;
    }

    public void setVirtualDataSource(VirtualDataSource virtualDataSource) {
        this.virtualDataSource = virtualDataSource;
    }
}
