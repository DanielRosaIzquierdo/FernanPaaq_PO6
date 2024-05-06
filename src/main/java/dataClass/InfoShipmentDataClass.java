package dataClass;

import java.time.LocalDate;

public class InfoShipmentDataClass {
    private int id;
    private LocalDate createDate;
    private LocalDate expectDate;
    private LocalDate deliveryDate;
    private int postalCode;
    private String status;
    private String sender;
    private String receiver;
    private String address;
    private String city;

    public InfoShipmentDataClass(int id, LocalDate createDate, LocalDate expectDate, LocalDate deliveryDate, int postalCode, String status, String sender, String receiver, String address, String city) {
        this.id = id;
        this.createDate = createDate;
        this.expectDate = expectDate;
        this.deliveryDate = deliveryDate;
        this.postalCode = postalCode;
        this.status = status;
        this.sender = sender;
        this.receiver = receiver;
        this.address = address;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getExpectDate() {
        return expectDate;
    }

    public void setExpectDate(LocalDate expectDate) {
        this.expectDate = expectDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        if(deliveryDate != null){
            return String.format("""
                 * =============================================== *
                | Id:%46d|
                | Fecha de creación:%31s|
                | Fecha estimada:%34s|
                | Fecha de entrega:%32s|
                | Código postal:%35s|
                | Estado:%42s|
                | Remitente:%39s|
                | Destinatario:%36s|
                | Dirección:%39s|
                | Ciudad:%42s|
                * ================================================ *
                """, id, createDate, expectDate, deliveryDate, postalCode, status, sender, receiver, address, city);
        }
           return String.format("""
                 * =============================================== *
                | Id:%46d|
                | Fecha de creación:%31s|
                | Fecha estimada:%34s|
                | Fecha de entrega:                    Sin entregar|
                | Código postal:%35s|
                | Estado:%42s|
                | Remitente:%39s|
                | Destinatario:%36s|
                | Dirección:%39s|
                | Ciudad:%42s|
                * ================================================ *
                """, id, createDate, expectDate, postalCode, status, sender, receiver, address, city);
    }

}
