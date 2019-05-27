package cn.itsource.aigou.service.impl;

import cn.itsource.aigou.client.RedisClient;
import cn.itsource.aigou.client.TemplateClient;
import cn.itsource.aigou.domain.ProductType;
import cn.itsource.aigou.mapper.ProductTypeMapper;
import cn.itsource.aigou.service.IProductTypeService;
import cn.itsource.aigou.util.StrUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品目录 服务实现类
 * </p>
 *
 * @author chenbo
 * @since 2019-05-16
 */
@Service
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductType> implements IProductTypeService {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private TemplateClient templateClient;

    @Override
    public List<ProductType> loadTreeData() {
//先从redis中获取
        String productTypesStr = redisClient.get("productTypes");
        if(StringUtils.isEmpty(productTypesStr)){
            //从数据库中查询
            //使用循环
            List<ProductType> productTypes = loadDataTree();
            //存到redis中
            String jsonString = JSONArray.toJSONString(productTypes);
            redisClient.set("productTypes",jsonString);
            //返回
            return productTypes;
        }
        //转换成List
        List<ProductType> productTypes = JSONArray.parseArray(productTypesStr, ProductType.class);
        return productTypes;
    }

    @Override
    public void createStaticPage() {
        String templatePath = "E:\\IDEA\\workspace\\aigou-parent\\aigou-product-parent\\product-service\\src\\main\\resources\\template\\product.type.vm";
        String targetPath = "E:\\IDEA\\workspace\\aigou-parent\\aigou-product-parent\\product-service\\src\\main\\resources\\template\\product.type.vm.html";
        List<ProductType> productTypes = loadDataTree();
        Map<String,Object> map = new HashMap<>();
        map.put("model",productTypes);
        map.put("templatePath",templatePath);
        map.put("targetPath",targetPath);
        templateClient.createStaticPage(map);

        templatePath = "E:\\IDEA\\workspace\\aigou-parent\\aigou-product-parent\\product-service\\src\\main\\resources\\template\\home.vm";
        targetPath = "E:\\IDEA\\workspace\\aigou-web-parent\\ecommerce\\home.html";
        Map<String,Object> model = new HashMap<>();
        map = new HashMap<>();
        model.put("staticRoot","E:\\IDEA\\workspace\\aigou-parent\\aigou-product-parent\\product-service\\src\\main\\resources\\");
        map.put("model",model);
        map.put("templatePath",templatePath);
        map.put("targetPath",targetPath);
        templateClient.createStaticPage(map);
    }

    @Override
    public String getPathById(Long id) {
        ProductType productType = baseMapper.selectById(id);
        return productType.getPath();
    }

    @Override
    public boolean save(ProductType entity) {
        //先执行保存
        boolean result = super.save(entity);
        sychornizedOperate();
        return result;
    }

    @Override
    public boolean removeById(Serializable id) {
        boolean result = super.removeById(id);
        sychornizedOperate();
        return result;
    }

    @Override
    public boolean updateById(ProductType entity) {
        boolean result = super.updateById(entity);
        sychornizedOperate();
        return result;
    }


    private void updateRedis(){
        List<ProductType> productTypes = loadDataTree();
        //转成json字符串缓存到redis中
        String jsonString = JSONArray.toJSONString(productTypes);
        redisClient.set("productTypes",jsonString);
    }

    public void sychornizedOperate(){
        updateRedis();
        loadDataTree();
    }

    /**
     * 第一种方式递归
     */
    private List<ProductType> loadDataTree(Long pid){
        //查询子类型
        List<ProductType> children = baseMapper.selectList(new QueryWrapper<ProductType>().eq("pid", pid));
        //递归出口
        if(children==null||children.size()==0){
            return null;
        }
        for (ProductType productType : children) {
            //对子进行循环
            List<ProductType> c = loadDataTree(productType.getId());
            //将所有的孙子类型放入到子的children属性中
            productType.setChildren(c);
        }
        return children;
    }


    /**
     * 第二种方式循环
     */
    private List<ProductType> loadDataTree(){
        //查询所有
        List<ProductType> productTypes = baseMapper.selectList(null);
        Map<Long,ProductType> map = new HashMap<>();
        for (ProductType productType : productTypes) {
            map.put(productType.getId(),productType);
        }
        //放一级类型
        List<ProductType> list = new ArrayList<>();
        //子找父
        for (ProductType productType : productTypes) {
            //如果pid为0则自己就是父
            if(productType.getPid()==0){
                list.add(productType);
            }else{
                map.get(productType.getPid()).getChildren().add(productType);
            }
        }
        return list;
    }

}
