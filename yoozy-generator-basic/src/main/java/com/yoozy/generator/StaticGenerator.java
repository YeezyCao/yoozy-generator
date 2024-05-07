package com.yoozy.generator;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.net.URL;

public class StaticGenerator {


    public static void copyfile(File input, File output) {
        FileUtil.copy(input, output, true);
    }

    public static void main(String[] args) {
        // 获得资源文件夹中的文件
        URL resource = StaticGenerator.class.getClass().getResource("");
    }
}
