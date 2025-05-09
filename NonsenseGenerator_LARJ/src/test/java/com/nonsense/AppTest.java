package com.nonsense;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}



/*@echo off
REM Percorso assoluto alla chiave JSON
set GOOGLE_APPLICATION_CREDENTIALS=%cd%\google-key.json

echo 🔧 Compilazione in corso...
mvn compile

echo 🚀 Avvio del programma...
mvn exec:java -Dexec.mainClass="com.nonsense.LanguageExample" */