package view;

import appController.AppController;
import comunicaciones.Comunicaciones;
import data.Estado;
import data.Menus;
import data.PlantillasEmail;
import dataClass.InfoShipmentDataClass;
import models.*;
import persistencia.PersistenciaDisco;
import utils.Utils;

import java.util.Scanner;


public class Main {
    public static final Scanner S = new Scanner(System.in);

    public static void main(String[] args) {
        AppController controller = new AppController();
        //controller.mock();
        //guardaDatos(controller);


        boolean on = true;
        do {
            System.out.println(Menus.LOGO);
            if (controller.isInvitado()) System.out.println(Menus.LOGIN_MENU);
            else System.out.println(Menus.LOGIN_MENU_INVITADO);
            switch (S.nextLine()) {
                case "1":
                    login(controller);
                    break;
                case "2":
                    registro(controller);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "3":
                    if (controller.isInvitado()) seguimientoPaqueteSinRegistro(controller);
                    else System.out.println("Opción introducida incorrecta");
                    Utils.pulsaParaCuntinuar();
                    break;
                default:
                    System.out.println("Opción introducida incorrecta");
                    break;
            }

        } while (on);
    }

    private static void seguimientoPaqueteSinRegistro(AppController controller) {
        String entrada, resultado = "";
        InfoShipmentDataClass dataClass;
        do {
            System.out.println("Introduce el numero de sequimiento");
            entrada = S.nextLine();
        } while (!Utils.validaEntradaNumero(entrada));
        int idSeguimiento = Integer.parseInt(entrada);
        Shipment s = controller.searchShipmentByIdGlobal(idSeguimiento);
        if (s == null) System.out.println("No hay ningun paquete con ese numero de referencia");
        else {
            if (s.getEmailUserNoRegister().equals("")) {
                dataClass = new InfoShipmentDataClass(s.getId(), s.getCreateDate(), s.getExpectDate(),
                        s.getDeliveryDate(), s.getPostalCode(), s.getStatus(), controller.searchUserById(s.getIdSender()).getName(),
                        controller.searchUserById(s.getIdReceiver()).getName(), s.getAddress(), s.getCity());
                resultado += dataClass + "\n";
            } else {
                dataClass = new InfoShipmentDataClass(s.getId(), s.getCreateDate(), s.getExpectDate(),
                        s.getDeliveryDate(), s.getPostalCode(), s.getStatus(), controller.searchUserById(s.getIdSender()).getName(),
                        s.getEmailUserNoRegister(), s.getAddress(), s.getCity());
                resultado += dataClass + "\n";
            }
            System.out.println(resultado);
        }
    }


    private static void registro(AppController controller) {
        String entrada;

        System.out.println("Introduce tu email: ");
        String email = S.nextLine();

        if (controller.searchUserByEmail(email) != null || controller.searchDriverByEmail(email) != null
                || controller.searchAdminByEmail(email) != null) {
            System.out.println("Este correo ya está registrado");
            Utils.pulsaParaCuntinuar();
        } else {
            System.out.println("Introduce tu nombre: ");
            String name = S.nextLine();
            System.out.println("Introduce tu apellido: ");
            String surname = S.nextLine();

            do {
                System.out.println("Introduce tu teléfono: ");
                entrada = S.nextLine();
            } while (!Utils.validaEntradaNumero(entrada));
            int phone = Integer.parseInt(entrada);

            System.out.println("Introduce tu estado: ");
            String state = S.nextLine();
            System.out.println("Introduce tu ciudad: ");
            String city = S.nextLine();
            System.out.println("Introduce tu calle: ");
            String street = S.nextLine();

            do {
                System.out.println("Introduce tu número de domicilio: ");
                entrada = S.nextLine();
            } while (!Utils.validaEntradaNumero(entrada));
            int streetNum = Integer.parseInt(entrada);

            do {
                System.out.println("Introduce tu código postal: ");
                entrada = S.nextLine();
            } while (!Utils.validaEntradaNumero(entrada));
            int postalCode = Integer.parseInt(entrada);

            System.out.println("Introduce tu contraseña: ");
            String pass = S.nextLine();
            System.out.println("Enviando correo de verificacion, se le avisara cuando este enviado");
            validaToken(email, name);

            controller.addUser(name, surname, email, phone, pass, street, streetNum, city, state, postalCode);

            System.out.println("Usuario registrado correctamente");

            guardaDatos(controller);
        }
    }

