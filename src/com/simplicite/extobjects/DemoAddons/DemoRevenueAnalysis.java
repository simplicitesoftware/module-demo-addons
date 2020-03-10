package com.simplicite.extobjects.DemoAddons;

import com.simplicite.util.AppLog;
import com.simplicite.util.Crosstab;
import com.simplicite.util.ExternalObject;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.Tool;
import com.simplicite.util.tools.HTMLTool;
import com.simplicite.util.tools.Parameters;

/**
 * Revenue analysis
 */
public class DemoRevenueAnalysis extends ExternalObject {
	private static final long serialVersionUID = 1L;

	/**
	 * Display method
	 * @param params Request parameters
	 */
	@Override
	public Object display(Parameters params) {
		try {
			appendCSSIncludes(new String[] {
				"https://cdn3.devexpress.com/jslib/18.1.3/css/dx.common.css",
				"https://cdn3.devexpress.com/jslib/18.1.3/css/dx.light.css"
			});
			appendJSInclude(
				"https://cdn3.devexpress.com/jslib/18.1.3/js/dx.all.js"
			);

			String inst = params.getParameter("inst");
			ObjectDB obj = getGrant().getObject(inst, "DemoOrder");
			String fields = "[" +
				"{ caption: '" + Tool.toJS(obj.getField("demoOrdPrdId.demoPrdSupId.demoSupCode").getDisplay()) + "', width: 150, dataField: 'demoOrdPrdId__demoPrdSupId__demoSupCode', area: 'row', sortBySummaryField: 'Total' }," +
				"{ caption: '" + Tool.toJS(obj.getField("demoOrdPrdId.demoPrdReference").getDisplay()) + "', dataField: 'demoOrdPrdId__demoPrdReference', width: 100, area: 'row' }," +
				"{ caption: '" + Tool.toJS(obj.getField("demoOrdDate").getDisplay()) + "', dataField: 'demoOrdDate', dataType: 'date', area: 'column' }," +
				"{ caption: 'Quantity', dataField: 'demoOrdQuantity', dataType: 'number', summaryType: 'sum', area: 'data' }," +
				"{ caption: 'Total', dataField: 'demoOrdTotal', dataType: 'number', summaryType: 'sum', format: 'currency', area: 'data' }" +
			"]";
			//obj.search(false); // Re-apply current instance filters for a non paginated search
			Crosstab c = obj.getCrosstab("DemoOrder-TC2");
			c.build(obj);
			String data = c.toJSONCubes();
			c.clear();

			return javascript(getName() + ".render(" + fields + ", " + data + ", null, "+ Tool.getCurrentYear() +");");
		} catch (Exception e) {
			AppLog.error(getClass(), "display", null, e, getGrant());
			return e.getMessage();
		}
	}
}