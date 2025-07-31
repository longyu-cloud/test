package cn.longyu.remark.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.longyu.api.dto.remark.LikedTimesDTO;
import cn.longyu.remark.domain.dto.LikeRecordFormDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.longyu.remark.domain.po.LikedRecord;
import cn.longyu.remark.service.LikedRecordService;
import cn.longyu.remark.mapper.LikedRecordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cn.longyu.conmon.constants.MqConstants.Exchange.LIKE_RECORD_EXCHANGE;
import static cn.longyu.conmon.constants.MqConstants.Key.LIKED_TIMES_KEY_TEMPLATE;

/**
* @author Administrator
* @description 针对表【liked_record(点赞记录表)】的数据库操作Service实现
* @createDate 2025-07-28 20:58:28
*/
@Service
@RequiredArgsConstructor
@Slf4j
public class LikedRecordServiceImpl extends ServiceImpl<LikedRecordMapper, LikedRecord>
    implements LikedRecordService{

    //模拟登录用户测试id
    private final Long USER_ID= 1L;
    private final RabbitTemplate rabbitTemplate;

    /**
     * 点赞或者是取消点赞
     * @param dto 业务id，是否点赞，业务类型
     */
    @Override
    public void addLikesRecord(LikeRecordFormDTO dto) {
        //判断是点攒还是取消点攒,成功添加数据库 失败删除记录
        Boolean consequence=dto.getLiked()? likeRecord(dto):unlikeRecord(dto);
        //查询数据库是否有记录避免用户重复点赞或者是取消点赞
        if (!consequence){
            throw new IllegalArgumentException("无效的点赞");
        }
        //如果执行成功，统计点赞数
        Long count = lambdaQuery().eq(LikedRecord::getBizId, dto.getBizId()).count();
        String routingKey = StrUtil.format(LIKED_TIMES_KEY_TEMPLATE, dto.getBizType());
        LikedTimesDTO likedTimesDTO = LikedTimesDTO.of(dto.getBizId(), count);
        sendLikedTimesDTO(likedTimesDTO,routingKey);

    }

    /**
     * mq发送消息
     * @param likedTimesDTO
     * @param routingKey
     */
    public void sendLikedTimesDTO(LikedTimesDTO likedTimesDTO, String routingKey) {
//        在发送消息的地方，给 convertAndSend 方法传入 CorrelationData ，用于标识消息，
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString()); // 生成唯一消息ID
        rabbitTemplate.convertAndSend(LIKE_RECORD_EXCHANGE, routingKey, likedTimesDTO, correlationData);
        log.info("开始发送消息，消息ID：{}，内容：{}，路由键：{}",
                correlationData.getId(),
                likedTimesDTO,
                routingKey);
    }

    /**
     * 点赞
     * @param dto
     * @return
     */
    private Boolean likeRecord(LikeRecordFormDTO dto) {

        //从数据库中查询是否有记录
        Long count = this.lambdaQuery().
                eq(LikedRecord::getBizId, dto.getBizId()).
                eq(LikedRecord::getUserId, USER_ID).count();
        //判断记录是否存在
        if (count>0){
            //存在直接返回
            return false;
        }
        //直接新增一条记录
        LikedRecord likedRecord = new LikedRecord();
        likedRecord.setBizId(dto.getBizId()).setBizType(dto.getBizType()).setUserId(USER_ID);
        save(likedRecord);
        return true;
    }

    /**
     * 取消点赞
     * @param dto
     * @return
     */
    private Boolean unlikeRecord(LikeRecordFormDTO dto) {

        //直接删除数据库中的记录
        return remove(new LambdaQueryWrapper<LikedRecord>().eq(LikedRecord::getBizId, dto.getBizId()).
                eq(LikedRecord::getUserId, USER_ID));
    }
}




