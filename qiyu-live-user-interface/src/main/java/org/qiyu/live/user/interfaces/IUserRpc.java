package org.qiyu.live.user.interfaces;

import org.qiyu.live.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

/**
 * @param
 * @Auther:北
 * @Date:2025/8/23
 * @Description:
 * @VERSON:1.0
 */
public interface IUserRpc {
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
     *
     * @param userDTO
     * @return
     */
    boolean insertOne(UserDTO userDTO);

    /**
     * 批量查询用户信息
     * @param userIdList
     * @return
     */
    Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIdList);

}
