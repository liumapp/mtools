package com.liumapp.qtools.loader;

import com.liumapp.qtools.async.AsyncTool;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @file ToolsLoaderTest.java
 * @author liumapp
 * @email liumapp.com@gmail.com
 * @homepage http://www.liumapp.com
 * @date 2021/3/2 13:44
 */
public class ToolsLoaderTest extends TestCase {
    
    
    @Test
    public void testLoading () {
        ToolsLoader<AsyncTool> toolsLoader = ToolsLoader.getToolsLoader(AsyncTool.class);
//        AsyncTool asyncTool = toolsLoader.getToolClass("default");
    }

}