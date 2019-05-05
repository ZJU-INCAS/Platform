package team.educoin.transaction.fabric;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import team.educoin.transaction.pojo.FabricFileInfo;

import java.util.List;

@Component
@FeignClient(value = "fabricFileClient", url = "${educoin.fabric.url}")
@RequestMapping("/api")
public interface FileFabricClient {
    // 获取fabric上的资源信息(测试，真正的获取直接从数据库获取)
    @RequestMapping(value = "/Service", method = RequestMethod.GET)
    List<FabricFileInfo> getFile();
    // 注册资源
    @RequestMapping(value = "/Service", method = RequestMethod.POST, consumes = "application/json")
    String registerService(@RequestBody FabricFileInfo fabricFileInfo);
    // 删除资源
    @RequestMapping(value = "/Service/{id}", method = RequestMethod.DELETE)
    int deleteService(@PathVariable("id") String id);
    // 修改资源信息
    @RequestMapping(value = "/Service/{id}", method = RequestMethod.PUT)
    String updateService(@PathVariable("id") String id, @RequestBody FabricFileInfo fabricFileInfo);
}
