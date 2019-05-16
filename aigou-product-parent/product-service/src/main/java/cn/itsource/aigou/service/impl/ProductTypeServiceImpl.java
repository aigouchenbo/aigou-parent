package cn.itsource.aigou.service.impl;

import cn.itsource.aigou.domain.ProductType;
import cn.itsource.aigou.mapper.ProductTypeMapper;
import cn.itsource.aigou.service.IProductTypeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

    @Override
    public List<ProductType> loadTreeData() {
//        return loadDataTree(0L);
        return loadDataTree();
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
