package Aparta_Suites_UQ.controller.servicio;

import Aparta_Suites_UQ.controller.Acceso;
import Aparta_Suites_UQ.model.Empresa;
public class AutenticacionServicio {

    private static final String LOGIN_ADMIN_USUARIO = "sanglie";
    private static final String LOGIN_ADMIN_CLAVE = "123";

    private static final String PORTAL_ADMIN_USUARIO = "admin123";
    private static final String PORTAL_ADMIN_CLAVE = "1234";
    private static final String PORTAL_RECEPCION_USUARIO = "recepcion123";
    private static final String PORTAL_RECEPCION_CLAVE = "123";

    private static AutenticacionServicio instance;

    private AutenticacionServicio() {
    }

    public static AutenticacionServicio getInstance() {
        if (instance == null) {
            instance = new AutenticacionServicio();
        }
        return instance;
    }
    public Acceso autenticarLogin(String usuario, String password) {
        if (LOGIN_ADMIN_USUARIO.equals(usuario) && LOGIN_ADMIN_CLAVE.equals(password)) {
            return Acceso.ADMINISTRADOR;
        }
        if (Empresa.validarCredencialEmpleado(usuario, password)) {
            return Acceso.RECEPCION;
        }
        return Acceso.DENEGADO;
    }
    public Acceso autenticarPortal(String usuario, String password) {
        String u = usuario == null ? "" : usuario.trim();
        String p = password == null ? "" : password.trim();
        if (PORTAL_ADMIN_USUARIO.equals(u) && PORTAL_ADMIN_CLAVE.equals(p)) {
            return Acceso.ADMINISTRADOR;
        }
        if (PORTAL_RECEPCION_USUARIO.equals(u) && PORTAL_RECEPCION_CLAVE.equals(p)) {
            return Acceso.RECEPCION;
        }
        return Acceso.DENEGADO;
    }
    public boolean esAdministradorPortal(String usuario, String password) {
        String u = usuario == null ? "" : usuario.trim();
        String p = password == null ? "" : password.trim();
        return PORTAL_ADMIN_USUARIO.equals(u) && PORTAL_ADMIN_CLAVE.equals(p);
    }
}
