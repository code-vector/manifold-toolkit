package com.lanyine.mf.mybatis.kit.generator

import com.lanyine.mf.mybatis.util.PropertiesUtil
import org.apache.commons.lang3.text.WordUtils

import javax.swing.filechooser.FileSystemView
import java.sql.DriverManager
import java.sql.ResultSet

/**
 * 第一步：
 * @Description: 生成配置文件
 * @author ： shadow
 */
class ConfigGenerator {
    def static desktopPath = FileSystemView.getFileSystemView().getHomeDirectory().getPath() + "/mybatis-data"
    def config_template = getResourceFile("mysql/config-tpl.xml").text
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    // JDBC driver name and database URL
    def prop, entity_path, mapper_path, DB_URL, DB_USER, DB_PASS, DB_NAME = "";

    ConfigGenerator() {
        ConfigGenerator(PropertiesUtil.getProperties("config.properties"))
    }

    ConfigGenerator(def properties) {
        prop = PropertiesUtil.getProperties("config.properties")
        prop = PropertiesUtil.getProperties("config.properties")
        entity_path = prop.getProperty("entity.package")
        mapper_path = prop.getProperty("mapper.package")
        DB_URL = prop.getProperty("url")
        DB_USER = prop.getProperty("user")
        DB_PASS = prop.getProperty("password")

        //get db name
        def s = DB_URL.lastIndexOf('/') + 1
        def e = DB_URL.lastIndexOf('?')
        e = (e == -1) ? DB_URL.length() : e;
        DB_NAME = DB_URL.substring(s, e)
    }


    def getResourceFile(String name) {
        def url = getClass().classLoader.getResource(name)
        if (url == null) {
            println("File['${name}'] in the resource directory not found.")
            System.exit(-1)
        }
        new File(url.getPath())
    }

    def tableConfig(ResultSet rs) {
        StringBuilder sb = new StringBuilder();
        def tableName;
        def poName;
        while (rs.next()) {
            tableName = rs.getString(1)
            poName = camelCase(tableName) + "Po";
            sb.append(getTableText(tableName, poName));
        }
        sb
    }

    def camelCase(String tableName) {
        // 去掉$前面的部分
        if (tableName.indexOf('$') > 0) {
            tableName = tableName.split('\\\$')[1];
        }
        String[] temps = tableName.split("_");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < temps.length; i++) {
            sb.append(WordUtils.capitalizeFully(temps[i]));
        }
        return sb.toString();
    }


    def getTableText(table, entity) {
        """\t\t<table tableName="${table}" domainObjectName="${
            entity
        }" """ + ' schema="" enableCountByExample="false" enableDeleteByExample="false" enableSelectByExample="false" enableUpdateByExample="false" />\r\n'
    }

    def generateConfigXml() {
        def xml = new File(desktopPath, DB_NAME + "-Config.xml");
        if (xml.exists())
            xml.delete()

        xml.createNewFile();

        // 生成配置文件
        def conn, configXml;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)
            def rs = conn.prepareStatement("show tables;").executeQuery();
            configXml = config_template.replace("[user]", DB_USER).replace("[password]", DB_PASS)
                    .replace("[url]", DB_URL).replace("[desktop]", desktopPath)
                    .replace("[entity.package]", entity_path)
                    .replace("[mapper.package]", mapper_path)
                    .replace('<table tableName=""/>', tableConfig(rs).toString().trim())
            // 写入到文件
            xml.append(configXml.toString(), "UTF-8")
            println("配置文件路径：" + xml.toPath())
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) conn.close()
        }
    }

}
