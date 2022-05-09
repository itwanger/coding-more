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
     * 页数
     */
    private Integer pageSize = 10;

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
    private HttpServletResponse response;

    /**
     * 排序字段,按数据库字段:menu_order,page_view,post_modified;多个以逗号间隔
     */
    private String orderBy = "menu_order,post_modified";
    /**
     * 是否升序，boolean类型
     */
    boolean isAsc = false;


    private WebRequestParam(Builder builder) {
        id = builder.id;
        if (builder.page != null) {
            page = builder.page;
        }

        isAsc = builder.isAsc;
        if(builder.orderBy != null){
            orderBy = builder.orderBy;
        }
        request = builder.request;
        model = builder.model;
        response = builder.response;

        postId = builder.postId;
        channelId = builder.channelId;
    }


    public static class Builder {
        private Integer id;
        private Integer page;
        private HttpServletRequest request;
        private ModelMap model;
        private HttpServletResponse response;
        /*private Integer pageSize;*/
        private Long channelId;
        private Long postId;
        private String orderBy ;
        private boolean isAsc ;

        public Builder() {

        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder page(int page) {
            this.page = page;
            return this;
        }

        public Builder orderBy(String orderBy) {
            this.orderBy = orderBy;
            return this;
        }
        public Builder isAsc(boolean isAsc) {
            this.isAsc = isAsc;
            return this;
        }

        public Builder request(HttpServletRequest request) {
            this.request = request;
            return this;
        }

        public Builder model(ModelMap model) {
            this.model = model;
            return this;
        }

        public Builder response(HttpServletResponse response) {
            this.response = response;
            return this;
        }

        public Builder channelId(Long channelId) {
            this.channelId = channelId;
            return this;
        }

        public Builder postId(Long postId) {
            this.postId = postId;
            return this;
        }

        public WebRequestParam build() {
            return new WebRequestParam(this);
        }


    }

}
