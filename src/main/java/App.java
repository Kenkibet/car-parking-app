import com.google.gson.Gson;
import exceptions.ApiException;
import modules.Vehicle;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;
public class App {
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
 public static void main(String[] args){
     port(getHerokuAssignedPort());

     Gson gson = new Gson();
     options("/*", (request, response) -> {

         String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
         if (accessControlRequestHeaders != null) {
             response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
         }

         String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
         if (accessControlRequestMethod != null) {
             response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
         }
         return "OK";
     });

     before((request, response) -> {
         response.header("Access-Control-Allow-Origin", "*");
     });
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
     get("/api/packVehicle/find/:vehicleId","application/json",(request, response) -> {
         int vehicleId= Integer.parseInt(request.params("vehicleId"));
         return gson.toJson(Vehicle.findById(vehicleId));

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
     exception(ApiException.class, (exc, req, res) -> {
         ApiException err = (ApiException) exc;
         Map<String, Object> jsonMap = new HashMap<>();
         jsonMap.put("status", err.getStatusCode());
         jsonMap.put("errorMessage", err.getMessage());
         res.type("application/json"); //after does not run in case of an exception.
         res.status(err.getStatusCode()); //set the status
         res.body(gson.toJson(jsonMap));  //set the output.
     });
 }

}
