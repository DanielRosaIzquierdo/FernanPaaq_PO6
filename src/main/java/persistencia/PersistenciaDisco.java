package persistencia;

import appController.AppController;
import config.Config;
import models.*;
import utils.Utils;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Properties;


public class PersistenciaDisco {
    public static String APPDATA_PATH = Config.rutaAPPDATA_PATH();
    public static String APPDATA_PATH_U = Config.rutaAPPDATA_PATH_U();
    public static String APPDATA_PATH_D = Config.rutaAPPDATA_PATH_D();
    public static String APPDATA_PATH_A = Config.rutaAPPDATA_PATH_A();
    public static String APPDATA_PATH_TA = Config.rutaAPPDATA_PATH_TA();
    public static String APPDATA_PATH_TNRU = Config.rutaAPPDATA_PATH_TNRU();
    public static String APPDATA_PATH_LOG = Config.rutaAPPDATA_PATH_LOG();


    public static boolean modoInvitado() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("src/main/java/config/config.properties"));
            return Boolean.parseBoolean(properties.getProperty("invitado", "true"));
        } catch (IOException e) {
            return true;
        }

    }

    public static boolean hayDatos() {
        File file = new File(APPDATA_PATH + "/admins");
        if (file == null) return false;
        if (file.list().length == 0) return false;

        File file2 = new File(APPDATA_PATH + "/users");
        if (file.list().length == 0) return false;

        File file3 = new File(APPDATA_PATH + "/drivers");
        if (file.list().length == 0) return false;
        return true;
    }


    public static boolean grabaUsuarios(ArrayList<User> usuarios) {
        for (User u :
                usuarios) {
            if (!PersistenciaDisco.grabaUsuarioUnico(u)) return false;
        }
        return true;
    }

    private static boolean grabaUsuarioUnico(User u) {
        try {
            FileOutputStream fos = new FileOutputStream(APPDATA_PATH_U + "/" + u.getId() + ".dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(u);
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean grabaConductores(ArrayList<Driver> drivers) {
        for (Driver d :
                drivers) {
            if (!PersistenciaDisco.grabaConductorUnico(d)) return false;
        }
        return true;
    }

    private static boolean grabaConductorUnico(Driver d) {
        try {
            FileOutputStream fos = new FileOutputStream(APPDATA_PATH_D + "/" + d.getId() + ".dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(d);
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean grabaAdmins(ArrayList<Admin> admins) {
        for (Admin a :
                admins) {
            if (!PersistenciaDisco.grabaAdminUnico(a)) return false;
        }
        return true;
    }

    private static boolean grabaAdminUnico(Admin a) {
        try {
            FileOutputStream fos = new FileOutputStream(APPDATA_PATH_A + "/" + a.getId() + ".dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(a);
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean grabaShipmentsTA(ArrayList<Shipment> shipments) {
        try {
            FileOutputStream fos = new FileOutputStream(APPDATA_PATH_TA + "/shipmentsTA.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(shipments);
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean grabaShipmentsTNRU(ArrayList<Shipment> shipments) {
        try {
            FileOutputStream fos = new FileOutputStream(APPDATA_PATH_TNRU + "/shipmentsTNRU.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(shipments);
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean grabaDatosApp(ArrayList<Admin> admins, ArrayList<Driver> drivers, ArrayList<User> users,
                                        ArrayList<Shipment> shipmentsTA, ArrayList<Shipment> shipmentsTNRU) {
        if (grabaAdmins(admins) && grabaConductores(drivers) && grabaUsuarios(users) &&
                grabaShipmentsTA(shipmentsTA) && grabaShipmentsTNRU(shipmentsTNRU)) return true;
        return false;
    }

    public static ArrayList<User> leeUsers() {
        ArrayList<User> users = new ArrayList<>();
        File file = new File(APPDATA_PATH_U);
        String[] ficheros = file.list();
        for (int i = 0; i < ficheros.length; i++) {
            users.add(PersistenciaDisco.leeUserUnico(ficheros[i]));
        }
        return users;
    }

    private static User leeUserUnico(String fichero) {
        User temp;
        try {
            FileInputStream fis = new FileInputStream(APPDATA_PATH_U + "/" + fichero);
            ObjectInputStream ois = new ObjectInputStream(fis);
            temp = (User) ois.readObject();
            ois.close();
            return temp;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }


    public static ArrayList<Driver> leeDrivers() {
        ArrayList<Driver> drivers = new ArrayList<>();
        File file = new File(APPDATA_PATH_D);
        String[] ficheros = file.list();
        for (int i = 0; i < ficheros.length; i++) {
            drivers.add(PersistenciaDisco.leeDriverUnico(ficheros[i]));
        }
        return drivers;
    }

    private static Driver leeDriverUnico(String fichero) {
        Driver temp;
        try {
            FileInputStream fis = new FileInputStream(APPDATA_PATH_D + "/" + fichero);
            ObjectInputStream ois = new ObjectInputStream(fis);
            temp = (Driver) ois.readObject();
            ois.close();
            return temp;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static ArrayList<Admin> leeAdmins() {
        ArrayList<Admin> admins = new ArrayList<>();
        File file = new File(APPDATA_PATH_A);
        String[] ficheros = file.list();
        for (int i = 0; i < ficheros.length; i++) {
            admins.add(PersistenciaDisco.leeAdminUnico(ficheros[i]));
        }
        return admins;
    }

    private static Admin leeAdminUnico(String fichero) {
        Admin temp;
        try {
            FileInputStream fis = new FileInputStream(APPDATA_PATH_A + "/" + fichero);
            ObjectInputStream ois = new ObjectInputStream(fis);
            temp = (Admin) ois.readObject();
            ois.close();
            return temp;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static String getUltimoInicioSesion(int id) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("src/main/java/config/config.properties"));
            return properties.getProperty(String.valueOf(id), "PRIMER INICIO DE SESIÓN");
        } catch (IOException e) {
            return "Error";
        }

    }

    public static boolean grabaUltimoInicioSesion(int id) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("src/main/java/config/config.properties"));
            properties.setProperty(String.valueOf(id), Utils.fechaHoraAString(LocalDateTime.now()));
            properties.store(new FileWriter("src/main/java/config/config.properties"),
                    "actualización último incio sesión");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void logInicio(Object persona) {
        String mensaje = "";
        if (persona instanceof User)
            mensaje = "Inicio de sesión;" + ((User) persona).getName() + ";Usuario;" + Utils.fechaHoraAString(LocalDateTime.now());
        if (persona instanceof Driver)
            mensaje = "Inicio de sesión;" + ((Driver) persona).getName() + ";Conductor;" + Utils.fechaHoraAString(LocalDateTime.now());
        if (persona instanceof Admin)
            mensaje = "Inicio de sesión;" + ((Admin) persona).getName() + ";Administrador;" + Utils.fechaHoraAString(LocalDateTime.now());

        if (!mensaje.equals("")) grabaEnLog(mensaje);
    }

    private static void grabaEnLog(String mensaje) {
        try {
            FileWriter fw = new FileWriter(APPDATA_PATH_LOG, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(mensaje + "\n");
            bw.close();
        } catch (IOException e) {
        }
    }

    public static void logSalida(Object persona) {
        String mensaje = "";
        if (persona instanceof User)
            mensaje = "Cierre de sesión;" + ((User) persona).getName() + ";Usuario;" + Utils.fechaHoraAString(LocalDateTime.now());
        if (persona instanceof Driver)
            mensaje = "Cierre de sesión;" + ((Driver) persona).getName() + ";Conductor;" + Utils.fechaHoraAString(LocalDateTime.now());
        if (persona instanceof Admin)
            mensaje = "Cierre de sesión;" + ((Admin) persona).getName() + ";Administrador;" + Utils.fechaHoraAString(LocalDateTime.now());

        if (!mensaje.equals("")) grabaEnLog(mensaje);
    }

    public static void logEnvio(User user, User receptor) {
        String mensaje = "";
        mensaje = "Nuevo envío;" + user.getId() + ";" + receptor.getId() + ";" + Utils.fechaHoraAString(LocalDateTime.now());
        grabaEnLog(mensaje);
    }

    public static void logEnvio(User user, String email) {
        String mensaje;
        mensaje = "Usuario sin registrar;Nuevo envío;" + user.getId() + ";" + email + ";" + Utils.fechaHoraAString(LocalDateTime.now());
        grabaEnLog(mensaje);
    }


    public static void logActualizaEnvio(int id, String estado) {
        String mensaje;
        mensaje = "Actualización envío;" + id + ";" + estado + ";" + Utils.fechaHoraAString(LocalDateTime.now());
        grabaEnLog(mensaje);
    }

    public static String muestraProperties() {
        String properties = "";
        try {
            FileReader fr = new FileReader("src/main/java/config/config.properties");
            BufferedReader br = new BufferedReader(fr);
            String linea = "";
            while (linea != null) {
                linea = br.readLine();
                if (linea != null) properties += linea + "\n";
            }
            br.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

        return properties;
    }

    public static String muestraUltimosIniciosSesion() {
        String resultado = "";
        try {
            FileReader fr = new FileReader(APPDATA_PATH_LOG);
            BufferedReader br = new BufferedReader(fr);
            String linea = "";
            while (linea != null) {
                linea = br.readLine();
                if (linea != null && linea.contains("Inicio de sesión")) resultado += linea + "\n";
            }
            br.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

        return resultado;
    }

    public static boolean creaBackup(AppController controller, String ruta) {
        try {
            FileOutputStream fos = new FileOutputStream(ruta + "\\backup.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(controller);
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public static ArrayList<Shipment> leeShipmentsTA() {
        ArrayList<Shipment> shipments;
        try {
            FileInputStream fis = new FileInputStream(APPDATA_PATH_TA + "/shipmentsTA.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            shipments = (ArrayList<Shipment>) ois.readObject();
            ois.close();
            return shipments;
        } catch (FileNotFoundException e) {
            return shipments = new ArrayList<>();
        } catch (IOException e) {
            return shipments = new ArrayList<>();
        } catch (ClassNotFoundException e) {
            return shipments = new ArrayList<>();
        }
    }

    public static ArrayList<Shipment> leeShipmentsTNRU() {
        ArrayList<Shipment> shipments;
        try {
            FileInputStream fis = new FileInputStream(APPDATA_PATH_TNRU + "/shipmentsTNRU.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            shipments = (ArrayList<Shipment>) ois.readObject();
            ois.close();
            return shipments;
        } catch (FileNotFoundException e) {
            return shipments = new ArrayList<>();
        } catch (IOException e) {
            return shipments = new ArrayList<>();
        } catch (ClassNotFoundException e) {
            return shipments = new ArrayList<>();
        }
    }
}
