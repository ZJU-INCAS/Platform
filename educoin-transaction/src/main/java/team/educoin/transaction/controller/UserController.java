package team.educoin.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;
import team.educoin.common.controller.CommonResponse;
import team.educoin.transaction.service.impl.UserServiceImpl;

/**
 * description:
 *
 * @author: chenzhou04
 * @create: 2019-04-16
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @RequestMapping(method = RequestMethod.GET)
    public CommonResponse testFabricRequest() {
        CommonResponse res = new CommonResponse();
        res.setStatus(0);
        res.setMessage("success");
        res.setData(userServiceImpl.getUserInfo());
        return res;
    }

}
