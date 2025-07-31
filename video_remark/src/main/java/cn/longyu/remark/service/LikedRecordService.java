package cn.longyu.remark.service;

import cn.longyu.remark.domain.dto.LikeRecordFormDTO;
import cn.longyu.remark.domain.po.LikedRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【liked_record(点赞记录表)】的数据库操作Service
* @createDate 2025-07-28 20:58:28
*/
public interface LikedRecordService extends IService<LikedRecord> {


    /**
     * 点赞或者是取消点赞
     * @param dto 业务id，是否点赞，业务类型
     */
    void addLikesRecord(LikeRecordFormDTO dto);
}
