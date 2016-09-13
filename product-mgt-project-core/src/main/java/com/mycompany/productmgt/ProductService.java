package com.mycompany.productmgt;

public interface ProductService {
	
	/**
     * Returns a price for a given product.
     * If the product has no price yet, it returns an initial price (by default 0.99).
     * If the product was created more than 6 months ago, it returns its price decreased of a specific percentage (by default 20).
     * Else, it returns its price increased of a specific percentage (by default 10).
     * The initial price and the percentages can be configured via the extension point "priceUpdate".
     *
     * @param DocumentModel product
     */
	public double computePrice(ProductAdapter product);
}
