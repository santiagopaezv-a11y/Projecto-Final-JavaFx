package Aparta_Suites_UQ.utils;

import Aparta_Suites_UQ.Empresa;
import java.io.*;

public class PersistenciaUtil {

    // Ruta a la carpeta "data" en la raíz del proyecto
    private static final String CARPETA_DATA = "data";
    private static final String RUTA_ARCHIVO = CARPETA_DATA + File.separator + "empresa.dat";

    public static void guardarEmpresa(Empresa empresa) {
        if (empresa == null) return;

        try {
            File carpeta = new File(CARPETA_DATA);
            if (!carpeta.exists()) {
                carpeta.mkdirs(); // La crea automáticamente si no la habías creado
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA_ARCHIVO))) {
                oos.writeObject(empresa);
                System.out.println("[Persistencia]  Guardado con éxito en: " + RUTA_ARCHIVO);
            }
        } catch (IOException e) {
            System.err.println("[Persistencia] ❌ Error al guardar: " + e.getMessage());
            e.printStackTrace(); // Te dirá qué clase no es Serializable si llega a fallar
        }
    }

    public static Empresa cargarEmpresa() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (Empresa) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Persistencia] ❌ Error al cargar: " + e.getMessage());
            return null;
        }
    }
}