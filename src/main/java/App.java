import com.google.gson.Gson;
import modules.Vehicle;

import static spark.Spark.*;
public class App {
 public static void main(String[] args){
     Gson gson = new Gson();
     //Add vehicles in the packing lot
     post("/api/packVehicle/new", "application/json", (request, response) -> {
         Vehicle vehicle = gson.fromJson(request.body(),Vehicle.class);
         vehicle.save();
         response.status(201);
         return gson.toJson(vehicle);
     });
     //get list of all vehicles
     get("/api/packVehicle/all", "application/json", (request, response) -> {
         return gson.toJson(Vehicle.getAllVehicles());
     });
     //get vehicle types
     get("/api/packVehicle/type", "application/json", (request, response) -> {
         String vehicleType= request.queryParams("name");
         System.out.println(vehicleType);
         return gson.toJson(Vehicle.findByType(vehicleType));
     });
     //checkout
     post("/api/packVehicle/checkout/:vehicleId", "application/json", (request, response) -> {
         int vehicleId= Integer.parseInt(request.params("vehicleId"));
         Vehicle.findById(vehicleId).checkout(true);
         response.status(201);
         return gson.toJson(Vehicle.getAllVehicles());

     });
     //remove vehicle from database
     post("/api/packVehicle/delete/:vehicleId", "application/json", (request, response) -> {
         int vehicleId= Integer.parseInt(request.params("vehicleId"));
         Vehicle.deleteSingleInstance(vehicleId);
         response.status(201);
         return gson.toJson(Vehicle.getAllVehicles());
     });
     //clear All vehicles
     post("/api/packVehicle/clearall", "application/json", (request, response) -> {
         Vehicle.clearAll();
         response.status(201);
         return gson.toJson(Vehicle.getAllVehicles());
     });
     //FILTERS
     after((req, res) ->{
         res.type("application/json");
     });
 }

}
