package team.educoin.transaction.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import team.educoin.common.controller.CommonResponse;

/**
 * description:
 *
 * @author: chenzhou04
 * @create: 2019-04-16
 */
@RequestMapping("/test")
public class TestController {



    @RequestMapping(value = "/fabric",method = RequestMethod.GET)
    public CommonResponse testFabricRequest(){
        return new CommonResponse();
    }

    @RequestMapping(value = "/mysql",method = RequestMethod.GET)
    public CommonResponse testMysqlRequest(){
        return new CommonResponse();
    }
}
