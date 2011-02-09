package test.com.tonyxzt;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tonyxzt.language.core.*;
import org.tonyxzt.language.io.InMemoryOutStream;
import org.tonyxzt.language.io.InputStream;
import org.tonyxzt.language.util.FakeBrowserActivator;

import static org.mockito.Mockito.*;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Tonino
 * Date: 09/02/11
 * Time: 17.51
 * To change this template use File | Settings | File Templates.
 */
public class TranslatorTestWithMock {
    Map<String,GenericDictionary> mapMockedDictionaries;
    private Translator translator;
    FakeBrowserActivator browserActivator;
    InMemoryOutStream outStream = new InMemoryOutStream();

    @Before
    public void SetUp() throws Exception {

        browserActivator = new FakeBrowserActivator();

        ContentProvider gDicContentProviderMock =  mock(ContentProvider.class);
        when(gDicContentProviderMock.supportedLanguges()).thenReturn("italian");
        when(gDicContentProviderMock.getInfoUrl()).thenReturn("http://www.google.com/dictionary");
        when(gDicContentProviderMock.retrieve(anyString(),anyString(),anyString())).thenReturn(StubbedGHtmlContent.content);

        ContentProvider gApiContentProviderMock =  mock(ContentProvider.class);
        when(gApiContentProviderMock.supportedLanguges()).thenReturn("italian");
        when(gApiContentProviderMock.getInfoUrl()).thenReturn(null);
        when(gApiContentProviderMock.retrieve(anyString(),anyString(),anyString())).thenReturn("hi");

        mapMockedDictionaries = new HashMap<String,GenericDictionary>();

        mapMockedDictionaries.put("gDic",new GenericDictionary("gDic",gDicContentProviderMock,new GDicContentFilter()));
        mapMockedDictionaries.put("gApi",new GenericDictionary("gApi",gApiContentProviderMock,new ContentFilterIdentity()));


        translator = new Translator(mapMockedDictionaries,browserActivator,outStream);
    }






    @Test
    public void canGetThePlainLanguageName() throws Exception {
        translator.setCommand(new String[]{"--dic=gApi", "--languages"});
        translator.doAction();
        Assert.assertTrue("extend languages description is not contained", outStream.getContent().toLowerCase().contains("italian"));
    }

    @Test
    public void canGetLanguagesFromSpecifigDictionary() throws Exception {
        translator.setCommand(new String[]{"--dic=gApi", "--languages"});
        translator.doAction();
        Assert.assertTrue("extend languages description is not contained",outStream.getContent().toLowerCase().contains("italian"));
    }


    @Test
    public void forUnsupportedLanguageShouldGetAWarningMessage() throws Exception {
        translator.setCommand(new String[]{"--dic=gUnsupported"});
        translator.doAction();
        Assert.assertTrue(outStream.getContent().contains("unresolved dictionary"));
    }



    @Test
    public void canReadFromInputFileMultipleLines() throws Exception {

        translator.setCommand(new String[]{"--dic=gDic", "--oriLang=it", "--targetLang=en", "--inFile=infile"});
        translator.doAction();

        Assert.assertTrue(outStream.getContent().contains("salut!"));
        Assert.assertTrue(outStream.getContent().contains("\n"));
    }


    @Test
    public void canGetTheUrlService() {
        translator.setCommand(new String[] {"--dic=gDic", "--info"});
        translator.doAction();
        Assert.assertEquals("http://www.google.com/dictionary", browserActivator.getOutUrl());
    }


    @Test
    public void canReadFromInputFile() throws Exception {
        InputStream inputStream = new InputStream() {
            int count = 0;
            public String next() {
                if (count++<3) return "hi";
                else return null;
            }
        };

        translator.setCommand(new String[]{"--dic=gDic", "--oriLang=it", "--targetLang=en", "--inFile=infile"});
        translator.doAction();
        System.out.println(outStream.getContent());
        Assert.assertTrue(outStream.getContent().contains("salut"));
    }

    @Test
    public void helpCommandShouldReturnAvailablesDictionaries() throws Exception {
        mapMockedDictionaries.put("myDic",new GenericDictionary("myDic",  (new ContentProvider(){
            public String retrieve(String word, String langIn, String langOut) throws Exception {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public String supportedLanguges() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public String getInfoUrl() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }),new ContentFilter(){
            public String filter(String content) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }));
        translator.setCommand(new String[]{"--help"});
        translator.doAction();

        Assert.assertTrue(outStream.getContent().contains("gApi"));
        Assert.assertTrue(outStream.getContent().contains("gDic"));
        Assert.assertTrue(outStream.getContent().contains("myDic"));
    }





}
