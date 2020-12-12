package com.liumapp.qtools.file.helper;

import com.liumapp.qtools.Qtools;
import com.liumapp.qtools.QtoolsFactory;
import com.liumapp.qtools.file.core.FileHelper;
import com.liumapp.qtools.file.core.enums.IOEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * file CommonBase64HelperTest.java
 * author liumapp
 * github https://github.com/liumapp
 * email liumapp.com@gmail.com
 * homepage http://www.liumapp.com
 * date 2020/12/12
 */
public class CommonBase64HelperTest {

    private Qtools qtools;

    private FileHelper fileHelper;

    @Before
    public void init () {
        this.qtools = QtoolsFactory.getFactoryInstance().getInstance();
        this.fileHelper = qtools.newFileHelperBuilder()
                .setIoType(IOEnum.NIO)
                .setSupportTransferTo(true)
                .setAutoCreateFolder(true)
                .build();
    }

    @Test
    public void saveFile() {
        byte[] contents = fileHelper.readyBytesByFilePath(
                this.getClass().getResource("/base64Content.txt").getPath()
        );
        fileHelper.writeBytesToFile(fileHelper.base64().decodeBytes(contents),
                this.getClass().getResource("/").getPath() + "2.mp3");
    }

    @Test
    public void encodeBytes() {
        byte[] content = fileHelper.readyBytesByFilePath(
                this.getClass().getResource("/1.mp3").getPath()
        );
        String base64String = new String(fileHelper.base64().encodeBytes(content));
        System.out.println(base64String);
        fileHelper.writeBytesToFile(fileHelper.base64().decodeString(base64String),
                this.getClass().getResource("/").getPath() + "3.mp3");
    }

    @Test
    public void header() {
        byte[] contents = fileHelper.readyBytesByFilePath(
                this.getClass().getResource("/base64Content.txt").getPath()
        );
        String header = fileHelper.base64().header(new String(contents));
        assertEquals("data:audio/mpeg", header);
    }

    @Test
    public void removeHeader() {
        byte[] contents = fileHelper.readyBytesByFilePath(
                this.getClass().getResource("/base64Content.txt").getPath()
        );
        String contentsWithoutHeader = fileHelper.base64().removeHeader(new String(contents));
        assertEquals(4979, contentsWithoutHeader.length());
    }


}