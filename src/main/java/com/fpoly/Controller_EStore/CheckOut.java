package com.fpoly.Controller_EStore;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fpoly.Entity.Order;
import com.fpoly.Entity.OrdersDetails;
import com.fpoly.Entity.Product;
import com.fpoly.Entity.User;
import com.fpoly.Repository.OrderDAO;
import com.fpoly.Repository.Order_DetailDAO;
import com.fpoly.Repository.ProductDAO;
import com.fpoly.Repository.UserDAO;
import com.fpoly.SendMail.MailInfo;
import com.fpoly.SendMail.MailerService;
import com.fpoly.Utils.CookieService;

@Controller
public class CheckOut {
//	
//	@Autowired
//	Product_Detail idP;
//	
	@Autowired
	ProductDAO Pdao;
	
	@Autowired
	UserDAO Udao;
	
	@Autowired
	CookieService cookieService;
	
	@Autowired
	OrderDAO odao;
	
	@Autowired
	Order_DetailDAO oddao;
	
	@Autowired
	MailerService mailer;
	
	private int idP;
	private int quantity;
	@PostMapping("/checkout")
	public String checkout (
			@RequestParam("id") int idProduct,
			@RequestParam("quantity") int quantity,
			@RequestParam("color") String color,
			@RequestParam("size") String size,
			Model model
			) {
		Product pr = Pdao.getReferenceById(idProduct);
		model.addAttribute("product", pr);	
		model.addAttribute("quantity", quantity);	
		model.addAttribute("color", color);	
		model.addAttribute("size", size);	
		idP = pr.getId();
		this.quantity = quantity;
		return "User/checkout";
	}
	
	@PostMapping("/pay")
	public String pay(Model model, @RequestParam("address") String address, @RequestParam String txtTo, @RequestParam String txtContent)throws IOException {
		
		String userName = cookieService.getValue("uName");
		if(!userName.equals("")) {
			User us = Udao.findByUserName(userName);
			
			Order order = new Order();
			order.setUser(us);
			order.setAddress(address);
			order.setCreatedate(new Date());
			odao.save(order);
			Product pr = Pdao.getReferenceById(idP);
			System.out.println(pr.getId());
			OrdersDetails detail = new OrdersDetails();
			detail.setPrice(pr.getPrice());
			detail.setProduct(pr);
			detail.setOrder(order);
			detail.setQuantity(quantity);
			detail.setStatus(2);
			
			oddao.save(detail);
			
			if (!txtTo.isEmpty()) {
				// send mail
				MailInfo mail = new MailInfo();
				mail.setFrom("wwatermelonjuice@gmail.com");
				mail.setTo(txtTo);
				mail.setSubject("Đơn hàng của bạn đã đặt thành công");
				mail.setBody(txtContent);
				// Gửi mail
				mailer.queue(mail);
				System.out.print("<h1>Mail của bạn đã được gửi đi</h1>");
			}else {
				return "redirect:/login";
			}
		}else {
			return "redirect:/login";
		}
		return "redirect:/product-detail"+ "?id="+idP;
	}
	@PostMapping("/send")
	public String send(Model model, @RequestParam String txtTo, @RequestParam String txtContent) throws IOException {
		MailInfo mail = new MailInfo();
		mail.setFrom("wwatermelonjuice@gmail.com");
		mail.setTo(txtTo);
		mail.setSubject("Đơn hàng của bạn đã được thanh toán");
		mail.setBody(txtContent);
		// Gửi mail
		mailer.queue(mail);
		System.out.print("<h1>Mail của bạn đã được gửi đi</h1>");

		return "redirect:/shop-cart";

	}
}
