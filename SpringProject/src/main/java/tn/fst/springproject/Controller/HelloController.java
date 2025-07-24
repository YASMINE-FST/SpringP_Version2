package tn.fst.springproject.Controller;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/hello")
@CrossOrigin(origins = "http://localhost:4200") // Autorise Angular à appeler ce backend

public class HelloController {

    @GetMapping
    public String sayHello() {
        return "Hello from Spring Boot!";
    }
}