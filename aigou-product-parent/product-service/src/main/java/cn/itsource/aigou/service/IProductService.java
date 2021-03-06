package cn.itsource.aigou.service;

import cn.itsource.aigou.domain.Product;
import cn.itsource.aigou.domain.Specification;
import cn.itsource.aigou.query.ProductQuery;
import cn.itsource.aigou.util.PageList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品 服务类
 * </p>
 *
 * @author chenbo
 * @since 2019-05-20
 */
public interface IProductService extends IService<Product> {

    PageList<Product> getByQuery(ProductQuery query);

    void batchDeleteByIds(List<? extends Serializable> ids);

    List<Specification> getViewProperties(Long productId);

    void saveViewProperties(List<Specification> specifications,Long productId);

    List<Specification> getSkuProperties(Long productId);

    void saveSkuProperties(List<Specification> specifications, Long productId, List<Map<String,String>> skus);

    void onSale(List<Long> ids);

    void offSale(List<Long> idsStr);

    List<Map<String,Object>> loadCrumbs(Long productTypeId);
}
