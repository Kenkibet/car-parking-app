package modules;

import org.sql2o.Connection;
import org.sql2o.Sql2oException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class Vehicle {
    private String type;
    private String numberPlate;
    private String  model;
    private Timestamp checkin;
    private Timestamp checkout;
    private boolean is_checkout;
    private int id;
    public Vehicle(String type, String model, String numberPlate){
        this.type= type;
        this.numberPlate= numberPlate;
        this.model = model;
        this.is_checkout= false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return is_checkout == vehicle.is_checkout && id == vehicle.id && Objects.equals(type, vehicle.type) && Objects.equals(numberPlate, vehicle.numberPlate) && Objects.equals(model, vehicle.model) && Objects.equals(checkin, vehicle.checkin) && Objects.equals(checkout, vehicle.checkout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, numberPlate, model, checkin, checkout, is_checkout, id);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Timestamp getCheck_in() {
        return checkin;
    }



    public Timestamp getCheck_out() {
        return checkout;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIs_checkout() {
        return is_checkout;
    }

    public void setIs_checkout(boolean is_checkout) {
        this.is_checkout = is_checkout;
    }

    public void save(){
        try(Connection con= DB.sql2o.open()){
            String save= "INSERT INTO vehicles (type, model, numberPlate,  checkin, checkout, is_checkout) VALUES (:type, :model, :numberplate ,now(), now(), :is_checkout)";
            this.id= (int) con.createQuery(save,true)
                    .addParameter("type", this.type)
                    .addParameter("model", this.model)
                    .addParameter("numberplate", this.numberPlate)
                    .addParameter("is_checkout", this.is_checkout)
                    .executeUpdate()
                    .getKey();
        } catch(Sql2oException err){
            System.out.println("error::: "+ err);
        }
    }

    public void checkout(boolean isTrue){
        try(Connection con= DB.sql2o.open()){
            String checkout= "UPDATE vehicles SET checkout= now(), is_checkout= :is_checkout WHERE id = :id";
            con.createQuery(checkout)
                    .addParameter("is_checkout", isTrue)
                    .addParameter("id", this.id)
                    .executeUpdate();
            this.is_checkout=true;
        }
    }
     public static void deleteSingleInstance(int id){
        try(Connection con= DB.sql2o.open()){
            String delete= "DELETE FROM vehicles WHERE id=:id";
            con.createQuery(delete).addParameter("id",id).executeUpdate();
        }
    }
    public static List<Vehicle> findByType(String type){
        try(Connection con= DB.sql2o.open()){
            String vehType = "SELECT * FROM vehicles WHERE type = :type";
           return con.createQuery(vehType).addParameter("type",type).executeAndFetch(Vehicle.class);
        }
    }
    public static Vehicle findById(int id){
        try(Connection con = DB.sql2o.open()){
            String findbyid = "SELECT * FROM vehicles WHERE id = :id";
           return con.createQuery(findbyid).addParameter("id",id).executeAndFetchFirst(Vehicle.class);
        }
    }
    public static void clearAll(){
        try(Connection con= DB.sql2o.open()){
            String clearAll= "DELETE FROM vehicles *";
            con.createQuery(clearAll).executeUpdate();
        }
    }
    public static List<Vehicle> getAllVehicles(){
        try(Connection con = DB.sql2o.open()){
            String getAll="SELECT * FROM vehicles";
           return con.createQuery(getAll).executeAndFetch(Vehicle.class);
        }
    }

}
