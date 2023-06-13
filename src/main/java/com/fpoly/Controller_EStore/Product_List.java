package com.fpoly.Controller_EStore;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fpoly.Entity.Product;
import com.fpoly.Service.ProductService;

@Controller
public class Product_List {
	
	@Autowired
	ProductService productService;
	
	@GetMapping("/product-list")
	public String productDetail (Model model) {
		// lấy tất cả sản phẩm
		List<Product> list  = productService.findAlls();
		model.addAttribute("list", list);
		return "User/product-list";
	}
	
	@GetMapping("/product-list-search")
	public String searchByName (Model model, @RequestParam("serchByName") String name) {
		// lấy sản phẩm có tên
		System.out.println(name);
		List<Product> list  = productService.searchByname(name);
		model.addAttribute("list", list);
		return "User/product-list";
	}
	
}
