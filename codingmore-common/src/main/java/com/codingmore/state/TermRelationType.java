package com.codingmore.state;

/**
 * 文章栏目关系类型
 */
public enum TermRelationType {
    /**
     * 内容
     */
    CONTENT(1),
    /**
     * 内容链接
     */
    CONTENT_LINK(2),
    /**
     * 栏目链接
     */
    CHANNEL_LINK(3);


    private Integer type;

    public Integer getType() {
        return type;
    }

    TermRelationType(Integer type) {
        this.type = type;
    }
}
