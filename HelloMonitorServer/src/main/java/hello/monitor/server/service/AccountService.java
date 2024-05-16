package hello.monitor.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hello.monitor.server.entity.dto.Account;
import hello.monitor.server.entity.vo.request.*;
import hello.monitor.server.entity.vo.response.SubAccountVO;

import java.util.List;

/**
 * 用户的方法
 * @author ChangxueDeng
 */
public interface AccountService extends IService<Account> {
    /**
     * 通过用户名或邮箱查找用户
     * @param text 用户名或邮箱
     * @return {@link Account}
     */
    Account findAccountByUsernameOrEmail(String text);

    /**
     * 通过Id查找用户
     * @param id 用户id
     * @return {@link Account}
     */
    Account findAccountById(int id);

    /**
     * 校验邮箱是否有效并发送验证码
     * @param email 邮箱
     * @param type 验证码类型
     * @param ip 用户ip
     * @return {@link String}
     */
    String emailVerify(String email, String type, String ip);

    /**
     * 进行重置密码时进行邮箱校验
     * @param resetConfirmVO 校验参数
     * @return {@link String}
     */
    String resetConfirm(ResetConfirmVO resetConfirmVO);

    /**
     * 重置用户密码
     * @param resetPasswordVO 重置密码参数
     * @return {@link String}
     */
    String resetPassword(ResetPasswordVO resetPasswordVO);
    String changePassword(int id, UserChangePasswoedVO vo);
    String resetEmail(int id, UserResetEmailVO vo);
    String createSubAccount(CreateSubAccountVO vo);
    String deleteSubAccount(int id, int subUid);
    List<SubAccountVO> getSubAccountList();

}
