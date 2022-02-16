package com.codingmore.service.impl;

import com.codingmore.model.PostTagRelation;
import com.codingmore.mapper.PostTagRelationMapper;
import com.codingmore.service.IPostTagRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标签文章关系表 服务实现类
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
@Service
public class PostTagRelationServiceImpl extends ServiceImpl<PostTagRelationMapper, PostTagRelation> implements IPostTagRelationService {

}
