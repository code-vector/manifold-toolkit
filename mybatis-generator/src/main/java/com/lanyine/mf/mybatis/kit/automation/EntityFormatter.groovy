package com.lanyine.mf.mybatis.kit.automation

import org.apache.commons.io.FileUtils

/**
 * 重写持久化文件
 * Created by shadow on 2016/8/23.
 */
class EntityFormatter extends MybatisFormatter {
    def imports = "import com.lanyine.manifold.mybatis.base.MEntity;"
    def serialVersionUID = "\tprivate static final long serialVersionUID = 1L;"

    @Override
    def formatDir(String mapperDir, String outputDir) {
        formatDir(mapperDir, outputDir, "持久化对象", "java")
    }

    @Override
    List<String> formateFile(File file) {
        def texts = FileUtils.readLines(file, "UTF-8")
        def list = new ArrayList<>()

        def temp = ""
        texts.each { text ->
            temp = text.trim()
            if (temp.startsWith("package ")) {
                addText(list, temp, "", imports)
            } else if (temp.startsWith("public class")) {
                addText(list, temp.replace("{", "implements MEntity {"), serialVersionUID, "")
            } else {
                list.add(text)
            }
        }
        list
    }

}

