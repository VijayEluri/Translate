package org.tonyxzt.language.util;

import org.tonyxzt.language.core.GenericDictionary;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Tonino
 * Date: 23/01/11
 * Time: 15.00
 * To change this template use File | Settings | File Templates.
 */
public interface ImplementationInjector {
    public Map<String,GenericDictionary>  getMap() throws Exception;
}
