package com.mycompany.productmgt;

import java.util.Calendar;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

public class ProductServiceImpl extends DefaultComponent implements ProductService {

	 protected PriceUpdateDescriptor priceUpdateDescriptor = new PriceUpdateDescriptor();
	 public static String PRICE_UPDATE_EXT_PT = "priceUpdate";

	/**
     * Component activated notification.
     * Called when the component is activated. All component dependencies are resolved at that moment.
     * Use this method to initialize the component.
     *
     * @param context the component context.
     */
    @Override
    public void activate(ComponentContext context) {
        super.activate(context);
    }

    /**
     * Component deactivated notification.
     * Called before a component is unregistered.
     * Use this method to do cleanup if any and free any resources held by the component.
     *
     * @param context the component context.
     */
    @Override
    public void deactivate(ComponentContext context) {
        super.deactivate(context);
    }
    
    /**
     * Application started notification.
     * Called after the application started.
     * You can do here any initialization that requires a working application
     * (all resolved bundles and components are active at that moment)
     *
     * @param context the component context. Use it to get the current bundle context
     * @throws Exception
     */
    @Override
    public void applicationStarted(ComponentContext context) {
    	this.priceUpdateDescriptor.setDefaultSettings();
    }

    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
    	if (PRICE_UPDATE_EXT_PT.equals(extensionPoint) && contribution instanceof PriceUpdateDescriptor) {
    		PriceUpdateDescriptor priceUpdate = (PriceUpdateDescriptor) contribution;
    		this.priceUpdateDescriptor.setInitialPrice(priceUpdate.initialPrice);
    		this.priceUpdateDescriptor.setPercentageOfIncrease(priceUpdate.percentageOfIncrease);
    		this.priceUpdateDescriptor.setPercentageOfDecrease(priceUpdate.percentageOfDecrease);
    	}
    }

    @Override
    public void unregisterContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
    	if (PRICE_UPDATE_EXT_PT.equals(extensionPoint) && contribution instanceof PriceUpdateDescriptor) {
    		this.priceUpdateDescriptor.setDefaultSettings();
    	}
    }
    
    @Override
    public double computePrice(ProductAdapter product) {
    	try {
    		double currentPrice = product.getPrice();
    		Calendar creationDate = (Calendar) product.getCreationDate();
        	Calendar halfYearDate = Calendar.getInstance();
        	halfYearDate.add(Calendar.MONTH, -6);
        	if (creationDate.before(halfYearDate)) {
        		return (double) Math.round((100 - this.priceUpdateDescriptor.getPercentageOfDecrease()) * currentPrice) / 100;
        	} else {
        		return (double) Math.round((100 + this.priceUpdateDescriptor.getPercentageOfIncrease()) * currentPrice) / 100;
        	}
    	} catch (Exception e) {
    		return (double) this.priceUpdateDescriptor.getInitialPrice();
    	}
    }
}
