package test.com.tonyxzt;

import com.google.api.translate.Language;
import com.tonyxzt.language.Translator;
import com.tonyxzt.language.TranslatorMock;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tonyx
 * Date: 20/12/10
 * Time: 22.33
 * To change this template use File | Settings | File Templates.
 */
public class TraslatorTest {
    @Test
    public void withPlainApi() throws Exception {
        Translator translator = new Translator();
        Assert.assertEquals("salut",translator.translate("hi",Language.ENGLISH,Language.FRENCH));
    }
    @Test
    @Ignore
    // in some installation of idea the test launches the -Dfile.encoding=windows-1252 parameter that
    // give false error on the test: the correct par is -Dfile.encoding=UTF-8
    public void chinese() throws Exception {
        Translator translator = new Translator();
        Assert.assertEquals("你好",translator.translate("hi",Language.ENGLISH,Language.CHINESE));
    }
    @Test
    public void withDictionaryScrapApiEnglishFrench() throws Exception {
        Translator translator = new Translator(Translator.TranslationMode.USES_DICTIONARY_BY_SCRAPING);
        Assert.assertTrue(translator.translate("hi", Language.ENGLISH, Language.FRENCH).contains("bonjour"));
    }
    @Test
    public void withDictionaryScrapApiEnglishItalian() throws Exception {
        Translator translator = new Translator(Translator.TranslationMode.USES_DICTIONARY_BY_SCRAPING);
        Assert.assertTrue(translator.translate("hi",Language.ENGLISH,Language.ITALIAN).contains("ciao"));
        Assert.assertTrue(translator.translate("hi", Language.ENGLISH, Language.ITALIAN).contains("salve"));
    }
    @Test
    public void removeTheHtmlTags() throws Exception {
        Translator translator = new Translator(Translator.TranslationMode.USES_DICTIONARY_BY_SCRAPING);
        Assert.assertFalse(translator.translate("hello", Language.ENGLISH, Language.FRENCH).contains("<"));
    }

    @Test
    public void canGetHelpMessage() throws Exception {
        String params[] = {"executable","--help"};
        String returned = Translator.parseCommandLine(params);
        Assert.assertTrue(returned.contains("help"));
    }


    @Test
    public void languageList() throws Exception {
        Assert.assertNotNull(Language.validLanguages);
        Assert.assertTrue("should countain en",Language.validLanguages.contains("en"));
        Assert.assertFalse("should not countain xx", Language.validLanguages.contains("xx"));
    }

    @Test
    public void ValidLanguageCRFormat() throws Exception {
        Translator translator = new Translator();
        Assert.assertTrue(translator.validLanguages().contains("\n"));
    }
    //   return "usage: gtranslate [--gApi|--gDic] [--oriLang=orilang] [--targetLang=targetlang] word";

    @Test
    public void canHandleDictionaryParameter() throws Exception {
        TranslatorMock translator = new TranslatorMock();
        translator.wrapCommandLineParameters(new String[]{"--gDic","ciao"});
        Assert.assertEquals(Translator.TranslationMode.USES_DICTIONARY_BY_SCRAPING,translator.getMode());
    }

    @Test
    public void passgPlainTranlator() throws Exception {
        TranslatorMock translator = new TranslatorMock();
        translator.wrapCommandLineParameters(new String[]{"--gApi","ciao"});
        Assert.assertEquals(Translator.TranslationMode.USES_ONLY_API,translator.getMode());
    }

    @Test
    public void canSetLanguage() throws Exception {
        TranslatorMock translator = new TranslatorMock();
        translator.wrapCommandLineParameters(new String[]{"--oriLang=it","ciao"});
        //Assert.assertEquals(Translator.TranslationMode.USES_ONLY_API,translator.getMode());
        Assert.assertEquals("it",translator.getOriLang());
    }

    @Test
    public void orilanItalianTargetEnSayHi() throws Exception {
        TranslatorMock translator = new TranslatorMock();
        translator.wrapCommandLineParameters(new String[]{"--oriLang=it","--targetLang=en","hi"});
        Assert.assertEquals("it",translator.getOriLang());
        Assert.assertEquals("hi",translator.translate("hi"));
    }
}
