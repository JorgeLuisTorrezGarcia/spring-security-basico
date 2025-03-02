package com.practica.producto.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping("/producto")
@PreAuthorize("denyAll()")
public class TestProductoController {

    String[] producto = {"papa", "arroz"};
    Integer[] precios = {10,32};

    @GetMapping("/allProducto")
    @PreAuthorize("permitAll()")
    public String getProducto(){

        return Arrays.stream(producto).toList().toString();
    }

    @GetMapping("/precios")
    @PreAuthorize("hasAuthority('CREATE')")
    public String getPrecios(){

        return Arrays.toString(precios);

        //return precios.toString();
    }
}
