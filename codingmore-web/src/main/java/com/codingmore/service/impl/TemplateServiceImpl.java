package com.codingmore.service.impl;

import cn.hutool.core.io.FileUtil;
import com.codingmore.service.ITemplateService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TemplateServiceImpl implements ITemplateService, InitializingBean {

    /**
     * 模板根目录
     */
    @Value("${template.rootPath}")
    private String rootPath;


    @Override
    public List<String> getChannelTemplateList(String templateName) {
        return FileUtil.listFileNames( rootPath+File.separator+templateName+File.separator+CHANNEL_DIR);
    }

    @Override
    public List<String> getTemplateList() {
        File[] files = FileUtil.ls(rootPath);
        return  Arrays.asList(files).stream().map(item->{return item.getName();}).collect(Collectors.toList());
    }

    @Override
    public List<String> getContentTemplateList(String templateName) {
        return FileUtil.listFileNames( rootPath+File.separator+templateName+File.separator+CONTENT_DIR);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        createTemplate();
    }

    private void createTemplate(){
        File rootFile = new File(rootPath);
        if(!rootFile.exists()){
            rootFile.mkdirs();
        }
    }
}
