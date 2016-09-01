package com.lanyine.mf.mybatis.kit.automation

import org.dom4j.Document
import org.dom4j.Element
import org.dom4j.io.SAXReader
import org.dom4j.tree.DefaultElement

import java.util.stream.Collectors

/**
 * 重写 xml 文件
 * Created by shadow on 2016/8/23.
 */
class MapperFormatter extends MybatisFormatter {

    @Override
    def formatDir(String mapperDir, String outputDir) {
        formatDir(mapperDir, outputDir, "XML配置文件", "xml")
    }

    @Override
    List<String> formateFile(File file) {
        //1. head
        def sb = new StringBuilder('<?xml version="1.0" encoding="UTF-8"?>\n')
        sb.append('<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">\n')

        //2.mapper
        SAXReader saxReader = new SAXReader(false);
        saxReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        Document doc = saxReader.read(file)

        Element root = doc.getRootElement()

        def namespace = root.attributeValue("namespace")
        sb.append("""<mapper namespace="${namespace}">\n""")

        //3.table name
        Element select = getElementById("selectByPrimaryKey", root)
        def text = select.textTrim
        def s = text.lastIndexOf('from') + 4
        def e = text.lastIndexOf('where')
        def table = text[s..<e].trim()
        sb.append("""\t<sql id="table">${table}</sql>\n""")

        //4. resultMap
        def resultMaps = getElementById("BaseResultMap", root)
        handlerText(resultMaps, sb)

        //5.result
        def base_Column_List = getElementById("Base_Column_List", root).text.trim().replaceAll('\n\\s+', '\n\t\t')
        sb.append("""\t<sql id="Base_Column_List">\n\t\t${base_Column_List}\n\t</sql>""")

        //6.end
        sb.append('\n\n</mapper>')
        Arrays.asList(sb.toString())
    }

    def getElementById(String attr, Element root) {
        List<DefaultElement> list = root.content().stream().filter({ n -> n instanceof DefaultElement }).collect(Collectors.toList())
        for (Element ele : list) {
            if (ele.attributeValue("id") == attr) {
                return ele
            }
        }
        return null
    }

    def handlerText(Element root, StringBuilder sb) {
        List<DefaultElement> list = root.content().stream().filter({ n -> n instanceof DefaultElement }).collect(Collectors.toList())
        def temp = list[0].asXML()
        sb.append("""\t${temp}\n""")
        for (def i = 1; i < list.size() - 1; i++) {
            temp = list[i].asXML()
            sb.append("""\t\t${temp}\n""")
        }
        temp = list.get(list.size() - 1).asXML()
        sb.append("""\t${temp}\n""")
    }
}
