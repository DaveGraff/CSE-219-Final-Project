/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.pkg219.pkgfinal.project;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author David
 */
public class DataStateTest {
    
    public DataStateTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getIsSaved method, of class DataState.
     */
    @Test
    public void testGetIsSaved() {
        System.out.println("getIsSaved");
        DataState instance = null;
        boolean expResult = false;
        boolean result = instance.getIsSaved();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setIsSaved method, of class DataState.
     */
    @Test
    public void testSetIsSaved() {
        System.out.println("setIsSaved");
        boolean b = false;
        DataState instance = null;
        instance.setIsSaved(b);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getData method, of class DataState.
     */
    @Test
    public void testGetData() {
        System.out.println("getData");
        DataState instance = null;
        String expResult = "";
        String result = instance.getData();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setData method, of class DataState.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        String d = "";
        DataState instance = null;
        instance.setData(d);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIsWrong method, of class DataState.
     */
    @Test
    public void testGetIsWrong() {
        System.out.println("getIsWrong");
        DataState instance = null;
        boolean expResult = false;
        boolean result = instance.getIsWrong();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleSaveRequest method, of class DataState.
     */
    @Test
    public void testHandleSaveRequest() {
        System.out.println("handleSaveRequest");
        String text = "";
        DataState instance = null;
        instance.handleSaveRequest(text);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of saveToFile method, of class DataState.
     */
    @Test
    public void testSaveToFile() throws Exception {
        System.out.println("saveToFile");
        DataState instance = null;
        instance.saveToFile();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isWrong method, of class DataState.
     */
    @Test
    public void testIsWrong() {
        System.out.println("isWrong");
        String text = "";
        DataState instance = null;
        String[] expResult = null;
        String[] result = instance.isWrong(text);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of saveAlert method, of class DataState.
     */
    @Test
    public void testSaveAlert() {
        System.out.println("saveAlert");
        String reason = "";
        DataState instance = null;
        instance.saveAlert(reason);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleLoadRequest method, of class DataState.
     */
    @Test
    public void testHandleLoadRequest() {
        System.out.println("handleLoadRequest");
        DataState instance = null;
        String[] expResult = null;
        String[] result = instance.handleLoadRequest();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkForSave method, of class DataState.
     */
    @Test
    public void testCheckForSave() {
        System.out.println("checkForSave");
        DataState instance = null;
        instance.checkForSave();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleNewRequest method, of class DataState.
     */
    @Test
    public void testHandleNewRequest() {
        System.out.println("handleNewRequest");
        DataState instance = null;
        boolean expResult = false;
        boolean result = instance.handleNewRequest();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
