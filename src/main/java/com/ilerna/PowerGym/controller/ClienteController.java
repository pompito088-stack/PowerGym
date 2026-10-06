package com.ilerna.PowerGym.controller;

import com.ilerna.PowerGym.exception.DniDuplicadoException;
import com.ilerna.PowerGym.model.Cliente;
import com.ilerna.PowerGym.model.Entrenador;
import com.ilerna.PowerGym.model.TipoMembresia;
import com.ilerna.PowerGym.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @InitBinder("cliente")
    public void quitarEspaciosDeLosTextos(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes/listar";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        agregarListasDeApoyo(model);
        return "clientes/formulario";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", clienteService.buscarPorId(id));
        agregarListasDeApoyo(model);
        return "clientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("cliente") Cliente cliente,
                           BindingResult resultado,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            agregarListasDeApoyo(model);
            return "clientes/formulario";
        }
        try {
            clienteService.guardar(cliente);
        } catch (DniDuplicadoException ex) {
            resultado.rejectValue("dni", "dni.duplicado", ex.getMessage());
            agregarListasDeApoyo(model);
            return "clientes/formulario";
        }
        redirectAttributes.addFlashAttribute("mensaje", "Cliente guardado correctamente.");
        return "redirect:/clientes";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        clienteService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Cliente eliminado correctamente.");
        return "redirect:/clientes";
    }

    private void agregarListasDeApoyo(Model model) {
        model.addAttribute("membresias", TipoMembresia.values());
        model.addAttribute("entrenadores", Entrenador.values());
    }
}
