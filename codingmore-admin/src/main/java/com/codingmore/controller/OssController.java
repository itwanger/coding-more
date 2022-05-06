package com.codingmore.controller;

import com.codingmore.service.IOssService;
import com.codingmore.webapi.ResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 * 上传 控制器
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
@Controller
@Api(tags = "上传")
@RequestMapping("/ossController")
public class OssController {
    @Autowired
    private IOssService ossService;

    @RequestMapping(value = "/upload",method=RequestMethod.POST)
    @ResponseBody
    @ApiOperation("上传")
    public ResultObject<String> upload(@RequestParam("file") MultipartFile file, HttpServletRequest req)  {
        return ResultObject.success(ossService.upload(file));
    }
}

