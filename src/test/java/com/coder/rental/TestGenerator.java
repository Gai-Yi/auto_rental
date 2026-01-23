package com.coder.rental;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;
import java.util.Collections;

public class TestGenerator {
    private final static String AUTHOR = "gaiyi";

    private final static String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/auto_rental";

    private final static String JDBC_USERNAME = "root";

    private final static String JDBC_PASSWORD = "root";

    private final static String OUT_DIR = ".\\src\\main\\java";

    private final static String PACKAGE_NAME = "com.coder";

    private final static String MODULE_NAME = "rental";

    private static final String[] TABLES={
            "auto_maker","auto_brand","auto_info",
            "sys_dept","sys_permission","sys_role","sys_user","sys_role_permission","sys_user_role",
            "busi_customer","busi_maintain","busi_violation","busi_order","busi_rental_type"
    };

    private static final String[] PREFIX={"busi_","sys_"};

    @Test
    void generator() {
        FastAutoGenerator.create(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD)
                // 全局配置
                .globalConfig(builder -> builder
                        .author(AUTHOR)
                        .enableSwagger()
                        .outputDir(OUT_DIR)
                )
                // 存储位置配置
                .packageConfig(builder -> builder
                        // 父文件夹名字，比如我这里就是com.coder
                        .parent(PACKAGE_NAME)
                        // 代码生成的文件夹名，比如我这里是rental，那么加起来就是com.coder.rental
                        .moduleName(MODULE_NAME)
                        // pathInfo可以将OutputFile中定义的各层级目录（entity,service,serviceImpl,mapper,xml,controller）放在指定的文件夹内
                        // 而不是默认的文件夹，这里我将mapper单独提出来放在resources目录下
                        .pathInfo(Collections.singletonMap(OutputFile.xml, ".\\src\\main\\resources\\mapper"))
                )
                // 配置生成策略，包括要包含的表、表名前缀等
                .strategyConfig(builder-> {
                    builder.addInclude(TABLES)
                            // 去除掉表的前缀
                            .addTablePrefix(PREFIX)
                            // 配置实体类
                            .entityBuilder()
                            // 添加@Data注解
                            .enableLombok()
                            // 添加链式编程注解
                            .enableChainModel()
                            // 配置控制器
                            .controllerBuilder()
                            // 生成一个@RestController注解
                            .enableRestStyle();
                })
                // 生成代码的模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
