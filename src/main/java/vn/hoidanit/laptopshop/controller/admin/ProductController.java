package vn.hoidanit.laptopshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;
import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    @GetMapping("/admin/product")
    public String getProduct(Model model) {
        List<Product> arrProducts = this.productService.getAllProducts();
        model.addAttribute("productList", arrProducts);
        return "admin/product/show";
    }

    @GetMapping("/admin/product/create") // method GET
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String createProductPage(Model model,
            @ModelAttribute("newProduct") @Valid Product newProduct,
            BindingResult newProductBindingResult,
            @RequestParam("productImgFile") MultipartFile file) {

        // validate
        if (newProductBindingResult.hasErrors()) {
            return "/admin/product/create";
        }

        String imgProduct = this.uploadService.handleSaveUploadFile(file, "product");

        newProduct.setImage(imgProduct);
        this.productService.handleSaveProduct(newProduct);
        return "redirect:/admin/product";
    }

    // view detail product
    @RequestMapping(value = "/admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product product = this.productService.getProductById(id);
        model.addAttribute("product", product);
        return "admin/product/detail";
    }

    @RequestMapping(value = "/admin/product/update/{id}")
    public String getUpdateProductPage(Model model, @PathVariable long id) {
        Product product = this.productService.getProductById(id);
        model.addAttribute("newProduct", product);
        return "admin/product/update";
    }

    @PostMapping(value = "/admin/product/update")
    public String postUpdateProduct(@ModelAttribute("newProduct") @Valid Product newProduct,
            BindingResult newProductBindingResult,
            @RequestParam("productImgFile") MultipartFile file) {
        // validate
        if (newProductBindingResult.hasErrors()) {
            return "/admin/product/update";
        }
        Product product = this.productService.getProductById(newProduct.getId());
        if (product != null) {
            product.setName(newProduct.getName());
            product.setPrice(newProduct.getPrice());
            product.setDetailDesc(newProduct.getDetailDesc());
            product.setShortDesc(newProduct.getShortDesc());
            product.setQuantity(newProduct.getQuantity());
            product.setFactory(newProduct.getFactory());
            product.setTarget(newProduct.getTarget());
            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveUploadFile(file, "product");
                product.setImage(img);
            }
            this.productService.handleSaveProduct(product);
        }
        return "redirect:/admin/product";
    }

    // delete product
    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newProduct", new Product());
        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete")
    public String postDeleteUser(Model model, @ModelAttribute("newProduct") Product product) {
        this.productService.deleteProductById(product.getId());
        return "redirect:/admin/product";
    }
}
