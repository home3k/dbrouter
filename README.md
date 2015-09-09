Database Router
===========

A database route component base Spring.

## Basic Function

* Spring Jdbc DAL，CRUD based jdbcTemplate.
* Read/write separation of multiple data sources. Support for annotation of the standby switch, directional data source access.
* Multiple data source access load balancing. Support for custom routing algorithm.
* Customized sub-libraries, sub-table strategy. Application of seamless intervention.
* Basic fault detection, fault recovery mechanisms.
* Non-invasive code.

## Maven

Stored in the maven repository

```xml

		<dependency>
			<groupId>com.haoyayi.dbrouter</groupId>
		    <artifactId>dbrouter-core</artifactId>
		    <version>1.0.0</version>
		</dependency>

```

## Configuration

### 1 DataSource Configuration


```xml
		<bean id="masterDataSource1" class="com.mchange.v2.c3p0.ComboPooledDataSource" init-method="init" destroy-method="close">
            ....
        </bean>
        <bean id="slaveDataSource1" class="com.mchange.v2.c3p0.ComboPooledDataSource" init-method="init" destroy-method="close">
            ....
        </bean>
        <bean id="slaveDataSource2" class="com.mchange.v2.c3p0.ComboPooledDataSource" init-method="init" destroy-method="close">
            ....
        </bean>
```
```xml
        <!-- Custom datasource -->
        <bean id="virtualDataSource" class="com.haoyayi.dbrouter.core.datasource.VirtualDataSource">
            <property name="slaves">
        		<map key-type="java.lang.String">
        			<entry value-ref="slaveDataSource1" key="slaveDataSource1"></entry>
        			<entry value-ref="slaveDataSource2" key="slaveDataSource2"></entry>
        		</map>
        	</property>
            <property name="masters">
        		<map key-type="java.lang.String">
        			<entry value-ref="masterDataSource1" key="masterDataSource1"></entry>
        		</map>
        	</property>
        	<!-- Not provided. Default using rr way -->
        	<property name="loadBalancer" ref="randomLoadBalance"/>
        	<!-- Not provided default. -->
        	<property name="scaleRuler" ref="scaleRuler"/>
        </bean>
```
```xml
        <!-- Custom load balancer. -->
        <bean id="randomLoadBalance" class="com.haoyayi.dbrouter.core.load.impl.RandomLoadBalance" />
        <!-- Custom scale out mechanism。 -->
        <bean id="scaleRuler" class="com.haoyayi.dbrouter.core.rule.ScaleRuler">
            <property name="codeGenerator" ref="...."/>
        </bean>
```



### 2 Spring Jdbc Configuration

```xml
		<!-- config jdbcTemplate to DbRouterJdbcTemplate， dataSource to virtualDataSource -->
		<bean id="jdbcTemplate" class="com.haoyayi.dbrouter.core.jdbc.DbRouterJdbcTemplate">
            <property name="dataSource" ref="virtualDataSource"/>
        </bean>
```

### 3 Monitor Configuration

```xml
		<!-- 配置monitor，指向HeartbeatMonitor， dataSource指向之前配置的virtualDataSource，interval配置监控间隔时间，不设置默认为5s -->
		<bean id="heartbeatMonitor" class="com.haoyayi.dbrouter.core.monitor.HeartbeatMonitor">
                <property name="virtualDataSource" ref="virtualDataSource"/>
                <property name="interval" value="5"/>
        </bean>
```

### 4 如果希望通过Annotation，自定义分库分表参数，及目标分库类型（比如某些特殊查询需要走主库），则需要配置aop advice。


```xml
    <aop:aspectj-autoproxy/>
    <!-- dbOperationDesc Handler. -->
    <bean id="dbOpInterceptor" class="com.haoyayi.dbrouter.core.DBOperationDescHandler"/>
    <!-- 配置advice的路径表达式. -->
    <aop:config>
        <aop:aspect id="dbOpExecutor" ref="dbOpInterceptor" order="1">
            <aop:pointcut id="dbOpOperation"
                          expression="execution(* com.haoyayi.thor.dal.impl.*.*(..))"/>
            <aop:around pointcut-ref="dbOpOperation" method="adviceDbOperation"/>
        </aop:aspect>
    </aop:config>
```

```xml
    <!-- keyDesc Handler. -->
    <bean id="keyInterceptor" class="com.haoyayi.dbrouter.core.KeyDescHandler"/>
    <!-- 配置advice的路径表达式. -->
    <aop:config>
        <aop:aspect id="keyExecutor" ref="keyInterceptor" order="1">
            <aop:pointcut id="keyOperation"
                          expression="execution(* com.haoyayi.thor.dal.impl.*.*(..))"/>
            <aop:around pointcut-ref="keyOperation" method="adviceKey"/>
        </aop:aspect>
    </aop:config>
```

## Get Started

```java
        /**
         * 确保全集的jdbcTemplate为dbRouter提供的jdbcTemplate，如果有多个，可以用@Qualifier来制定。
         */
        @Autowired
        @Qualifier("jdbcTemplate")
        private JdbcTemplate jdbcTemplate;
```

```java
        /**
         * 需要分表的时候
         */
         @Autowired
         private ScaleRuler scaleRuler;
         String table =  scaleRuler.getTableName(currentTableName, key, seperator);
         /**
          * 如果通过Annotation设置了key
          */
         String table =  scaleRuler.getTableName(currentTableName, seperator);
         // seperator为可选参数，如果无分隔符可以不设置。
 ```
 ```java
         /**
          * 通过Annotation设置主从库
          */
         @DBOperationDesc(db ="MASTER")
         public void updateDentist(Dentist dentist) {
         }
         /**
          * 通过Annotation设置scale key
          */
         @KeyDesc(key="{0}.uid")<br/>
         public void updateDentist(Dentist dentist) {
         }
```

## Copyright

Copyright 2015-2020 51haoyayi.com


