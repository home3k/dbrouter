/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */

package com.haoyayi.dbrouter.core.anno;

import java.lang.annotation.*;

/**
 * Annotation for DAO class/method.
 *
 * key default is "". If using key to scale out, pls config the parameter. E.g. {0} for the <b>FIRST paremeter</b>.
 *
 * E.g.<br/>
 * <p>
 *
 * 					@KeyDesc(key="{0}.uid")<br/>
 *                  public void updateDentist(Dentist dentist) {<br/>
 *                         ......<br/>
 *                  }<br/>
 *                  <br/>
 *
 *                 	******************EQUALS******************<br/>
 *                  <br/>
 *
 *                	public void updateDentist(Dentist dentist) {<br/>
 *                         KeyContextHolder.setContextKey(info.getUid())
 *                         ;<br/>
 *
 *                         ......<br/>
 *                         KeyContextHolder.clearContextKey();<br/>
 *                 	}<br/>
 *                         </p>
 *
 *                  Method 2 has better performance.
 *
 * @author home3k (sunkai@51haoyayi.com)
 *
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public abstract @interface KeyDesc {

    /**
     * Key for scale out.
     *
     * @return String
     */
    public String key() default "";

}
