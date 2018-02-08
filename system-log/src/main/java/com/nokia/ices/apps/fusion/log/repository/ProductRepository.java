package com.nokia.ices.apps.fusion.log.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.log.domain.Product;

/**
 * 产品
 * @author Administrator
 */
@RepositoryRestResource(collectionResourceRel = "product", path = "product" ,itemResourceRel="product")
public interface ProductRepository extends JpaRepository<Product, Long>,JpaSpecificationExecutor<Product> {

	/*@Modifying
	@Query("update Product h set h.product=:product,h.orderby=:orderby,h.projectName=:projectName, where h.id =:id")
	void updateProduct(@Param("product")String product,@Param("orderby")Integer orderby, @Param("id")Long id);
*/
}