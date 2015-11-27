package io.pivotal.multiget.function;

import io.pivotal.domain.Trade;
import io.pivotal.domain.TradeKey;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.FunctionAdapter;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.cache.execute.FunctionException;
import com.gemstone.gemfire.cache.execute.RegionFunctionContext;
import com.gemstone.gemfire.cache.partition.PartitionRegionHelper;

/**
 * Application Function to retrieve values for multiple keys in a region.
 */
public class MultiGetFunction extends FunctionAdapter {

  private static final long serialVersionUID = 1L;
  public static final String ID = "mutiGetFunction";
  
  @Override
  public void execute(FunctionContext fc) {
    if (!(fc instanceof RegionFunctionContext)){
      throw new FunctionException("This is a data aware function, and has to be called using FunctionService.onRegion.");
    }
    RegionFunctionContext context = (RegionFunctionContext)fc;
//    Region<TradeKey, Trade> tradeRegion = PartitionRegionHelper.getLocalDataForContext(context);
    Region<TradeKey, Trade> tradeRegion = context.getDataSet();
    
    @SuppressWarnings("unchecked")
    Set<TradeKey> keys = (Set<TradeKey>)context.getFilter();
    Set<TradeKey> keysTillSecondLast = new HashSet<TradeKey>();
    int setSize = keys.size();
    Iterator<TradeKey> keysIterator = keys.iterator();
    for (int i = 0; i < (setSize - 1); i++) {
      keysTillSecondLast.add(keysIterator.next());
    }
    
    for (TradeKey k : keysTillSecondLast) {
      Trade trade = tradeRegion.get(k);
      String tradeStatus = (trade == null) ? "null" : trade.toString();
      context.getResultSender().sendResult(tradeStatus);
    }
    TradeKey lastResult = keysIterator.next();
    Trade trade = tradeRegion.get(lastResult);
    String tradeStatus = (trade == null) ? "null" : trade.toString();
    context.getResultSender().lastResult(tradeStatus);
  }

  @Override
  public String getId() {
    return ID;
  }
}
