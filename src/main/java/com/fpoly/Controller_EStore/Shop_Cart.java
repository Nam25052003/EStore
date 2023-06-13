package com.fpoly.Controller_EStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fpoly.Entity.Category;
import com.fpoly.Entity.Order;
import com.fpoly.Entity.OrdersDetails;
import com.fpoly.Entity.Product;
import com.fpoly.Entity.ProductDetail;
import com.fpoly.Repository.CategoryDAO;
import com.fpoly.Repository.ProductDAO;
import com.fpoly.SendMail.MailInfo;
import com.fpoly.SendMail.MailerService;
import com.fpoly.Service.AddToCartService;

@Controller
public class Shop_Cart {

	@Autowired
	ProductDAO Pdao;

	@Autowired
	HttpServletRequest request;

	@Autowired
	AddToCartService cartService;

	@Autowired
	CategoryDAO daoC;

	@Autowired
	AddToCartService addToCartService;

	@GetMapping("/shop-cart")
	public String shopCart(Model model) {
//		for (Product ordersDetails : cartService.getAllP()) {
//			System.out.println(ordersDetails.getPrice());
//		}
		model.addAttribute("nhien", "nhien");
		// model.getAttribute("nhien");
		model.addAttribute("details", list);
		for (OrdersDetails ordersDetails : list) {
			System.out.println(ordersDetails.getPrice());
		}
		return "User/shopCart";
	}

	@ResponseBody
	@GetMapping("/shop-carts")
	public List<OrdersDetails> shopCarts(Model model) {

		return list;
	}

	List<OrdersDetails> list = new ArrayList<>();
	OrdersDetails details = new OrdersDetails();
	Order order = new Order();
	Product product = new Product();
	Category cate = new Category();

	@GetMapping("/addtocart")
	public String addtoCart(Model model, @RequestParam("id") int id, @RequestParam("quantity") int quantity,
			@RequestParam("color") String color, @RequestParam("size") String size

	) {
		Product pr = Pdao.getReferenceById(id);
		pr.setCategory(new Category());

		if (pr != null) {
			HttpSession session = request.getSession();
			if (session.getAttribute("order") == null) {

				product.setId(pr.getId());
				product.setBrand(pr.getBrand());
				product.setCategory(pr.getCategory());
				product.setCreatedate(pr.getCreatedate());
				product.setDescription(pr.getDescription());
				product.setImgProduct(pr.getImgProduct());
				product.setName(pr.getName());
				product.setPrice(pr.getPrice());
				product.setQuantity(pr.getQuantity());

				details.setId(1);
				details.setStatus(1);
				details.setQuantity(quantity);
				details.setPrice(pr.getPrice());
				details.setProduct(product);

				list.add(details);

				order.setOrderDetail(list);
				session.setAttribute("order", order);
			} else {
				// order = (Order)session.getAttribute("order");
				List<OrdersDetails> list = order.getOrderDetail();
				boolean check = false;
				for (OrdersDetails ordersDetails : list) {
					if (ordersDetails.getId() == pr.getId()) {
						ordersDetails.setQuantity(ordersDetails.getQuantity() + quantity);
						check = true;
					}
				}
				if (check == false) {
					OrdersDetails details = new OrdersDetails();
					details.setQuantity(quantity);
					details.setPrice(product.getPrice());
					details.setProduct(product);
					list.add(details);
				}
				session.setAttribute("order", order);

			}

			for (OrdersDetails details : order.getOrderDetail()) {
				System.out.println(details.getProduct().getName());
			}

		}
		return "redirect:/product-detail" + "?id=" + pr.getId();
	}


}
