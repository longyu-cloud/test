package cn.longyu.remark.mapper;

import cn.longyu.remark.domain.po.LikedRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author Administrator
* @description 针对表【liked_record(点赞记录表)】的数据库操作Mapper
* @createDate 2025-07-28 20:58:28
* @Entity cn.longyu.remark.domain.po.LikedRecord
*/
public interface LikedRecordMapper extends BaseMapper<LikedRecord> {

}




