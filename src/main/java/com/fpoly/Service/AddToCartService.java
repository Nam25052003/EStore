package com.fpoly.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.fpoly.Entity.Product;
import com.fpoly.Entity.ProductDetail;
import com.fpoly.Repository.ProductDAO;

@SessionScope
@Service
public class AddToCartService {

	@Autowired
	ProductDAO daoP;
	@Autowired
	HttpServletRequest request;
	
	// Map<Integer, Product> map = new HashMap<>();
	List<ProductDetail> list = new ArrayList<>();
	
	public void addID(Integer id,String color, String size) {
		HttpSession session = request.getSession();
		
		ProductDetail product_Detail = new ProductDetail();
		Product product = daoP.getReferenceById(id);
		product_Detail.setColor(color);
		product_Detail.setSize(size);
		product_Detail.setProduct(product);
		list.add(product_Detail);
		session.setAttribute("order", product_Detail);
		System.out.println("add thanh cong");
		
	}
	
	public List<ProductDetail> getAll(){
		for (ProductDetail productDetail : list) {
			System.out.println(productDetail.getProduct().getName());
		}
		return list;
	}

}
