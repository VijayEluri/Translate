package com.tonyxzt.language;

import org.apache.commons.httpclient.NameValuePair;

import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: tonyx
 * Date: 21/12/10
 * Time: 1.21
 * To change this template use File | Settings | File Templates.
 */
public class GoogleDictionary implements OnLineDictionary{

    protected FilterAndCleanContent filter = new GoogleDictionaryFilterAndCleanContent();
    protected ExternalSourceManager sourceManager = new ExternalSourceManager();

    public  String lookUp(String ciao, String langIn, String langOut) throws Exception {
        NameValuePair[] params = new NameValuePair[]{new NameValuePair("aq", "f"), new NameValuePair("langpair", langIn+"|"+langOut),
                new NameValuePair("q", ciao), new NameValuePair("hl", "en")};
        String theResult = sourceManager.lookupTranslationByProviderByGet("http://www.google.com/translate_dict", params);
        return filter.filter(theResult);

    }
}
