package com.lanyine.mf.mybatis

import com.lanyine.mf.mybatis.kit.automation.EntityFormatter
import com.lanyine.mf.mybatis.kit.automation.InterfaceFormatter
import com.lanyine.mf.mybatis.kit.automation.MapperFormatter
import com.lanyine.mf.mybatis.kit.generator.ConfigGenerator
import com.lanyine.mf.mybatis.kit.generator.MybatisGenerator
import com.lanyine.mf.mybatis.util.PropertiesUtil

/**
 * @Description:
 * @author ： shadow
 */
class App {
    def static properties = PropertiesUtil.getProperties("config.properties")

    def static configXml() {
        new ConfigGenerator(properties).generateConfigXml()
    }

    def static mybatisFile(def name) {
        def DB_URL = properties.getProperty("url")

        if (name == null) {
            //get db name
            def s = DB_URL.lastIndexOf('/') + 1
            def e = DB_URL.lastIndexOf('?')
            e = (e == -1) ? DB_URL.length() : e;
            name = DB_URL.substring(s, e)
        }
        def configFile = new File(ConfigGenerator.desktopPath, name + "-Config.xml").getPath()
        String[] args = ["-configfile", configFile, "-overwrite"];
        MybatisGenerator.generator(args)
    }


    def static void main(String[] args) {
        println("start ... ")
        //1.生成配置
//         configXml()

        //2.生成mybatis文件
        //生成配置文件后，建议按照模块分别重新命名【包名】，并将之前生成的配置文件拆分成多个部分，针对每个部分分别调用下面的方法
        //生成Mybatis文件，如果未进行拆分，则直接使用空的数组
//        mybatisFile(null)

//        //3.重写mybatis文件
//        //一般接口和XML文件路径是一样的
        def xmlInDir = "C:\\Users\\shadow\\Desktop\\mybatis-data\\dao"
        def xmlOutDir = "C:\\Users\\shadow\\Desktop\\mybatis-data\\out\\dao"
//        new MapperFormatter().formatDir(xmlInDir, xmlOutDir)
//        new InterfaceFormatter().formatDir(xmlInDir, xmlOutDir)
//        //3.2持久化对象,一般不需要重写
//        def entityInDir = "C:\\Users\\shadow\\Desktop\\mybatis-data\\entity"
//        def entityOutDir = "C:\\Users\\shadow\\Desktop\\mybatis-data\\out\\entity"
//        new EntityFormatter().formatDir(entityInDir, entityOutDir)
    }


}