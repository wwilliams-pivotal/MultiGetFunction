package io.pivotal.domain;

import com.gemstone.gemfire.cache.EntryOperation;
import com.gemstone.gemfire.cache.PartitionResolver;

/**
 * Trade requires a compound key and offers colocation with the
 * Inventories region
 * 
 * @author wwilliams
 *
 */
public class TradeKey implements
    PartitionResolver<TradeKey, Trade>, Comparable<TradeKey> {

  private String tradeId;
  private int tradeDate;

  private static int prime = 31;
  private static int LESS_THAN = -1;
  private static int GREATER_THAN = 1;
  private static int EQUAL = 0;

  /**
   * zero-arg constructor for Spring
   */
  public TradeKey() {
  }

  public TradeKey(String tradeId, int tradeDate) {
    super();
    this.tradeId = tradeId;
    this.tradeDate = tradeDate;
  }

  /**
   * Overrides the partition key by returning the key of the Inventories region
   */
  public Object getRoutingObject(
      EntryOperation<TradeKey, Trade> opDetails) {
    return tradeId;
  }

  public void close() {
  }

  @Override
  public int hashCode() {
    return tradeId.hashCode() * prime + tradeDate * prime;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;

    TradeKey other = (TradeKey) obj;
    return tradeId.equals(other.getTradeId()) && tradeDate == other.getTradeDate();
  }

  /**
   * @return the tradeId
   */
  public String getTradeId() {
    return tradeId;
  }

  /**
   * @param tradeId the tradeId to set
   */
  public void setTradeId(String tradeId) {
    this.tradeId = tradeId;
  }

  /**
   * @return the date
   */
  public int getTradeDate() {
    return tradeDate;
  }

  /**
   * @param date the date to set
   */
  public void setTradeDate(int tradeDate) {
    this.tradeDate = tradeDate;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "TradeKey [tradeId=" + tradeId
        + ", tradeDate=" + tradeDate + "]";
  }

  public int compareTo(TradeKey o) {
    if (this == o) {
      return EQUAL;
    }
    
    int tradeComparison = tradeId.compareTo(o.tradeId);
    if (tradeComparison != EQUAL) {
      return tradeComparison;
    }
    if (tradeDate < o.getTradeDate())
      return LESS_THAN;
    if (tradeDate > o.getTradeDate())
      return GREATER_THAN;
    
    return EQUAL;
  }

  public String getName() {
    return this.getClass().getName();
  }
}
