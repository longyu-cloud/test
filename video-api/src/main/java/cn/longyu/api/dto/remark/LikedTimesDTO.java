package cn.longyu.api.dto.remark;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计业务的点赞数
 */
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Data
public class LikedTimesDTO {
    /**
     * 点赞的业务id
     */
    private Long bizId;
    /**
     * 总的点赞次数
     */
    private Long likedTimes;
}
