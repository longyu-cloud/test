package cn.longyu.remark.controller;


import cn.longyu.remark.domain.dto.LikeRecordFormDTO;
import cn.longyu.remark.service.LikedRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikedRecordController {

    private final LikedRecordService likedRecordService;

    /**
     * 点赞或者是取消点赞
     * @param dto 业务id，是否点赞，业务类型
     */
    @PostMapping
    public void addLikesRecord(@RequestBody LikeRecordFormDTO dto){
        likedRecordService.addLikesRecord(dto);
    }
}
