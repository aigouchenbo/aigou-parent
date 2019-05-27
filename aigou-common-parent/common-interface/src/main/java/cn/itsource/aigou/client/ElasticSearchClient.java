package cn.itsource.aigou.client;

import cn.itsource.aigou.domain.ProductDoc;
import cn.itsource.aigou.util.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("AIGOU-COMMON")
public interface ElasticSearchClient {

    /**
     * 保存一个
     * @param productDoc
     * @return
     */
    @PostMapping("/elasticSearch/save")
    AjaxResult save(@RequestBody ProductDoc productDoc);

    /**
     * 保存多个
     * @param productDocs
     * @return
     */
    @PostMapping("/elasticSearch/saveBatch")
    AjaxResult saveBatch(@RequestBody List<ProductDoc> productDocs);

    /**
     * 删除一个
     * @param id
     * @return
     */
    @DeleteMapping("/elasticSearch/delete")
    AjaxResult delete(@RequestParam("id") Long id);

    /**
     * 删除多个
     * @param productDocs
     * @return
     */
    @PostMapping("/elasticSearch/deleteBatch")
    AjaxResult deleteBatch(@RequestBody List<ProductDoc> productDocs);

    @PostMapping("/elasticSearch/deleteBatchByIds")
    void deleteBatchByIds(List<Long> idsStr);
}
