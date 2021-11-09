package ru.java.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.java.dto.ClientDto;
import ru.java.model.AddressEntity;
import ru.java.model.ClientEntity;
import ru.java.model.PhoneEntity;
import ru.java.service.DBServiceClient;

import java.util.List;
import java.util.Set;

@Controller
public class ClientController {
    private final DBServiceClient clientService;

    public ClientController(DBServiceClient clientService) {
        this.clientService = clientService;
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/client")
    public String clients(Model model) {
        List<ClientEntity> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "clients";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        var attributeValue = new ClientDto();
        model.addAttribute("client", attributeValue);
        return "new";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(@ModelAttribute ClientDto clientDto) {
        var phone = new PhoneEntity(clientDto.getPhone());
        var address = new AddressEntity(clientDto.getAddress());
        var clientEntity = new ClientEntity(
                clientDto.getLogin(),
                clientDto.getPassword(),
                address,
                Set.of(phone)
        );
        clientService.saveClient(clientEntity);
        return new RedirectView("/client", true);
    }
}
