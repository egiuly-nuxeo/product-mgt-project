package com.mycompany.productmgt;

import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.SimpleFeature;

@Deploy({
	"com.mycompany.productmgt.product-mgt-project-core",
	"studio.extensions.egiuly-SANDBOX"
})
@Features(CoreFeature.class)
public class ProductFeature extends SimpleFeature {}