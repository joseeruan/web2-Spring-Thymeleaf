package jose.ruan.web2.controller;

import jose.ruan.web2.dao.ProductDAO;
import jose.ruan.web2.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductDAO productDAO;

    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @GetMapping
    public String list(Model model) {
        List<Product> products = productDAO.findAll();
        model.addAttribute("products", products);
        return "products/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        return "products/form";
    }

    @PostMapping
    public String save(@ModelAttribute Product product) {
        if (product.getCode() == 0) {
            productDAO.save(product);
        } else {
            productDAO.update(product);
        }
        return "redirect:/products";
    }

    @GetMapping("/edit/{code}")
    public String editForm(@PathVariable int code, Model model) {
        Product product = productDAO.findByCode(code);
        model.addAttribute("product", product);
        return "products/form";
    }

    @GetMapping("/delete/{code}")
    public String delete(@PathVariable int code) {
        productDAO.delete(code);
        return "redirect:/products";
    }
}
