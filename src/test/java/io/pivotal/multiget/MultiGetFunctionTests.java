
package io.pivotal.multiget;

import io.pivotal.domain.Trade;
import io.pivotal.domain.TradeKey;
import io.pivotal.multiget.function.MultiGetFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.FunctionException;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.cache.execute.ResultCollector;


public class MultiGetFunctionTests {

  private static TradeKey key1 = new TradeKey("1", 161231);
  private static TradeKey key2 = new TradeKey("2", 161231);
  private static TradeKey key3 = new TradeKey("3", 161231);
  private static TradeKey key4 = new TradeKey("4", 161231);
  private static TradeKey key5 = new TradeKey("5", 161231);
  private static TradeKey key6 = new TradeKey("6", 161231);

  private static ClientCache clientCache = null;

  @BeforeClass
  public static void initCache() {
    ClientCacheFactory ccf = new ClientCacheFactory();
    ccf.set("cache-xml-file", "./target/classes/config/clientCache.xml");
    System.out.println(new File(".").getAbsolutePath());
    clientCache = ccf.create();
    
 }
  public static void main(String[] args) throws Exception {
    MultiGetFunctionTests multiGetFunctionTests = new MultiGetFunctionTests();
    multiGetFunctionTests.testFunctionGets();
  }
  
  @Test
  public void testFunctionGets() {
    
    Region<TradeKey, Trade> tradeRegion = clientCache.getRegion("Trades");
    if (tradeRegion == null) {
      System.out.println("[" + tradeRegion + "] The region " + "Trades"
          + " could not be created in the cache.");
      return;
    }

      populateTradeRegion(tradeRegion);
      
      try {
        doFunctionExecute(tradeRegion);
      } catch (Exception e) {
        e.printStackTrace();
        Assert.fail();
      }
  
    System.out.println("\nMultiuserSecurityClient closed.");
    System.out.println("-----------------------------------------------------");
  }

   private void doFunctionExecute(Region<TradeKey, Trade> tradeRegion) throws Exception {
    try {
      MultiGetFunction function = new MultiGetFunction();

      Set<TradeKey> keysForGet = new HashSet<TradeKey>();
      keysForGet.add(key1);
      keysForGet.add(new TradeKey("2", 161231));
      keysForGet.add(new TradeKey("3", 161231));

      ResultCollector<?, ?> rc =  FunctionService
          .onRegion(tradeRegion)
          .withFilter(keysForGet)
          .execute(MultiGetFunction.ID);
    
    Object result = rc.getResult();
    if (!(result instanceof List<?>)) {
      Assert.fail("Something other than a List was returned");
    }
    
    List<Object> availabilitiesResults = (List<Object>) result;
    Assert.assertTrue("must not be empty", !availabilitiesResults.isEmpty());
    if (!(availabilitiesResults.get(0) instanceof String)) {
      System.out.println(availabilitiesResults.get(0));
      Assert.fail("Something other than a String was returned");
    }
    for (Object statusObject : availabilitiesResults) {
      String status = (String) statusObject;
      System.out.println(status);
      Assert.assertFalse("An error was returned" + "\n" + status, status.equalsIgnoreCase("null"));
    }
    }
    catch (FunctionException ex) {
       ex.printStackTrace();
     }
    finally {
   
    tradeRegion.close();
    }
  }
   
   private void populateTradeRegion(Region<TradeKey, Trade> tradeRegion) {
     tradeRegion.put(key1, new Trade(key1.getTradeId(), key1.getTradeDate()));
     tradeRegion.put(key2, new Trade(key2.getTradeId(), key2.getTradeDate()));
     tradeRegion.put(key3, new Trade(key3.getTradeId(), key3.getTradeDate()));
     tradeRegion.put(key4, new Trade(key4.getTradeId(), key4.getTradeDate()));
     tradeRegion.put(key5, new Trade(key5.getTradeId(), key5.getTradeDate()));
     tradeRegion.put(key6, new Trade(key6.getTradeId(), key6.getTradeDate()));
   }
  
  public static void pressEnterToContinue() throws IOException {
    System.out.println("Press Enter to continue.");
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    bufferedReader.readLine();
  }
}
