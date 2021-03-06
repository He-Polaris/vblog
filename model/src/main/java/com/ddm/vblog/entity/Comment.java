package com.ddm.vblog.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author DindDangMao
 * @since 2019-01-29
 */
@Data
@TableName("vblog_comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 文章ID
     */
    private String articleId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 自己评论数量
     */
    @TableField(exist = false)
    private Integer subLevelCount;

    /**
     * 该评论的回复集合
     */
    @TableField(exist = false)
    private List<Reply> reply;

    /**
     * 此评论的回复数
     */
    @TableField(exist = false)
    private Integer replyCount = 0;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 头像
     */
    private String avatar;
    


}
