//-----------------------------------------------------------
// Client side JavaScript for revenue analysis
//-----------------------------------------------------------

/* global DevExpress */

var DemoRevenueAnalysis = DemoRevenueAnalysis || (() => {
	function render(fields, data, expRow, expColumn, dateFormat) {
		DevExpress.viz.currentTheme('generic.light');

		// ZZZ convert localized date fields' strings as dates
		for (const f of fields)
			if (f.dataType === 'date')
				for (const d of data)
					d[f.dataField] = moment(d[f.dataField], dateFormat).toDate();

		const pivotGridChart = $('#DemoRevenueAnalysis-chart').dxChart({
			commonSeriesSettings: { type: 'bar' },
			tooltip: {
				enabled: true,
				format: 'currency',
				customizeTooltip: function(args) {
					return {
						html: args.seriesName + ' | Total<div class="currency">' + args.valueText + '</div>'
					};
				}
			},
			size: { height: 300 },
			adaptiveLayout: { width: 450 }
		}).dxChart('instance');

		const pivotGrid = $('#DemoRevenueAnalysis-grid').dxPivotGrid({
			allowSortingBySummary: true,
			allowFiltering: true,
			showBorders: true,
			showColumnGrandTotals: true,
			showRowGrandTotals: true,
			showRowTotals: false,
			showColumnTotals: false,
			fieldChooser: {
				enabled: true,
				height: 400
			},
			dataSource: {
				fields: fields,
				store: data
			}
		}).dxPivotGrid('instance');

		pivotGrid.bindChart(pivotGridChart, {
			dataFieldsDisplayMode: 'splitPanes',
			alternateDataFields: false
		});

		const expand = () => {
			const dataSource = pivotGrid.getDataSource();
			if (expRow) dataSource.expandHeaderItem('row', [expRow]);
			if (expColumn) dataSource.expandHeaderItem('column', [expColumn]);
		};

		setTimeout(expand, 0);
	}

	return { render: render };
})();
