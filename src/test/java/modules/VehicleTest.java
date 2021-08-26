package modules;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {
    Vehicle vehicle;
    @BeforeAll
     static void beforeAll(){
       DB.sql2o= new Sql2o("jdbc:postgresql://localhost:5432/packing_db_test","softwaredev","1234");
    }
    @BeforeEach
    void beforeEach(){
        vehicle = new Vehicle("car", "Toyota v8", "KCB 586B");
    }
    @AfterEach
    void afterEach(){
        try(Connection con = DB.sql2o.open()){
            String clearAll= "DELETE FROM vehicles *";
            con.createQuery(clearAll).executeUpdate();
        }
    }
 @Test
     void checkInstanceOfVehicleCorrectly_true(){
        assertTrue(vehicle instanceof Vehicle);
 }
  @Test
    void checkInstanceOfGetters(){
        assertEquals(vehicle.getType(), "car");
        assertEquals(vehicle.getModel(), "Toyota v8");
        assertEquals(vehicle.getNumberPlate(),"KCB 586B");

  }
  @Test
    void checkInstanceOfSetters(){
        vehicle.setModel("Hino");
        vehicle.setNumberPlate("KBA 548C");
        vehicle.setType("Lorry");
      assertEquals(vehicle.getType(), "Lorry");
      assertEquals(vehicle.getModel(), "Hino");
      assertEquals(vehicle.getNumberPlate(),"KBA 548C");
  }
  @Test
    void test_instanceOfSave_true(){
        vehicle.save();
        assertEquals(Vehicle.getAllVehicles().get(0).getId(),vehicle.getId());
  }
  @Test
    void test_instanceOfCheckout_true(){
        vehicle.save();
      vehicle.checkout(true);
      assertEquals(Vehicle.getAllVehicles().get(0).isIs_checkout(),true);

  }
  @Test
    void test_instanceOfFindById(){
        vehicle.save();
        assertEquals(Vehicle.findById(vehicle.getId()).getId(), vehicle.getId());
  }
  @Test
    void testInstanceOfFindByType(){
        vehicle.save();
        Vehicle vehicle1= new Vehicle("Lorry", "Hino", "KBC 098B");
        vehicle1.save();
        assertEquals(Vehicle.findByType("Lorry").get(0).getId(), vehicle1.getId());
  }
  @Test
    void testInstanceOfDeleteById(){
      vehicle.save();
      Vehicle vehicle1= new Vehicle("Lorry", "Hino", "KBC 098B");
      vehicle1.save();
      Vehicle.deleteSingleInstance(vehicle1.getId());
      assertEquals(Vehicle.getAllVehicles().size(), 1);
  }
  @Test
    void testInstanceOfClearAll(){
      vehicle.save();
      Vehicle vehicle1= new Vehicle("Lorry", "Hino", "KBC 098B");
      vehicle1.save();
      Vehicle.clearAll();
      assertEquals(Vehicle.getAllVehicles().size(),0);
  }

}