    private static void guardaDatos(AppController controller) {
        System.out.println("Guardando datos...");
        if (PersistenciaDisco.grabaDatosApp(controller.getAdmins(), controller.getDrivers(), controller.getUsers(),
                controller.getShipmentsToAssign(), controller.getShipmentsToNoRegisterUsers()))
            System.out.println("Datos guardados correctamente");
        else System.out.println("Error al guardar");
    }

    public static int generaNumAleatorio() {
        return (int) (Math.random() * 1000000);
    }

    public static void validaToken(String email, String nombre) {
        int codigoValidar = -1;
        int cont = 0;
        int codigo = generaNumAleatorio();
        boolean datoCorrecto;


        if (Comunicaciones.enviarMensajeSinFactura(email, "Verificación de email", PlantillasEmail.plantillaEmail(nombre.toUpperCase(), codigo)))
            System.out.println("Correo enviado");
        else System.out.println("Error al enviar correo");

        do {
            if (cont != 0) System.out.println("El codigo es erroneo");
            do {
                datoCorrecto = true;
                try {
                    System.out.print("Introduce el código que hemos enviado a tu email: ");
                    codigoValidar = Integer.parseInt(S.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Debes introducir un número");
                    datoCorrecto = false;
                    Utils.pulsaParaCuntinuar();
                }
            } while (!datoCorrecto);
            cont++;
        } while (codigo != codigoValidar);

        System.out.println("Correo validado");
    }

    public static void login(AppController controller) {
        System.out.print("Introduce tu email: ");
        String email = S.nextLine();
        System.out.print("Introduce tu contraseña: ");
        String pass = S.nextLine();

        Object persona = controller.login(email, pass);
        if (persona == null) System.out.println("Usuario no registrado o datos introducidos incorrectos");
            // TODO: 24/04/2024
        else if (persona instanceof User) userMenu(controller, (User) persona);
        else if (persona instanceof Driver) driverMenu(controller, (Driver) persona);
        else if (persona instanceof Admin) adminMenu(controller, (Admin) persona);
    }

    public static void userMenu(AppController controller, User user) {
        PersistenciaDisco.logInicio(user);
        String fechaUltimaSesion = PersistenciaDisco.getUltimoInicioSesion(user.getId());
        PersistenciaDisco.grabaUltimoInicioSesion(user.getId());
        String op;
        boolean cierraSesion = false;
        do {
            System.out.println(Menus.userMenu(user.getName(), user.numDeliveriesPendingToDeliver(), fechaUltimaSesion));
            op = S.nextLine();
            switch (op) {
                case "1":
                    realizaEnvio(controller, user);
                    guardaDatos(controller);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "2":
                    enviosReceiver(controller, user);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "3":
                    modificaDatosEntregaEnvio(controller, user);
                    guardaDatos(controller);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "4":
                    enviosSender(controller, user);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "5":
                    pintaPerfilUsuario(user);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "6":
                    modificaDatos(controller, user);
                    guardaDatos(controller);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "7":
                    cierraSesion = cierraSesion();
                    PersistenciaDisco.logSalida(user);
                    break;
                default:
                    System.out.println("Opción introducida incorrecta");
                    Utils.pulsaParaCuntinuar();
            }
        } while (!cierraSesion);
    }

    private static void enviosReceiver(AppController controller, User user) {
        String resultado = controller.enviosReceiver(user);
        if (resultado.equals("")) System.out.println("No se te ha enviado ningun paquete");
        else {
            System.out.println(resultado);
        }

    }

    private static void enviosSender(AppController controller, User user) {
        String resultado = controller.enviosSender(user);
        if (resultado.equals("")) System.out.println("No has enviado ningun paquete");
        else {
            System.out.println(resultado);
        }

    }

    private static void modificaDatosEntregaEnvio(AppController controller, User user) {
        String entrada;
        int op;
        String newAddress;
        System.out.println(controller.pintaEnviosUsuariosAddress(user));
        do {
            System.out.println("¿A qué envío quieres modificarle los datos de entrega?");
            entrada = S.nextLine();
        } while (!Utils.validaEntradaNumero(entrada));
        op = Integer.parseInt(entrada);
        System.out.println("Introduce la nueva dirección: ");
        newAddress = S.nextLine();
        controller.modificaDatosEnvios(user, op, newAddress);
    }

    private static void modificaDatos(AppController controller, User user) {
        int op;
        String dato = "";
        System.out.println("""
                ¿Qué dato quieres modificar?
                1.  Nombre
                2.  Apellido
                3.  Email
                4.  Contraseña
                5.  Teléfono
                6.  Calle
                7.  Número
                8.  Ciudad
                9.  Estado
                10. Código postal
                11. Salir
                                
                Introduce una opción: """);
        op = Integer.parseInt(S.nextLine());
        if (op < 11) {
            System.out.println("Introduce el nuevo dato: ");
            dato = S.nextLine();
        }
        if (controller.modificaDatos(op, dato, user)) System.out.println("Dato modificado con éxito");
        else Utils.saliendo();
    }

    private static void pintaPerfilUsuario(User user) {
        System.out.println(user);
    }

    private static void realizaEnvio(AppController controller, User user) {
        String email, entrada;
        User receptor;

        System.out.print("Dime el email a quien le quieres enviar el paquete: ");
        email = S.nextLine();
        receptor = controller.searchUserByEmail(email);
        if (receptor == null) {
            System.out.println("Usuario no encontrado en la base de datos");
            System.out.println("¿Quieres enviarle un paquete a un usuario no registrado? (Este usuario vera menos informacion del paquete) SI/NO");
            entrada = S.nextLine();
            if (respuestasSioNO(entrada) == 0) System.out.println("Respuesta erronea");
            if (respuestasSioNO(entrada) == -1) System.out.println("No se enviara ningun paquete");
            if (respuestasSioNO(entrada) == 1) {
                String nombre, direccion;
                int resultado, codPostal, cont = 0;
                boolean notificacion = true;
                System.out.print("Dime el nombre del destinatario: ");
                nombre = S.nextLine();

                System.out.print("Dime la ciudad: ");
                String ciudad = S.nextLine();

                do {
                    System.out.print("Dime el codigo postal del destinatario: ");
                    entrada = S.nextLine();
                } while (!Utils.validaEntradaNumero(entrada));
                codPostal = Integer.parseInt(entrada);

                System.out.print("Dime la direccion del destinatario :");
                direccion = S.nextLine();

                do {
                    if (cont > 0) System.out.println("Las respiuesta solo puede ser SI / NO");
                    System.out.print("¿Quiere recibir notificaciones? (SI/NO): ");
                    entrada = S.nextLine();
                    resultado = respuestasSioNO(entrada);
                    if (resultado == 1) notificacion = true;
                    if (resultado == -1) notificacion = false;
                    cont++;
                } while (resultado == 0);
                controller.addShipmentToNoRegisterUsers(user.getId(), email, nombre, notificacion, codPostal, direccion, ciudad);
                PersistenciaDisco.logEnvio(user, email);
            }
        } else {
            int resultado, cont = 0;
            boolean notificacion = true;
            do {
                if (cont > 0) System.out.println("Las respiuesta slo puede ser SI / NO");
                System.out.print("¿Quiere recibir notificaciones? (SI/NO): ");
                entrada = S.nextLine();
                resultado = respuestasSioNO(entrada);
                if (resultado == 1) notificacion = true;
                if (resultado == -1) notificacion = false;
                cont++;
            } while (resultado == 0);
            System.out.println("Enviando correo...");
            controller.addShipment(user.getId(), receptor.getId(), notificacion);
            PersistenciaDisco.logEnvio(user, receptor);
        }
    }

    private static int respuestasSioNO(String respuesta) {
        if (respuesta.equalsIgnoreCase("SI")) return 1;
        if (respuesta.equalsIgnoreCase("NO")) return -1;
        return 0;
    }

    public static boolean cierraSesion() {
        char respuesta;
        System.out.println("¿Está seguro de cerrar la sesión? (Y/N)");
        respuesta = S.nextLine().toUpperCase().charAt(0);
        if (respuesta == 'Y') {
            Utils.cierraSesion();
            return true;
        } else {
            System.out.println("El programa sigue en ejecución");
            Utils.pulsaParaCuntinuar();
        }
        return false;
    }

    public static void driverMenu(AppController controller, Driver driver) {
        PersistenciaDisco.logInicio(driver);
        String op;
        boolean cierraSesion = false;
        do {
            System.out.println(Menus.driverMenu(driver.getName(), driver.numShipmentsPending()));
            op = S.nextLine();
            switch (op) {
                case "1":
                    pintaEnviosConductor(controller, driver);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "2":
                    cambiaEstadoEnvio(controller, driver);
                    guardaDatos(controller);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "3":
                    verHistoricoPaquetes(controller, driver);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "4":
                    addZonaEntrega(controller, driver);
                    guardaDatos(controller);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "5":
                    pintaPerfilDriver(driver);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "6":
                    modificaDatosDriver(controller, driver);
                    guardaDatos(controller);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "7":
                    cierraSesion = cierraSesion();
                    PersistenciaDisco.logSalida(driver);
                    break;
                default:
                    System.out.println("Opción introducida incorrecta");
                    Utils.pulsaParaCuntinuar();
            }
        } while (!cierraSesion);
    }

    private static void addZonaEntrega(AppController controller, Driver driver) {
        String entrada;
        int zona;
        do {
            System.out.println("Introduce la nueva zona de entrega: ");
            entrada = S.nextLine();
        } while (!Utils.validaEntradaNumero(entrada));
        zona = Integer.parseInt(entrada);
        if (!controller.addZonaEntrega(driver, zona)) System.out.println("Ya tienes esa zona de entrega");
        else System.out.println("Zona de entrega añadida correctamente");
    }

    private static void verHistoricoPaquetes(AppController controller, Driver driver) {
        String resultado = controller.historicoPaquetes(driver);
        if (resultado.equals("")) System.out.println("No tienes ningun paquete, trabaja más");
        else System.out.println(resultado);
    }

    private static void cambiaEstadoEnvio(AppController controller, Driver driver) {
        String estado = "";
        String entrada;
        if (driver.getShipments().isEmpty()) {
            System.out.println("No tienes ningún envío asignado");
        } else {
            do {
                System.out.println("¿A qué envío quieres modificarle el estado?");
                System.out.println(controller.pintaEnviosLista(driver));
                System.out.println("Introduce su id: ");
                entrada = S.nextLine();
            } while (!Utils.validaEntradaNumero(entrada));
            int id = Integer.parseInt(entrada);
            if (!driver.envioExiste(id)) System.out.println("Ese envío no existe");
            else {
                System.out.println("""
                        1. En almacén
                        2. En reparto
                        3. Entregado
                                        
                        Introduce el estado: """);
                switch (S.nextLine()) {
                    case "1":
                        estado = Estado.EN_ALMACEN;
                        if (!controller.modificaEstadoEnvios(driver, id, estado))
                            System.out.println("No se pudo cambiar el estado");
                        PersistenciaDisco.logActualizaEnvio(id, estado);
                        break;
                    case "2":
                        estado = Estado.EN_REPARTO;
                        System.out.println("Enviando correo");
                        controller.modificaEstadoEnvios(driver, id, estado);
                        PersistenciaDisco.logActualizaEnvio(id, estado);
                        break;
                    case "3":
                        estado = Estado.ENTREGADO;
                        System.out.println("Enviando correo");
                        controller.entregaEnvio(driver, id, estado);
                        PersistenciaDisco.logActualizaEnvio(id, estado);
                        break;
                    default:
                        System.out.println("Opción incorrecta");
                }
            }
        }
    }

    private static void pintaEnviosConductor(AppController controller, Driver driver) {
        System.out.println(controller.pintaEnviosDriverNoEntregados(driver));
    }

    private static void modificaDatosDriver(AppController controller, Driver driver) {
        int op;
        String dato = "";
        System.out.println("""
                ¿Qué dato quieres modificar?
                1.  Nombre
                2.  Contraseña
                3.  Email     
                Introduce una opción: """);
        op = Integer.parseInt(S.nextLine());
        if (op < 4) {
            System.out.println("Introduce el nuevo dato: ");
            dato = S.nextLine();
        }
        if (controller.modificaDatosDriver(op, dato, driver)) System.out.println("Dato modificado con éxito");
        else Utils.saliendo();
    }

    private static void pintaPerfilDriver(Driver driver) {
        System.out.println(driver);
        System.out.println("Zonas de envío:");
        System.out.println(driver.pintaZonasEnvio());
    }

    public static void adminMenu(AppController controller, Admin admin) {
        PersistenciaDisco.logInicio(admin);
        String op;
        boolean cierraSesion = false;
        do {
            System.out.println(Menus.adminMenu(admin.getName(), controller.numUsers(), controller.numDrivers(),
                    controller.numShipmentsPending(), controller.numShipmentsNoDriver(),
                    controller.numShipmentsUserNoRegister(), controller.mediaDays()));
            op = S.nextLine();
            switch (op) {
                case "1":
                    pintaEnviosSinAsignar(controller);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "2":
                    asignaEnvioConductor(controller);
                    guardaDatos(controller);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "3":
                    resumenUsers(controller);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "4":
                    resumenDrivers(controller);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "5":
                    pintaPerfilAdmin(admin);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "6":
                    modificaDatosAdmin(controller, admin);
                    guardaDatos(controller);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "7":
                    addConductor(controller);
                    guardaDatos(controller);
                    break;
                case "8":
                    muestraInfoPrograma();
                    Utils.pulsaParaCuntinuar();
                    break;
                case "9":
                    enviaExcelPorCorreo(controller);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "10":
                    realizaBackup(controller);
                    Utils.pulsaParaCuntinuar();
                    break;
                case "11":
                    cierraSesion = cierraSesion();
                    PersistenciaDisco.logSalida(admin);
                    break;
                default:
                    System.out.println("Opción introducida incorrecta");
                    Utils.pulsaParaCuntinuar();
            }
        } while (!cierraSesion);
    }

    private static void enviaExcelPorCorreo(AppController controller) {
        System.out.println("Enviando correo");
        controller.enviaExcel();
        System.out.println("Correo enviado correctamente");
    }

    private static void realizaBackup(AppController controller) {
        System.out.println("¿Dónde quieres realizar el backup? Indica la ruta absoluta");
        System.out.println("Ejemplo: C:\\java\\FernanPaaq_P6");
        String ruta = S.nextLine();
        if (PersistenciaDisco.creaBackup(controller, ruta)) System.out.println("Copia realizada correctamente");
        else System.out.println("Error al realizar la copia");

    }

    private static void muestraInfoPrograma() {
        System.out.println(PersistenciaDisco.muestraProperties());
    }

    private static void addConductor(AppController controller) {
        String entrada;

        System.out.println("Introduce el email: ");
        String email = S.nextLine();
        if (controller.searchUserByEmail(email) != null || controller.searchDriverByEmail(email) != null
                || controller.searchAdminByEmail(email) != null) {
            System.out.println("Este correo ya está registrado");
            Utils.pulsaParaCuntinuar();
        } else {
            System.out.println("Introduce el nombre: ");
            String name = S.nextLine();

            System.out.println("Introduce la contraseña: ");
            String pass = S.nextLine();

            do {
                System.out.println("Introduce una zona de envío: ");
                entrada = S.nextLine();
            } while (!Utils.validaEntradaNumero(entrada));
            int deliveryZone = Integer.parseInt(entrada);

            controller.addDriver(name, email, pass, deliveryZone);
        }
    }

    private static void pintaEnviosSinAsignar(AppController controller) {
        System.out.println(controller.pintaEnviosSinAsignar());
    }

    private static void resumenDrivers(AppController controller) {
        System.out.println(controller.resumenDrivers());
    }

    private static void resumenUsers(AppController controller) {
        System.out.println(controller.resumenUsers());
    }

    private static void asignaEnvioConductor(AppController controller) {
        int opEnvio;
        int opDriver;
        String entrada;

        do {
            System.out.println(controller.pintaEnviosPorAsignar());
            System.out.println("Selecciona un envío: ");
            entrada = S.nextLine();
        } while (!Utils.validaEntradaNumero(entrada));
        opEnvio = Integer.parseInt(entrada);


        do {
            System.out.println(controller.pintaConductores());
            System.out.println("Selecciona un conductor: ");
            entrada = S.nextLine();
        } while (!Utils.validaEntradaNumero(entrada));
        opDriver = Integer.parseInt(entrada);

        if (controller.asignaEnvioConductor(opEnvio, opDriver)) {
            System.out.println("Envío asignado correctamente");
        } else System.out.println("Ha habido un error");
    }

    private static void pintaPerfilAdmin(Admin admin) {
        System.out.println(admin);
    }

    private static void modificaDatosAdmin(AppController controller, Admin admin) {
        int op;
        String dato = "";
        System.out.println("""
                ¿Qué dato quieres modificar?
                1.  Nombre
                2.  Contraseña
                3.  Email     
                Introduce una opción: """);
        op = Integer.parseInt(S.nextLine());
        if (op < 4) {
            System.out.println("Introduce el nuevo dato: ");
            dato = S.nextLine();
        }
        if (controller.modificaDatosAdmin(op, dato, admin)) System.out.println("Dato modificado con éxito");
        else Utils.saliendo();
    }
}
