package com.mycompany.productmgt;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * XMap descriptor for the contribution of a product price update policy.
 */
@XObject("priceUpdate")
public class PriceUpdateDescriptor {
	
	public double DEFAULT_INITIAL_PRICE = 0.99;
	public double DEFAULT_PCT_INCREASE = 10;
	public double DEFAULT_PCT_DECREASE = 20;
	
	@XNode("@initialPrice")
    protected double initialPrice;
	
    @XNode("@percentageOfIncrease")
    protected double percentageOfIncrease;
    
    @XNode("@percentageOfDecrease")
    protected double percentageOfDecrease;
    
    public double getInitialPrice() {
        return this.initialPrice;
    }

    public void setInitialPrice(double initialPrice) {
        this.initialPrice = initialPrice;
    }
    
    public double getPercentageOfIncrease() {
        return this.percentageOfIncrease;
    }

    public void setPercentageOfIncrease(double percentageOfIncrease) {
        this.percentageOfIncrease = percentageOfIncrease;
    }
    
    public double getPercentageOfDecrease() {
        return this.percentageOfDecrease;
    }

    public void setPercentageOfDecrease(double percentageOfDecrease) {
        this.percentageOfDecrease = percentageOfDecrease;
    }
    
    public void setDefaultSettings() {
    	this.setInitialPrice(this.DEFAULT_INITIAL_PRICE);
		this.setPercentageOfIncrease(this.DEFAULT_PCT_INCREASE);
		this.setPercentageOfDecrease(this.DEFAULT_PCT_DECREASE);
    }
}