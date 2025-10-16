package org.qiyu.live.user.provider.service;

import org.qiyu.live.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

/**
 * @param
 * @Auther:北
 * @Date:2025/10/4
 * @Description:
 * @VERSON:1.0
 */
public interface IUserService {

    /**
     * 根据用户ID进行查询
     * @param userId
     * @return
     */
    UserDTO getByUserId(Long userId);


    /**
     * 用户信息更细
     * @param userDTO
     * @return
     */

    boolean updateUserInfo(UserDTO userDTO);

    /**
     * 插入用户信息
     * @param userDTO
     * @return
     */
    boolean insertOne(UserDTO userDTO);

    Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIdList);
}
