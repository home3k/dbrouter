/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */
package com.haoyayi.dbrouter.core.datasource;

import com.haoyayi.dbrouter.core.context.KeyContextHolder;
import com.haoyayi.dbrouter.core.context.RWContextHolder;
import com.haoyayi.dbrouter.core.load.LoadBalancer;
import com.haoyayi.dbrouter.core.load.LoadBalancerFactory;
import com.haoyayi.dbrouter.core.load.LoadType;
import com.haoyayi.dbrouter.core.rule.ScaleRuler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This dataSource deals with multi DataSource.It's
 * responsible for locating the right DataSource through threadLocal context
 * which holding key and operation type(READ/WRITE).<br>
 * <p/>
 * <b>Note: </b> The annotation
 * {@link com.haoyayi.dbrouter.core.anno.DBOperationDesc} should be
 * used for DAO.Otherwise, set the context <b> Manually</b>
 * <p/>
 * E.g.
 * KeyContextHolder.setContextKey(info.getUserid());
 * RWContextHolder.setContextRwType(RWContextHolder.TYPE_WRITE);
 *
 * @author home3k (sunkai@51haoyayi.com)
 */
public class VirtualDataSource extends AbstractRoutingDataSource implements InitializingBean {

    private static Logger LOG = LoggerFactory.getLogger(VirtualDataSource.class);

    private List<String> slaveKeys;

    private List<String> masterKeys;

    private Map<String, DataSource> slaves;

    private Map<String, DataSource> masters;

    private ScaleRuler scaleRuler;

    // Using rr balancer default.
    private LoadBalancer loadBalancer = LoadBalancerFactory.getLoadBalancer(LoadType.BALANCE_ROUND_ROBIN);


    public void afterPropertiesSet() {

        Map<Object, Object> allDataSources = new HashMap<Object, Object>();
        if (masters != null) {
            allDataSources.putAll(masters);
            super.setDefaultTargetDataSource(masters.values().iterator().next());
        }
        if (slaves != null) {
            allDataSources.putAll(slaves);
        }
        super.setTargetDataSources(allDataSources);
        //Have to do it
        super.afterPropertiesSet();
        LOG.info("Inject to Spring Routing Datasource ok.");

        init();
    }

    protected void init() {
    }

    /**
     * Lookup current dataSource key.
     *
     * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String rwType = getDBType();
        String masterNumber = getMasterKey();
        if (null == rwType || null == masterNumber) {
            LOG.error("Determining Error. Context is not set!");
            throw new IllegalArgumentException("Determining Error. Context is not set!");
        }
        if (rwType.equals(RWContextHolder.TYPE_WRITE)) {
            LOG.debug("End determine datasource with " + masterNumber);
            return masterNumber;
        } else {
            String slaveNumber = getSlaveKey();
            LOG.debug("End determine datasource with " + slaveNumber);
            return slaveNumber;
        }
    }

    /**
     * Get the database operation type of current request.
     */
    private String getDBType() {
        return RWContextHolder.getContextRwType();
    }

    /**
     * Get master key.
     */
    private String getMasterKey() {
        if (this.masterKeys.size() >= 2 && scaleRuler != null) {
            return scaleRuler.getDatabaseNo(KeyContextHolder
                    .getContextKey());
        } else {
            return masterKeys.get(0);
        }
    }

    /**
     * Get slave key.
     */
    private String getSlaveKey() {
        return this.slaveKeys.get(this.loadBalancer.getNext(this.slaveKeys.size()));
    }

    public DataSource gainTargetDataSource() {
        return this.determineTargetDataSource();
    }

    public Map<String, DataSource> getSlaves() {
        return slaves;
    }

    public void setSlaves(Map<String, DataSource> slaves) {
        this.slaves = slaves;
        this.slaveKeys = new ArrayList<>(this.slaves.keySet());

    }

    public Map<String, DataSource> getMasters() {
        return masters;
    }

    public void setMasters(Map<String, DataSource> masters) {
        this.masters = masters;
        this.masterKeys = new ArrayList<>(this.masters.keySet());
    }

    public void removeMaster(String master) {
        this.masters.remove(master);
        this.masterKeys.remove(master);
    }

    public void addMaster(String master, DataSource dataSource) {
        this.masters.put(master, dataSource);
        this.masterKeys.add(master);
    }

    public void removeSlave(String slave) {
        this.slaves.remove(slave);
        this.slaveKeys.remove(slave);
    }

    public void addSlave(String slave, DataSource dataSource) {
        this.slaves.put(slave, dataSource);
        this.slaveKeys.add(slave);
    }

    public ScaleRuler getScaleRuler() {
        return scaleRuler;
    }

    public void setScaleRuler(ScaleRuler scaleRuler) {
        this.scaleRuler = scaleRuler;
    }

    public LoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    public void setLoadBalancer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }
}
