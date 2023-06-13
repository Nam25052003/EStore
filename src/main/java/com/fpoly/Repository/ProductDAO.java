package com.fpoly.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpoly.Entity.Product;

@EnableJpaRepositories
@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {

	@Query("SELECT p FROM Product p ")
	List<Product> findProductData();

	List<Product> findByNameLike(String name);

	@Query("SELECT p FROM Product p  where p.quantity > 0")
	List<Product> findProductInventory();


	@Query(value = "SELECT Products.id, Products.name, Products.imgP, Products.price, COUNT(OrdersDetails.productID) AS total_ordered\n"
			+ "	 		FROM Orders\n"
			+ "	 		JOIN OrdersDetails ON Orders.id = OrdersDetails.orderID\n"
			+ "	 		JOIN Products ON OrdersDetails.productID = Products.id\n"
			+ "	 		GROUP BY Products.id, Products.name, Products.imgP, Products.price\n"
			+ "	 		ORDER BY total_ordered DESC;",nativeQuery = true)
	 List<Object> sortListSelling();
	
	// lấy ra product co' Id = 
		Product findByIdLike(Integer id);
		
		// Lấy ra sản phẩm top 6 được yêu thích
	@Query( value = "	select top 6  Products.id,  Products.[name],Products.price,imgP,brand,categoryId,quantity,description,createdate\n"
			+ "					from  Products\n"
			+ "					join Favorite on Products.id =  Favorite.productId				\n"
			+ "					GROUP BY Products.id , Products.[name],Products.price,imgP,brand,categoryId,quantity,description,createdate\n"
			+ "					ORDER BY COUNT(Favorite.productId) DESC",nativeQuery = true)	
	 List<Product> findProductDataTop6Favorite();
	
	@Query( value ="select top 6 * 	from  Products	ORDER BY createdate DESC",nativeQuery = true)
	 List<Product> findProductDataTop6Date();
	
	@Query(value ="select top 6 * from Products where brand = :brand ",nativeQuery = true)
	 List<Product> findProductDataTop6Brand(@Param("brand") String brand); 
}
