package com.lanyine.mf.mybatis.kit.automation

import org.apache.commons.io.FileUtils

/**
 * 重写文件的公共父类，包含一些公共方法
 * Created by shadow on 2016/8/24.
 */

abstract class MybatisFormatter {

    def formatDir(String mapperDir, String outputDir, String tag, String suffix) {
        File dir = new File(mapperDir)
        def output = new File(outputDir)

        if (dir.isFile() || output.isFile())
            return println("请传入${tag}的输入目录和输出目录")

        if (!dir.exists())
            return println("${tag}[${dir}] 输入文件夹不存在")

        if (!output.exists()) {
            println("${tag}${output}]输出文件夹不存在")
            output.mkdirs()
            println("${tag}输出文件夹创建成功：${output}")
        }

        dir.eachFileMatch(~/.*.${suffix}/) { file ->
            saveFile(output, file)
        }
    }

    def saveFile(File output, File file) {
        def text = formateFile(file)
        def newFile = new File(output, file.getName())
        FileUtils.writeLines(newFile, "UTF-8", text)
        println("${newFile}文件创建成功...")
    }

    def addText(List list, String... text) {
        text.each { t -> list.add(t) }
        list
    }

    abstract def List<String> formateFile(File file)

    abstract def formatDir(String mapperDir, String outputDir)
}
