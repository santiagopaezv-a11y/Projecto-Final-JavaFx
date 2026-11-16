package Aparta_Suites_UQ.model.interfaces;

public interface Autenticable{
    String getUsuario();
    String getContrasena();
    default boolean validarCredenciales(String usuario, String contrasena) {
        return getUsuario() != null && getContrasena() != null
                && getUsuario().equals(usuario) && getContrasena().equals(contrasena);
    }
}
