package service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import entity.ArticleTag;
import mapper.ArticleTagMapper;
import org.springframework.stereotype.Service;
import service.ArticleTagService;

/**
 * <p>
 * 文章标签表 服务实现类
 * </p>
 *
 * @author DindDangMao
 * @since 2019-01-29
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
