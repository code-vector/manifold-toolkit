package com.lanyine.mf.mybatis.kit.automation

import org.apache.commons.io.FileUtils

/**
 * 重写接口文件
 * Created by shadow on 2016/8/23.
 */
class InterfaceFormatter extends MybatisFormatter {
    def imports = "import com.lanyine.manifold.mybatis.base.BaseMapper;"

    @Override
    def formatDir(String mapperDir, String outputDir) {
        formatDir(mapperDir, outputDir, "接口文件", "Mapper.java")
    }

    @Override
    List<String> formateFile(File file) {
        def texts = FileUtils.readLines(file, "UTF-8")
        def list = new ArrayList<>()

        def classDefine = "", entity = "", PK = ""
        texts.each { text ->
            text = text.trim()
            if (text.startsWith("package ")) {
                addText(list, text, "", imports)
            } else if (text.startsWith("import ")) {
                list.add(text)
            } else if (text.startsWith("public ")) {
                classDefine = text
            } else if (text.contains(" deleteByPrimaryKey")) {
                def s = text.lastIndexOf('(') + 1
                def e = text.lastIndexOf(' ')
                PK = text[s..<e]
            } else if (text.contains(" insert")) {
                def s = text.lastIndexOf('(') + 1
                def e = text.lastIndexOf(' ')
                entity = text[s..<e]
            }
        }
        def parents = "extends BaseMapper<${entity}, ${PK}> {"
        classDefine = classDefine.replace("{", parents)
        addText(list, "", classDefine, "", "}")
        list
    }

}