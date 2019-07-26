package com.ddm.vblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ddm.vblog.dto.article.ArticleFileDTO;
import com.ddm.vblog.dto.article.ArticleQueryParamsDTO;
import com.ddm.vblog.entity.Article;
import com.ddm.vblog.page.Page;

import java.util.List;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author DindDangMao
 * @since 2019-01-29
 */
public interface ArticleService extends IService<Article> {

    /**
     * 获取到最热文章
     * @param iPage 分页对象
     * @return 当前页, 总页数, 当前页文章信息
     */
    IPage<Article> getHotArticle(IPage<Article> iPage);

    /**
     * 获取到最新的文章
     * @return 当前页, 总页数, 当前页最新文章信息
     */
    IPage<Article> getNewArticle(IPage<Article> iPage);

    /**
     * 获取文章归档视图
     * @return
     */
    List<ArticleFileDTO> fileArticle();

    /**
     * 获取该文章的详细信息
     * @param id 文章id
     * @return 文章信息
     */
    Article  getArticleById(String id);

    /**
     * 增加文章的查看数
     * @param id 文章id
     * @param ip 发起请求主机的ip
     * @return 返回当前文章查看数
     */
    Integer addArticleViewCount(String id, String ip);

    /**
     * 加载首页文章数据
     * @param page 分页信息
     * @param queryParams 查询条件字段对象
     * @return 分页信息、文章列表
     */
    IPage<Article> loadHomeArticle(Page page, ArticleQueryParamsDTO queryParams);

    /**
     * 获取一个文章的评论数
     * @param articleId 文章id
     * @return 文章评论数
     */
    int getCommentCount(String articleId);
}
