package cn.itsource.aigou.controller;

import cn.itsource.aigou.domain.ProductDoc;
import cn.itsource.aigou.repository.ProductDocRepository;
import cn.itsource.aigou.util.AjaxResult;
import cn.itsource.aigou.util.PageList;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ProductEsController {

    @Autowired
    private ProductDocRepository productDocRepository;

    /**
     * 保存一个
     * @param productDoc
     * @return
     */
    @PostMapping("/elasticSearch/save")
    public AjaxResult save(@RequestBody ProductDoc productDoc){
        try {
            productDocRepository.save(productDoc);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return  AjaxResult.me().setSuccess(false).setMessage("保存失败："+e.getMessage());
        }
    }

    /**
     * 保存多个
     * @param productDoc
     * @return
     */
    @PostMapping("/elasticSearch/saveBatch")
    public AjaxResult saveBatch(@RequestBody List<ProductDoc> productDoc){
        try {
            productDocRepository.saveAll(productDoc);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return  AjaxResult.me().setSuccess(false).setMessage("保存失败："+e.getMessage());
        }
    }

    /**
     * 删除一个
     * @param id
     * @return
     */
    @DeleteMapping("/elasticSearch/delete")
    public AjaxResult delete(@RequestParam("id") Long id){
        try {
            productDocRepository.deleteById(id);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("删除失败："+e.getMessage());
        }
    }

    /**
     * 删除多个
     * @param productDocs
     * @return
     */
    @PostMapping("/elasticSearch/deleteBatch")
    public AjaxResult deleteBatch(@RequestBody List<ProductDoc> productDocs){
        try {
            productDocRepository.deleteAll(productDocs);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("删除失败："+e.getMessage());
        }
    }

    /**
     * 根据id删除多个
     * @param ids
     * @return
     */
    @PostMapping("/elasticSearch/deleteBatchByIds")
    public AjaxResult deleteBatchByIds(@RequestBody List<Long> ids){
        try {
            for (Long id : ids) {
                if (productDocRepository.existsById(id)){
                    productDocRepository.deleteById(id);
                }
            }
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("删除失败："+e.getMessage());
        }
    }

    @PostMapping("/elasticSearch/list")
    public PageList<ProductDoc> listProduct(@RequestBody Map<String,String> params){
        String all = params.get("keyword");
        String productTypeId = params.get("productTypeId");
        String brandId = params.get("brandId");
        Integer page = Integer.parseInt(params.get("page"));
        Integer size = Integer.parseInt(params.get("size"));
        //价格
        String minPrice = params.get("minPrice");
        String maxPrice = params.get("maxPrice");

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        //查询条件  --  all   match匹配
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if(StringUtils.isNotEmpty(all)){
            boolQueryBuilder.must(new MatchQueryBuilder("all",all));
        }
        //过滤
        List<QueryBuilder> filterList = boolQueryBuilder.filter();
        //类型过滤
        if(StringUtils.isNotEmpty(productTypeId)){
            filterList.add(new TermQueryBuilder("productTypeId",productTypeId));
        }
        //品牌过滤
        if(StringUtils.isNotEmpty(brandId)){
            filterList.add(new TermQueryBuilder("brandId",brandId));
        }
        //最小价格和最大价格过滤
        if(StringUtils.isNotEmpty(minPrice)){
            filterList.add(new RangeQueryBuilder("maxPrice").gte(Integer.parseInt(minPrice)));
        }
        if(StringUtils.isNotEmpty(maxPrice)){
            filterList.add(new RangeQueryBuilder("minPrice").lte(Integer.parseInt(maxPrice)));
        }
        builder.withQuery(boolQueryBuilder);

        //排序
        String sortField = params.get("sortField");
        //默认按照销量
        String order = "saleCount";
        SortOrder sortOrder = SortOrder.DESC;
        //排序规则：
        String sortRule = params.get("sortRule");
        if("asc".equals(sortRule)){
            sortOrder = SortOrder.ASC;
        }
        if("xp".equals(sortField)){
            order = "onSaleTime";
        }
        if("pl".equals(sortField)){
            order = "commontCount";
        }
        if("rq".equals(sortField)){
            order = "viewCount";
        }
        //价格排序
        if("jg".equals(sortField)){
            if(sortOrder == SortOrder.DESC){
                order = "maxPrice";
            }else{
                order = "minPrice";
            }
        }
        builder.withSort(new FieldSortBuilder(order).order(sortOrder));
        //分页
        builder.withPageable(PageRequest.of(page-1,size));
        Page<ProductDoc> productDocPage = productDocRepository.search(builder.build());
        return new PageList<>(productDocPage.getTotalElements(),productDocPage.getContent());
    }

}
