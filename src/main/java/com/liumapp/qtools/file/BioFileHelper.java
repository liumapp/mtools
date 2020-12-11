package com.liumapp.qtools.file;

import com.liumapp.qtools.file.core.AbstractFileHelper;
import com.liumapp.qtools.file.core.FileHelper;
import com.liumapp.qtools.file.core.annotations.IOType;
import com.liumapp.qtools.file.core.enums.IOEnum;

import java.io.IOException;
import java.io.Serializable;

/**
 * file BioFileHelper.java
 * author liumapp
 * github https://github.com/liumapp
 * email liumapp.com@gmail.com
 * homepage http://www.liumapp.com
 * date 2020/12/11
 */
@IOType(IOEnum.BIO)
public class BioFileHelper extends AbstractFileHelper implements FileHelper, Serializable {

    private static final long serialVersionUID = -7187701923903043140L;

    protected BioFileHelper(FileHelperParam param) {
        this.fileHelperParam = param;
    }

    @Override
    public byte[] readyBytesByFilePath(String filePath) throws IOException {
        return new byte[0];
    }
}