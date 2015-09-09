/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */
package com.haoyayi.dbrouter.core.anno;

import java.lang.annotation.*;

/**
 * Annotation for DAO method.
 *
 * db default is <b>SLAVE</b>.The parameter pattern is SLAVE/MASTER.
 *
 * E.g.<br/>
 * <p>
 *
 * 					@DBOperationDesc(db ="MASTER")<br/>
 *                  public void updateDentist(Dentist dentist) {<br/>
 *                         ......<br/>
 *                  }<br/>
 * <br/>
 *
 *                 	******************EQUALS******************<br/>
 * <br/>
 *
 *                	public void updateDentist(Dentist dentist) {<br/>
 *                         RWContextHolder.setContextRwType(RWContextHolder.TYPE_WRITE
 *                         );<br/>
 *                         ......<br/>
 *                         RWContextHolder.clearContextRwType();<br/>
 *                 	}<br/>
 *                         </p>
 *
 *                 Method 2 has better performance.
 *
 * @author home3k (sunkai@51haoyayi.com)
 *
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public abstract @interface DBOperationDesc {

	/**
	 * DB operation type
	 *
	 * @return String
	 */
	public String db() default "SLAVE";

}
