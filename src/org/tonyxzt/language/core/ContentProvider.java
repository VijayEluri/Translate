package org.tonyxzt.language.core;

import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Tonino
 * Date: 20/01/11
 * Time: 12.22
 *
 * (c) Antonio Lucca 2011
 * License gpl3: http://www.gnu.org/licenses/gpl-3.0.txt
 *
 * To change this template use File | Settings | File Templates.
 */
public interface ContentProvider {
    String retrieve(String word, String langIn, String langOut) throws Exception;
    String supportedLanguges();
    String getInfoUrl();
}
