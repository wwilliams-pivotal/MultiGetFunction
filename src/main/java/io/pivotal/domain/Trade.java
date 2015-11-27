package io.pivotal.domain;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.gemfire.mapping.Region;

/**
 * Value class to hold Trade
 * @author Pivotal Data Engineering
 *
 */
@Region(value="Trade")
public class Trade {

  private int tradeDate;
  private String tradeId;

  /*
   * Zero-arg Constructor for Spring
   */
  public Trade() {
  }

  
  /**
   * Build the Trade
   * 
   * @param tradeId
   * @param tradeDate
   */
  @PersistenceConstructor
  public Trade(String tradeId, int tradeDate) {
    super();
    this.tradeId = tradeId;
    this.tradeDate = tradeDate;
 
  }

  /**
   * @return the tradeId
   */
  public String getTradeId() {
    return tradeId;
  }

  /**
   * @return the tradeDate
   */
  public int getTradeDate() {
    return tradeDate;
  }


  /*
   * Spring needs setters to populate the bean with generic readers
   */

  /**
   * @param tradeDate the tradeDate to set
   */
  public void setTradeDate(int tradeDate) {
    this.tradeDate = tradeDate;
  }


  /**
   * @param tradeId the tradeId to set
   */
  public void setTradeId(String tradeId) {
    this.tradeId = tradeId;
  }


  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Trade [tradeDate="
        + tradeDate + ", tradeId=" + tradeId + "]";
  }
}