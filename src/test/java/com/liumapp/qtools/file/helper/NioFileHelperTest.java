package com.liumapp.qtools.file.helper;

import com.liumapp.qtools.Qtools;
import com.liumapp.qtools.QtoolsFactory;
import com.liumapp.qtools.file.core.enums.IOEnum;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * file NioFileHelperTest.java
 * author liumapp
 * github https://github.com/liumapp
 * email liumapp.com@gmail.com
 * homepage http://www.liumapp.com
 * date 2020/12/11
 */
public class NioFileHelperTest {

    @Test
    public void readyBytesByFilePath() {
        Qtools qtools = QtoolsFactory.getFactoryInstance().getInstance();
        byte[] b = qtools.newFileHelperBuilder()
                .setAutoCreateFolder(true)
                .setIoType(IOEnum.NIO)
                .setSupportTransferTo(true)
                .build()
                .readyBytesByFilePath(this.getClass().getResource("/base64Content.txt").getPath());
        System.out.println(new String(b,0, b.length));
        Assert.assertEquals(5002, b.length);
    }

    /**
     * 1. 根据base64文本创建文件;
     * 2. 通过零拷贝存储文件在系统磁盘;
     * 3. 如果磁盘目录不存在，则创建;
     * 4. 如果磁盘目录地址已有同名文件，则覆盖;
     */
    public void testBase64() throws IOException {

    }
}