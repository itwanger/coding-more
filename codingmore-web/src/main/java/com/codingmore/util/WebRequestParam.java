package com.codingmore.util;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Data
@EqualsAndHashCode(callSuper = false)
public class WebRequestParam {
    private Integer id;
    /**
     * 分页参数 默认第一页
     */
    private Integer page = 1;
   
    /**
     * 栏目id
     */
    private Long channelId;
    /**
     * 内容id
     */
    private Long postId;
    private HttpServletRequest request;
    private ModelMap model;
    private  HttpServletResponse response;



    private  WebRequestParam (Builder builder){
        id = builder.id;
        page = builder.page;
        request = builder.request;
        model = builder.model;
        response = builder.response;
     
        postId = builder.postId;
        channelId = builder.channelId;
    }



    public static  class  Builder {
        private Integer id;
        private Integer page;
        private HttpServletRequest request;
        private ModelMap model;
        private  HttpServletResponse response;
      
        private Long channelId;
        private Long postId;
        public Builder(){

        }
        public Builder id(int id){
            this.id = id;
            return this;
        }
        public Builder page(int page){
            this.page = page;
            return this;
        }

        public Builder request(HttpServletRequest request){
            this.request = request;
            return this;
        }
        public Builder model(ModelMap model){
            this.model = model;
            return this;
        }
        public Builder response(HttpServletResponse response){
            this.response = response;
            return this;
        }
      
        public Builder channelId(Long channelId){
            this.channelId = channelId;
            return this;
        }
        public Builder postId(Long postId){
            this.postId = postId;
            return this;
        }
        public WebRequestParam build(){
            return new WebRequestParam(this);
        }
    }

}
