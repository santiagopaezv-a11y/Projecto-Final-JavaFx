package Aparta_Suites_UQ.controller.servicio;

import Aparta_Suites_UQ.model.Empresa;
import Aparta_Suites_UQ.model.huesped.Administrador;
import Aparta_Suites_UQ.model.huesped.Empleado;
import Aparta_Suites_UQ.model.huesped.Recepcionista;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class EmpleadoServicio {

    private static EmpleadoServicio instance;

    private Empleado empleadoEditar;

    private EmpleadoServicio() {
    }

    public static EmpleadoServicio getInstance() {
        if (instance == null) {
            instance = new EmpleadoServicio();
        }
        return instance;
    }

    public List<Empleado> listarEmpleados() {
        return Empresa.getInstance().getListaEmpleados();
    }

    public Empleado getEmpleadoEditar() {
        return empleadoEditar;
    }

    public void setEmpleadoEditar(Empleado empleado) {
        this.empleadoEditar = empleado;
    }

    public void eliminarEmpleado(Empleado empleado) {
        listarEmpleados().remove(empleado);
    }


    public boolean coincideFiltro(Empleado empleado, String texto, String rol) {
        String textoBusqueda = texto == null ? "" : texto.toLowerCase().trim();
        boolean coincideTexto = textoBusqueda.isEmpty()
                || (empleado.getNombre() != null && empleado.getNombre().toLowerCase().contains(textoBusqueda))
                || (empleado.getDocumento() != null && empleado.getDocumento().toLowerCase().contains(textoBusqueda))
                || (empleado.getCorreo() != null && empleado.getCorreo().toLowerCase().contains(textoBusqueda));
        String rolEmpleado = empleado.getRol() != null ? empleado.getRol() : "";
        boolean coincideRol = rol == null || rol.equals("TODOS") || rolEmpleado.equalsIgnoreCase(rol);
        return coincideTexto && coincideRol;
    }

    public boolean guardarEmpleado(String documento, String nombre, String rol, LocalDate fechaNacimiento,
                                   String telefono, String correo, String password) {
        validarDatos(documento, nombre, rol, fechaNacimiento, telefono, correo, password);

        String doc = documento.trim();
        String nom = nombre.trim();
        String tel = telefono.trim();
        String mail = correo.trim();
        String clave = password.trim();

        if (empleadoEditar != null) {
            empleadoEditar.setDocumento(doc);
            empleadoEditar.setNombre(nom);
            empleadoEditar.setFechaNacimiento(fechaNacimiento);
            empleadoEditar.setTelefono(tel);
            empleadoEditar.setCorreo(mail);
            empleadoEditar.setContrasena(clave);
            empleadoEditar = null;
            return true;
        }

        for (Empleado existente : listarEmpleados()) {
            if (existente.getDocumento().equalsIgnoreCase(doc)) {
                throw new IllegalArgumentException(
                        "Ya existe un empleado registrado con el número de documento: " + doc);
            }
        }

        Empleado nuevo;
        if ("ADMINISTRADOR".equalsIgnoreCase(rol)) {
            nuevo = new Administrador(nom, doc, fechaNacimiento, tel, mail, clave);
        } else {
            nuevo = new Recepcionista(nom, doc, fechaNacimiento, tel, mail, clave);
        }
        listarEmpleados().add(nuevo);
        return false;
    }

    private void validarDatos(String documento, String nombre, String rol, LocalDate fechaNacimiento,
                              String telefono, String correo, String password) {
        if (estaVacio(documento) || estaVacio(nombre) || rol == null || fechaNacimiento == null
                || estaVacio(telefono) || estaVacio(correo) || estaVacio(password)) {
            throw new IllegalArgumentException("Por favor complete todos los campos obligatorios (*).");
        }
        Period edad = Period.between(fechaNacimiento, LocalDate.now());
        if (edad.getYears() < 18) {
            throw new IllegalArgumentException("El empleado debe ser mayor de edad (18 años o más).");
        }
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
