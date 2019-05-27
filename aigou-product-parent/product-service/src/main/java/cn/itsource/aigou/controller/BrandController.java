package cn.itsource.aigou.controller;

import cn.itsource.aigou.domain.Brand;
import cn.itsource.aigou.query.BrandQuery;
import cn.itsource.aigou.service.IBrandService;
import cn.itsource.aigou.util.AjaxResult;
import cn.itsource.aigou.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class BrandController {
    @Autowired
    public IBrandService brandService;

    /**
    * 保存和修改公用的
    * @param brand  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/brand",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody Brand brand){
        try {
            if(brand.getId()!=null){
                brandService.updateById(brand);
            }else{
                brandService.save(brand);
            }
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("保存对象失败！"+e.getMessage());
        }
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @RequestMapping(value="/brandDL/{id}",method=RequestMethod.DELETE)
    public AjaxResult delete(@PathVariable("id") Long id){
        try {
            brandService.removeById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
        }
    }

    /**
     * 批量删除
     * @param idsStr
     * @return
     */
    @GetMapping(value="/brand/batchDelete")
    @ResponseBody
    public AjaxResult batchDelete(String idsStr){
        try {
            String[] ids = idsStr.split(",");
            List<String> list = Arrays.asList(ids);
            System.out.println(list);
            brandService.batchDeleteByIds(list);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
        }
    }

    //获取
    @RequestMapping(value = "/brand/{id}",method = RequestMethod.GET)
    public Brand get(@PathVariable("id") Long id)
    {
        return brandService.getById(id);
    }

    /**
    * 查看所有信息
    * @return
    */
    @RequestMapping(value = "/brand/list",method = RequestMethod.GET)
    public List<Brand> list(){
        return brandService.list();
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/brand/page",method = RequestMethod.POST)
    public PageList<Brand> json(@RequestBody BrandQuery query)
    {
        return brandService.getByQuery(query);
    }

    /**
     * 根据类型编号加载所有品牌
     * @param productTypeId
     * @return
     */
    @GetMapping("/brand/loadByProductType")
    public Map<String,Object> loadByProductType(@RequestParam("productTypeId") Long productTypeId){
        return brandService.loadByPrductTypeId(productTypeId);
    }

}
