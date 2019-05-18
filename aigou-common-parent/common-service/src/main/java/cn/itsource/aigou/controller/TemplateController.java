package cn.itsource.aigou.controller;

import cn.itsource.aigou.util.VelocityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TemplateController {

    @PostMapping
    public void createStaticPage(@RequestBody Map<String,Object> map){
        Object model =map.get("model");
        String templatePath = (String)map.get("templatePath");
        String targetPath = (String)map.get("targetPath");
        VelocityUtils.staticByTemplate(model,templatePath,targetPath);
    }

}
