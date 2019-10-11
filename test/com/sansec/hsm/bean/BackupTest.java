/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sansec.hsm.bean;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yuan
 */
public class BackupTest {
    
    public BackupTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getInstnce method, of class Backup.
     */
    @Test
    public void testGetInstnce() {
        System.out.println("getInstnce");
        Backup expResult = null;
        Backup result = Backup.getInstnce();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of exportPart method, of class Backup.
     */
    @Test
    public void testExportPart() throws Exception {
        System.out.println("exportPart");
        int part = 0;
        String pin = "";
        Backup instance = null;
        instance.exportPart(part, pin);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkPrivilege method, of class Backup.
     */
    @Test
    public void testCheckPrivilege() throws Exception {
        System.out.println("checkPrivilege");
        Backup.checkPrivilege();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateFile method, of class Backup.
     */
    @Test
    public void testGenerateFile() throws Exception {
        System.out.println("generateFile");
        Backup instance = null;
        instance.generateFile();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteFile method, of class Backup.
     */
    @Test
    public void testDeleteFile() {
        System.out.println("deleteFile");
        Backup instance = null;
        instance.deleteFile();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class Backup.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        Backup.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
